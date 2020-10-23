#!/usr/bin/env sh

DEFAULT_JAVA_OPTS=""
DEFAULT_VERTX_OPTS="-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory"

MAIN_CLASS=${MAIN_CLASS:-"io.vertx.core.Launcher"}
JAVA_OPTS=${JAVA_OPTS:-""}
VERTX_OPTS=${VERTX_OPTS:-""}

CMD="java -cp libs/* ${DEFAULT_JAVA_OPTS} ${JAVA_OPTS} ${DEFAULT_VERTX_OPTS} ${VERTX_OPTS} ${MAIN_CLASS}"

eval "${CMD} $*"

