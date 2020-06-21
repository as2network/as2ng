package com.freighttrust.processing.modules

import com.freighttrust.processing.processors.As2StorageProcessor
import com.typesafe.config.Config
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ProcessingModule = module {

  single { get<Config>(named("app")).getConfig("processing") }

  factory { As2StorageProcessor(get(), get(), get()) }

}
