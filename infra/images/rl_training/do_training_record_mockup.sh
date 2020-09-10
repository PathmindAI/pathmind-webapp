#!/bin/bash

NAMESPACE="default"
if [ "${ENVIRONMENT}" != "prod" ]
then
	NAMESPACE=${ENVIRONMENT}
fi

#Increase the restrats count
psql "$DB_URL_CLI" << EOF
update public.trainer_job
set restarts=restarts+1
where job_id='${S3PATH}';
commit;
EOF

#Checked if pod crashed more than 3 times already
CRASHES=`kubectl get pods -n ${ENVIRONMENT} | grep ${S3PATH} | grep Evicted | wc -l`
if [ $CRASHES -ge 3 ]
then
        description="Job crashed more than 3 times, job is killed"
        curl -X POST -H 'Content-type: application/json' \
        --data "{'text':':x:Job ${S3PATH}\nDescription: ${description}\nEnv: ${ENVIRONMENT}\nUser: ${EMAIL}\nhttps://s3.console.aws.amazon.com/s3/buckets/${s3_url_link}/'}" \
        https://hooks.slack.com/services/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
        echo "Job crashed more than 3 times, job is killed"
        #Set the status in trainer_job
        psql "$DB_URL_CLI" << EOF
update public.trainer_job
set status=5,ec2_end_date=now(),update_date=NOW(),description='${description}'
where job_id='${S3PATH}';
commit;
EOF
        aws s3 cp ${log_file} ${s3_url}/output/${log_file} > /dev/null
        echo ${description} > errors.log
        aws s3 cp errors.log ${s3_url}/output/errors.log > /dev/null
        aws sqs send-message \
                --queue-url ${SQS_URL} \
                --message-body '{"S3Bucket": "'${S3BUCKET}'", "S3Path":"'${S3PATH}'", "destroy":"0"}' \
                --message-group-id training
        sleep 1h
fi

#Check training age and kill it if is older than 24 hours
AGE=`psql -t "$DB_URL_CLI" << EOF
select extract(hour from age(NOW(),create_date))  from public.trainer_job where job_id='${S3PATH}'
EOF`

if [ $AGE -ge 24 ]
then
	description="Job running for more than 24 hours, job is killed"
	curl -X POST -H 'Content-type: application/json' \
	--data "{'text':':x:Job ${S3PATH}\nDescription: ${description}\nEnv: ${ENVIRONMENT}\nUser: ${EMAIL}\nhttps://s3.console.aws.amazon.com/s3/buckets/${s3_url_link}/'}" \
	https://hooks.slack.com/services/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
	echo "Killing on timeout"
	#Set the status in trainer_job
	psql "$DB_URL_CLI" << EOF
update public.trainer_job
set status=5,ec2_end_date=now(),update_date=NOW(),description='${description}'
where job_id='${S3PATH}';
commit;
EOF
	aws s3 cp ${log_file} ${s3_url}/output/${log_file} > /dev/null
	echo ${description} > errors.log
	aws s3 cp errors.log ${s3_url}/output/errors.log > /dev/null
	aws sqs send-message \
		--queue-url ${SQS_URL} \
		--message-body '{"S3Bucket": "'${S3BUCKET}'", "S3Path":"'${S3PATH}'", "destroy":"0"}' \
		--message-group-id training
	sleep 1h
fi

sleep_time=60
training_update_timeout=3600
s3_url="s3://${S3BUCKET}/${S3PATH}"
s3_url_link="${S3BUCKET}/${S3PATH}"
log_file="process_output.log"
aws s3 sync ${s3_url} ./ > /dev/null

#remove existing folders
find . -mindepth 1 -maxdepth 1 -type d | xargs -I {} rm -rf {}

