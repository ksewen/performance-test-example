FROM maven:3.9.3-sapmachine-17 as builder

COPY gateway-for-test /gateway-for-test

WORKDIR /gateway-for-test

RUN mkdir out && \
    /gateway-for-test/resources/scripts/build-app.sh

FROM openjdk:17-slim-buster

RUN mkdir /gateway-for-test

COPY --from=builder /gateway-for-test/target/gateway-for-test-0.0.1.jar /gateway-for-test
COPY --from=builder /gateway-for-test/resources/scripts/bootstrap.sh  /

WORKDIR /gateway-for-test

EXPOSE 8071

ENTRYPOINT [ "/bootstrap.sh" ]