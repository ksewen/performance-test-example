#!/bin/sh
start_service() {
  java -server ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /service-for-test/service-for-test-0.0.3.jar
}

start_service
