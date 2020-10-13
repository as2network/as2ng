/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables;


import com.freighttrust.jooq.Keys;
import com.freighttrust.jooq.Public;
import com.freighttrust.jooq.tables.records.TradingPartnerRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
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
public class TradingPartner extends TableImpl<TradingPartnerRecord> {

    private static final long serialVersionUID = 1041835184;

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
    public final TableField<TradingPartnerRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('trading_partner_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.trading_partner.name</code>.
     */
    public final TableField<TradingPartnerRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.trading_partner.email</code>.
     */
    public final TableField<TradingPartnerRecord, String> EMAIL = createField(DSL.name("email"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.trading_partner.key_pair_id</code>.
     */
    public final TableField<TradingPartnerRecord, Long> KEY_PAIR_ID = createField(DSL.name("key_pair_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

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
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> TradingPartner(Table<O> child, ForeignKey<O, TradingPartnerRecord> key) {
        super(child, key, TRADING_PARTNER);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public Identity<TradingPartnerRecord, Long> getIdentity() {
        return Keys.IDENTITY_TRADING_PARTNER;
    }

    @Override
    public UniqueKey<TradingPartnerRecord> getPrimaryKey() {
        return Keys.TRADING_PARTNER_PKEY;
    }

    @Override
    public List<UniqueKey<TradingPartnerRecord>> getKeys() {
        return Arrays.<UniqueKey<TradingPartnerRecord>>asList(Keys.TRADING_PARTNER_PKEY, Keys.TRADING_PARTNER_NAME_KEY, Keys.TRADING_PARTNER_KEY_PAIR_ID_KEY);
    }

    @Override
    public List<ForeignKey<TradingPartnerRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<TradingPartnerRecord, ?>>asList(Keys.TRADING_PARTNER__TRADING_PARTNER_KEY_PAIR_ID_FKEY);
    }

    public KeyPair keyPair() {
        return new KeyPair(this, Keys.TRADING_PARTNER__TRADING_PARTNER_KEY_PAIR_ID_FKEY);
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
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, String, String, Long, Object> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
