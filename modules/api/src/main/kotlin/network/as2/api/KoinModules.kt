package network.as2.api

import com.fasterxml.uuid.Generators

import com.typesafe.config.Config
import io.vertx.ext.web.client.WebClient
import network.as2.persistence.StorageService
import network.as2.persistence.local.LocalStorageService
import network.as2.persistence.s3.S3StorageService
import org.koin.core.qualifier._q
import org.koin.dsl.bind
import org.koin.dsl.module

val ApiModule = module {

  single(_q("http")) {
    val config = get<Config>(_q("app"))
    config.getConfig("http")
  }

  single(_q("baseUrl")) {
    val config = get<Config>(_q("http"))

    val protocol = if (config.getBoolean("https")) "https" else "http"
    val host = config.getString("host")
    val port = config.getInt("port")

    "$protocol://$host:$port"
  }

  factory { Generators.timeBasedGenerator() }

  single { WebClient.create(get()) }

  single {

    val config = get<Config>(_q("app"))

    // configurable file service
    when (val provider = config.getString("fileService")) {
      "s3" -> get<S3StorageService>()
      "local" -> get<LocalStorageService>()
      else -> throw IllegalArgumentException("Unknown file persistence provider: $provider")
    }


  }.bind(StorageService::class)
}
