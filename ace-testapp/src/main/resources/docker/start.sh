#Java虚拟机启动参数

#减少tomcat启动时间
JVM_OPTS=" -Djava.security.egd=file:/dev/./urandom "

#SPRING_OPTS
#spring 的启动参数，由docker创建容器时带入
#docker run -d -e SPRING_OPTS=" -Dspring.application.name=XXX -Dspring.application.name=XXX  " image
echo "Asia/Shanghai" > /etc/timezone

if [ -f "/etc/profile" ]
then
  source /etc/profile
fi

java $JVM_OPTS -jar  $SPRING_OPTS /app.jar