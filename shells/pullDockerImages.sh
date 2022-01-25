#!/bin/bash

if [ -z $1 ]
then
  echo "Please input Registry Host!!"
  exit 1
fi
docker pull $1:5000/redis
docker tag $1:5000/redis redis
docker pull $1:5000/jnacos
docker tag $1:5000/jnacos jnacos
docker pull $1:5000/nginx
docker tag $1:5000/nginx nginx

apps=('auth' 'fileserver' 'dashboards' 'report' 'gateway' 'platform' 'public' 'webservice' 'quartz')

for i in $(seq 0 8 )
do
  echo "*********************************Docker Pull ${apps[i]}******************************************"
  oldImages=`docker images | grep ${apps[i]} | awk '{print $3}'`
  docker rmi $oldImages
  docker pull $1:5000/${apps[$i]}
  docker tag $1:5000/${apps[$i]} ${apps[$i]}
done

docker pull $1:5000/bff
docker tag $1:5000/bff bff