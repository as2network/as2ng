/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq;


import com.freighttrust.jooq.tables.*;
import com.freighttrust.jooq.tables.records.*;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;

import javax.annotation.processing.Generated;


/**
 * A class modelling foreign key relationships and constraints of tables of
 * the <code>public</code> schema.
 */
@Generated(
  value = {
    "http://www.jooq.org",
    "jOOQ version:3.12.3"
  },
  comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Keys {

  // -------------------------------------------------------------------------
  // IDENTITY definitions
  // -------------------------------------------------------------------------

  public static final Identity<FileRecord, Integer> IDENTITY_FILE = Identities0.IDENTITY_FILE;

  // -------------------------------------------------------------------------
  // UNIQUE and PRIMARY KEY definitions
  // -------------------------------------------------------------------------

  public static final UniqueKey<As2MdnRecord> AS2_MDN_PKEY = UniqueKeys0.AS2_MDN_PKEY;
  public static final UniqueKey<As2MessageRecord> AS2_MESSAGE_PKEY = UniqueKeys0.AS2_MESSAGE_PKEY;
  public static final UniqueKey<CertificateRecord> CERTIFICATE_PKEY = UniqueKeys0.CERTIFICATE_PKEY;
  public static final UniqueKey<FileRecord> FILE_PKEY = UniqueKeys0.FILE_PKEY;
  public static final UniqueKey<FileRecord> FILE_BUCKET_KEY_KEY = UniqueKeys0.FILE_BUCKET_KEY_KEY;
  public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = UniqueKeys0.FLYWAY_SCHEMA_HISTORY_PK;
  public static final UniqueKey<TradingChannelRecord> TRADING_CHANNEL_PKEY = UniqueKeys0.TRADING_CHANNEL_PKEY;
  public static final UniqueKey<TradingPartnerRecord> TRADING_PARTNER_PKEY = UniqueKeys0.TRADING_PARTNER_PKEY;
  public static final UniqueKey<TradingPartnerRecord> TRADING_PARTNER_NAME_KEY = UniqueKeys0.TRADING_PARTNER_NAME_KEY;

  // -------------------------------------------------------------------------
  // FOREIGN KEY definitions
  // -------------------------------------------------------------------------

  public static final ForeignKey<As2MdnRecord, As2MessageRecord> AS2_MDN__AS2_MDN_MESSAGE_ID_FKEY = ForeignKeys0.AS2_MDN__AS2_MDN_MESSAGE_ID_FKEY;
  public static final ForeignKey<As2MdnRecord, FileRecord> AS2_MDN__AS2_MDN_BODY_FILE_ID_FKEY = ForeignKeys0.AS2_MDN__AS2_MDN_BODY_FILE_ID_FKEY;
  public static final ForeignKey<As2MessageRecord, TradingPartnerRecord> AS2_MESSAGE__AS2_MESSAGE_FROM_FKEY = ForeignKeys0.AS2_MESSAGE__AS2_MESSAGE_FROM_FKEY;
  public static final ForeignKey<As2MessageRecord, TradingPartnerRecord> AS2_MESSAGE__AS2_MESSAGE_TO_FKEY = ForeignKeys0.AS2_MESSAGE__AS2_MESSAGE_TO_FKEY;
  public static final ForeignKey<As2MessageRecord, FileRecord> AS2_MESSAGE__AS2_MESSAGE_BODY_FILE_ID_FKEY = ForeignKeys0.AS2_MESSAGE__AS2_MESSAGE_BODY_FILE_ID_FKEY;
  public static final ForeignKey<CertificateRecord, TradingPartnerRecord> CERTIFICATE__CERTIFICATE_TRADING_PARTNER_ID_FKEY = ForeignKeys0.CERTIFICATE__CERTIFICATE_TRADING_PARTNER_ID_FKEY;
  public static final ForeignKey<TradingChannelRecord, TradingPartnerRecord> TRADING_CHANNEL__TRADING_CHANNEL_SENDER_ID_FKEY = ForeignKeys0.TRADING_CHANNEL__TRADING_CHANNEL_SENDER_ID_FKEY;
  public static final ForeignKey<TradingChannelRecord, TradingPartnerRecord> TRADING_CHANNEL__TRADING_CHANNEL_RECIPIENT_ID_FKEY = ForeignKeys0.TRADING_CHANNEL__TRADING_CHANNEL_RECIPIENT_ID_FKEY;

  // -------------------------------------------------------------------------
  // [#1459] distribute members to avoid static initialisers > 64kb
  // -------------------------------------------------------------------------

  private static class Identities0 {
    public static Identity<FileRecord, Integer> IDENTITY_FILE = Internal.createIdentity(File.FILE, File.FILE.ID);
  }

  private static class UniqueKeys0 {
    public static final UniqueKey<As2MdnRecord> AS2_MDN_PKEY = Internal.createUniqueKey(As2Mdn.AS2_MDN, "as2_mdn_pkey", As2Mdn.AS2_MDN.ID);
    public static final UniqueKey<As2MessageRecord> AS2_MESSAGE_PKEY = Internal.createUniqueKey(As2Message.AS2_MESSAGE, "as2_message_pkey", As2Message.AS2_MESSAGE.ID);
    public static final UniqueKey<CertificateRecord> CERTIFICATE_PKEY = Internal.createUniqueKey(Certificate.CERTIFICATE, "certificate_pkey", Certificate.CERTIFICATE.TRADING_PARTNER_ID);
    public static final UniqueKey<FileRecord> FILE_PKEY = Internal.createUniqueKey(File.FILE, "file_pkey", File.FILE.ID);
    public static final UniqueKey<FileRecord> FILE_BUCKET_KEY_KEY = Internal.createUniqueKey(File.FILE, "file_bucket_key_key", File.FILE.BUCKET, File.FILE.KEY);
    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, "flyway_schema_history_pk", FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK);
    public static final UniqueKey<TradingChannelRecord> TRADING_CHANNEL_PKEY = Internal.createUniqueKey(TradingChannel.TRADING_CHANNEL, "trading_channel_pkey", TradingChannel.TRADING_CHANNEL.SENDER_ID, TradingChannel.TRADING_CHANNEL.RECIPIENT_ID);
    public static final UniqueKey<TradingPartnerRecord> TRADING_PARTNER_PKEY = Internal.createUniqueKey(TradingPartner.TRADING_PARTNER, "trading_partner_pkey", TradingPartner.TRADING_PARTNER.ID);
    public static final UniqueKey<TradingPartnerRecord> TRADING_PARTNER_NAME_KEY = Internal.createUniqueKey(TradingPartner.TRADING_PARTNER, "trading_partner_name_key", TradingPartner.TRADING_PARTNER.NAME);
  }

  private static class ForeignKeys0 {
    public static final ForeignKey<As2MdnRecord, As2MessageRecord> AS2_MDN__AS2_MDN_MESSAGE_ID_FKEY = Internal.createForeignKey(com.freighttrust.jooq.Keys.AS2_MESSAGE_PKEY, As2Mdn.AS2_MDN, "as2_mdn__as2_mdn_message_id_fkey", As2Mdn.AS2_MDN.MESSAGE_ID);
    public static final ForeignKey<As2MdnRecord, FileRecord> AS2_MDN__AS2_MDN_BODY_FILE_ID_FKEY = Internal.createForeignKey(com.freighttrust.jooq.Keys.FILE_PKEY, As2Mdn.AS2_MDN, "as2_mdn__as2_mdn_body_file_id_fkey", As2Mdn.AS2_MDN.BODY_FILE_ID);
    public static final ForeignKey<As2MessageRecord, TradingPartnerRecord> AS2_MESSAGE__AS2_MESSAGE_FROM_FKEY = Internal.createForeignKey(com.freighttrust.jooq.Keys.TRADING_PARTNER_PKEY, As2Message.AS2_MESSAGE, "as2_message__as2_message_from_fkey", As2Message.AS2_MESSAGE.FROM);
    public static final ForeignKey<As2MessageRecord, TradingPartnerRecord> AS2_MESSAGE__AS2_MESSAGE_TO_FKEY = Internal.createForeignKey(com.freighttrust.jooq.Keys.TRADING_PARTNER_PKEY, As2Message.AS2_MESSAGE, "as2_message__as2_message_to_fkey", As2Message.AS2_MESSAGE.TO);
    public static final ForeignKey<As2MessageRecord, FileRecord> AS2_MESSAGE__AS2_MESSAGE_BODY_FILE_ID_FKEY = Internal.createForeignKey(com.freighttrust.jooq.Keys.FILE_PKEY, As2Message.AS2_MESSAGE, "as2_message__as2_message_body_file_id_fkey", As2Message.AS2_MESSAGE.BODY_FILE_ID);
    public static final ForeignKey<CertificateRecord, TradingPartnerRecord> CERTIFICATE__CERTIFICATE_TRADING_PARTNER_ID_FKEY = Internal.createForeignKey(com.freighttrust.jooq.Keys.TRADING_PARTNER_PKEY, Certificate.CERTIFICATE, "certificate__certificate_trading_partner_id_fkey", Certificate.CERTIFICATE.TRADING_PARTNER_ID);
    public static final ForeignKey<TradingChannelRecord, TradingPartnerRecord> TRADING_CHANNEL__TRADING_CHANNEL_SENDER_ID_FKEY = Internal.createForeignKey(com.freighttrust.jooq.Keys.TRADING_PARTNER_PKEY, TradingChannel.TRADING_CHANNEL, "trading_channel__trading_channel_sender_id_fkey", TradingChannel.TRADING_CHANNEL.SENDER_ID);
    public static final ForeignKey<TradingChannelRecord, TradingPartnerRecord> TRADING_CHANNEL__TRADING_CHANNEL_RECIPIENT_ID_FKEY = Internal.createForeignKey(com.freighttrust.jooq.Keys.TRADING_PARTNER_PKEY, TradingChannel.TRADING_CHANNEL, "trading_channel__trading_channel_recipient_id_fkey", TradingChannel.TRADING_CHANNEL.RECIPIENT_ID);
  }
}
