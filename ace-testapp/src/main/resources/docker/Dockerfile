FROM acejdk
VOLUME /tmp
ADD testapp.jar app.jar
ADD start.sh start.sh
ENTRYPOINT ["/bin/sh","/start.sh"]