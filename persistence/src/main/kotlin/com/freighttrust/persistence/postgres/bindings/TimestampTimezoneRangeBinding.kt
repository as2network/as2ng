package com.freighttrust.persistence.postgres.bindings

import org.jooq.Binding
import org.jooq.BindingGetResultSetContext
import org.jooq.BindingGetSQLInputContext
import org.jooq.BindingGetStatementContext
import org.jooq.BindingRegisterContext
import org.jooq.BindingSQLContext
import org.jooq.BindingSetSQLOutputContext
import org.jooq.BindingSetStatementContext
import org.jooq.Converter
import org.jooq.conf.ParamType
import org.jooq.impl.DSL
import java.sql.SQLFeatureNotSupportedException
import java.sql.Types
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

data class TsTzRange(
  val start: Instant? = null,
  val end: Instant? = null,
  val startInclusive: Boolean = true,
  val endInclusive: Boolean = true
) {

  override fun toString(): String {
    return super.toString()
  }
}

fun String.fromTimestampWithZone(): Instant? =
  replace("\"", "")
    .replace(" ", "T")
    .takeIf { it != "" }
    ?.let { ZonedDateTime.parse(it).toInstant() }

class TimestampTimezoneRangeConverter : Converter<Any, TsTzRange> {

  private val rangeRegex = Regex("([\\[(])(.*?),(.*?)([])])")

  override fun from(dbObject: Any?): TsTzRange?  =
    dbObject
      ?.toString()
      ?.let { str ->

        if(!str.matches(rangeRegex)) {
          null
        } else {

          val match = rangeRegex
            .findAll(dbObject.toString())
            .toList()
            .first()

          val startBound = match.groupValues[1]
          val start = match.groupValues[2].fromTimestampWithZone()
          val end = match.groupValues[3].fromTimestampWithZone()
          val endBound = match.groupValues[4]

          return TsTzRange(start, end, startBound == "[", endBound == "]")
        }
      }

  override fun to(userObject: TsTzRange?) =
    userObject?.let {
      val startBound = if (it.startInclusive) "[" else "("
      val endBound = if (it.endInclusive) "]" else ")"
      val start = userObject.start?.toString() ?: ""
      val end = userObject.end?.toString() ?: ""
      "$startBound$start,$end$endBound"
    }

  override fun fromType(): Class<Any> = Any::class.java
  override fun toType(): Class<TsTzRange> = TsTzRange::class.java
}

class TimestampTimezoneRangeBinding : Binding<Any, TsTzRange> {

  override fun converter(): Converter<Any, TsTzRange> = TimestampTimezoneRangeConverter()

  override fun sql(ctx: BindingSQLContext<TsTzRange>) {
    if(ctx.render().paramType() == ParamType.INLINED)
      ctx.render().visit(DSL.inline(ctx.convert(converter()).value())).sql("::tstzrange")
    else
      ctx.render().sql("?::tstzrange")
  }

  override fun register(ctx: BindingRegisterContext<TsTzRange>) {
    ctx.statement().registerOutParameter(ctx.index(), Types.OTHER)
  }

  override fun set(ctx: BindingSetStatementContext<TsTzRange>) {
    ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null))
  }

  override fun get(ctx: BindingGetResultSetContext<TsTzRange>) {
    ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()))
  }

  override fun get(ctx: BindingGetStatementContext<TsTzRange>) {
    ctx.convert(converter()).value(ctx.statement().getString(ctx.index()))
  }

  override fun set(ctx: BindingSetSQLOutputContext<TsTzRange>) {
    throw SQLFeatureNotSupportedException()
  }

  override fun get(ctx: BindingGetSQLInputContext<TsTzRange>) {
    throw SQLFeatureNotSupportedException()
  }
}
