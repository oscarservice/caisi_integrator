#!/bin/sh

pushd ${WORKING_ROOT}

	MYSQLDUMP_OPTIONS="-d"
	SED_OPTIONS="s/\(ENGINE=InnoDB\) AUTO_INCREMENT=[0-9]*/\1/g"
	
	mysqldump ${MYSQLDUMP_OPTIONS} caisi_integrator | sed "${SED_OPTIONS}" > target/local_schema.sql
	ssh integrator.caisi.ca "mysqldump ${MYSQLDUMP_OPTIONS} caisi_integrator | sed \"${SED_OPTIONS}\"" > target/remote_schema.sql
	
popd