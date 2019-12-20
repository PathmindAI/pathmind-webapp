#!/usr/bin/bash

if [ "$1" == "" ] || [ "$2" == "" ]
then
	echo "Usage create_cluster.sh <cluster_name> <state_bucket>"
	echo "Example create_cluster.sh  pathmind.k8s.local pathmind-kops-state"
	exit 2
fi

export NAME=$1
BUCKET_NAME=$2
export KOPS_STATE_STORE="s3://${BUCKET_NAME}/k8s.${NAME}"
REGION="us-east-1"
#MASTER_ZONES="us-east-1a,us-east-1b,us-east-1c"
MASTER_ZONES="us-east-1a"
#ZONES="us-east-1a,us-east-1b,us-east-1c"
ZONES="us-east-1a"
#NODE_COUNT=3
NODE_COUNT=1
NODE_SIZE="t2.medium"
MASTER_SIZE="t2.medium"

#If bucket does not exist create it
if ! aws s3api head-bucket --bucket ${BUCKET_NAME} 2>/dev/null; then
	aws s3api create-bucket --bucket ${BUCKET_NAME} --region ${REGION}
fi

#Create cluster
kops create cluster \
--cloud=aws \
--master-zones=${MASTER_ZONES} \
--zones=${ZONES} \
--node-count=${NODE_COUNT} \
--topology private \
--networking kopeio-vxlan \
--node-size=${NODE_SIZE} \
--master-size=${MASTER_SIZE} \
${NAME}

kops update cluster ${NAME} --yes

while true
do
	kops validate cluster $NAME > /dev/null
	if [ $? == 0 ]
	then
		exit 0
	fi
	sleep 5
done
