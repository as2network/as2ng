package com.freighttrust.s3

import com.amazonaws.ClientConfiguration
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import com.freighttrust.s3.repositories.FileRepository
import com.typesafe.config.Config
import org.koin.core.qualifier.named
import org.koin.dsl.module

val S3Module = module {

  single(named("s3")) {
    val config = get<Config>(named("app"))
    config.getConfig("s3")
  }

  single {

    val config = get<Config>(named("s3"))

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
      .build()
  }

  single {
    TransferManagerBuilder
      .standard()
      .withS3Client(get())
      .build()
  }

  single {
    val config = get<Config>(named("s3"))
    FileRepository(get(), get(), config.getString("bucket"))
  }

}
