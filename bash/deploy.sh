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
app_name="lie-fe"
############### properties ####################
################ AT WORK ######################
###############################################
#appfolder="/home/mshevelin/workspace/"${appname}"-assembla"
#tomcat_folder="C:\Users\mikhail_shevelin\workspace\tomcat8"
java_home="C:\jdk1.8.0_171"

################################################
################ AT HOME #######################
################################################
app_folder="C:/Users\mikhail_shevelin/workspace/lie-fe/"${app_name}
tomcat_folder="C:/Users/mikhail_shevelin/workspace/tomcat8"
M2_HOME='C:/maven-3.5.4'
export M2_HOME
M2=${M2_HOME}/bin
export M2
PATH=${PATH}:${M2}
export PATH
mvn=${M2}/mvn.cmd
logfile='./1'

################################################
export JAVA_HOME=${java_home}
tomcat_bin=${tomcat_folder}/bin
tomcat_web_apps=${tomcat_folder}/webapps
if [[ ! -e ${app_folder} ]]; then echo 'ERROR: no app folder' ${app_folder} 'found';exit 1; fi
if [[ ! -e ${tomcat_bin} ]]; then echo 'ERROR: no tomcat/bin found';exit 1; fi
cd ${app_folder}
${mvn} clean install $@ | tee out.txt ; test ${PIPESTATUS[0]} -eq 0
if [[ ${PIPESTATUS[0]} -ne "0" ]]; then
    echo ===================================================
    echo maven build failed, see output for details;exit 1;
    echo ===================================================
fi
cd ${tomcat_bin}
if [[ "$(ps axf | grep catalina | grep -v grep)" ]]; then
    echo ///////////////////////////
    echo        stopping tomcat...
    bash shutdown.sh
    sleep 5
    echo ///////////////////////////
    echo        tomcat has been stopped
fi
bash shutdown.sh
cd ${tomcat_web_apps}
if [[ -e ${tomcat_web_apps}/${app_name} ]]; then
    echo ///////////////////////////
    echo      remove old deployment...
    rm -rf -- ${tomcat_web_apps}/${app_name}
fi
if [[ -e  ${tomcat_web_apps}/${app_name}.war ]]; then
    echo ///////////////////////////
    echo      remove old war...
    rm -rf -- ${tomcat_web_apps}/${app_name}.war
fi
echo ///////////////////////////
echo      deploying new version...
cp  ${app_folder}/target/${app_name}.war ${tomcat_web_apps}/${app_name}.war
cd ${tomcat_bin}
echo ///////////////////////////
echo         starting tomcat...
echo ///////////////////////////
bash startup.sh
sleep 5
# firefox "http://localhost:8080/"${appname}
"C:/Program Files (x86)/Google/Chrome/Application/chrome.exe" "http://localhost:8080/"${app_name}
