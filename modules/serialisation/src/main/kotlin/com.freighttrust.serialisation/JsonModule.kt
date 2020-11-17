package com.freighttrust.serialisation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.freighttrust.common.util.TsTzRange
import com.freighttrust.serialisation.json.TsTzRangeSerializer
import org.koin.dsl.module

val JsonModule = module {

  factory {
    ObjectMapper()
      .registerModule(KotlinModule())
      .registerModule(JavaTimeModule())
      .registerModule(
        SimpleModule()
          .apply { addSerializer(TsTzRange::class.java, TsTzRangeSerializer())}
      )
      .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
      .enable(SerializationFeature.INDENT_OUTPUT)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .setDateFormat(StdDateFormat().withColonInTimeZone(true))
  }

}
