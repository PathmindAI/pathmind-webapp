FROM ubuntu

#Set workdir
WORKDIR /app

#Copy files
COPY trigger.py .
COPY LoggerInit.py .
COPY requirements.txt .
COPY rl_training_deployment*.yaml ./

#env
ENV DEBIAN_FRONTEND=noninteractive

ENV TZ=UTC

#basic packages
RUN apt-get update && \
	apt-get install postgresql-client -y && \
        apt-get install python3 -y && \
	apt-get install python3-pip -y && \
	apt-get install curl -y && \
	apt-get install vim -y && \
	apt-get install jq -y && \
	apt-get install libpq-dev -y && \
	pip3 install -r requirements.txt && \
	curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.17.0/bin/linux/amd64/kubectl && \
	chmod +x ./kubectl && \
	mv ./kubectl /usr/local/bin/kubectl 

CMD ["python3","trigger.py"]
#CMD ["bash","-c","sleep 10000000"]
