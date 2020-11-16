package com.freighttrust.persistence.s3

import io.kotest.core.spec.style.FunSpec
import org.koin.test.KoinTest
import org.testcontainers.containers.localstack.LocalStackContainer

class S3FileServiceSpec : FunSpec(), KoinTest {

  val localStack = LocalStackContainer()
    .withServices(LocalStackContainer.Service.S3)



}
