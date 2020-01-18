#!/bin/bash

sleep_time=60
s3_url="s3://${S3BUCKET}/${S3PATH}"
aws s3 sync ${s3_url} ./

#Set the status in trainer_job
psql "$DB_URL_CLI" << EOF
update public.trainer_job set status=3,ec2_create_date=now() where job_id='${S3PATH}';
commit;
EOF

bash script.sh > process_output.log 2>&1 &
pid=$!

tail -f process_output.log &
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
        aws s3 cp --recursive ./work/PPO ${s3_url}/output/
        aws s3 cp ./work/trial_complete ${s3_url}/output/trial_complete
        aws s3 cp ./work/trial_error ${s3_url}/output/trial_error
        aws s3 cp ./work/trial_list ${s3_url}/output/trial_list
        sleep $sleep_time
done

wait $pid
return_code=$?

if [ "$return_code" == 0 ]
then
	status=4
else
	status=5
fi

aws s3 cp --recursive ./work/PPO ${s3_url}/output/
aws s3 cp ./work/trial_complete ${s3_url}/output/trial_complete
aws s3 cp ./work/trial_error ${s3_url}/output/trial_error
aws s3 cp ./work/trial_list ${s3_url}/output/trial_list
aws s3 cp process_output.log ${s3_url}/output/process_output.log
aws s3 cp errors.log ${s3_url}/output/errors.log

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
	cp trial_* ../result
	cd `find "$DIR"/.. -iname checkpoint_* -type d | sort -V | tail -1`
	zip $OLDPWD/../result/$(basename `dirname $DIR`)/checkpoint.zip ./*
	aws s3 cp $OLDPWD/../result/$(basename `dirname
	$DIR`)/checkpoint.zip \
		${s3_url}/output/${S3_DIR}/
	cd $OLDPWD
done

#Set the status in trainer_job
psql "$DB_URL_CLI" << EOF
update public.trainer_job set status=${status},ec2_end_date=now()  where job_id='${S3PATH}';
commit;
EOF

psql "$DB_URL_CLI" << EOF
select job_id ,create_date,ec2_create_date,ec2_end_date,status from public.trainer_job where job_id='${S3PATH}';
EOF

#Send sqs notification to destroy
aws sqs send-message \
        --queue-url ${SQS_URL} \
        --message-body '{"S3Bucket": "'${S3BUCKET}'", "S3Path":"'${S3PATH}'", "destroy":"1"}' \
        --message-group-id training

kill -9 $pid_tail
