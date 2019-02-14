#!/bin/sh

# This script is used to setup the config to run the server / tomcat.

export WORKING_ROOT=`pwd`

export CATALINA_BASE=${WORKING_ROOT}/catalina_base

export JAVA_OPTS="-Djava.awt.headless=true -server -Xincgc -Xshare:off -Dcom.sun.management.jmxremote  -Xms384m -Xmx384m -XX:MaxNewSize=64m -XX:MaxPermSize=96m"

