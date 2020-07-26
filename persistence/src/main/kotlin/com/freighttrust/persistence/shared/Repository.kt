package com.freighttrust.persistence.shared

interface Repository<T> {

  interface Context

  suspend fun <U> transaction(run: suspend (Context) -> U?): U?

  suspend fun findById(record: T, ctx: Context? = null): T?

  suspend fun insert(record: T, ctx: Context? = null): T

  suspend fun update(record: T, ctx: Context? = null): T

  suspend fun deleteById(record: T, ctx: Context? = null): Int

}
