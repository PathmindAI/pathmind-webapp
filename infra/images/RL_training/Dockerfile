# Build an image that can do RL training in SageMaker
# This is a centos base image and installs conda and java on top of it

FROM conda/miniconda3-centos7

MAINTAINER Pathmind

#Set workdir
WORKDIR /app

#Define Arguments
ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ARG AWS_DEFAULT_REGION='us-east-1'
ARG GIT_USERNAME
ARG GIT_PASSWORD

#define environment variables
ENV CLASS_SNIPPET='' \
	RESET_SNIPPET='' \
	REWARD_SNIPPET='reward = after[0] - before[0];' \
	METRICS_SNIPPET='' \
	DISCRETE_ACTIONS='125' \
	CONTINUOUS_OBSERVATIONS='70' \
	MAX_ITERATIONS='30' \
	RANDOM_SEED='1' \
	MAX_REWARD_MEAN='2147483647' \
	TEST_ITERATIONS='0' \
	LEARNING_RATES='0.001,1.0E-5' \
	GAMMAS='0.9,0.99' \
	BATCH_SIZES='128' \
	STEP_TIME='1' \
	STOP_TIME='420' \
	TIME_UNIT='MINUTE' \
	MAX_TIME_IN_SEC='1800' \
	AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} \
	AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} \
	AWS_DEFAULT_REGION=${AWS_DEFAULT_REGION} \
	PATH=/opt/conda/envs/env/bin:$PATH

#Install packages
RUN yum update -y 
RUN yum install epel-release -y
RUN yum install centos-release-scl scl-utils -y
RUN yum install maven -y
RUN yum install java-1.8.0-openjdk gcc-c++ java-1.8.0-openjdk-devel git wget devtoolset-7 rh-maven35 unzip bzip2 libXext libSM libXrender python-pip -y

#Create conda env
RUN conda update -n base -c defaults conda && \
	conda create -n pathmind python=3.6 && \
	echo "source activate pathmind" > ~/.bashrc 

#install pip packahes
RUN source activate pathmind && \
	conda install tensorflow pybind11 && \
	pip install --upgrade pip && \
	pip install setproctitle requests psutil gym ray[rllib]

#Install awscli
RUN curl "https://d1vvhvl2y92vvt.cloudfront.net/awscli-exe-linux-x86_64.zip" -o "/tmp/awscliv2.zip" && \
	cd /tmp/ && \
	unzip awscliv2.zip && \
	./aws/install

#build nativerl
RUN git clone https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/SkymindIO/skil-somatic-cloud.git && \
	scl enable devtoolset-7 rh-maven35 bash && \
	cd skil-somatic-cloud/nativerl && \
	source activate pathmind && \
	mvn -e clean package -Djavacpp.platform=linux-x86_64 && \
	cp -r target /usr/local/envs/pathmind/lib/python3.6/site-packages/nativerl

#Download s3 files
RUN aws2 s3 cp s3://sagemaker-files.pathmind.com/baseEnv.zip baseEnv.zip  && \
	aws2 s3 cp s3://sagemaker-files.pathmind.com/PathmindPolicy.jar PathmindPolicy.jar && \
	aws2 s3 cp s3://sagemaker-files.pathmind.com/model.zip model.zip


#Extract required files
RUN mkdir work  && \
	mv skil-somatic-cloud/nativerl/examples/train.sh work && \
	mv skil-somatic-cloud/nativerl/target/* work/ && \
	unzip -q baseEnv.zip  && \
	rm baseEnv.zip  && \
	mv baseEnv/* work/  && \
	rm -r baseEnv  && \
	mv PathmindPolicy.jar work/lib/  && \
	unzip -q model.zip -d work && \
	rm model.zip  && \
	echo > work/setup.sh  && \
	mkdir -p work/database  && \
	touch work/database/db.properties  && \
	#Activate conda before in train.sh
	sed -i '1s/^/cd work\n source activate pathmind\n/' work/train.sh  && \
	#Clean files
	rm -rf skil-somatic-cloud


#Start the Training
ENTRYPOINT ["/bin/bash", "-c", "source work/train.sh"]
