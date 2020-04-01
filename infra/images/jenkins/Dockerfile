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

#Install python packages
RUN apt update && \
	apt-get install postgresql-client -y && \
	apt-get install maven -y && \
	apt-get install -y unzip xvfb libxi6 libgconf-2-4 && \
	apt-get install -y default-jdk  && \
	apt install python-pip -y && \
	pip install awscli

#install chhrome driver
RUN curl -sS -o - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add && \
	echo "deb [arch=amd64]  http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list && \
	apt-get -y update && \
	apt-get -y install google-chrome-stable && \
	wget https://chromedriver.storage.googleapis.com/2.41/chromedriver_linux64.zip && \
	unzip chromedriver_linux64.zip && \
	mv chromedriver /usr/bin/chromedriver && \
	chmod +x /usr/bin/chromedriver

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

# Install kubectl and helm
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl && \
    chmod +x ./kubectl && \
    mv ./kubectl /usr/local/bin/kubectl && \
    curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 > get_helm.sh && \
    chmod 700 get_helm.sh && \
    ./get_helm.sh


#RUN java -jar /var/jenkins_home/war/WEB-INF/jenkins-cli.jar -s http://localhost:8080/ -auth admin:password reload-configuration
