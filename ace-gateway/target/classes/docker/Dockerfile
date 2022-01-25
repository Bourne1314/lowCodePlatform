FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD gateway.jar app.jar
ADD start.sh start.sh
ENTRYPOINT ["/bin/sh","/start.sh"]