package com.freighttrust.persistence.s3

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import com.typesafe.config.Config
import org.koin.core.qualifier.named
import org.koin.dsl.module

val S3PersistenceModule = module {

  single<AmazonS3> {

    val config = get<Config>(named("app"))
      .getConfig("persistence.s3")

    AmazonS3ClientBuilder
      .standard()
      .withPathStyleAccessEnabled(true)
      .withClientConfiguration(
        ClientConfiguration()
          .withSignerOverride("AWSS3V4SignerType")
      )
      .withEndpointConfiguration(
        AwsClientBuilder.EndpointConfiguration(
          config.getString("endpoint.serviceEndpoint"),
          config.getString("endpoint.signingRegion")
        )
      )
      .withCredentials(
        AWSStaticCredentialsProvider(
          BasicAWSCredentials(
            config.getString("endpoint.accessKey"),
            config.getString("endpoint.secretKey")
          )
        )
      )
      .build()
  }

  single {
    TransferManagerBuilder
      .standard()
      .withS3Client(get())
      // 5 mb
      .withMultipartUploadThreshold(1024 * 1024 * 5)
      .build()
  }

  single {

    val config = get<Config>(named("app"))
      .getConfig("persistence.s3")

    S3FileService(get(), get(), config.getString("bucket"))
  }
}