#Get the user
ID=`echo ${S3PATH} | sed "s/id//g"`
EMAIL=`psql -t "$DB_URL_CLI" << EOF
select email from pathmind_user
where id= (select pathmind_user_id from project
where id= (select project_id from model
where id= (select model_id from experiment where id=(select experiment_id from run where id=${ID}))))
EOF`

#Get the instance type and cost
instanceid=`curl http://169.254.169.254/latest/dynamic/instance-identity/document | jq -r '.instanceId'`
instance_type=`aws ec2 describe-spot-instance-requests | jq -r ".SpotInstanceRequests | .[] | select (.InstanceId ==\"${instanceid}\").LaunchSpecification.InstanceType"`
instance_price=`aws ec2 describe-spot-instance-requests | jq -r ".SpotInstanceRequests | .[] | select (.InstanceId ==\"${instanceid}\").SpotPrice"`

#Check if we need to resume the training
aws s3 sync ${s3_url}/output/ /tmp/PPO/
output_files=`ls  /tmp/PPO/experiment_state*json | wc -l`
if [ "${output_files}" -ge 1 ]
then
	set -e
	echo "Resuming Training"
	export RESUME=true
	rm -rf /app/work/PPO/*
	touch restarting
	aws s3 cp restarting ${s3_url}/output/
	aws s3 sync ${s3_url}/output/ /app/work/PPO/
	echo `ls -1 /app/work/PPO/ | wc -l`" files were downloaded from ${s3_url}/output/"
	aws s3 cp ${s3_url}/output/ ${s3_url}/output_backup_`date '+%Y%m%d%H%M'`/ --recursive
	aws s3 rm ${s3_url}/output/ --recursive
	aws s3 cp ${s3_url}/mockup/ ${s3_url}/mockup_backup_`date '+%Y%m%d%H%M'`/ --recursive
	aws s3 rm ${s3_url}/mockup/ --recursive
	touch restarted
	aws s3 cp restarted ${s3_url}/output/
	set +e
else
	#Set the status in trainer_job
	psql "$DB_URL_CLI" << EOF
update public.trainer_job
set status=3,ec2_create_date=now(),update_date=NOW(),ec2_instance_type='${instance_type}',ec2_max_price='${instance_price}',restarts=0
where job_id='${S3PATH}';
commit;
EOF
fi

bash script.sh > ${log_file} 2>&1 &
pid=$!

tail -f ${log_file} &
pid_tail=$!

sleep $sleep_time

minuto=0
while true
do
        ps ax | grep $pid | grep -v grep > /dev/null
        if [ $? != 0 ]
        then
                echo "Process has been terminated"
                break
        fi

        last_line=$(tail -n 1 ${log_file})
        if [ "${last_line}" = 'Training has been completed' ]
        then
                echo $last_line
                break
        fi

        #Upload files
        aws s3 sync ./work/PPO ${s3_url}/output/ > /dev/null
        aws s3 sync ./work/PPO ${s3_url}/mockup/${minuto}/ > /dev/null
        minuto=$((minuto+1))
        #Check the training response timeout
        last_modification=`echo $(( $(date +%s) - $(stat -c %Y -- "${log_file}") ))`
        if [ ${last_modification} -ge ${training_update_timeout} ]
        then
                description="No update in the log file ${log_file} for more than ${last_modification} seconds, job is killed"
                curl -X POST -H 'Content-type: application/json' \
                --data "{'text':':x:Job ${S3PATH}\nDescription: ${description}\nEnv: ${ENVIRONMENT}\nUser: ${EMAIL}\nhttps://s3.console.aws.amazon.com/s3/buckets/${s3_url_link}/'}" \
                https://hooks.slack.com/services/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
                echo "Killing on timeout"
                #Set the status in trainer_job
                psql "$DB_URL_CLI" << EOF
update public.trainer_job
set status=5,ec2_end_date=now(),update_date=NOW(),description='${description}'
where job_id='${S3PATH}';
commit;
EOF
		aws s3 cp ${log_file} ${s3_url}/output/${log_file} > /dev/null
		echo ${description} > errors.log
		aws s3 cp errors.log ${s3_url}/output/errors.log > /dev/null
		aws sqs send-message \
			--queue-url ${SQS_URL} \
			--message-body '{"S3Bucket": "'${S3BUCKET}'", "S3Path":"'${S3PATH}'", "destroy":"0"}' \
			--message-group-id training
                sleep 1h
        fi
        sleep $sleep_time
done

wait $pid
script_exit=$?
status=4
description="Job finished"
if [ ${script_exit} != 0 ]
then
	description=`tail -c 254 ${log_file} | tr '\n' ' ' | sed "s/'//g"`
	curl -X POST -H 'Content-type: application/json' \
		--data "{'text':':x:Training ${S3PATH} Job Crashed\nError: ${description}\nEnv: ${ENVIRONMENT}\nUser: ${EMAIL}\nhttps://s3.console.aws.amazon.com/s3/buckets/${s3_url_link}/'}" \
		https://hooks.slack.com/services/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
	echo "Training crashed"
	tail -1  ${log_file} >> errors.log
	status=5
fi

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
                ${s3_url}/output/${S3_DIR}/ > /dev/null
        aws s3 cp $OLDPWD/../result/policy_$(basename `dirname $DIR`).zip \
                ${s3_url}/mockup/${minuto}/${S3_DIR}/ > /dev/null
        cd $OLDPWD
        cd `find "$DIR"/.. -iname checkpoint_* -type d | sort -V | tail -1`
        zip $OLDPWD/../result/$(basename `dirname $DIR`)/checkpoint.zip ./*
        aws s3 cp $OLDPWD/../result/$(basename `dirname $DIR`)/checkpoint.zip \
                ${s3_url}/output/${S3_DIR}/ > /dev/null
        aws s3 cp $OLDPWD/../result/$(basename `dirname $DIR`)/checkpoint.zip \
                ${s3_url}/mockup/${minuto}/${S3_DIR}/ > /dev/null
        cd $OLDPWD
done

cd ..

#Check errors
bash errorCheck.sh
#Upload the final files only after policy and checkpoint are uploaded
aws s3 sync ./work/PPO ${s3_url}/output/ > /dev/null
aws s3 cp ${log_file} ${s3_url}/output/${log_file} > /dev/null
aws s3 cp errors.log ${s3_url}/output/errors.log > /dev/null
aws s3 sync ./work/PPO ${s3_url}/mockup/${minuto}/ > /dev/null
aws s3 cp ${log_file} ${s3_url}/mockup/${minuto}/${log_file} > /dev/null
aws s3 cp errors.log ${s3_url}/mockup/${minuto}/errors.log > /dev/null

#delete old state files
if [ "${RESUME}" == true ]
then
	for file in `ls -1tr ./work/PPO/experiment_state*json | sed '$d'`
	do
		#Delete locally
		echo "Deleting old ${file}"
		rm ${file}
		#Delete from s3
		file=`basename ${file}`
		aws s3 rm ${s3_url}/output/${file} > /dev/null
	done
fi

#Validate that the training was successful
NUM_SAMPLES=`grep NUM_SAMPLES script.sh | grep export | cut -f2 -d"'" | sed "s/ //g"`
SUCCESS=`grep '"status": "TERMINATED",' work/PPO/experiment_state-* | wc -l | sed "s/ //g"`
if [ "${SUCCESS}" != "${NUM_SAMPLES}" ]
then
        description="Only ${SUCCESS} trials were successful out of ${NUM_SAMPLES}"
        curl -X POST -H 'Content-type: application/json' \
        --data "{'text':':x:Job Failed ${S3PATH}\nDescription: ${description}\nEnv: ${ENVIRONMENT}\nUser: ${EMAIL}\nhttps://s3.console.aws.amazon.com/s3/buckets/${s3_url_link}/'}" \
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
sleep 1h
