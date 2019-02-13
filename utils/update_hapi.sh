#!/bin/sh

# this script is not really used, it's just an example of how to check in
# a jar into the cvs maven repository

mvn install:install-file -DgroupId=ca.uhn -DartifactId=hapi -Dversion=0.5.1 -Dpackaging=jar -Dfile=/tmp/hapi-0.5.1.jar -DcreateChecksum=true -DgeneratePom=true -DlocalRepositoryPath=lib -DlocalRepositoryId=local-cvs

