FROM jenkins/jenkins:lts

# Running as root to have an easy support for Docker
USER root

# A default admin user
ENV ADMIN_USER=admin \
    ADMIN_PASSWORD=password

#Github key
COPY id_rsa .
RUN chmod 600 id_rsa && \
        mkdir -p ~/.ssh && \
        mv id_rsa ~/.ssh && \
        ssh-keyscan -H github.com >> /etc/ssh/ssh_known_hosts

# Jenkins init scripts
COPY *groovy /usr/share/jenkins/ref/init.groovy.d/
COPY clean_test_s3.sh /usr/bin/clean_test_s3.sh
COPY sqs_policy.json /tmp/sqs_policy.json
RUN chmod +x /usr/bin/clean_test_s3.sh

#Install python packages
RUN apt update && \
        apt-get install postgresql-client -y && \
        apt-get install maven -y && \
        apt-get install jq -y && \
        apt-get install -y unzip xvfb libxi6 libgconf-2-4 && \
        apt install python-pip -y && \
        apt install nawk -y && \
        pip install awscli

#Install JDK
RUN  cd /tmp/ && \
	wget https://download.java.net/openjdk/jdk11/ri/openjdk-11+28_linux-x64_bin.tar.gz && \
	tar zxvf /tmp/openjdk-11+28_linux-x64_bin.tar.gz && \
	mv jdk-11 /usr/lib/

ENV JAVA_HOME="/usr/lib/jdk-11"
ENV PATH="/usr/lib/jdk-11/bin:${PATH}"

#Install nodejs
RUN curl -sL https://deb.nodesource.com/setup_12.x | bash - && \
        apt-get install -y nodejs

# Install plugins at Docker image build time
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/install-plugins.sh $(cat /usr/share/jenkins/plugins.txt) && \
    mkdir -p /usr/share/jenkins/ref/ && \
    echo lts > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state && \
    echo lts > /usr/share/jenkins/ref/jenkins.install.InstallUtil.lastExecVersion

# Install Docker
RUN apt-get -qq update && \
    apt-get -qq -y install curl && \
    curl -sSL https://get.docker.com/ | sh

# Install kubectl, kops and helm
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl && \
    chmod +x ./kubectl && \
    mv ./kubectl /usr/local/bin/kubectl && \
    curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 > get_helm.sh && \
    chmod 700 get_helm.sh && \
    ./get_helm.sh

#Install pg 11
RUN apt-get update && \
    apt-get install -y wget vim && \
    apt-get install gnupg -y && \
    wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc |  apt-key add - && \
    apt-get install lsb-core -y && \
    RELEASE=$(lsb_release -cs) && \
    echo "deb http://apt.postgresql.org/pub/repos/apt/ ${RELEASE}"-pgdg main |  tee  /etc/apt/sources.list.d/pgdg.list && \
    apt-get update && \
    apt-get -y install postgresql-client-11

#Junit
COPY junit/* /usr/bin/

RUN chmod +x /usr/bin/flaky.py  
RUN chmod +x /usr/bin/xml2json.py
