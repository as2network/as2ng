FROM gradle:jdk11 AS build

RUN mkdir -p /opt
WORKDIR /opt

COPY . .

RUN gradle :modules:as2-lib:assemble \
  && gradle :modules:as2-lib:assembleRuntimeDependencies

FROM oracle/graalvm-ce:20.2.0-java11

RUN mkdir -p /opt/as2

WORKDIR /opt/as2

RUN mkdir libs \
  && mkdir config \
  && mkdir data

COPY --from=build  /opt/modules/as2-lib/build/libs/*.jar libs/

COPY docker/as2-lib/entrypoint.sh entrypoint.sh

ENTRYPOINT ["/opt/as2/entrypoint.sh"]
CMD ["config/config.xml"]

VOLUME /opt/as2/config
VOLUME /opt/as2/data
