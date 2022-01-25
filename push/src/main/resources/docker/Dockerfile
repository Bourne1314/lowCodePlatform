FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD push.jar app.jar
ADD start.sh start.sh
ENTRYPOINT ["/bin/sh","/start.sh"]