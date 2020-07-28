#!/usr/bin/bash

sleep_time=5

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
description="Spot instance will be terminated by AWS"
curl -X POST -H 'Content-type: application/json' \
	--data "{'text':':x:Spot Instance Termination Job'}" \
	https://hooks.slack.com/services/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

sleep 24h
