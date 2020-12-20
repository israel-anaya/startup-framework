echo OFF
echo -----------------------------------------------------------------
echo Startup Framework
echo -----------------------------------------------------------------

call mvn -f startup-starter-parent/pom.xml clean
call mvn -f startup-starter-core/pom.xml clean
call mvn -f startup-starter-data/pom.xml clean
call mvn -f startup-starter-ms-parent/pom.xml clean
call mvn -f startup-starter-msa-parent/pom.xml clean



