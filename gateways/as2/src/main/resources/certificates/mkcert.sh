#!/usr/bin/env bash

# Give script sane defaults
set -o errexit
# set -o nounset
# set -o xtrace
# set -o verbose

run() {
  local name="${1:-"OpenAS2A"}"

  openssl req -x509 \
    -sha256 \
    -nodes \
    -newkey rsa:4096 \
    -keyout "${name}".key \
    -days 730 \
    -outform pem \
    -out "${name}".cer \
    -subj "/C=US/ST=California/O=FreightTrust/CN=${name}"

  cat "${name}".cer "${name}".key > "${name}".pem

  openssl pkcs12 \
    -export \
    -inkey "${name}".key \
    -in "${name}".pem \
    -name "${name}" \
    -out "${name}".p12 \
    -password pass:password
}
run "$@"
