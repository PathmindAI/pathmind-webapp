#!/usr/bin/bash
set -e

if [ "$1" == "" ] || [ "$2" == "" ]
then
	echo "Usage create_bastion.sh <cluster_name> <state_bucket>"
	echo "Example create_bastion.sh pathmind.k8s.local pathmind-kops-state"
	exit 2
fi

export NAME=$1
BUCKET_NAME=$2
export KOPS_STATE_STORE="s3://${BUCKET_NAME}/k8s.${NAME}"

echo :wq | kops create instancegroup bastions --role Bastion --subnet utility-us-east-1a --name ${NAME}
kops update cluster ${NAME} --yes

set +e

#Wait for bastion to come up
while true
do
        ssh admin@`aws elb --output=table describe-load-balancers|grep DNSName.\*bastion|awk '{print $4}'` ls > /dev/null
        if [ $? == 0 ]
        then
                break
        fi
        sleep 10
done

