#!/usr/bin/env bash

function die() {
  echo "$@" >&2
  exit -1
}

function checkTravisSecure() {
    if [[ $TRAVIS_SECURE_ENV_VARS != "true" ]]; then
        echo "no secure env vars available, skipping deployment"
        exit
    fi
}

function usage() {
    echo "${0} gpg-decryption-hash gpg-key-file"
}

function encryption_var_name() {
    local -r HASH="$1"
    local -r NAME="$2"

    echo "encrypted_${HASH}_${NAME}"
}

function install_gpg_key() {
    local -r KEY="${!1}"
    local -r IV="${!2}"
    local -r IN="$3"
    local -r OUT="$4"

    openssl aes-256-cbc -K "$KEY" -iv "$IV" -in "$IN" -out "$OUT" -d
    gpg2 --batch --import scr.travis.gpg.asc
}

function install_gpg() {
    sudo apt-get update -qq
    sudo apt-get install -y gnupg2
}

function mvn_deploy() {
    local -r SETTINGS_FILE="$1"
    mvn clean deploy -Drelease -s "$SETTINGS_FILE"
}

function main() {
    checkTravisSecure

    if (($# < 3)); then
        die $(usage)
    fi
    local -r HASH="$1"
    local -r ENC_GPG_KEY_FILE="$2"
    local -r MVN_SETTINGS_FILE="$3"

    install_gpg
    install_gpg_key $(encryption_var_name "$HASH" key) $(encryption_var_name "$HASH" iv) "$ENC_GPG_KEY_FILE" "${ENC_GPG_KEY_FILE/.asc}"
    mvn_deploy "$MVN_SETTINGS_FILE"
}

if ((${#BASH_SOURCE[@]} == 1)); then
    main "$@"
fi
