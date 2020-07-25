/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, FreightTrust & Clearing Corporation
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.freighttrust.persistence.s3

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import com.freighttrust.persistence.s3.repositories.FileRepository
import com.freighttrust.persistence.s3.repositories.S3FileRepository
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

  single<FileRepository> {
    val config = get<Config>(named("s3"))
    S3FileRepository(get(), get(), config.getString("bucket"))
  }
}
