#!/bin/bash

if [ -z $1 ]
then
  echo "Please input Nacos Address, like xx.xx.xx.xx"
  exit 1
fi

nacosAddress=$1":8848"

if [ -z $2 ]
then
  echo "Please input Gateway Or Nginx Address, like http://xx.xx.xx.xx:xxxx"
  exit 1
fi

if [ -f "docker-compose-node.yml" ]
then
   cp docker-compose-node.yml docker-compose-node-cp.yml
   sed -i "s#{NacosAddress}#$nacosAddress#g" docker-compose-node-cp.yml
   sed -i "s#{GatewayOrNginxUrl}#$2#g" docker-compose-node-cp.yml
   docker-compose -f docker-compose-node-cp.yml up -d
else
   echo "docker-compose.yml does not exist!! "
   exit 1
fi