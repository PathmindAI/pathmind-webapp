FROM ubuntu

#Set workdir
WORKDIR /app

#Copy files
COPY requirements.txt .
COPY do_training.sh .

#basic packages
RUN apt-get update && \
        apt-get install postgresql -y && \
	apt-get install rsync -y && \
        apt-get install python -y && \
        apt-get install curl -y && \
        apt-get install vim -y && \
        apt-get install jq -y && \
        apt-get install bsdtar -y && \
        apt-get install unzip -y && \
        apt-get install zip -y && \
        apt-get install libsm-dev -y && \
        apt-get install libxrender-dev -y && \
        apt-get install libxext-dev -y && \
        apt-get install fontconfig -y && \
        curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py && \
        python get-pip.py && \
        pip install -r requirements.txt && \
        curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.17.0/bin/linux/amd64/kubectl && \
        chmod +x ./kubectl && \
        mv ./kubectl /usr/local/bin/kubectl && \
        curl -Lo kops https://github.com/kubernetes/kops/releases/download/1.15.0/kops-linux-amd64 && \
        chmod +x ./kops && \
        mv ./kops /usr/local/bin/

#use bsdtar instead of tar
RUN cp /usr/bin/bsdtar /bin/tar

RUN chmod +x /app/do_training.sh

CMD ["bash","-c","/app/do_training.sh"]
#CMD ["bash","-c","sleep 300000000"]

