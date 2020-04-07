FROM ubuntu

#Define ENV
ARG AWS_DEFAULT_REGION
ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ARG NAME
ARG BUCKET_NAME
ENV NAME=${NAME}
ENV KOPS_STATE_STORE="s3://${BUCKET_NAME}/k8s.${NAME}"
ENV DOCKER_HOST=tcp://localhost:2375
ENV TESTS_USERNAME=evegeniy@skymind.io
ENV TESTS_PASSWORD='pw!skymind19'

#Install python packages
RUN apt update && \
	apt-get install ssh -y && \
	apt-get install git -y && \
	apt-get install postgresql-client -y && \
	apt-get install maven -y && \
	apt-get install -y unzip xvfb libxi6 libgconf-2-4 && \
	apt-get install -y default-jdk  && \
	apt install python-pip -y && \
	pip install awscli

#Github key
COPY id_rsa .
RUN chmod 600 id_rsa && \
        mkdir -p ~/.ssh && \
        mv id_rsa ~/.ssh && \
        ssh-keyscan -H github.com >> /etc/ssh/ssh_known_hosts

#install chhrome driver
RUN apt-get update &&\
	apt-get install -y curl unzip xvfb libxi6 libgconf-2-4 && \
	curl -sS -o - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add && \
	echo "deb [arch=amd64]  http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list && \
	apt-get -y update &&\
	apt-get -y install google-chrome-stable && \
	wget https://chromedriver.storage.googleapis.com/2.41/chromedriver_linux64.zip &&\
	unzip chromedriver_linux64.zip && \
	mv chromedriver /usr/bin/chromedriver && \
	chown root:root /usr/bin/chromedriver && \
	chmod +x /usr/bin/chromedriver

# Install Docker
RUN apt-get -qq update && \
    curl -sSL https://get.docker.com/ | sh

# Install kubectl and helm
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl && \
    chmod +x ./kubectl && \
    mv ./kubectl /usr/local/bin/kubectl && \
    curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 > get_helm.sh && \
    chmod 700 get_helm.sh && \
    ./get_helm.sh && \
    curl -Lo kops https://github.com/kubernetes/kops/releases/download/$(curl -s https://api.github.com/repos/kubernetes/kops/releases/latest | grep tag_name | cut -d '"' -f 4)/kops-linux-amd64 && \
    chmod +x ./kops && \
    mv ./kops /usr/local/bin/

#Configure AWS
RUN printf "%s\n%s\n${AWS_DEFAULT_REGION}\njson" "${AWS_ACCESS_KEY_ID}" "${AWS_SECRET_ACCESS_KEY}" | aws configure

#Configure kube
RUN kops export kubecfg --name ${NAME}
