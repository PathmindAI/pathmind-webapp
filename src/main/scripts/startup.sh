/home/site/wwwroot/monitor_OOM.sh &
java -Xmx4096m -XX:+UseG1GC -Dvaadin.productionMode -jar /home/site/wwwroot/pathmind-0.0.1-SNAPSHOT.jar --server.port=80
