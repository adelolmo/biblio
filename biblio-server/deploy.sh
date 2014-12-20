#!/bin/bash
INSTALLATION_DIRECTORY="/opt/biblio-server"
echo -n "Enter the installation directory [$INSTALLATION_DIRECTORY]:"
read option
if [ ! -z "$option" ]; then
    INSTALLATION_DIRECTORY="$option"
    echo "* Directory: $INSTALLATION_DIRECTORY"
fi

echo "Installing in $INSTALLATION_DIRECTORY..."

if [ ! -d $INSTALLATION_DIRECTORY ]; then
    echo "* create directory $INSTALLATION_DIRECTORY"
    mkdir $INSTALLATION_DIRECTORY
fi

mvn clean install
ssh root@peke "mkdir $INSTALLATION_DIRECTORY"
scp target/biblio-server*.tar.gz root@peke:/tmp
ssh root@peke "tar zxvf /tmp/biblio-server*.tar.gz -C $INSTALLATION_DIRECTORY"