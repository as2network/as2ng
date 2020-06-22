/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq;


import com.freighttrust.jooq.tables.As2Mdn;
import com.freighttrust.jooq.tables.As2Message;
import com.freighttrust.jooq.tables.FlywaySchemaHistory;
import com.freighttrust.jooq.tables.TradingChannel;
import com.freighttrust.jooq.tables.TradingPartner;
import com.freighttrust.jooq.tables.TradingPartnerCertificate;

import javax.annotation.processing.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables of the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index AS2_MDN_PKEY = Indexes0.AS2_MDN_PKEY;
    public static final Index AS2_MESSAGE_PKEY = Indexes0.AS2_MESSAGE_PKEY;
    public static final Index FLYWAY_SCHEMA_HISTORY_PK = Indexes0.FLYWAY_SCHEMA_HISTORY_PK;
    public static final Index FLYWAY_SCHEMA_HISTORY_S_IDX = Indexes0.FLYWAY_SCHEMA_HISTORY_S_IDX;
    public static final Index TRADING_CHANNEL_PKEY = Indexes0.TRADING_CHANNEL_PKEY;
    public static final Index TRADING_PARTNER_NAME_KEY = Indexes0.TRADING_PARTNER_NAME_KEY;
    public static final Index TRADING_PARTNER_PKEY = Indexes0.TRADING_PARTNER_PKEY;
    public static final Index TRADING_PARTNER_CERTIFICATE_PKEY = Indexes0.TRADING_PARTNER_CERTIFICATE_PKEY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index AS2_MDN_PKEY = Internal.createIndex("as2_mdn_pkey", As2Mdn.AS2_MDN, new OrderField[] { As2Mdn.AS2_MDN.ID }, true);
        public static Index AS2_MESSAGE_PKEY = Internal.createIndex("as2_message_pkey", As2Message.AS2_MESSAGE, new OrderField[] { As2Message.AS2_MESSAGE.ID }, true);
        public static Index FLYWAY_SCHEMA_HISTORY_PK = Internal.createIndex("flyway_schema_history_pk", FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, new OrderField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
        public static Index FLYWAY_SCHEMA_HISTORY_S_IDX = Internal.createIndex("flyway_schema_history_s_idx", FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, new OrderField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.SUCCESS }, false);
        public static Index TRADING_CHANNEL_PKEY = Internal.createIndex("trading_channel_pkey", TradingChannel.TRADING_CHANNEL, new OrderField[] { TradingChannel.TRADING_CHANNEL.SENDER_ID, TradingChannel.TRADING_CHANNEL.RECIPIENT_ID }, true);
        public static Index TRADING_PARTNER_NAME_KEY = Internal.createIndex("trading_partner_name_key", TradingPartner.TRADING_PARTNER, new OrderField[] { TradingPartner.TRADING_PARTNER.NAME }, true);
        public static Index TRADING_PARTNER_PKEY = Internal.createIndex("trading_partner_pkey", TradingPartner.TRADING_PARTNER, new OrderField[] { TradingPartner.TRADING_PARTNER.ID }, true);
        public static Index TRADING_PARTNER_CERTIFICATE_PKEY = Internal.createIndex("trading_partner_certificate_pkey", TradingPartnerCertificate.TRADING_PARTNER_CERTIFICATE, new OrderField[] { TradingPartnerCertificate.TRADING_PARTNER_CERTIFICATE.TRADING_PARTNER_ID, TradingPartnerCertificate.TRADING_PARTNER_CERTIFICATE.ALIAS }, true);
    }
}
