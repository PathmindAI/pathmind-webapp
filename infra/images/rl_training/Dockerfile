FROM ubuntu

#Set workdir
WORKDIR /app

#Copy files
COPY requirements.txt .
COPY *.sh ./

#env
ENV DEBIAN_FRONTEND=noninteractive

#basic packages
RUN apt-get update && \
        apt-get install build-essential libssl-dev libffi-dev libxml2-dev libxslt1-dev zlib1g-dev -y && \
        apt-get install postgresql -y && \
        apt-get install rsync -y && \
        apt-get install python3 -y && \
        apt-get install python3-pip -y && \
        apt-get install curl -y && \
        apt-get install vim -y && \
        apt-get install jq -y && \
        apt-get install libarchive-tools -y && \
        apt-get install unzip -y && \
        apt-get install zip -y && \
        apt-get install python-dev -y && \
        apt-get install libsm-dev -y && \
        apt-get install libxrender-dev -y && \
        apt-get install libxext-dev -y && \
        apt-get install fontconfig -y && \
        pip3 install -r requirements.txt && \
        curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.17.0/bin/linux/amd64/kubectl && \
        chmod +x ./kubectl && \
        mv ./kubectl /usr/local/bin/kubectl 

#use bsdtar instead of tar
RUN cp /usr/bin/bsdtar /bin/tar

RUN chmod +x /app/*.sh

CMD ["bash","-c","/app/do_training.sh"]
#CMD ["bash","-c","sleep 300000000"]
