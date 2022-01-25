#!/usr/bin/python
# -*- coding: UTF-8 -*-

import os
import sys
import json

def hasFile(file):
    return os.path.exists(file)

def upService(services,cmd):
    print services
    for image in services:
        #是否只启动指定的服务
        apps = []
        if not cmd == [] and not cmd[0].startswith('-'):
            apps = cmd
        if not cmd == [] and cmd[0].startswith('-'):
            apps = cmd[1:]
        if not apps == [] and apps.count(image) == 0:
            break
        print '[INFO]create: ' + image + ' service'
        str = 'run '
        if not cmd == [] and cmd[0].startswith('-'):
            # -itd
            str += cmd[0] + ' '
        str += '--name 1_' + image + ' '
        #配置项
        configs = services[image]
        for key in configs:
            if key == 'expose':
                str += '--expose=' + configs[key] + ' '
            elif key == 'ports':
                str += '-p ' + configs[key] + ' '
            elif key == 'environment':
                environments = configs[key]
                for environment in environments:
		    print environment
                    str += '-e ' + environment + ' '
            elif key == 'volumes':
                volumes = configs[key]
                for volume in volumes:
		    print volume
                    str += '-v ' + volume + ' '
        str = 'docker ' + str + image
        print '[INFO]execute: ' + str
        os.system(str)

def rmService(services,cmd):
    for image in services:
        if not cmd == [] and cmd.count(image) == 0:
            break
        print '[INFO]delete: ' + key + ' service'
        str = 'docker rm -f' + image
        print '[INFO]excute: ' + str
        os.system(str)


def addService(services,cmds):
    executeDockerCommand('',[],'auth',services[key])

def getServiceNum(image):
    executeDockerCommand('',[],'auth',services[key])

def executeDockerCommand(oprate,cmd,image,configs):
    str = oprate + ' '
    if not cmd == []:
        str += cmd[0] + ' '
    str += '--name 1_' + image + ' '
    for key in configs:
        if key == 'expose':
            str += '--expose=' + configs[key] + ' '
        elif key == 'ports':
            str += '-p ' + configs[key] + ' '
        elif key == 'environment':
            environments = configs[key]
            for environment in environments:
                str += '-e ' + configs[key] + ' '
        elif key == 'volume':
            volumes = configs[key]
            for volume in volumes:
                str += '-v ' + configs[key] + ' '
    str = 'docker ' + str + image
    print '执行 ' + str
    os.system(str)

def readJson(file):
    print file
    f = open(file,'r')
    res = f.read()
    return json.loads(res)

def main():
    args = sys.argv[1:]
    file = ''
    cmds = []
    if  args[0] == '-f':
        file = args[1]
        if not hasFile(file):
            print '请指定配置文件'
            sys.exit(-1)
        cmds = args[2:]
    else:
        file = 'aceServices.json'
        if not hasFile(file):
            print '请指定配置文件'
            sys.exit(-1)
        cmds = args
    services = readJson(file)
    oprate = cmds[0]
    if oprate == 'up':
        upService(services,cmds[1:])
    elif oprate == 'rm':
        rmService(services,cmds[1:])
    elif oprate == 'add':
        addService(services,cmds[1:])

main()
