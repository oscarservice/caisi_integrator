#!/bin/sh
mvn -Dcheckstyle.skip=true test -Dtest=ResetDevelopmentTestData
