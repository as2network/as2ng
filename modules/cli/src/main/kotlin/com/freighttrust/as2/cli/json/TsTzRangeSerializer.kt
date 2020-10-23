package com.freighttrust.as2.cli.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.freighttrust.persistence.postgres.bindings.TsTzRange

class TsTzRangeSerializer : StdSerializer<TsTzRange>(TsTzRange::class.java) {

  override fun serialize(value: TsTzRange, gen: JsonGenerator, provider: SerializerProvider) {

    val objectMapper = gen.codec as ObjectMapper

    val str = StringBuilder()
      .append(if(value.startInclusive) "[" else "(")
      .append(if(value.start == null) "" else objectMapper.writeValueAsString(value.start).replace("\"", ""))
      .append(",")
      .append(if(value.end == null) "" else objectMapper.writeValueAsString(value.end).replace("\"", ""))
      .append(if(value.endInclusive) "]" else ")")
      .toString()

    gen.writeString(str)

  }
}
