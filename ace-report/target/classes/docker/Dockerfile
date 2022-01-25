#FROM openjdk:8-jdk-alpine
FROM acejdk
VOLUME /tmp
ADD report.jar app.jar
RUN mkdir Localization
COPY zh-CHS.xml /Localization/zh-CHS.xml
ADD start.sh start.sh
ENTRYPOINT ["/bin/sh","/start.sh"]