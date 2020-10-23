package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.persistence.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Table
import org.jooq.impl.DSL
import kotlin.coroutines.CoroutineContext

abstract class AbstractJooqRepository<R : Record, Pojo>(
  private val dbCtx: DSLContext,
  protected val table: Table<R>,
  protected val pojoClass: Class<Pojo>,
  override val coroutineContext: CoroutineContext = Dispatchers.IO
) : Repository<Pojo>, CoroutineScope {

  class JooqContext(val dbCtx: DSLContext) : Repository.Context

  // TODO there has to be a generic way of doing this
  protected abstract fun idQuery(value: Pojo): Condition

  protected fun jooqContext(ctx: Repository.Context?): DSLContext =
    if (ctx == null)
      dbCtx
    else {
      require(ctx is JooqContext) { "ctx must be instance of JooqContext, received: ${ctx::class}" }
      ctx.dbCtx
    }

  override suspend fun <U> transaction(run: suspend (Repository.Context) -> U): U {
    var result: Deferred<U>? = null
    coroutineScope {
      dbCtx.transaction { txConfig ->
        result = async { run(JooqContext(DSL.using(txConfig))) }
      }
    }
    return result!!.await()
  }

  override suspend fun exists(value: Pojo, ctx: Repository.Context?): Boolean =
    coroutineScope {
      jooqContext(ctx)
        .fetchCount(table, idQuery(value)) == 1
    }

  override suspend fun findAll(ctx: Repository.Context?): List<Pojo> =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .fetchInto(pojoClass)
        .toList()
    }

  override suspend fun findById(value: Pojo, ctx: Repository.Context?): Pojo? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(idQuery(value))
        .fetchOne()
        ?.into(pojoClass)
    }

  override suspend fun insert(value: Pojo, ctx: Repository.Context?): Pojo =
    coroutineScope {
      with(jooqContext(ctx)) {
        insertInto(table)
          .set(newRecord(table, value))
          .returning()
          .fetchOne()
          .into(pojoClass)
      }
    }

  override suspend fun update(value: Pojo, ctx: Repository.Context?): Pojo =
    coroutineScope {
      with(jooqContext(ctx)) {
        update(table)
          .set(newRecord(table, value))
          .where(idQuery(value))
          .returning()
          .fetchOne()
          .into(pojoClass)
      }
    }

  override suspend fun deleteById(value: Pojo, ctx: Repository.Context?): Int =
    coroutineScope {
      jooqContext(ctx)
        .deleteFrom(table)
        .where(idQuery(value))
        .execute()
    }

}
