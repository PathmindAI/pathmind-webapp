#!/usr/bin/bash
set -e

if [ "$1" == "" ]
then
        echo "Usage deploy.sh <environment>"
        echo "Example deploy.sh test"
        exit 2
fi

env=$1
secrets="secret_${1}.tfvars"
vars="terraform_${2}.tfvars"
backend="backend_${2}.tf"

if [ ! -f $secrets ]
then
	echo "file $secrets does not exist"
        exit 2
fi

if [ ! -f $vars ]
then
	echo "file $vars does not exist"
        exit 2
fi

if [ ! -f $backend ]
then
	echo "file $backend does not exist"
        exit 2
fi

cp $backend backend.tf

export NAME=`grep cluster_name ${vars} | awk -F'=' '{print $2}' | sed "s/ //g" | sed 's/"//g'`
BUCKET_NAME=`grep kops_bucket ${vars} | awk -F'=' '{print $2}' | sed "s/ //g" | sed 's/"//g'`
MASTER_ZONES=`grep master_zones ${vars} | awk -F'=' '{print $2}' | sed "s/ //g" | sed 's/"//g'`
ZONES=`grep node_zones ${vars} | awk -F'=' '{print $2}' | sed "s/ //g" | sed 's/"//g'`
export KOPS_STATE_STORE="s3://${BUCKET_NAME}/k8s.${NAME}"
REGION=`aws configure list | grep region | awk '{print $2}'`
NODE_COUNT=`grep node_count ${vars} | awk -F'=' '{print $2}' | sed "s/ //g" | sed 's/"//g'`
NODE_SIZE=`grep node_size ${vars} | awk -F'=' '{print $2}' | sed "s/ //g" | sed 's/"//g'`
MASTER_SIZE=`grep master_size ${vars} | awk -F'=' '{print $2}' | sed "s/ //g" | sed 's/"//g'`

#If bucket does not exist create it
if ! aws s3api head-bucket --bucket ${BUCKET_NAME} 2>/dev/null; then
	aws s3api create-bucket --bucket ${BUCKET_NAME} --region ${REGION}
fi

#remove old module
rm -rf modules 2> /dev/null

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

rm dns.tf >/dev/null 2>&1

terraform init
terraform apply --target=null_resource.ingress --auto-approve

sleep 20

zone_id=`./get_elb.sh ingress default | grep zone_id | cut -f2 -d':' | sed "s/ //g"`
elb_name=`./get_elb.sh ingress default | grep elb_name | cut -f2 -d':' | sed "s/ //g"`

cp dns.template dns.tf
sed -i "s/{{ZONE_ID}}/$zone_id/g" dns.tf
sed -i "s/{{ELB_NAME}}/$elb_name/g" dns.tf

terraform apply --auto-approve
