FROM acejdk
VOLUME /tmp
ADD fileserver.jar app.jar
ADD start.sh start.sh
ENTRYPOINT ["/bin/sh","/start.sh"]