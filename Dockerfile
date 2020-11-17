# Step 1: build jar with maven
FROM base as webapp-build
WORKDIR /app
COPY . .
RUN mvn package -Pproduction

# Step 2: copy only jar to minimized zulu container
FROM azul/zulu-openjdk-alpine:11
COPY --from=webapp-build /app/pathmind-webapp/target/pathmind-webapp-0.0.1-SNAPSHOT.jar /app/pathmind-webapp/target/pathmind-webapp-0.0.1-SNAPSHOT.jar
EXPOSE 80
CMD ["java", "-Xmx4096m", "-XX:+UseG1GC", "-Dvaadin.productionMode", "-jar", "/app/pathmind-webapp/target/pathmind-webapp-0.0.1-SNAPSHOT.jar", "--server.port=80"]
