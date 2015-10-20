#!/usr/bin/env bash
source "$(dirname $0)/common.sh" || exit -1
checkTravisSecure

openssl aes-256-cbc -K $encrypted_ae4d2db0dbde_key -iv $encrypted_ae4d2db0dbde_iv -in scr.travis.gpg.asc.enc -out scr.travis.gpg.asc -d
gpg2 --import scr.travis.gpg.asc
