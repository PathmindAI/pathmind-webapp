#!/bin/bash

while true
do
        #Read SQS queue
        message=`aws sqs receive-message \
        --queue-url ${SQS_URL} \
        --max-number-of-messages 1 --wait-time-seconds 20`
        if [ "$message" != "" ]
        then
                echo "Message received ${message}"
                #Parse data
                ReceiptHandle=`echo $message | jq '.Messages[0].ReceiptHandle'`
                body=`echo $message | jq -r '.Messages[0].Body'`
                S3Bucket=`echo $body  | jq -r '.S3Bucket'`
                S3Path=`echo $body  | jq -r '.S3Path'`
                s3_url="s3://${S3Bucket}/${S3Path}"
                #Create spot instance group
                #Delete SQS
                aws sqs delete-message \
                --queue-url ${SQS_URL} \
                --receipt-handle ${ReceiptHandle}
        fi
done

