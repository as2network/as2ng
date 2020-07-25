/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq;


import com.freighttrust.jooq.tables.*;

import javax.annotation.processing.Generated;


/**
 * Convenience access to all tables in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>public.as2_mdn</code>.
     */
    public static final As2Mdn AS2_MDN = As2Mdn.AS2_MDN;

    /**
     * The table <code>public.as2_message</code>.
     */
    public static final As2Message AS2_MESSAGE = As2Message.AS2_MESSAGE;

    /**
     * The table <code>public.certificate</code>.
     */
    public static final Certificate CERTIFICATE = Certificate.CERTIFICATE;

    /**
     * The table <code>public.certificate_history</code>.
     */
    public static final CertificateHistory CERTIFICATE_HISTORY = CertificateHistory.CERTIFICATE_HISTORY;

    /**
     * The table <code>public.file</code>.
     */
    public static final File FILE = File.FILE;

    /**
     * The table <code>public.flyway_schema_history</code>.
     */
    public static final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>public.trading_channel</code>.
     */
    public static final TradingChannel TRADING_CHANNEL = TradingChannel.TRADING_CHANNEL;

    /**
     * The table <code>public.trading_channel_history</code>.
     */
    public static final TradingChannelHistory TRADING_CHANNEL_HISTORY = TradingChannelHistory.TRADING_CHANNEL_HISTORY;

    /**
     * The table <code>public.trading_partner</code>.
     */
    public static final TradingPartner TRADING_PARTNER = TradingPartner.TRADING_PARTNER;

    /**
     * The table <code>public.trading_partner_history</code>.
     */
    public static final TradingPartnerHistory TRADING_PARTNER_HISTORY = TradingPartnerHistory.TRADING_PARTNER_HISTORY;
}
