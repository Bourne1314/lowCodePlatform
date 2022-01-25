#!/bin/bash

set -e

currdir=$(cd $(dirname $0); pwd)
cd $currdir
cd ..
mvn -U clean install -pl ace-common,ace-dbplus,ace-interface,ace-file,ace-platform-core,ace-fileserver-core,ace-orgauth-core,ace-quartz-core  -DskipTests=true -Dmaven.javadoc.skip=true -am


apps=('fileserver' 'gateway' 'platform' 'orgauth' 'report' 'auth' 'quartz' 'dashboards' 'public')

for i in $(seq 0 3 )
do
   echo "[INFO]*****************************Docker Build ${apps[i]}******************************************"
   dir="ace-"${apps[$i]}
     if [ -d $dir ]
     then
       cd $dir
       cd src/main/resources
       if [ -f "bootstrap.yml" ]
       then
          sed -i 's#server-addr.*##g' bootstrap.yml
       fi
       cd -
       cp -r ../shells/start.sh src/main/resources/docker
       mvn -U clean install -DskipTests=true -P prod
       cd ..
     else
       echo "[ERROR]*********************************$dir doexs not exist!!*********************************"
     fi
done

exit 0

echo "[INFO]*****************************Install ace-platform-single JAR******************************************"
cd ace-platform/
cp -r ../shells/start.sh src/main/resources/docker
mvn clean install -DskipTests=true -f pom-single.xml
cd ..

echo "[INFO]*****************************Docker Build ace-webservice******************************************"
cd ace-webservice/
cp -r ../shells/start.sh src/main/resources/docker
mvn clean install -DskipTests=true -P prod
cd ..
