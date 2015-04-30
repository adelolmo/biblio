## Biblio

Solution to organize your personal physical library

<img src="https://raw.githubusercontent.com/adelolmo/biblio/gh-pages/images/biblio-desktop.png" width="100%"/>

## Install

### Desktop Application
1. Go to [latest release](https://github.com/adelolmo/biblio/releases/latest)
2. Download the zip Desktop Client (e.g. biblio-desktop-client-VERSION-dist.zip)
3. Extract the zip file in your computer. (e.g. C:\Users\Your-User\Desktop\)
4. Rename the directory to "biblio".

### Android App
1. On your Android device, go to [latest release](https://github.com/adelolmo/biblio/releases/latest)
2. Enable "Unknown Sources" in your device. [howto](http://www.androidcentral.com/allow-app-installs-unknown-sources)
3. Download the Android APK file (e.g. biblio-android-VERSION.apk)
4. Install the biblio .apk file.

### Server
This step is only required if you want to deploy your own version of the Biblio server.
Normal users can skip this section and move directly to the "Run" section.

#### Deploy into Tomcat
1. Go to [latest release](https://github.com/adelolmo/biblio/releases/latest)
2. Download the Server WAR file (e.g. biblio-server-war-VERSION.war)
3. Copy the WAR file into tomcat's webapp directory ( TOMCAT/webapps )
4. Start Tomcat

#### Run as Standalone
TODO

## Run

### On Windows
1. Go to the biblio installation directory. (e.g. C:\Users\Your-User\Desktop\biblio)
2. Execute the file "startup.bat".

### On Linux
    # Go to the biblio installation directory. (e.g. /opt/biblio-desktop)
    $ cd /opt/biblio-desktop

    # set execution permissions
    $ chmod +x startup.sh

    # launch biblio desktop application
    $ startup.sh

## First steps
TODO

### How to Link Desktop Application with Android App
Once the Biblio Desktop Application is running:

1. Go to File -> Settings
2. Set "Server URL" to "http://biblio-adoorg.rhcloud.com" and click "Save". Application restart might be needed.
** If you have deployed your own version of the server, you should enter here where the server can be reached.
3. Go to File -> Android
4. Take your device running the Biblio App and use the button to scan the QR code shown in the computer's screen.
Now your computer and your mobile are linked. Every time that you scan a book's barcode its details will be sent to the server.
Once the Biblio Desktop Application is started the book's details will be added to the list.

## Build

### Build requirements (tested):
- Java SDK 1.8
- Apache Maven 3.0.5

### Compilation
In the project directory

    # compile and create artifacts
    $ mvn clean install
