/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq;


import com.freighttrust.jooq.tables.DispositionNotification;
import com.freighttrust.jooq.tables.File;
import com.freighttrust.jooq.tables.FlywaySchemaHistory;
import com.freighttrust.jooq.tables.KeyPair;
import com.freighttrust.jooq.tables.Message;
import com.freighttrust.jooq.tables.Request;
import com.freighttrust.jooq.tables.TradingChannel;
import com.freighttrust.jooq.tables.TradingPartner;
import com.freighttrust.jooq.tables.records.DispositionNotificationRecord;
import com.freighttrust.jooq.tables.records.FileRecord;
import com.freighttrust.jooq.tables.records.FlywaySchemaHistoryRecord;
import com.freighttrust.jooq.tables.records.KeyPairRecord;
import com.freighttrust.jooq.tables.records.MessageRecord;
import com.freighttrust.jooq.tables.records.RequestRecord;
import com.freighttrust.jooq.tables.records.TradingChannelRecord;
import com.freighttrust.jooq.tables.records.TradingPartnerRecord;

import javax.annotation.processing.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.13.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<FileRecord, Long> IDENTITY_FILE = Identities0.IDENTITY_FILE;
    public static final Identity<KeyPairRecord, Long> IDENTITY_KEY_PAIR = Identities0.IDENTITY_KEY_PAIR;
    public static final Identity<TradingChannelRecord, Long> IDENTITY_TRADING_CHANNEL = Identities0.IDENTITY_TRADING_CHANNEL;
    public static final Identity<TradingPartnerRecord, Long> IDENTITY_TRADING_PARTNER = Identities0.IDENTITY_TRADING_PARTNER;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<DispositionNotificationRecord> DISPOSITION_NOTIFICATION_PKEY = UniqueKeys0.DISPOSITION_NOTIFICATION_PKEY;
    public static final UniqueKey<FileRecord> FILE_PKEY = UniqueKeys0.FILE_PKEY;
    public static final UniqueKey<FileRecord> FILE_BUCKET_KEY_KEY = UniqueKeys0.FILE_BUCKET_KEY_KEY;
    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = UniqueKeys0.FLYWAY_SCHEMA_HISTORY_PK;
    public static final UniqueKey<KeyPairRecord> KEY_PAIR_PKEY = UniqueKeys0.KEY_PAIR_PKEY;
    public static final UniqueKey<MessageRecord> MESSAGE_PKEY = UniqueKeys0.MESSAGE_PKEY;
    public static final UniqueKey<RequestRecord> REQUEST_PKEY = UniqueKeys0.REQUEST_PKEY;
    public static final UniqueKey<RequestRecord> REQUEST_MESSAGE_ID_KEY = UniqueKeys0.REQUEST_MESSAGE_ID_KEY;
    public static final UniqueKey<TradingChannelRecord> TRADING_CHANNEL_PKEY = UniqueKeys0.TRADING_CHANNEL_PKEY;
    public static final UniqueKey<TradingChannelRecord> TRADING_CHANNEL_SENDER_ID_RECIPIENT_ID_KEY = UniqueKeys0.TRADING_CHANNEL_SENDER_ID_RECIPIENT_ID_KEY;
    public static final UniqueKey<TradingChannelRecord> TRADING_CHANNEL_SENDER_AS2_IDENTIFIER_RECIPIENT_AS2_IDENTIF_KEY = UniqueKeys0.TRADING_CHANNEL_SENDER_AS2_IDENTIFIER_RECIPIENT_AS2_IDENTIF_KEY;
    public static final UniqueKey<TradingPartnerRecord> TRADING_PARTNER_PKEY = UniqueKeys0.TRADING_PARTNER_PKEY;
    public static final UniqueKey<TradingPartnerRecord> TRADING_PARTNER_NAME_KEY = UniqueKeys0.TRADING_PARTNER_NAME_KEY;
    public static final UniqueKey<TradingPartnerRecord> TRADING_PARTNER_KEY_PAIR_ID_KEY = UniqueKeys0.TRADING_PARTNER_KEY_PAIR_ID_KEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<DispositionNotificationRecord, RequestRecord> DISPOSITION_NOTIFICATION__DISPOSITION_NOTIFICATION_REQUEST_ID_FKEY = ForeignKeys0.DISPOSITION_NOTIFICATION__DISPOSITION_NOTIFICATION_REQUEST_ID_FKEY;
    public static final ForeignKey<MessageRecord, RequestRecord> MESSAGE__MESSAGE_REQUEST_ID_FKEY = ForeignKeys0.MESSAGE__MESSAGE_REQUEST_ID_FKEY;
    public static final ForeignKey<RequestRecord, TradingChannelRecord> REQUEST__REQUEST_TRADING_CHANNEL_ID_FKEY = ForeignKeys0.REQUEST__REQUEST_TRADING_CHANNEL_ID_FKEY;
    public static final ForeignKey<RequestRecord, RequestRecord> REQUEST__REQUEST_ORIGINAL_REQUEST_ID_FKEY = ForeignKeys0.REQUEST__REQUEST_ORIGINAL_REQUEST_ID_FKEY;
    public static final ForeignKey<TradingChannelRecord, TradingPartnerRecord> TRADING_CHANNEL__TRADING_CHANNEL_SENDER_ID_FKEY = ForeignKeys0.TRADING_CHANNEL__TRADING_CHANNEL_SENDER_ID_FKEY;
    public static final ForeignKey<TradingChannelRecord, TradingPartnerRecord> TRADING_CHANNEL__TRADING_CHANNEL_RECIPIENT_ID_FKEY = ForeignKeys0.TRADING_CHANNEL__TRADING_CHANNEL_RECIPIENT_ID_FKEY;
    public static final ForeignKey<TradingPartnerRecord, KeyPairRecord> TRADING_PARTNER__TRADING_PARTNER_KEY_PAIR_ID_FKEY = ForeignKeys0.TRADING_PARTNER__TRADING_PARTNER_KEY_PAIR_ID_FKEY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<FileRecord, Long> IDENTITY_FILE = Internal.createIdentity(File.FILE, File.FILE.ID);
        public static Identity<KeyPairRecord, Long> IDENTITY_KEY_PAIR = Internal.createIdentity(KeyPair.KEY_PAIR, KeyPair.KEY_PAIR.ID);
        public static Identity<TradingChannelRecord, Long> IDENTITY_TRADING_CHANNEL = Internal.createIdentity(TradingChannel.TRADING_CHANNEL, TradingChannel.TRADING_CHANNEL.ID);
        public static Identity<TradingPartnerRecord, Long> IDENTITY_TRADING_PARTNER = Internal.createIdentity(TradingPartner.TRADING_PARTNER, TradingPartner.TRADING_PARTNER.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<DispositionNotificationRecord> DISPOSITION_NOTIFICATION_PKEY = Internal.createUniqueKey(DispositionNotification.DISPOSITION_NOTIFICATION, "disposition_notification_pkey", new TableField[] { DispositionNotification.DISPOSITION_NOTIFICATION.REQUEST_ID }, true);
        public static final UniqueKey<FileRecord> FILE_PKEY = Internal.createUniqueKey(File.FILE, "file_pkey", new TableField[] { File.FILE.ID }, true);
        public static final UniqueKey<FileRecord> FILE_BUCKET_KEY_KEY = Internal.createUniqueKey(File.FILE, "file_bucket_key_key", new TableField[] { File.FILE.BUCKET, File.FILE.KEY }, true);
        public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, "flyway_schema_history_pk", new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
        public static final UniqueKey<KeyPairRecord> KEY_PAIR_PKEY = Internal.createUniqueKey(KeyPair.KEY_PAIR, "key_pair_pkey", new TableField[] { KeyPair.KEY_PAIR.ID }, true);
        public static final UniqueKey<MessageRecord> MESSAGE_PKEY = Internal.createUniqueKey(Message.MESSAGE, "message_pkey", new TableField[] { Message.MESSAGE.REQUEST_ID }, true);
        public static final UniqueKey<RequestRecord> REQUEST_PKEY = Internal.createUniqueKey(Request.REQUEST, "request_pkey", new TableField[] { Request.REQUEST.ID }, true);
        public static final UniqueKey<RequestRecord> REQUEST_MESSAGE_ID_KEY = Internal.createUniqueKey(Request.REQUEST, "request_message_id_key", new TableField[] { Request.REQUEST.MESSAGE_ID }, true);
        public static final UniqueKey<TradingChannelRecord> TRADING_CHANNEL_PKEY = Internal.createUniqueKey(TradingChannel.TRADING_CHANNEL, "trading_channel_pkey", new TableField[] { TradingChannel.TRADING_CHANNEL.ID }, true);
        public static final UniqueKey<TradingChannelRecord> TRADING_CHANNEL_SENDER_ID_RECIPIENT_ID_KEY = Internal.createUniqueKey(TradingChannel.TRADING_CHANNEL, "trading_channel_sender_id_recipient_id_key", new TableField[] { TradingChannel.TRADING_CHANNEL.SENDER_ID, TradingChannel.TRADING_CHANNEL.RECIPIENT_ID }, true);
        public static final UniqueKey<TradingChannelRecord> TRADING_CHANNEL_SENDER_AS2_IDENTIFIER_RECIPIENT_AS2_IDENTIF_KEY = Internal.createUniqueKey(TradingChannel.TRADING_CHANNEL, "trading_channel_sender_as2_identifier_recipient_as2_identif_key", new TableField[] { TradingChannel.TRADING_CHANNEL.SENDER_AS2_IDENTIFIER, TradingChannel.TRADING_CHANNEL.RECIPIENT_AS2_IDENTIFIER }, true);
        public static final UniqueKey<TradingPartnerRecord> TRADING_PARTNER_PKEY = Internal.createUniqueKey(TradingPartner.TRADING_PARTNER, "trading_partner_pkey", new TableField[] { TradingPartner.TRADING_PARTNER.ID }, true);
        public static final UniqueKey<TradingPartnerRecord> TRADING_PARTNER_NAME_KEY = Internal.createUniqueKey(TradingPartner.TRADING_PARTNER, "trading_partner_name_key", new TableField[] { TradingPartner.TRADING_PARTNER.NAME }, true);
        public static final UniqueKey<TradingPartnerRecord> TRADING_PARTNER_KEY_PAIR_ID_KEY = Internal.createUniqueKey(TradingPartner.TRADING_PARTNER, "trading_partner_key_pair_id_key", new TableField[] { TradingPartner.TRADING_PARTNER.KEY_PAIR_ID }, true);
    }

    private static class ForeignKeys0 {
        public static final ForeignKey<DispositionNotificationRecord, RequestRecord> DISPOSITION_NOTIFICATION__DISPOSITION_NOTIFICATION_REQUEST_ID_FKEY = Internal.createForeignKey(Keys.REQUEST_PKEY, DispositionNotification.DISPOSITION_NOTIFICATION, "disposition_notification_request_id_fkey", new TableField[] { DispositionNotification.DISPOSITION_NOTIFICATION.REQUEST_ID }, true);
        public static final ForeignKey<MessageRecord, RequestRecord> MESSAGE__MESSAGE_REQUEST_ID_FKEY = Internal.createForeignKey(Keys.REQUEST_PKEY, Message.MESSAGE, "message_request_id_fkey", new TableField[] { Message.MESSAGE.REQUEST_ID }, true);
        public static final ForeignKey<RequestRecord, TradingChannelRecord> REQUEST__REQUEST_TRADING_CHANNEL_ID_FKEY = Internal.createForeignKey(Keys.TRADING_CHANNEL_PKEY, Request.REQUEST, "request_trading_channel_id_fkey", new TableField[] { Request.REQUEST.TRADING_CHANNEL_ID }, true);
        public static final ForeignKey<RequestRecord, RequestRecord> REQUEST__REQUEST_ORIGINAL_REQUEST_ID_FKEY = Internal.createForeignKey(Keys.REQUEST_PKEY, Request.REQUEST, "request_original_request_id_fkey", new TableField[] { Request.REQUEST.ORIGINAL_REQUEST_ID }, true);
        public static final ForeignKey<TradingChannelRecord, TradingPartnerRecord> TRADING_CHANNEL__TRADING_CHANNEL_SENDER_ID_FKEY = Internal.createForeignKey(Keys.TRADING_PARTNER_PKEY, TradingChannel.TRADING_CHANNEL, "trading_channel_sender_id_fkey", new TableField[] { TradingChannel.TRADING_CHANNEL.SENDER_ID }, true);
        public static final ForeignKey<TradingChannelRecord, TradingPartnerRecord> TRADING_CHANNEL__TRADING_CHANNEL_RECIPIENT_ID_FKEY = Internal.createForeignKey(Keys.TRADING_PARTNER_PKEY, TradingChannel.TRADING_CHANNEL, "trading_channel_recipient_id_fkey", new TableField[] { TradingChannel.TRADING_CHANNEL.RECIPIENT_ID }, true);
        public static final ForeignKey<TradingPartnerRecord, KeyPairRecord> TRADING_PARTNER__TRADING_PARTNER_KEY_PAIR_ID_FKEY = Internal.createForeignKey(Keys.KEY_PAIR_PKEY, TradingPartner.TRADING_PARTNER, "trading_partner_key_pair_id_fkey", new TableField[] { TradingPartner.TRADING_PARTNER.KEY_PAIR_ID }, true);
    }
}
