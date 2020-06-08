#!/bin/bash

set -x

exec > >(tee /var/log/user-data.log) 2>&1

# SSM on amazon linux 2
yum install -y https://s3.amazonaws.com/ec2-downloads-windows/SSMAgent/latest/linux_amd64/amazon-ssm-agent.rpm
systemctl enable amazon-ssm-agent
systemctl start amazon-ssm-agent

yum update -y
yum upgrade -y
yum install -y nfs-utils

# install AWS CLI
curl -O https://bootstrap.pypa.io/get-pip.py
python get-pip.py
export PATH=/usr/local/bin:$PATH
pip install awscli --upgrade

# ECS config
echo "ECS_CLUSTER=${cluster_name}" >> /etc/ecs/ecs.config
echo "ECS_ENABLE_TASK_IAM_ROLE=true" >> /etc/ecs/ecs.config

# Mount EFS
mkdir /efs

echo "ECS started"

