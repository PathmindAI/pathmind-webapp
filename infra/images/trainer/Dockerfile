FROM ubuntu

#Set workdir
WORKDIR /app

#Copy files
COPY spot_ig_template*.yaml ./
COPY trigger.py .
COPY LoggerInit.py .
COPY requirements.txt .
COPY rl_training_deployment.yaml .

#basic packages
RUN apt-get update && \
	apt-get install postgresql -y && \
	apt-get install python -y && \
	apt-get install curl -y && \
	apt-get install vim -y && \
	apt-get install jq -y && \
	apt-get install libpq-dev -y && \
	curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py && \
	python get-pip.py && \
	pip install -r requirements.txt && \
	curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.17.0/bin/linux/amd64/kubectl && \
	chmod +x ./kubectl && \
	mv ./kubectl /usr/local/bin/kubectl && \
	curl -Lo kops https://github.com/kubernetes/kops/releases/download/1.15.0/kops-linux-amd64 && \
	chmod +x ./kops && \
	mv ./kops /usr/local/bin/

CMD ["python","trigger.py"]
#CMD ["bash","-c","sleep 10000000"]
