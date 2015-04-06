#!/bin/bash
INSTALLATION_DIRECTORY="/opt/biblio-desktop"
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

#rm -Rf $INSTALLATION_DIRECTORY/*
mvn clean install
#tar zxvf target/biblio-install*.tar.gz -C $INSTALLATION_DIRECTORY
unzip -o -d $INSTALLATION_DIRECTORY target/*.zip
