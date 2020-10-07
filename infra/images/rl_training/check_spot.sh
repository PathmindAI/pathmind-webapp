#!/usr/bin/bash

sleep_time=5
S3BUCKET=$1
S3PATH=$2
ENVIRONMENT=$3
EMAIL=$4
log_file=$5

s3_url="s3://${S3BUCKET}/${S3PATH}"
s3_url_link="${S3BUCKET}/${S3PATH}"

while true
do
	STATUS=`curl -s -o /dev/null -w '%{http_code}' http://169.254.169.254/latest/meta-data/spot/instance-action`
	if [ $STATUS -eq 200 ]; then
		echo "Spot instance will be terminated by AWS"
		break
	fi
	sleep $sleep_time
done

aws s3 cp ${log_file} ${s3_url}/output/${log_file} > /dev/null
aws s3 sync ./work/PPO ${s3_url}/output/ > /dev/null
ls -alR ./work/ > /tmp/debug
aws s3 cp /tmp/debug ${s3_url}/output/debug > /dev/null
description="Spot instance will be terminated by AWS"
curl -X POST -H 'Content-type: application/json' \
	--data "{'text':':x:Spot Instance Termination Job ${S3PATH}\nDescription: ${description}\nEnv: ${ENVIRONMENT}\nUser: ${EMAIL}\nhttps://s3.console.aws.amazon.com/s3/buckets/${s3_url_link}/'}" \
	https://hooks.slack.com/services/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

#aws sqs send-message \
#	--queue-url ${SQS_URL} \
#	--message-body '{"S3Bucket": "'${S3BUCKET}'", "S3Path":"'${S3PATH}'", "destroy":"0"}' \
#	--message-group-id training
