package com.freighttrust.persistence.postgres.repositories

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import org.jooq.*
import kotlin.coroutines.CoroutineContext

abstract class AbstractJooqRepository<R : Record>(
  private val dbCtx: DSLContext,
  private val table: Table<R>,
  private val idFields: List<Field<*>>,
  override val coroutineContext: CoroutineContext = Dispatchers.IO
) : CoroutineScope {

  // TODO there has to be a generic way of doing this
  protected abstract fun idQuery(record: R): Condition

  suspend fun findById(record: R, ctx: DSLContext = dbCtx): R? =
    coroutineScope {
      ctx
        .selectFrom(table)
        .where(idQuery(record))
        .fetchOne()
    }

  suspend fun insert(record: R, ctx: DSLContext = dbCtx): R =
    coroutineScope {
      ctx.insertInto(table)
        .set(record)
        .returning(idFields)
        .fetchOne()
        .also { inserted ->
          table.newRecord()
            .apply {
              from(record)
              from(inserted)
            }
        }
    }

  suspend fun update(record: R, ctx: DSLContext = dbCtx): R =
    coroutineScope {
      ctx.update(table)
        .set(record)
        .returning()
        .fetchOne()

    }

  suspend fun deleteById(record: R, ctx: DSLContext = dbCtx): Int =
    coroutineScope {
      ctx
        .deleteFrom(table)
        .where(idQuery(record))
        .execute()
    }

}
