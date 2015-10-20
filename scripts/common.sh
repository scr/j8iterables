#!/usr/bin/env bash

function die() {
  echo "$1" >&2
  exit -1
}

function checkTravisSecure() {
    if [[ $TRAVIS_SECURE_ENV_VARS != "true" ]]; then
        echo "no secure env vars available, skipping deployment"
        exit
    fi
}
