FROM ubuntu

#Set workdir
WORKDIR /app

#Copy files
COPY . .

#basic packages
RUN apt-get update && \
        apt-get install python jq curl -y && \
        curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py && \
        python get-pip.py && \
	pip install -r requirements.txt 

RUN chmod +x /app/clean_kops_aws.sh

# Install kubectl and kops
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl && \
    chmod +x ./kubectl && \
    mv ./kubectl /usr/local/bin/kubectl  && \
    curl -Lo kops https://github.com/kubernetes/kops/releases/download/1.15.0/kops-linux-amd64 && \
    chmod +x ./kops && \
    mv ./kops /usr/local/bin/

CMD ["bash","-c","/app/clean_kops_aws.sh"]
#CMD ["sleep","12h"]
