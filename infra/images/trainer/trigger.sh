#!/bin/bash

IG_TEMPLATE="spot_ig_template.yaml"
IF_FILE=`echo ${IG_TEMPLATE} | sed "s/_template//g"`

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
                #Delete SQS
                aws sqs delete-message \
                --queue-url ${SQS_URL} \
                --receipt-handle ${ReceiptHandle}
                #Create spot instance group
                JOB_IG_FILE=${S3Path}_${IF_FILE}
                cp ${IG_TEMPLATE} ${JOB_IG_FILE}
                sed -i "s/{{CLUSTER_NAME}}/${NAME}/g" ${JOB_IG_FILE}
                sed -i "s/{{IG_NAME}}/${S3Path}/g" ${JOB_IG_FILE}
                sed -i "s/{{INSTANCE_TYPE}}/t2.medium/g" ${JOB_IG_FILE}
                sed -i "s/{{MAX_PRICE}}/0.0170/g" ${JOB_IG_FILE}
                kops create -f ${JOB_IG_FILE}
                kops update cluster ${NAME} --yes
        fi
done
