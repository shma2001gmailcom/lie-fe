#!/bin/bash
##
## script for comfortable developing with tomcat and maven
##
## 1. cd to appfolder dir
## 2. run mvn clean install
## 3. stop server
## 4. remove old war and deployment
## 5. copy new war from target dir to server webapp dir
## 6. start server
## 7. open project home page
appname="lie-fe"
############### properties ####################
################ AT WORK ######################
###############################################
#appfolder="/home/mshevelin/workspace/"${appname}"-assembla"
#tomcatfolder="/home/mshevelin/workspace/tomcat6"
#javahome="/usr/lib/jvm/jdk-6-oracle"

################################################
################ AT HOME #######################
################################################
appfolder="/home/misha/workspace/"${appname}/lie-fe
tomcatfolder="/home/misha/workspace/tomcat"
M3_HOME='~/workspace/apache-maven-3.3.9'
export M3_HOME
M3=${M3_HOME}/bin
export M3
PATH=${PATH}:${M3}
export PATH
logfile='./1'

################################################
#export JAVA_HOME=${javahome}
tomcatbin=${tomcatfolder}/bin
tomcatwebapps=${tomcatfolder}/webapps
if [ ! -e ${appfolder} ]; then echo 'ERROR: no appfolder' ${appfolder} 'found';exit 1; fi
if [ ! -e ${tomcatbin} ]; then echo 'ERROR: no tomcatbin found';exit 1; fi
cd ${appfolder}
mvn clean install $@ | tee out.txt ; test ${PIPESTATUS[0]} -eq 0
if [ ${PIPESTATUS[0]} -ne "0" ]; then
    echo ===================================================
    echo maven build failed, see output for details;exit 1;
    echo ===================================================
fi
cd ${tomcatbin}
if [ "$(ps axf | grep catalina | grep -v grep)" ]; then
    echo ///////////////////////////
    echo        stopping tomcat...
    bash shutdown.sh
    sleep 5
    echo ///////////////////////////
    echo        tomcat has been stopped
fi
cd ${tomcatwebapps}
if [ -e ${tomcatwebapps}/${appname} ]; then
    echo ///////////////////////////
    echo      remove old deployment...
    rm -rf -- ${tomcatwebapps}/${appname}
fi
if [ -e  ${tomcatwebapps}/${appname}.war ]; then
    echo ///////////////////////////
    echo      remove old war...
    rm -rf -- ${tomcatwebapps}/${appname}.war
fi
echo ///////////////////////////
echo      deploying new version...
cp  ${appfolder}/target/${appname}.war ${tomcatwebapps}/${appname}.war
cd ${tomcatbin}
echo ///////////////////////////
echo         starting tomcat...
echo ///////////////////////////
bash startup.sh
sleep 5
#firefox "http://localhost:8080/"${appname}
chromium-browser "http://localhost:8080/"${appname}
