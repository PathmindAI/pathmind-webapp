#!/usr/bin/bash
set -e

export NAME=`grep cluster_name terraform.tfvars | awk -F'=' '{print $2}' | sed "s/ //g" | sed 's/"//g'`
BUCKET_NAME=`grep kops_bucket terraform.tfvars | awk -F'=' '{print $2}' | sed "s/ //g" | sed 's/"//g'`
MASTER_ZONES=`grep master_zones terraform.tfvars | awk -F'=' '{print $2}' | sed "s/ //g" | sed 's/"//g'`
ZONES=`grep node_zones terraform.tfvars | awk -F'=' '{print $2}' | sed "s/ //g" | sed 's/"//g'`
export KOPS_STATE_STORE="s3://${BUCKET_NAME}/k8s.${NAME}"
REGION=`aws configure list | grep region | awk '{print $2}'`
NODE_COUNT=4
NODE_SIZE="t2.large"
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
--target=terraform --out=modules/kubernetes \
${NAME}

terraform init
terraform apply --auto-approve
