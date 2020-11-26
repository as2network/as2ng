package com.freighttrust.persistence.local

import com.typesafe.config.Config
import org.koin.core.qualifier.named
import org.koin.dsl.module

val LocalPersistenceModule = module {

  single {

    val config = get<Config>(named("app"))
      .getConfig("persistence.local")

    LocalStorageService(config.getString("baseDirectory"), get(), get())
  }

}
