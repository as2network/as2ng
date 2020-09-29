/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables;


import com.freighttrust.jooq.Indexes;
import com.freighttrust.jooq.Keys;
import com.freighttrust.jooq.Public;
import com.freighttrust.jooq.tables.records.TradingPartnerRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TradingPartner extends TableImpl<TradingPartnerRecord> {

    private static final long serialVersionUID = 56223183;

    /**
     * The reference instance of <code>public.trading_partner</code>
     */
    public static final TradingPartner TRADING_PARTNER = new TradingPartner();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TradingPartnerRecord> getRecordType() {
        return TradingPartnerRecord.class;
    }

    /**
     * The column <code>public.trading_partner.id</code>.
     */
    public final TableField<TradingPartnerRecord, String> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>public.trading_partner.name</code>.
     */
    public final TableField<TradingPartnerRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.trading_partner.email</code>.
     */
    public final TableField<TradingPartnerRecord, String> EMAIL = createField(DSL.name("email"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    public final TableField<TradingPartnerRecord, Object> VALIDITY = createField(DSL.name("validity"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").defaultValue(org.jooq.impl.DSL.field("tstzrange(CURRENT_TIMESTAMP, NULL::timestamp with time zone)", org.jooq.impl.SQLDataType.OTHER)), this, "");

    /**
     * Create a <code>public.trading_partner</code> table reference
     */
    public TradingPartner() {
        this(DSL.name("trading_partner"), null);
    }

    /**
     * Create an aliased <code>public.trading_partner</code> table reference
     */
    public TradingPartner(String alias) {
        this(DSL.name(alias), TRADING_PARTNER);
    }

    /**
     * Create an aliased <code>public.trading_partner</code> table reference
     */
    public TradingPartner(Name alias) {
        this(alias, TRADING_PARTNER);
    }

    private TradingPartner(Name alias, Table<TradingPartnerRecord> aliased) {
        this(alias, aliased, null);
    }

    private TradingPartner(Name alias, Table<TradingPartnerRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> TradingPartner(Table<O> child, ForeignKey<O, TradingPartnerRecord> key) {
        super(child, key, TRADING_PARTNER);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.TRADING_PARTNER_NAME_KEY, Indexes.TRADING_PARTNER_PKEY);
    }

    @Override
    public UniqueKey<TradingPartnerRecord> getPrimaryKey() {
        return Keys.TRADING_PARTNER_PKEY;
    }

    @Override
    public List<UniqueKey<TradingPartnerRecord>> getKeys() {
        return Arrays.<UniqueKey<TradingPartnerRecord>>asList(Keys.TRADING_PARTNER_PKEY, Keys.TRADING_PARTNER_NAME_KEY);
    }

    @Override
    public TradingPartner as(String alias) {
        return new TradingPartner(DSL.name(alias), this);
    }

    @Override
    public TradingPartner as(Name alias) {
        return new TradingPartner(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TradingPartner rename(String name) {
        return new TradingPartner(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TradingPartner rename(Name name) {
        return new TradingPartner(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, String, Object> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
