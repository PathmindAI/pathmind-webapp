FROM ubuntu

#Set workdir
WORKDIR /app

#Copy files
COPY trigger.py .
COPY build_and_push.sh .
COPY run_helm.sh .
COPY LoggerInit.py .
COPY requirements.txt .

#env
ENV DEBIAN_FRONTEND=noninteractive

#basic packages
RUN apt-get update && \
	apt-get install postgresql -y && \
	apt-get install git -y && \
        apt-get install python3 -y && \
        apt-get install python3-pip -y && \
	apt-get install curl -y && \
	apt-get install vim -y && \
	apt-get install jq -y && \
	apt-get install libpq-dev -y && \
	pip3 install -r requirements.txt && \
	curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.17.0/bin/linux/amd64/kubectl && \
	chmod +x ./kubectl && \
	mv ./kubectl /usr/local/bin/kubectl && \
        curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 > get_helm.sh && \ 
        chmod 700 get_helm.sh && \
        ./get_helm.sh

# Install Docker
RUN curl -sSL https://get.docker.com/ | sh

#Github key
COPY id_rsa .
RUN chmod 600 id_rsa && \
    mkdir -p ~/.ssh && \
    mv id_rsa ~/.ssh && \
    ssh-keyscan -H github.com >> /etc/ssh/ssh_known_hosts

CMD ["python3","trigger.py"]
#CMD ["bash","-c","sleep 10000000"]
