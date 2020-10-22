FROM gradle:jdk11 AS build

RUN mkdir -p /opt
WORKDIR /opt

COPY . .

RUN gradle :modules:as2-lib:shadowJar

FROM oracle/graalvm-ce:20.2.0-java11

RUN mkdir -p /opt/as2

WORKDIR /opt/as2

RUN mkdir libs \
  && mkdir config \
  && mkdir data

COPY --from=build  /opt/modules/as2-lib/build/libs/*.jar libs/

ENTRYPOINT ["java", "-classpath", "libs/*", "-Xms512m", "-Xmx512m", "com.helger.as2.app.MainOpenAS2Server"]
CMD ["/opt/as2/config/config.xml"]

VOLUME /opt/as2/config
VOLUME /opt/as2/data
