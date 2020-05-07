FROM base

WORKDIR /app

COPY . .

RUN mvn package -Pproduction 

EXPOSE 80
CMD ["java", "-Xmx4096m", "-XX:+UseG1GC", "-Dvaadin.productionMode", "-jar", "/app/pathmind-webapp/target/pathmind-webapp-0.0.1-SNAPSHOT.jar", "--server.port=80"]
