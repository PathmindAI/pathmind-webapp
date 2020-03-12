#!/bin/bash

sleep_time=60
training_update_timeout=3600
s3_url="s3://${S3BUCKET}/${S3PATH}"
s3_url_link="${S3BUCKET}/${S3PATH}"
log_file="process_output.log"
aws s3 sync ${s3_url} ./

#Get the user
ID=`echo ${S3PATH} | sed "s/id//g"`
EMAIL=`psql -t "$DB_URL_CLI" << EOF
select email from pathmind_user
where id= (select pathmind_user_id from project
where id= (select project_id from model
where id= (select model_id from experiment where id=(select experiment_id from run where id=${ID}))))
EOF`

#Set the status in trainer_job
psql "$DB_URL_CLI" << EOF
update public.trainer_job
set status=3,ec2_create_date=now(),update_date=NOW()
where job_id='${S3PATH}';
commit;
EOF


bash script.sh > ${log_file} 2>&1 &
pid=$!

tail -f ${log_file} &
pid_tail=$!

sleep $sleep_time

while true
do
        ps ax | grep $pid | grep -v grep > /dev/null
        if [ $? != 0 ]
        then
                break
        fi
        #Upload files
        aws s3 sync ./work/PPO ${s3_url}/output/
        #Check the training response timeout
        last_modification=`echo $(( $(date +%s) - $(stat -c %Y -- "${log_file}") ))`
        if [ ${last_modification} -ge ${training_update_timeout} ]
        then
                description="No update in the log file ${log_file} for more than ${last_modification} seconds, training is not killed"
                curl -X POST -H 'Content-type: application/json' \
                --data "{'text':'Killing Job ${S3PATH}\nDescription: ${description}\nEnv: ${ENVIRONMENT}\nUser: ${EMAIL}\nhttps://s3.console.aws.amazon.com/s3/buckets/${s3_url_link}'}" \
                https://hooks.slack.com/services/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
                echo "Killing on timeout"
                #Set the status in trainer_job
                psql "$DB_URL_CLI" << EOF
update public.trainer_job
set status=4,ec2_end_date=now(),update_date=NOW(),description='${description}'
where job_id='${S3PATH}';
commit;
EOF
                sleep 12h
        fi
        sleep $sleep_time
done

wait $pid

#Generate final files
cd work
mkdir -p result
for DIR in `find . -iname model -type d`; do
        cd $DIR
        mkdir -p $OLDPWD/../result/$(basename `dirname $DIR`)/
        cp ../progress.csv $OLDPWD/../result/$(basename `dirname $DIR`)
        cp ../../*.json $OLDPWD/../result/
        zip -r $OLDPWD/../result/policy_$(basename `dirname $DIR`).zip .
        S3_DIR=`echo $DIR | cut -f3 -d'/'`
        aws s3 cp $OLDPWD/../result/policy_$(basename `dirname $DIR`).zip \
                ${s3_url}/output/${S3_DIR}/
        cd $OLDPWD
        cd `find "$DIR"/.. -iname checkpoint_* -type d | sort -V | tail -1`
        zip $OLDPWD/../result/$(basename `dirname $DIR`)/checkpoint.zip ./*
        aws s3 cp $OLDPWD/../result/$(basename `dirname $DIR`)/checkpoint.zip \
                ${s3_url}/output/${S3_DIR}/
        cd $OLDPWD
done

cd ..

#Upload the final files only after policy and checkpoint are uploaded
aws s3 sync ./work/PPO ${s3_url}/output/
aws s3 cp ${log_file} ${s3_url}/output/${log_file}
aws s3 cp errors.log ${s3_url}/output/errors.log

#Validate that the training was successful
NUM_SAMPLES=`grep NUM_SAMPLES script.sh | grep export | cut -f2 -d"'" | sed "s/ //g"`
SUCCESS=`grep '"status": "TERMINATED",' work/PPO/experiment_state-* | wc -l | sed "s/ //g"`
status=4
description="Job finishsed"
if [ "${SUCCESS}" != "${NUM_SAMPLES}" ]
then
        description="Only ${SUCCESS} trials were successful out of ${NUM_SAMPLES}"
        curl -X POST -H 'Content-type: application/json' \
        --data "{'text':'Job Failed ${S3PATH}\nDescription: ${description}\nEnv: ${ENVIRONMENT}\nUser: ${EMAIL}\nhttps://s3.console.aws.amazon.com/s3/buckets/${s3_url_link}'}" \
        https://hooks.slack.com/services/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
        echo "Error on training"
        status=5
fi

#Set the status in trainer_job
psql "$DB_URL_CLI" << EOF
update public.trainer_job
set status=${status},ec2_end_date=now(),update_date=NOW(),description='${description}'
where job_id='${S3PATH}';
commit;
EOF

#Send sqs notification to destroy
aws sqs send-message \
        --queue-url ${SQS_URL} \
        --message-body '{"S3Bucket": "'${S3BUCKET}'", "S3Path":"'${S3PATH}'", "destroy":"0"}' \
        --message-group-id training

kill -9 $pid_tail

#Sleep until is destroyed
sleep 12h
