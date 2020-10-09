FROM gradle:jdk11 AS build

RUN mkdir -p /opt
WORKDIR /opt

COPY . .

RUN gradle as2-lib:server:shadowJar

FROM openjdk:11

RUN mkdir -p /opt/as2

WORKDIR /opt/as2

RUN mkdir libs \
  && mkdir config \
  && mkdir data

COPY --from=build  /opt/as2-lib/server/build/libs/*.jar libs/

ENTRYPOINT ["java", "-classpath", "libs/*", "-Xms512m", "-Xmx512m", "com.helger.as2.app.MainOpenAS2Server"]

VOLUME /opt/as2/config
VOLUME /opt/as2/data
