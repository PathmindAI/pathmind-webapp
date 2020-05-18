#!/bin/bash 
if [ "$1" == "" ]
then
	echo "Usage: $0 <BUILD_ID>"
	exit 1
fi

BUILD_ID=$1

#Clean s3
S3BUCKET=${BUILD_ID}-training-dynamic-files.pathmind.com
for ID in `aws s3 ls s3://${S3BUCKET}/ | awk '{print $2}' | grep id | sed 's@/@@g'`
do 
  aws sqs send-message \
    --queue-url https://queue.amazonaws.com/839270835622/dev-training-queue.fifo \
    --message-body "{\"S3Bucket\": \"${S3BUCKET}\",\"S3Path\":\"${ID}\", \"mockup\":\"1\", \"destroy\":\"1\"}" \
    --message-group-id training
done

sleep 60

aws s3 rb s3://${S3BUCKET} --force
