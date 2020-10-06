package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.persistence.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jooq.*
import org.jooq.impl.DSL
import kotlin.coroutines.CoroutineContext

abstract class AbstractJooqRepository<R : Record>(
  private val dbCtx: DSLContext,
  protected val table: Table<R>,
  protected val idFields: List<Field<*>>,
  override val coroutineContext: CoroutineContext = Dispatchers.IO
) : Repository<R>, CoroutineScope {

  class JooqContext(val dbCtx: DSLContext) : Repository.Context

  private val defaultContext = JooqContext(dbCtx)

  // TODO there has to be a generic way of doing this
  protected abstract fun idQuery(record: R): Condition

  protected fun jooqContext(ctx: Repository.Context?): DSLContext =
    if (ctx == null)
      dbCtx
    else {
      require(ctx is JooqContext) { "ctx must be instance of JooqContext, received: ${ctx::class}" }
      ctx.dbCtx
    }

  override suspend fun <U> transaction(run: suspend (Repository.Context) -> U?): U? {
    var result: U? = null
    coroutineScope {
      dbCtx.transaction { txConfig ->
        launch { result = run(JooqContext(DSL.using(txConfig))) }
      }
    }
    return result
  }


  override suspend fun findById(record: R, ctx: Repository.Context?): R? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(idQuery(record))
        .fetchOne()
    }

  override suspend fun insert(record: R, ctx: Repository.Context?): R =
    coroutineScope {
      jooqContext(ctx)
        .insertInto(table)
        .set(record)
        .returning()
        .fetchOne()
    }

  override suspend fun update(record: R, ctx: Repository.Context?): R =
    coroutineScope {
      jooqContext(ctx)
        .update(table)
        .set(record)
        .where(idQuery(record))
        .returning()
        .fetchOne()
    }

  override suspend fun deleteById(record: R, ctx: Repository.Context?): Int =
    coroutineScope {
      jooqContext(ctx)
        .deleteFrom(table)
        .where(idQuery(record))
        .execute()
    }

}
