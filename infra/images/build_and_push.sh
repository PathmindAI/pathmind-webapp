#!/usr/bin/env bash

# This script shows how to build the Docker image and push it to ECR to be ready for use
# by SageMaker.

# The argument to this script is the image name. This will be used as the image on the local
# machine and combined with the account and region to form the repository name for ECR.
docker_folder=$1
image=$2
GIT_USERNAME=$3
GIT_PASSWORD=$4

if [ "$docker_folder" == "" ] || [ "$image" == "" ] || [ "$GIT_USERNAME" == "" ] || [ "$GIT_PASSWORD" == "" ]
then
	    echo "Usage: $0 <docker-folder> <image-name> <git-user> <git-password>"
	        exit 1
fi

# Get the account number associated with the current IAM credentials
account=$(aws sts get-caller-identity --query Account --output text)

if [ $? -ne 0 ]
then
	    exit 255
fi


# Get the region defined in the current configuration (default to us-east-1 if none defined)
region=$(aws configure get region)
region=${region:-us-east-1}


fullname="${account}.dkr.ecr.${region}.amazonaws.com/${image}:latest"

# If the repository doesn't exist in ECR, create it.

aws ecr describe-repositories --repository-names "${image}" > /dev/null 2>&1

if [ $? -ne 0 ]
then
	    aws ecr create-repository --repository-name "${image}" > /dev/null
fi

# Get the login command from ECR and execute it directly
$(aws ecr get-login --region ${region} --no-include-email)

# Build the docker image locally with the image name and then push it to ECR
# with the full name.

docker build  -t ${image} --build-arg GIT_USERNAME=${GIT_USERNAME} --build-arg GIT_PASSWORD=${GIT_PASSWORD} --build-arg AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}  --build-arg AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}  -f ${docker_folder}/Dockerfile ${docker_folder}/. && \
	docker tag ${image} ${fullname} && \
	docker push ${fullname}
