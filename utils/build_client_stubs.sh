#!/bin/sh

rm -rf ../target/client_stubs
rm -rf ../target/client_stubs_classes

#for foo in `cat services.txt` 
#do
#	echo $foo
#	ant -f build_client_stubs.xml -DserviceName=${foo}
#done
#
#ant -f build_client_stubs.xml compile_client_stubs

ant -f build_client_stubs.xml


rm -f ~/.m2/repository/org/oscarehr/caisi_integrator/caisi_integrator_client_stubs/SNAPSHOT/*.jar
mvn install:install-file -DgroupId=org.oscarehr.caisi_integrator -DartifactId=caisi_integrator_client_stubs -Dversion=SNAPSHOT -Dpackaging=jar -Dfile=../target/caisi_integrator_client_stubs.jar -DcreateChecksum=true -DgeneratePom=true -DlocalRepositoryPath=../../oscar/local_repo -DlocalRepositoryId=local_repo


