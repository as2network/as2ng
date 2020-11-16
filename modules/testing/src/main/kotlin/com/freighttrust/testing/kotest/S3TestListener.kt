package com.freighttrust.testing.kotest

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3

class S3TestListener(
  private val bucket: String
) : TestListener {

  private val container = LocalStackContainer()
    .withServices(S3)

  override suspend fun beforeSpec(spec: Spec) {
    with(container) {
      start()

      // set config overrides to match container
      with(getEndpointConfiguration(S3)) {
        System.setProperty("persistence.s3.endpoint.serviceEndpoint", serviceEndpoint)
        System.setProperty("persistence.s3.endpoint.signingRegion", signingRegion)

      }

      // create the bucket
      val client = AmazonS3ClientBuilder
        .standard()
        .withEndpointConfiguration(getEndpointConfiguration(S3))
        .withCredentials(defaultCredentialsProvider)
        .build();

      withContext(Dispatchers.IO) {
        client.createBucket(bucket)
      }

      // config override for bucket
      System.setProperty("persistence.s3.bucket", bucket)

    }

  }

  override suspend fun afterSpec(spec: Spec) {
    container.stop()
  }
}
