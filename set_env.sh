#!/bin/sh

# This script is used to setup the config to run the server / tomcat.

export WORKING_ROOT=`pwd`

export CATALINA_BASE=${WORKING_ROOT}/catalina_base

MEM_SETTINGS="-Xms384m -Xmx384m -Xss128k -XX:MaxNewSize=64m -XX:MaxPermSize=96m "
export JAVA_OPTS="-Djava.awt.headless=true -server -Xincgc -Xshare:off -Dcom.sun.management.jmxremote -Dorg.apache.cxf.Logger=org.apache.cxf.common.logging.Log4jLogger -Dlog4j.override.configuration=${WORKING_ROOT}/integrator_log4j.xml -Dcaisi_integrator_config=integrator_override.xml "${MEM_SETTINGS}

