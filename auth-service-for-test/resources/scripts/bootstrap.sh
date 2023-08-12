#!/bin/sh
start_service() {
  java -server ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /auth-service-for-test/auth-service-for-test-0.0.2.jar
}

start_service
