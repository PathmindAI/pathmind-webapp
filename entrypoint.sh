#!/usr/bin/bash

HOSTNAME=$(hostname)
aws sqs create-queue --queue-name ${HOSTNAME}-updater-queue --attributes "{\"MessageRetentionPeriod\":\"60\"}"
cat ./infra/sqs_policy.json | sed \"s/BUILD_ID/${HOSTNAME}/g\" > /tmp/${HOSTNAME}-sqs_policy.json
aws sqs set-queue-attributes --queue-url https://sqs.us-east-1.amazonaws.com/839270835622/${HOSTNAME}-updater-queue --attributes file:/tmp/${HOSTNAME}-sqs_policy.json
aws sns subscribe --topic-arn arn:aws:sns:us-east-1:839270835622:${ENVIRONMENT}-updater-topic --protocol sqs --notification-endpoint arn:aws:sqs:us-east-1:839270835622:${HOSTNAME}-updater-queue --attributes "{\"RawMessageDelivery\":\"true\"}"

export SQS_UPDATER_URL="https://sqs.us-east-1.amazonaws.com/839270835622/${ENVIRONMENT}-updater-queue"

java -Xmx4096m -XX:+UseG1GC -Dvaadin.productionMode -jar /app/pathmind-webapp/target/pathmind-webapp-0.0.1-SNAPSHOT.jar --server.port=80
