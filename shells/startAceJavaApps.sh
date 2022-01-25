#!/bin/bash

if [ -z $1 ]
then
  echo "Please input Nacos Address, like xx.xx.xx.xx"
  exit 1
fi

nacosAddress=$1":8848"

if [ -z $2 ]
then
  echo "Please input Redis host, like xx.xx.xx.xx"
  exit 1
fi

if [ -f "docker-compose.yml" ]
then
   cp docker-compose.yml docker-compose-cp.yml
   sed -i "s#{NacosAddress}#$nacosAddress#g" docker-compose-cp.yml
   sed -i "s#{RedisHost}#$2#g" docker-compose-cp.yml
   docker-compose -f docker-compose-cp.yml up -d
else
   echo "docker-compose.yml does not exist!! "
   exit 1
fi