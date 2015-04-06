@echo off
@if not "%ECHO%" == ""  echo %ECHO%
if "%OS%"=="Windows_NT" @setlocal

set DIRNAME=.\
if "%OS%" == "Windows_NT" set DIRNAME=%~dp0%
set PROGNAME=startup.bat
if "%OS%" == "Windows_NT" set PROGNAME=%~nx0%

set APP_HOME==%CD%

rem set the classpath
rem for %%i in (%APP_HOME%"\lib\*.jar") do echo %%i
rem for %%i in (%APP_HOME%"\lib\*.jar") do call "%APP_HOME%"\lcp.bat %%i
for %%i in (%APP_HOME%"\lib\*.jar") do call lcp.bat %%i

echo DirectoryName : %DIRNAME%
echo ProgramName : %PROGNAME%

echo JAVA_HOME=%JAVA_HOME%
echo APP_HOME=%APP_HOME%

set LOGFILE=%CD%\log4j.xml

echo ClassPath : %LOCALCLASSPATH%

echo START APPLICATION
"%JAVA_HOME%"\bin\java %JAVA_OPTS% -Dlog4j.configuration=file:%LOGFILE% -Dfile.encoding=ISO-8859-1 -classpath biblio-install-1.6-jar-with-dependencies.jar "org.ado.biblio.InstallUpdate" &&
"%JAVA_HOME%"\bin\java %JAVA_OPTS% -Dlog4j.configuration=file:%LOGFILE% -Dfile.encoding=ISO-8859-1 -classpath "%LOCALCLASSPATH%" "org.ado.biblio.desktop.App"