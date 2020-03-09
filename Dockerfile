FROM azul/zulu-openjdk:11.0.5

WORKDIR /app

COPY . .

RUN apt-get update -y && \
	apt-get install wget -y && \
	apt-get install rsync -y && \
	wget https://deb.nodesource.com/setup_13.x && \
	bash ./setup_13.x && \
	apt-get update -y && \ 
	apt-get install nodejs -y && \
	apt-get install maven -y 
RUN mvn package -Pproduction 

EXPOSE 80
CMD ["java", "-Xmx4096m", "-XX:+UseG1GC", "-Dvaadin.productionMode", "-jar", "/app/target/pathmind-0.0.1-SNAPSHOT.jar", "--server.port=80"]
