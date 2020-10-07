/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables;


import com.freighttrust.jooq.Indexes;
import com.freighttrust.jooq.Keys;
import com.freighttrust.jooq.Public;
import com.freighttrust.jooq.tables.records.MessageRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
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
public class Message extends TableImpl<MessageRecord> {

    private static final long serialVersionUID = -1549531211;

    /**
     * The reference instance of <code>public.message</code>
     */
    public static final Message MESSAGE = new Message();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<MessageRecord> getRecordType() {
        return MessageRecord.class;
    }

    /**
     * The column <code>public.message.request_id</code>.
     */
    public final TableField<MessageRecord, UUID> REQUEST_ID = createField(DSL.name("request_id"), org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.message.encryption_algorithm</code>.
     */
    public final TableField<MessageRecord, String> ENCRYPTION_ALGORITHM = createField(DSL.name("encryption_algorithm"), org.jooq.impl.SQLDataType.VARCHAR(16), this, "");

    /**
     * The column <code>public.message.compression_algorithm</code>.
     */
    public final TableField<MessageRecord, String> COMPRESSION_ALGORITHM = createField(DSL.name("compression_algorithm"), org.jooq.impl.SQLDataType.VARCHAR(16), this, "");

    /**
     * The column <code>public.message.mics</code>.
     */
    public final TableField<MessageRecord, String[]> MICS = createField(DSL.name("mics"), org.jooq.impl.SQLDataType.VARCHAR(64).getArrayDataType(), this, "");

    /**
     * The column <code>public.message.is_mdn_requested</code>.
     */
    public final TableField<MessageRecord, Boolean> IS_MDN_REQUESTED = createField(DSL.name("is_mdn_requested"), org.jooq.impl.SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>public.message.is_mdn_async</code>.
     */
    public final TableField<MessageRecord, Boolean> IS_MDN_ASYNC = createField(DSL.name("is_mdn_async"), org.jooq.impl.SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>public.message.receipt_delivery_option</code>.
     */
    public final TableField<MessageRecord, String> RECEIPT_DELIVERY_OPTION = createField(DSL.name("receipt_delivery_option"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * Create a <code>public.message</code> table reference
     */
    public Message() {
        this(DSL.name("message"), null);
    }

    /**
     * Create an aliased <code>public.message</code> table reference
     */
    public Message(String alias) {
        this(DSL.name(alias), MESSAGE);
    }

    /**
     * Create an aliased <code>public.message</code> table reference
     */
    public Message(Name alias) {
        this(alias, MESSAGE);
    }

    private Message(Name alias, Table<MessageRecord> aliased) {
        this(alias, aliased, null);
    }

    private Message(Name alias, Table<MessageRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Message(Table<O> child, ForeignKey<O, MessageRecord> key) {
        super(child, key, MESSAGE);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.MESSAGE_PKEY);
    }

    @Override
    public UniqueKey<MessageRecord> getPrimaryKey() {
        return Keys.MESSAGE_PKEY;
    }

    @Override
    public List<UniqueKey<MessageRecord>> getKeys() {
        return Arrays.<UniqueKey<MessageRecord>>asList(Keys.MESSAGE_PKEY);
    }

    @Override
    public List<ForeignKey<MessageRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<MessageRecord, ?>>asList(Keys.MESSAGE__MESSAGE_REQUEST_ID_FKEY);
    }

    public Request request() {
        return new Request(this, Keys.MESSAGE__MESSAGE_REQUEST_ID_FKEY);
    }

    @Override
    public Message as(String alias) {
        return new Message(DSL.name(alias), this);
    }

    @Override
    public Message as(Name alias) {
        return new Message(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Message rename(String name) {
        return new Message(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Message rename(Name name) {
        return new Message(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<UUID, String, String, String[], Boolean, Boolean, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
