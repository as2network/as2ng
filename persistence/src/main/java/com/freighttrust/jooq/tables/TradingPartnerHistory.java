/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables;


import com.freighttrust.jooq.Public;
import com.freighttrust.jooq.tables.records.TradingPartnerHistoryRecord;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.13.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TradingPartnerHistory extends TableImpl<TradingPartnerHistoryRecord> {

    private static final long serialVersionUID = -942677935;

    /**
     * The reference instance of <code>public.trading_partner_history</code>
     */
    public static final TradingPartnerHistory TRADING_PARTNER_HISTORY = new TradingPartnerHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TradingPartnerHistoryRecord> getRecordType() {
        return TradingPartnerHistoryRecord.class;
    }

    /**
     * The column <code>public.trading_partner_history.id</code>.
     */
    public final TableField<TradingPartnerHistoryRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.trading_partner_history.name</code>.
     */
    public final TableField<TradingPartnerHistoryRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.trading_partner_history.email</code>.
     */
    public final TableField<TradingPartnerHistoryRecord, String> EMAIL = createField(DSL.name("email"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.trading_partner_history.key_pair_id</code>.
     */
    public final TableField<TradingPartnerHistoryRecord, Long> KEY_PAIR_ID = createField(DSL.name("key_pair_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    public final TableField<TradingPartnerHistoryRecord, Object> VALIDITY = createField(DSL.name("validity"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\""), this, "");

    /**
     * Create a <code>public.trading_partner_history</code> table reference
     */
    public TradingPartnerHistory() {
        this(DSL.name("trading_partner_history"), null);
    }

    /**
     * Create an aliased <code>public.trading_partner_history</code> table reference
     */
    public TradingPartnerHistory(String alias) {
        this(DSL.name(alias), TRADING_PARTNER_HISTORY);
    }

    /**
     * Create an aliased <code>public.trading_partner_history</code> table reference
     */
    public TradingPartnerHistory(Name alias) {
        this(alias, TRADING_PARTNER_HISTORY);
    }

    private TradingPartnerHistory(Name alias, Table<TradingPartnerHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private TradingPartnerHistory(Name alias, Table<TradingPartnerHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> TradingPartnerHistory(Table<O> child, ForeignKey<O, TradingPartnerHistoryRecord> key) {
        super(child, key, TRADING_PARTNER_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public TradingPartnerHistory as(String alias) {
        return new TradingPartnerHistory(DSL.name(alias), this);
    }

    @Override
    public TradingPartnerHistory as(Name alias) {
        return new TradingPartnerHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TradingPartnerHistory rename(String name) {
        return new TradingPartnerHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TradingPartnerHistory rename(Name name) {
        return new TradingPartnerHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, String, String, Long, Object> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
