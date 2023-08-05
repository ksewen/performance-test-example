#!/bin/bash

pushd $(dirname $0) > /dev/null
SCRIPTPATH=$(pwd -P)
popd > /dev/null

set_profile() {
    cp -f /auth-service-for-test/resources/properties/application.yml \
        /auth-service-for-test/src/main/resources/application.yml
}

do_package() {
  cd /auth-service-for-test/
  mvn package
}

set_profile
do_package