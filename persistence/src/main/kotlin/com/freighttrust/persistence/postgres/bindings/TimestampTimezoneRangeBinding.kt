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
)

class TimestampTimezoneRangeBinding : Binding<Any, TsTzRange> {

  private val rangeRegex = Regex("([\\[(])(.*?),(.*?)([])])")

  override fun converter(): Converter<Any, TsTzRange> = object : Converter<Any, TsTzRange> {
    override fun from(dbObject: Any?): TsTzRange?  =
      dbObject
        ?.toString()
        ?.let { str ->

          if(!str.matches(rangeRegex)) {
            null
          } else {

            val matches = rangeRegex.findAll(dbObject.toString()).toList()
            val startBound = matches[1].value
            val start = matches[2].value
              .let { str ->
                ZonedDateTime.parse(
                  str.replace("\"", "").replace(" ", "T") + ":00"
                ).toInstant()
              }
            val end = matches[3].value
              .let { str ->
                ZonedDateTime.parse(
                  str.replace("\"", "").replace(" ", "T") + ":00"
                ).toInstant()
              }
            val endBound = matches[4].value

            return TsTzRange(start, end, startBound == "[", endBound == "]")
          }
        }

    override fun to(userObject: TsTzRange?): Any = {
      userObject?.let {
        val startBound = if (it.startInclusive) "[" else "("
        val endBound = if (it.endInclusive) "]" else ")"
        val start = userObject.start?.toString() ?: ""
        val end = userObject.end?.toString() ?: ""
        "$startBound$start,$end$endBound"
      }
    }

    override fun fromType(): Class<Any> = Any::class.java
    override fun toType(): Class<TsTzRange> = TsTzRange::class.java
  }

  override fun sql(ctx: BindingSQLContext<TsTzRange>) {
    ctx.render().visit(DSL.`val`(ctx.convert<Any>(converter()).value())).sql("::tstzrange")
  }

  override fun register(ctx: BindingRegisterContext<TsTzRange>) {
    ctx.statement().registerOutParameter(ctx.index(), Types.OTHER)
  }

  override fun set(ctx: BindingSetStatementContext<TsTzRange>) {
    ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null))
  }

  override fun set(ctx: BindingSetSQLOutputContext<TsTzRange>) {
    throw SQLFeatureNotSupportedException()
  }

  override fun get(ctx: BindingGetResultSetContext<TsTzRange>) {
    ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()))
  }

  override fun get(ctx: BindingGetStatementContext<TsTzRange>) {
    ctx.convert(converter()).value(ctx.statement().getString(ctx.index()))
  }

  override fun get(ctx: BindingGetSQLInputContext<TsTzRange>) {
    throw SQLFeatureNotSupportedException()
  }
}
