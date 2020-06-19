/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.records;


import com.freighttrust.jooq.tables.TradingPartnership;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


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
public class TradingPartnershipRecord extends UpdatableRecordImpl<TradingPartnershipRecord> implements Record9<String, String, String, String, String, String, String[], String, String> {

    private static final long serialVersionUID = -2059163061;

    /**
     * Setter for <code>public.trading_partnership.sender_id</code>.
     */
    public TradingPartnershipRecord setSenderId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partnership.sender_id</code>.
     */
    public String getSenderId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.trading_partnership.recipient_id</code>.
     */
    public TradingPartnershipRecord setRecipientId(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partnership.recipient_id</code>.
     */
    public String getRecipientId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.trading_partnership.protocol</code>.
     */
    public TradingPartnershipRecord setProtocol(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partnership.protocol</code>.
     */
    public String getProtocol() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.trading_partnership.subject</code>.
     */
    public TradingPartnershipRecord setSubject(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partnership.subject</code>.
     */
    public String getSubject() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.trading_partnership.as2_url</code>.
     */
    public TradingPartnershipRecord setAs2Url(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partnership.as2_url</code>.
     */
    public String getAs2Url() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.trading_partnership.as2_mdn_to</code>.
     */
    public TradingPartnershipRecord setAs2MdnTo(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partnership.as2_mdn_to</code>.
     */
    public String getAs2MdnTo() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.trading_partnership.as2_mdn_options</code>.
     */
    public TradingPartnershipRecord setAs2MdnOptions(String... value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partnership.as2_mdn_options</code>.
     */
    public String[] getAs2MdnOptions() {
        return (String[]) get(6);
    }

    /**
     * Setter for <code>public.trading_partnership.encryption_algorithm</code>.
     */
    public TradingPartnershipRecord setEncryptionAlgorithm(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partnership.encryption_algorithm</code>.
     */
    public String getEncryptionAlgorithm() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.trading_partnership.signing_algorithm</code>.
     */
    public TradingPartnershipRecord setSigningAlgorithm(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partnership.signing_algorithm</code>.
     */
    public String getSigningAlgorithm() {
        return (String) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<String, String, String, String, String, String, String[], String, String> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<String, String, String, String, String, String, String[], String, String> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return TradingPartnership.TRADING_PARTNERSHIP.SENDER_ID;
    }

    @Override
    public Field<String> field2() {
        return TradingPartnership.TRADING_PARTNERSHIP.RECIPIENT_ID;
    }

    @Override
    public Field<String> field3() {
        return TradingPartnership.TRADING_PARTNERSHIP.PROTOCOL;
    }

    @Override
    public Field<String> field4() {
        return TradingPartnership.TRADING_PARTNERSHIP.SUBJECT;
    }

    @Override
    public Field<String> field5() {
        return TradingPartnership.TRADING_PARTNERSHIP.AS2_URL;
    }

    @Override
    public Field<String> field6() {
        return TradingPartnership.TRADING_PARTNERSHIP.AS2_MDN_TO;
    }

    @Override
    public Field<String[]> field7() {
        return TradingPartnership.TRADING_PARTNERSHIP.AS2_MDN_OPTIONS;
    }

    @Override
    public Field<String> field8() {
        return TradingPartnership.TRADING_PARTNERSHIP.ENCRYPTION_ALGORITHM;
    }

    @Override
    public Field<String> field9() {
        return TradingPartnership.TRADING_PARTNERSHIP.SIGNING_ALGORITHM;
    }

    @Override
    public String component1() {
        return getSenderId();
    }

    @Override
    public String component2() {
        return getRecipientId();
    }

    @Override
    public String component3() {
        return getProtocol();
    }

    @Override
    public String component4() {
        return getSubject();
    }

    @Override
    public String component5() {
        return getAs2Url();
    }

    @Override
    public String component6() {
        return getAs2MdnTo();
    }

    @Override
    public String[] component7() {
        return getAs2MdnOptions();
    }

    @Override
    public String component8() {
        return getEncryptionAlgorithm();
    }

    @Override
    public String component9() {
        return getSigningAlgorithm();
    }

    @Override
    public String value1() {
        return getSenderId();
    }

    @Override
    public String value2() {
        return getRecipientId();
    }

    @Override
    public String value3() {
        return getProtocol();
    }

    @Override
    public String value4() {
        return getSubject();
    }

    @Override
    public String value5() {
        return getAs2Url();
    }

    @Override
    public String value6() {
        return getAs2MdnTo();
    }

    @Override
    public String[] value7() {
        return getAs2MdnOptions();
    }

    @Override
    public String value8() {
        return getEncryptionAlgorithm();
    }

    @Override
    public String value9() {
        return getSigningAlgorithm();
    }

    @Override
    public TradingPartnershipRecord value1(String value) {
        setSenderId(value);
        return this;
    }

    @Override
    public TradingPartnershipRecord value2(String value) {
        setRecipientId(value);
        return this;
    }

    @Override
    public TradingPartnershipRecord value3(String value) {
        setProtocol(value);
        return this;
    }

    @Override
    public TradingPartnershipRecord value4(String value) {
        setSubject(value);
        return this;
    }

    @Override
    public TradingPartnershipRecord value5(String value) {
        setAs2Url(value);
        return this;
    }

    @Override
    public TradingPartnershipRecord value6(String value) {
        setAs2MdnTo(value);
        return this;
    }

    @Override
    public TradingPartnershipRecord value7(String... value) {
        setAs2MdnOptions(value);
        return this;
    }

    @Override
    public TradingPartnershipRecord value8(String value) {
        setEncryptionAlgorithm(value);
        return this;
    }

    @Override
    public TradingPartnershipRecord value9(String value) {
        setSigningAlgorithm(value);
        return this;
    }

    @Override
    public TradingPartnershipRecord values(String value1, String value2, String value3, String value4, String value5, String value6, String[] value7, String value8, String value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TradingPartnershipRecord
     */
    public TradingPartnershipRecord() {
        super(TradingPartnership.TRADING_PARTNERSHIP);
    }

    /**
     * Create a detached, initialised TradingPartnershipRecord
     */
    public TradingPartnershipRecord(String senderId, String recipientId, String protocol, String subject, String as2Url, String as2MdnTo, String[] as2MdnOptions, String encryptionAlgorithm, String signingAlgorithm) {
        super(TradingPartnership.TRADING_PARTNERSHIP);

        set(0, senderId);
        set(1, recipientId);
        set(2, protocol);
        set(3, subject);
        set(4, as2Url);
        set(5, as2MdnTo);
        set(6, as2MdnOptions);
        set(7, encryptionAlgorithm);
        set(8, signingAlgorithm);
    }
}
