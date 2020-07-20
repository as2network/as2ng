#!/usr/bin/env bash

# Give script sane defaults
set -o errexit
# set -o nounset
# set -o xtrace
# set -o verbose

run() {
  local name="${1:-"keystore"}"
  local files=(OpenAS2A OpenAS2B OpenAS2C OpenAS2D)

  for file in "${files[@]}"; do
    keytool -v \
      -importkeystore \
      -srckeystore "${file}".p12 \
      -srcstorepass password \
      -srcstoretype PKCS12 \
      -destkeystore "${name}".p12 \
      -deststorepass password
  done
}
run "$@"
