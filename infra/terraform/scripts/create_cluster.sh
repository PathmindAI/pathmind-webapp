#!/usr/bin/bash
set -e

if [ "$1" == "" ] || [ "$2" == "" ] || [ "$3" == "" ] || [ "$4" == "" ] || [ "$5" == "" ]
then
	echo "Usage create_cluster.sh <region> <cluster_name> <state_bucket> <master_zones> <node_zones>"
	echo "Example create_cluster.sh us-east-1 pathmind.k8s.local pathmind-kops-state us-east-1a us-east-1a"
	exit 2
fi

REGION=$1
export NAME=$2
BUCKET_NAME=$3
MASTER_ZONES=$4
ZONES=$5
export KOPS_STATE_STORE="s3://${BUCKET_NAME}/k8s.${NAME}"
#REGION="us-east-1"
#MASTER_ZONES="us-east-1a,us-east-1b,us-east-1c"
#ZONES="us-east-1a,us-east-1b,us-east-1c"
NODE_COUNT=2
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


#Create Bastion host
#kops create instancegroup bastions --role Bastion --subnet utility-us-east-1a --name ${NAME}
#kops update cluster ${NAME} --yes
