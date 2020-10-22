package com.freighttrust.as2.kotest.listeners

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3

class LocalStackListener(
  private val localStack: LocalStackContainer,
) : TestListener {

  override suspend fun beforeSpec(spec: Spec) {

    val client = AmazonS3ClientBuilder
      .standard()
      .withEndpointConfiguration(localStack.getEndpointConfiguration(S3))
      .withCredentials(localStack.defaultCredentialsProvider)
      .build();

    withContext(Dispatchers.IO) {
      client.createBucket(System.getProperty("s3.bucket"));
    }

  }
}
