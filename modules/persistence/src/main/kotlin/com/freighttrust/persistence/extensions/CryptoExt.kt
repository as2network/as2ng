package com.freighttrust.persistence.extensions

import org.postgresql.util.Base64
import java.io.ByteArrayInputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec

private val x509Factory = CertificateFactory.getInstance("X.509")

private val rsaKeyFactory = KeyFactory.getInstance("RSA")

fun String.toX509(): X509Certificate {
  val bytesIn = ByteArrayInputStream(Base64.decode(this))
  return x509Factory.generateCertificate(bytesIn) as X509Certificate
}

val X509Certificate.formattedSerialNumber
  get() = serialNumber.toString(16).chunked(2).joinToString(":")

fun X509Certificate.toBase64(): String =Base64.encodeBytes(this.encoded).replace("\n", "")

fun String.toPrivateKey(): PrivateKey {
  val keySpec = PKCS8EncodedKeySpec(Base64.decode(this))
  return rsaKeyFactory.generatePrivate(keySpec)
}
