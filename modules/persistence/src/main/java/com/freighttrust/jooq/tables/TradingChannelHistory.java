/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables;


import com.freighttrust.common.util.TsTzRange;
import com.freighttrust.jooq.Public;
import com.freighttrust.jooq.enums.TradingChannelType;
import com.freighttrust.jooq.tables.records.TradingChannelHistoryRecord;
import com.freighttrust.persistence.postgres.bindings.TimestampTimezoneRangeBinding;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row12;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TradingChannelHistory extends TableImpl<TradingChannelHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.trading_channel_history</code>
     */
    public static final TradingChannelHistory TRADING_CHANNEL_HISTORY = new TradingChannelHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TradingChannelHistoryRecord> getRecordType() {
        return TradingChannelHistoryRecord.class;
    }

    /**
     * The column <code>public.trading_channel_history.id</code>.
     */
    public final TableField<TradingChannelHistoryRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.trading_channel_history.name</code>.
     */
    public final TableField<TradingChannelHistoryRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>public.trading_channel_history.type</code>.
     */
    public final TableField<TradingChannelHistoryRecord, TradingChannelType> TYPE = createField(DSL.name("type"), SQLDataType.VARCHAR.asEnumDataType(com.freighttrust.jooq.enums.TradingChannelType.class), this, "");

    /**
     * The column <code>public.trading_channel_history.sender_id</code>.
     */
    public final TableField<TradingChannelHistoryRecord, Long> SENDER_ID = createField(DSL.name("sender_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.trading_channel_history.sender_as2_identifier</code>.
     */
    public final TableField<TradingChannelHistoryRecord, String> SENDER_AS2_IDENTIFIER = createField(DSL.name("sender_as2_identifier"), SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>public.trading_channel_history.sender_key_pair_id</code>.
     */
    public final TableField<TradingChannelHistoryRecord, Long> SENDER_KEY_PAIR_ID = createField(DSL.name("sender_key_pair_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.trading_channel_history.recipient_id</code>.
     */
    public final TableField<TradingChannelHistoryRecord, Long> RECIPIENT_ID = createField(DSL.name("recipient_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.trading_channel_history.recipient_as2_identifier</code>.
     */
    public final TableField<TradingChannelHistoryRecord, String> RECIPIENT_AS2_IDENTIFIER = createField(DSL.name("recipient_as2_identifier"), SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>public.trading_channel_history.recipient_key_pair_id</code>.
     */
    public final TableField<TradingChannelHistoryRecord, Long> RECIPIENT_KEY_PAIR_ID = createField(DSL.name("recipient_key_pair_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.trading_channel_history.allow_body_certificate_for_verification</code>.
     */
    public final TableField<TradingChannelHistoryRecord, Boolean> ALLOW_BODY_CERTIFICATE_FOR_VERIFICATION = createField(DSL.name("allow_body_certificate_for_verification"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>public.trading_channel_history.recipient_message_url</code>.
     */
    public final TableField<TradingChannelHistoryRecord, String> RECIPIENT_MESSAGE_URL = createField(DSL.name("recipient_message_url"), SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.trading_channel_history.validity</code>.
     */
    public final TableField<TradingChannelHistoryRecord, TsTzRange> VALIDITY = createField(DSL.name("validity"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\""), this, "", new TimestampTimezoneRangeBinding());

    private TradingChannelHistory(Name alias, Table<TradingChannelHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private TradingChannelHistory(Name alias, Table<TradingChannelHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.trading_channel_history</code> table reference
     */
    public TradingChannelHistory(String alias) {
        this(DSL.name(alias), TRADING_CHANNEL_HISTORY);
    }

    /**
     * Create an aliased <code>public.trading_channel_history</code> table reference
     */
    public TradingChannelHistory(Name alias) {
        this(alias, TRADING_CHANNEL_HISTORY);
    }

    /**
     * Create a <code>public.trading_channel_history</code> table reference
     */
    public TradingChannelHistory() {
        this(DSL.name("trading_channel_history"), null);
    }

    public <O extends Record> TradingChannelHistory(Table<O> child, ForeignKey<O, TradingChannelHistoryRecord> key) {
        super(child, key, TRADING_CHANNEL_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public TradingChannelHistory as(String alias) {
        return new TradingChannelHistory(DSL.name(alias), this);
    }

    @Override
    public TradingChannelHistory as(Name alias) {
        return new TradingChannelHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TradingChannelHistory rename(String name) {
        return new TradingChannelHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TradingChannelHistory rename(Name name) {
        return new TradingChannelHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row12 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row12<Long, String, TradingChannelType, Long, String, Long, Long, String, Long, Boolean, String, TsTzRange> fieldsRow() {
        return (Row12) super.fieldsRow();
    }
}
