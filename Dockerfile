FROM base

WORKDIR /app

COPY . .

#basic packages
RUN apt-get update && \
        apt-get install python -y && \
        apt-get install curl -y && \
        apt-get install vim -y && \
	curl -O https://bootstrap.pypa.io/get-pip.py && \
        python get-pip.py && \
        pip install awscli 

RUN chmod +x entrypoint.sh

RUN mvn package -Pproduction

EXPOSE 80
