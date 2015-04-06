#!/bin/bash
if [ -z "$APP_HOME" ]; then
    PRGDIR=`dirname "$0"`
    # echo "$PRGDIR"
    APP_HOME=`cd "$PRGDIR" ; pwd`
fi
echo "JAVA_HOME=$JAVA_HOME"
echo "APP_HOME=$APP_HOME"

_CLASSPATH="$APP_HOME"
for dependency in $APP_HOME/lib/*.jar
do
    _CLASSPATH="${_CLASSPATH}:${dependency}"
done

_UPDATE_CLASSPATH="$APP_HOME"
for dependency in $APP_HOME/*.jar
do
    _UPDATE_CLASSPATH="${_UPDATE_CLASSPATH}:${dependency}"
done

$JAVA_HOME/bin/java $JAVA_OPTS -Dlog4j.configuration=file:$APP_HOME/log4j.xml -Dfile.encoding=UTF-8 \
-cp "${_UPDATE_CLASSPATH}" "org.ado.biblio.InstallUpdate" &&
$JAVA_HOME/bin/java $JAVA_OPTS -Dlog4j.configuration=file:$APP_HOME/log4j.xml -Dfile.encoding=UTF-8 \
-classpath "${_CLASSPATH}" "org.ado.biblio.desktop.App" $@ > /dev/null &