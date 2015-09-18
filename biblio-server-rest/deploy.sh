#!/bin/bash

SSH_USERNAME=55fc24452d527107aa000144
SSH_HOST=bibliorest-adoorg.rhcloud.com
SSH_CMD=$SSH_USERNAME@$SSH_HOST
DIY_DIR=diy

echo "======================"
echo "Deploying to OpenShift"
echo "======================"
echo
echo "Host: $SSH_HOST"
echo

echo "> generate version.txt"
ls target/biblio-server-rest-*.jar|grep -oE "([0-9]\.[0-9]+)(-SNAPSHOT)?"> /tmp/version.txt
scp /tmp/version.txt $SSH_CMD:/var/lib/openshift/$SSH_USERNAME/$DIY_DIR
echo "Version: `cat /tmp/version.txt`"
rm /tmp/version.txt

echo "> copying scripts"
scp -r .openshift $SSH_CMD:/var/lib/openshift/$SSH_USERNAME/$DIY_DIR

echo "> deploying artifact"
mkdir -p /tmp/biblio-server-rest
cp target/biblio-server-rest*.jar /tmp/biblio-server-rest/biblio-server-rest.jar
scp /tmp/biblio-server-rest/biblio-server-rest.jar $SSH_CMD:/var/lib/openshift/$SSH_USERNAME/$DIY_DIR

echo "done"