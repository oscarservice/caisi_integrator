#!/bin/sh
##########
# This script should build the source forge archive we upload for deployment

pushd ${WORKING_ROOT}

	mvn clean package
	tar cvfz target/caisi_integrator_beta.tar.gz catalina_base set_env.sh
	
	echo ----------
	echo Run the following to upload the archive
	echo - sftp \<sf_user\>@frs.sourceforge.net
	echo - cd uploads
	echo - put ../target/caisi_integrator.tar.gz
	echo ----------

popd
