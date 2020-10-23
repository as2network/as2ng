#!/usr/bin/env sh

DEFAULT_JAVA_OPTS="-Xms512m -Xmx512m"

MAIN_CLASS=${MAIN_CLASS:-"com.helger.as2.app.MainOpenAS2Server"}
JAVA_OPTS=${JAVA_OPTS:-""}

CMD="java -cp libs/* ${DEFAULT_JAVA_OPTS} ${JAVA_OPTS} ${DEFAULT_VERTX_OPTS} ${VERTX_OPTS} ${MAIN_CLASS}"

eval "${CMD} $*"
