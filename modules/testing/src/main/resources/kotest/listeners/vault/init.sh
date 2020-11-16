#!/usr/bin/sh

# Install jq
if ! [ -x "$(command -v docker)" ]; then
    apk add jq
fi

# Auth
vault login "$VAULT_DEV_ROOT_TOKEN_ID"

# Enable pki
vault secrets enable pki

# Enable max ttl
vault secrets tune -max-lease-ttl=87600h pki

# Create ROOT CA
vault write -field=certificate pki/root/generate/internal \
        common_name="freighttrust.com" \
        ttl=87600h > CA_cert.crt

# Configure the CA and CRL urls
vault write pki/config/urls \
        issuing_certificates="http://127.0.0.1:8200/v1/pki/ca" \
        crl_distribution_points="http://127.0.0.1:8200/v1/pki/crl"

# Generate intermediate CA
vault secrets enable -path=pki_int pki

# Set maximum TTL to 43800
vault secrets tune -max-lease-ttl=43800h pki_int

# Save CSR of the intermediate CA
vault write -format=json pki_int/intermediate/generate/internal \
        common_name="freighttrust.com Intermediate Authority" \
        | jq -r '.data.csr' > pki_intermediate.csr

# Sign the intermediate CA with the ROOT CA
vault write -format=json pki/root/sign-intermediate csr=@pki_intermediate.csr \
        format=pem_bundle ttl="43800h" \
        | jq -r '.data.certificate' > intermediate.cert.pem

# Import the signed certificate
vault write pki_int/intermediate/set-signed certificate=@intermediate.cert.pem

# Create Role certificate
vault write pki_int/roles/freighttrust-dot-com \
        allowed_domains="freighttrust.com" \
        allow_subdomains=true \
        max_ttl="8760h"
