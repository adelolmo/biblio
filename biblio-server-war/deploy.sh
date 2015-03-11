#!/bin/bash

echo "======================"
echo "Deploying to OpenShift"
echo "======================"
echo

echo "> generate version.txt"
ls target/biblio-server-war-*.war|grep -oE "([0-9]\.[0-9]+)(-SNAPSHOT)?"> /tmp/version.txt
scp /tmp/version.txt 54d06aed4382ecb74b00008b@biblio-adoorg.rhcloud.com:/var/lib/openshift/54d06aed4382ecb74b00008b/app-root/dependencies/jbossews/webapps/
rm /tmp/version.txt
echo "Version: `cat /tmp/version.txt`"

echo "> deploying artifact"
mkdir -p /tmp/biblio-server-war
cp target/biblio-server-war*.war /tmp/biblio-server-war/ROOT.war
scp /tmp/biblio-server-war/ROOT.war 54d06aed4382ecb74b00008b@biblio-adoorg.rhcloud.com:/var/lib/openshift/54d06aed4382ecb74b00008b/app-root/dependencies/jbossews/webapps/
echo "done"