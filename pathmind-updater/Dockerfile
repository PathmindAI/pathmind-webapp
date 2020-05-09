FROM base

WORKDIR /app

COPY . .

RUN mvn package -Pproduction 

CMD ["java", "-Xmx4096m", "-XX:+UseG1GC", "-jar", "/app/pathmind-updater/target/pathmind-updater-0.0.1-SNAPSHOT.jar"]
