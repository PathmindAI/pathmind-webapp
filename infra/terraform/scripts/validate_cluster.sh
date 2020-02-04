#!/usr/bin/bash
set -e

if [ "$1" == "" ] || [ "$2" == "" ]
then
	echo "Usage validate_cluster.sh <cluster_name> <state_bucket>"
	echo "Example validate_cluster.sh pathmind.k8s.local pathmind-kops-state"
	exit 2
fi

export NAME=$1
BUCKET_NAME=$2
export KOPS_STATE_STORE="s3://${BUCKET_NAME}/k8s.${NAME}"

kops update cluster $NAME --target=terraform --out=modules/kubernetes/
kops rolling-update cluster --cloudonly --force --yes

set +e

while true
do
	kops validate cluster $NAME > /dev/null
	if [ $? == 0 ]
	then
		exit 0
	fi
	sleep 30
done


