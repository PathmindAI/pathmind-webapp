FROM base

WORKDIR /app

COPY . .

#basic packages
RUN apt-get update && \
        apt-get install python3 -y && \
        apt-get install python3-pip -y && \
        apt-get install curl -y && \
        apt-get install vim -y && \
	curl -O https://bootstrap.pypa.io/get-pip.py && \
        pip3 install awscli && \
        aws --version

RUN chmod +x entrypoint.sh

RUN mvn package -Pproduction

EXPOSE 80
