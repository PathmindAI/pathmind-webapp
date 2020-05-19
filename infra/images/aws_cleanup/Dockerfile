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

CMD ["bash","-c","/app/clean_kops_aws.sh"]
#CMD ["sleep","12h"]
