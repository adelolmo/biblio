## Biblio

Solution to organize your personal physical library

## Install

### Desktop Application
1. Go to [latest release](https://github.com/adelolmo/biblio/releases/latest)
2. Download the zip Desktop Client (e.g. biblio-desktop-client-1.5-dist.zip)
3. Extract the zip file in your computer. (e.g. C:\Users\Your-User\Desktop\)
4. Rename the directory to "biblio".

### Android App
1. On your Android device, go to [latest release](https://github.com/adelolmo/biblio/releases/latest)
2. Enable "Unknown Sources" in your device. [howto](http://www.androidcentral.com/allow-app-installs-unknown-sources)
3. Download the Android APK file (e.g. biblio-android-1.5.apk)
4. Install the biblio .apk file.

## Run

### On Windows
1. Go to the biblio installation directory. (e.g. C:\Users\Your-User\Desktop\biblio)
2. Execute the file "bin\startup.bat".

### On Linux
    # Go to the biblio installation directory. (e.g. /opt/biblio-desktop)
    $ cd /opt/biblio-desktop

    # set execution permissions
    $ chmod +x bin/startup.sh

    # launch biblio desktop application
    $ bin/startup.sh

## First steps
TODO
### How to Link Desktop Application with Android App

## Build

### Build requirements (tested):
- Java SDK 1.8
- Apache Maven 3.0.5

### Compilation
In the project directory

    # compile and create artifacts
    $ mvn clean install