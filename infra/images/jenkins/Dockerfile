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
        apt-get install jq -y && \
        apt-get install -y unzip xvfb libxi6 libgconf-2-4 && \
        apt install python-pip -y && \
        pip install awscli

#Install JDK
RUN mkdir /var/cache/oracle-jdk11-installer-local/ && \
        wget http://s3.amazonaws.com/public-pathmind.com/jdk-11.0.7_linux-x64_bin.tar.gz -P /var/cache/oracle-jdk11-installer-local/ && \
        echo "oracle-java11-installer shared/accepted-oracle-license-v1-2 select true" | debconf-set-selections && \
        echo "oracle-java11-installer shared/accepted-oracle-license-v1-2 seen true" | debconf-set-selections && \
        echo "deb http://ppa.launchpad.net/linuxuprising/java/ubuntu focal main" | tee /etc/apt/sources.list.d/linuxuprising-java.list && \
        apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 73C3DB2A && \
        apt-get update && \
        apt-get install oracle-java11-installer-local -y && \
        apt install oracle-java11-set-default-local
ENV JAVA_HOME="/usr/lib/jvm/java-11-oracle"
ENV PATH="/usr/lib/jvm/java-11-oracle/bin:${PATH}"
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

