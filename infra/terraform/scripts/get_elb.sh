#!/usr/bin/bash

if [ $# -lt 2 ]
then
        echo "Usage `basename $0` <Ingress Name> <namespace>"
        exit 1
fi

ING_NAME=$1
NAMESPACE=$2

ELB_NAME=`kubectl describe ing ${ING_NAME} -n ${NAMESPACE} | grep '^Address:' | awk '{print $2}'`
ZONE_ID=`aws elb describe-load-balancers | jq --arg DNSName $ELB_NAME -r '.LoadBalancerDescriptions | .[] | select(.DNSName=="\($DNSName)") | .CanonicalHostedZoneNameID'`

echo "{"
echo '"elb_name": "'${ELB_NAME}'",'
echo '"zone_id": "'${ZONE_ID}'"'
echo "}"

