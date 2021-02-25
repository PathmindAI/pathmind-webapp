#!/bin/bash

QUEUE_URL=
while true
do
	messages=`aws sqs receive-message \
		--queue-url ${QUEUE_URL} \
		--attribute-names All \
		--message-attribute-names All \
		--max-number-of-messages 1 \
		--wait-time-seconds 10 \
		--visibility-timeout 60`
done
