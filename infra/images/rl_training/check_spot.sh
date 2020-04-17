#!/usr/bin/bash

sleep_time=5
S3PATH=$1
ENVIRONMENT=$2
EMAIL=$3
s3_url_link=$4

while true
do
	STATUS=`curl -s -o /dev/null -w '%{http_code}' http://169.254.169.254/latest/meta-data/spot/instance-action`
	if [ $STATUS -eq 200 ]; then
		echo "Spot instance will be terminated by AWS"
		break
	fi
	sleep $sleep_time
done

description="Spot instance will be terminated by AWS"
curl -X POST -H 'Content-type: application/json' \
	--data "{'text':':x:Spot Instance Termination Job ${S3PATH}\nDescription: ${description}\nEnv: ${ENVIRONMENT}\nUser: ${EMAIL}\nhttps://s3.console.aws.amazon.com/s3/buckets/${s3_url_link}/'}" \
	https://hooks.slack.com/services/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
