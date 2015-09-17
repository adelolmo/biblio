#!/bin/bash

echo "======================"
echo "Deploying to OpenShift"
echo "======================"
echo

echo "> generate version.txt"
ls target/biblio-server-rest-*.jar|grep -oE "([0-9]\.[0-9]+)(-SNAPSHOT)?"> /tmp/version.txt
scp /tmp/version.txt 55f95d872d5271d059000008@bibliorest-adoorg.rhcloud.com:/var/lib/openshift/55f95d872d5271d059000008/diy
echo "Version: `cat /tmp/version.txt`"
rm /tmp/version.txt

echo "> copying scripts"
scp -r .openshift 55f95d872d5271d059000008@bibliorest-adoorg.rhcloud.com:/var/lib/openshift/55f95d872d5271d059000008/diy

echo "> deploying artifact"
mkdir -p /tmp/biblio-server-rest
cp target/biblio-server-rest*.jar /tmp/biblio-server-rest/biblio-server-rest.jar
scp /tmp/biblio-server-rest/biblio-server-rest.jar 55f95d872d5271d059000008@bibliorest-adoorg.rhcloud.com:/var/lib/openshift/55f95d872d5271d059000008/diy

echo "done"