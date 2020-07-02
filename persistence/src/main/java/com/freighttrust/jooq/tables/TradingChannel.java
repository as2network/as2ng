/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables;


import com.freighttrust.jooq.Indexes;
import com.freighttrust.jooq.Keys;
import com.freighttrust.jooq.Public;
import com.freighttrust.jooq.tables.records.TradingChannelRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import java.util.List;


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
public class TradingChannel extends TableImpl<TradingChannelRecord> {

  private static final long serialVersionUID = 227262993;

    /**
     * The reference instance of <code>public.trading_channel</code>
     */
    public static final TradingChannel TRADING_CHANNEL = new TradingChannel();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TradingChannelRecord> getRecordType() {
        return TradingChannelRecord.class;
    }

    /**
     * The column <code>public.trading_channel.sender_id</code>.
     */
    public final TableField<TradingChannelRecord, String> SENDER_ID = createField(DSL.name("sender_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>public.trading_channel.recipient_id</code>.
     */
    public final TableField<TradingChannelRecord, String> RECIPIENT_ID = createField(DSL.name("recipient_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>public.trading_channel.protocol</code>.
     */
    public final TableField<TradingChannelRecord, String> PROTOCOL = createField(DSL.name("protocol"), org.jooq.impl.SQLDataType.VARCHAR(16), this, "");

    /**
     * The column <code>public.trading_channel.as2_url</code>.
     */
    public final TableField<TradingChannelRecord, String> AS2_URL = createField(DSL.name("as2_url"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.trading_channel.as2_mdn_to</code>.
     */
    public final TableField<TradingChannelRecord, String> AS2_MDN_TO = createField(DSL.name("as2_mdn_to"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.trading_channel.as2_mdn_options</code>.
     */
    public final TableField<TradingChannelRecord, String> AS2_MDN_OPTIONS = createField(DSL.name("as2_mdn_options"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.trading_channel.encryption_algorithm</code>.
     */
    public final TableField<TradingChannelRecord, String> ENCRYPTION_ALGORITHM = createField(DSL.name("encryption_algorithm"), org.jooq.impl.SQLDataType.VARCHAR(16), this, "");

    /**
     * The column <code>public.trading_channel.signing_algorithm</code>.
     */
    public final TableField<TradingChannelRecord, String> SIGNING_ALGORITHM = createField(DSL.name("signing_algorithm"), org.jooq.impl.SQLDataType.VARCHAR(16), this, "");

    /**
     * Create a <code>public.trading_channel</code> table reference
     */
    public TradingChannel() {
        this(DSL.name("trading_channel"), null);
    }

    /**
     * Create an aliased <code>public.trading_channel</code> table reference
     */
    public TradingChannel(String alias) {
        this(DSL.name(alias), TRADING_CHANNEL);
    }

    /**
     * Create an aliased <code>public.trading_channel</code> table reference
     */
    public TradingChannel(Name alias) {
        this(alias, TRADING_CHANNEL);
    }

    private TradingChannel(Name alias, Table<TradingChannelRecord> aliased) {
        this(alias, aliased, null);
    }

    private TradingChannel(Name alias, Table<TradingChannelRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> TradingChannel(Table<O> child, ForeignKey<O, TradingChannelRecord> key) {
        super(child, key, TRADING_CHANNEL);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.TRADING_CHANNEL_PKEY);
    }

    @Override
    public UniqueKey<TradingChannelRecord> getPrimaryKey() {
        return Keys.TRADING_CHANNEL_PKEY;
    }

    @Override
    public List<UniqueKey<TradingChannelRecord>> getKeys() {
        return Arrays.<UniqueKey<TradingChannelRecord>>asList(Keys.TRADING_CHANNEL_PKEY);
    }

    @Override
    public List<ForeignKey<TradingChannelRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<TradingChannelRecord, ?>>asList(Keys.TRADING_CHANNEL__TRADING_CHANNEL_SENDER_ID_FKEY, Keys.TRADING_CHANNEL__TRADING_CHANNEL_RECIPIENT_ID_FKEY);
    }

    public TradingPartner tradingChannel_TradingChannelSenderIdFkey() {
        return new TradingPartner(this, Keys.TRADING_CHANNEL__TRADING_CHANNEL_SENDER_ID_FKEY);
    }

    public TradingPartner tradingChannel_TradingChannelRecipientIdFkey() {
        return new TradingPartner(this, Keys.TRADING_CHANNEL__TRADING_CHANNEL_RECIPIENT_ID_FKEY);
    }

    @Override
    public TradingChannel as(String alias) {
        return new TradingChannel(DSL.name(alias), this);
    }

    @Override
    public TradingChannel as(Name alias) {
        return new TradingChannel(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TradingChannel rename(String name) {
        return new TradingChannel(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TradingChannel rename(Name name) {
        return new TradingChannel(name, null);
    }

  // -------------------------------------------------------------------------
  // Row8 type methods
  // -------------------------------------------------------------------------

  @Override
  public Row8<String, String, String, String, String, String, String, String> fieldsRow() {
    return (Row8) super.fieldsRow();
  }
}