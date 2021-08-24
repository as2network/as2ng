FROM gradle:jdk11 AS build

RUN mkdir -p /opt/build
WORKDIR /opt/build

COPY . .

RUN gradle :modules:server:shadowJar

FROM oracle/graalvm-ce:20.2.0-java11

RUN mkdir -p /opt/as2ng/libs

WORKDIR /opt/as2ng

    
# echo quit | openssl s_client -showcerts -servername example.com -connect example.com:443 2>/dev/null | awk '/BEGIN/,/END/{ if(/BEGIN/){c=""};c=c $0 "\n"}END{print c}' >ca.crt || true


COPY --from=build  /opt/build/modules/server/build/libs/*.jar libs/
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser

COPY docker/server/entrypoint.sh entrypoint.sh
RUN chown -R javauser:javauser /opt/as2ng

USER javauser
ENTRYPOINT ["/opt/as2ng/entrypoint.sh"]
CMD ["run", "as2ng:network.as2.server.ServerVerticle"]

EXPOSE 8080

    
