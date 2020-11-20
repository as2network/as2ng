/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables;


import com.freighttrust.jooq.Keys;
import com.freighttrust.jooq.Public;
import com.freighttrust.jooq.tables.records.DispositionNotificationRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row8;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DispositionNotification extends TableImpl<DispositionNotificationRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.disposition_notification</code>
     */
    public static final DispositionNotification DISPOSITION_NOTIFICATION = new DispositionNotification();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DispositionNotificationRecord> getRecordType() {
        return DispositionNotificationRecord.class;
    }

    /**
     * The column <code>public.disposition_notification.request_id</code>.
     */
    public final TableField<DispositionNotificationRecord, UUID> REQUEST_ID = createField(DSL.name("request_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.disposition_notification.original_message_id</code>.
     */
    public final TableField<DispositionNotificationRecord, String> ORIGINAL_MESSAGE_ID = createField(DSL.name("original_message_id"), SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.disposition_notification.original_recipient</code>.
     */
    public final TableField<DispositionNotificationRecord, String> ORIGINAL_RECIPIENT = createField(DSL.name("original_recipient"), SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>public.disposition_notification.final_recipient</code>.
     */
    public final TableField<DispositionNotificationRecord, String> FINAL_RECIPIENT = createField(DSL.name("final_recipient"), SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>public.disposition_notification.reporting_ua</code>.
     */
    public final TableField<DispositionNotificationRecord, String> REPORTING_UA = createField(DSL.name("reporting_ua"), SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>public.disposition_notification.disposition</code>.
     */
    public final TableField<DispositionNotificationRecord, String> DISPOSITION = createField(DSL.name("disposition"), SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.disposition_notification.received_content_mic</code>.
     */
    public final TableField<DispositionNotificationRecord, String> RECEIVED_CONTENT_MIC = createField(DSL.name("received_content_mic"), SQLDataType.VARCHAR(512), this, "");

    /**
     * The column <code>public.disposition_notification.digest_algorithm</code>.
     */
    public final TableField<DispositionNotificationRecord, String> DIGEST_ALGORITHM = createField(DSL.name("digest_algorithm"), SQLDataType.VARCHAR(16), this, "");

    private DispositionNotification(Name alias, Table<DispositionNotificationRecord> aliased) {
        this(alias, aliased, null);
    }

    private DispositionNotification(Name alias, Table<DispositionNotificationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.disposition_notification</code> table reference
     */
    public DispositionNotification(String alias) {
        this(DSL.name(alias), DISPOSITION_NOTIFICATION);
    }

    /**
     * Create an aliased <code>public.disposition_notification</code> table reference
     */
    public DispositionNotification(Name alias) {
        this(alias, DISPOSITION_NOTIFICATION);
    }

    /**
     * Create a <code>public.disposition_notification</code> table reference
     */
    public DispositionNotification() {
        this(DSL.name("disposition_notification"), null);
    }

    public <O extends Record> DispositionNotification(Table<O> child, ForeignKey<O, DispositionNotificationRecord> key) {
        super(child, key, DISPOSITION_NOTIFICATION);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<DispositionNotificationRecord> getPrimaryKey() {
        return Keys.DISPOSITION_NOTIFICATION_PKEY;
    }

    @Override
    public List<UniqueKey<DispositionNotificationRecord>> getKeys() {
        return Arrays.<UniqueKey<DispositionNotificationRecord>>asList(Keys.DISPOSITION_NOTIFICATION_PKEY);
    }

    @Override
    public List<ForeignKey<DispositionNotificationRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<DispositionNotificationRecord, ?>>asList(Keys.DISPOSITION_NOTIFICATION__DISPOSITION_NOTIFICATION_REQUEST_ID_FKEY, Keys.DISPOSITION_NOTIFICATION__DISPOSITION_NOTIFICATION_ORIGINAL_MESSAGE_ID_FKEY);
    }

    public Request dispositionNotificationRequestIdFkey() {
        return new Request(this, Keys.DISPOSITION_NOTIFICATION__DISPOSITION_NOTIFICATION_REQUEST_ID_FKEY);
    }

    public Request dispositionNotificationOriginalMessageIdFkey() {
        return new Request(this, Keys.DISPOSITION_NOTIFICATION__DISPOSITION_NOTIFICATION_ORIGINAL_MESSAGE_ID_FKEY);
    }

    @Override
    public DispositionNotification as(String alias) {
        return new DispositionNotification(DSL.name(alias), this);
    }

    @Override
    public DispositionNotification as(Name alias) {
        return new DispositionNotification(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public DispositionNotification rename(String name) {
        return new DispositionNotification(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public DispositionNotification rename(Name name) {
        return new DispositionNotification(name, null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<UUID, String, String, String, String, String, String, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }
}
