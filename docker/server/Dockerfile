FROM gradle:jdk11 AS build

RUN mkdir -p /opt/build
WORKDIR /opt/build

COPY . .

RUN gradle :modules:server:shadowJar

FROM oracle/graalvm-ce:20.2.0-java11

RUN mkdir -p /opt/as2ng/libs

WORKDIR /opt/as2ng

COPY --from=build  /opt/build/modules/server/build/libs/*.jar libs/

COPY docker/server/entrypoint.sh entrypoint.sh
ENV JAVA_OPTS=""

ENTRYPOINT ["/opt/as2ng/entrypoint.sh"]
CMD ["run", "as2ng:network.as2.server.ServerVerticle"]

EXPOSE 8080
