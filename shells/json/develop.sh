#!/bin/bash
set -e

currdir=$(cd $(dirname $0); pwd)
cd $currdir

function getDockerIP {
  local imageName=$1
  echo $imageName
  contaninerName=`docker ps -a | grep $imageName"_1" | awk '{print $NF}'`
#  ip=`docker inspect $contaninerName | grep IPAddress | grep "[1-9]\{1,\}\.[1-9]" | awk -F\" '{print $4}'`
  ip=`docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $contaninerName`
}

ip=""

if [ -z $1 ]
then
  echo "Please input Registry Host!!"
  exit 1
fi

if [ -z $2 ]
then
  echo "Please input Redis Host!!"
  exit 1
fi

#拉取最新镜像，参数为镜像仓库的IP
sh pullDockerImages.sh $1

#清理docker镜像
python aceDevelop.py -f aceApps.json rm -f
python aceDevelop.py -f aceNode.json rm -f
python aceDevelop.py -f aceEnv.json rm -f

#安装jnacos、redis
cd $currdir
python aceDevelop.py -f aceEnv.json up -d jnacos

#安装ACE后台服务
#获取到jnacos以及redis的IP
getDockerIP jnacos
jnacosIp=$ip
echo "[INFO] jnacosIp: $jnacosIp"
#getDockerIP redis
redisIp=$2
echo "[INFO] redisIp: $redisIp"

sh startAceJavaApps.sh $jnacosIp $redisIp

#安装nginx
cd $currdir
if [ -d "/opt/nginx/" ]
then
  rm -rf /opt/nginx/
fi
mkdir -p /opt/nginx/conf /opt/nginx/logs
cp -r nginx.conf /opt/nginx/conf/
getDockerIP gateway
gatewayIp=$ip
echo "[INFO] gatewayIp: $gatewayIp"
sed -i "s#{gatewayIp}#$gatewayIp#g" /opt/nginx/conf/nginx.conf

docker-compose -f docker-compose-env.yml up -d nginx

#安装bff
getDockerIP nginx
nginxIp=$ip
echo "[INFO] nginxIp: nginxIp"
sh startAceNodeApps.sh $jnacosIp "http://"$nginxIp

