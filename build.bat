echo OFF
echo -----------------------------------------------------------------
echo Samples Startup Framework
echo -----------------------------------------------------------------

call mvn -f startup-starter-parent/pom.xml clean install
call mvn -f startup-starter-ms-parent/pom.xml clean install
call mvn -f startup-starter-core/pom.xml clean install
call mvn -f startup-starter-data/pom.xml clean install
