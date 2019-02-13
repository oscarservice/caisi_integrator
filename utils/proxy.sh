#!/bin/sh

CLASSPATH=${WORKING_ROOT}/target/classes

java -classpath ${CLASSPATH} org.oscarehr.caisi_integrator.util.Proxy 8086 127.0.0.1 8085
