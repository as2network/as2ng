FROM gradle:jdk11 AS build

RUN mkdir -p /opt/build
WORKDIR /opt/build

COPY . .

RUN gradle :modules:cli:shadowJar

FROM oracle/graalvm-ce:20.2.0-java11

RUN mkdir -p /opt/cli/libs \
  && mkdir -p /opt/cli/data

WORKDIR /opt/cli

COPY --from=build  /opt/build/modules/cli/build/libs/*.jar libs/

ENTRYPOINT ["java", "-classpath", "libs/*", "-Xms512m", "-Xmx512m", "network.as2.cli.MainKt"]

VOLUME /opt/cli/data



