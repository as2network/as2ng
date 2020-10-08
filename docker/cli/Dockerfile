FROM gradle:jdk14 AS build

RUN mkdir -p /opt/build
WORKDIR /opt/build

COPY . .

RUN gradle :cli:shadowJar

FROM openjdk:14

RUN mkdir -p /opt/cli/libs \
  && mkdir -p /opt/cli/data

WORKDIR /opt/cli

COPY --from=build  /opt/build/cli/build/libs/*.jar libs/

ENTRYPOINT ["java", "-classpath", "libs/*", "-Xms512m", "-Xmx512m", "com.freighttrust.as2.cli.MainKt"]

VOLUME /opt/cli/data



