#!/usr/bin/bash

if [ "$1" == "" ]
then
        echo "Usage get_vpc_id.sh <vpc_name>"
        echo "Example get_vpc_id.sh pathmind.k8s.local"
        exit 2
fi

vpc_name=$1
vpc_id=$(aws ec2 describe-vpcs --query 'Vpcs[?Tags[?Key==`Name`]|[?Value==`'$vpc_name'`]].VpcId' --output text)

echo "{"
echo '"vpc_id": "'${vpc_id}'"'
echo "}"

