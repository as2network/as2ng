/*
 * This file is generated by jOOQ.
 */
package network.as2.jooq.tables.records;


import network.as2.common.util.TsTzRange;
import network.as2.jooq.enums.TradingChannelType;
import network.as2.jooq.tables.TradingChannel;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Row12;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TradingChannelRecord extends UpdatableRecordImpl<TradingChannelRecord> implements Record12<Long, String, TradingChannelType, Long, String, Long, Long, String, Long, Boolean, String, TsTzRange> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.trading_channel.id</code>.
     */
    public TradingChannelRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.trading_channel.name</code>.
     */
    public TradingChannelRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.trading_channel.type</code>.
     */
    public TradingChannelRecord setType(TradingChannelType value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.type</code>.
     */
    public TradingChannelType getType() {
        return (TradingChannelType) get(2);
    }

    /**
     * Setter for <code>public.trading_channel.sender_id</code>.
     */
    public TradingChannelRecord setSenderId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.sender_id</code>.
     */
    public Long getSenderId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>public.trading_channel.sender_as2_identifier</code>.
     */
    public TradingChannelRecord setSenderAs2Identifier(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.sender_as2_identifier</code>.
     */
    public String getSenderAs2Identifier() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.trading_channel.sender_key_pair_id</code>.
     */
    public TradingChannelRecord setSenderKeyPairId(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.sender_key_pair_id</code>.
     */
    public Long getSenderKeyPairId() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>public.trading_channel.recipient_id</code>.
     */
    public TradingChannelRecord setRecipientId(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.recipient_id</code>.
     */
    public Long getRecipientId() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>public.trading_channel.recipient_as2_identifier</code>.
     */
    public TradingChannelRecord setRecipientAs2Identifier(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.recipient_as2_identifier</code>.
     */
    public String getRecipientAs2Identifier() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.trading_channel.recipient_key_pair_id</code>.
     */
    public TradingChannelRecord setRecipientKeyPairId(Long value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.recipient_key_pair_id</code>.
     */
    public Long getRecipientKeyPairId() {
        return (Long) get(8);
    }

    /**
     * Setter for <code>public.trading_channel.allow_body_certificate_for_verification</code>.
     */
    public TradingChannelRecord setAllowBodyCertificateForVerification(Boolean value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.allow_body_certificate_for_verification</code>.
     */
    public Boolean getAllowBodyCertificateForVerification() {
        return (Boolean) get(9);
    }

    /**
     * Setter for <code>public.trading_channel.recipient_message_url</code>.
     */
    public TradingChannelRecord setRecipientMessageUrl(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.recipient_message_url</code>.
     */
    public String getRecipientMessageUrl() {
        return (String) get(10);
    }

    /**
     * Setter for <code>public.trading_channel.validity</code>.
     */
    public TradingChannelRecord setValidity(TsTzRange value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.validity</code>.
     */
    public TsTzRange getValidity() {
        return (TsTzRange) get(11);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record12 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row12<Long, String, TradingChannelType, Long, String, Long, Long, String, Long, Boolean, String, TsTzRange> fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    @Override
    public Row12<Long, String, TradingChannelType, Long, String, Long, Long, String, Long, Boolean, String, TsTzRange> valuesRow() {
        return (Row12) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return TradingChannel.TRADING_CHANNEL.ID;
    }

    @Override
    public Field<String> field2() {
        return TradingChannel.TRADING_CHANNEL.NAME;
    }

    @Override
    public Field<TradingChannelType> field3() {
        return TradingChannel.TRADING_CHANNEL.TYPE;
    }

    @Override
    public Field<Long> field4() {
        return TradingChannel.TRADING_CHANNEL.SENDER_ID;
    }

    @Override
    public Field<String> field5() {
        return TradingChannel.TRADING_CHANNEL.SENDER_AS2_IDENTIFIER;
    }

    @Override
    public Field<Long> field6() {
        return TradingChannel.TRADING_CHANNEL.SENDER_KEY_PAIR_ID;
    }

    @Override
    public Field<Long> field7() {
        return TradingChannel.TRADING_CHANNEL.RECIPIENT_ID;
    }

    @Override
    public Field<String> field8() {
        return TradingChannel.TRADING_CHANNEL.RECIPIENT_AS2_IDENTIFIER;
    }

    @Override
    public Field<Long> field9() {
        return TradingChannel.TRADING_CHANNEL.RECIPIENT_KEY_PAIR_ID;
    }

    @Override
    public Field<Boolean> field10() {
        return TradingChannel.TRADING_CHANNEL.ALLOW_BODY_CERTIFICATE_FOR_VERIFICATION;
    }

    @Override
    public Field<String> field11() {
        return TradingChannel.TRADING_CHANNEL.RECIPIENT_MESSAGE_URL;
    }

    @Override
    public Field<TsTzRange> field12() {
        return TradingChannel.TRADING_CHANNEL.VALIDITY;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public TradingChannelType component3() {
        return getType();
    }

    @Override
    public Long component4() {
        return getSenderId();
    }

    @Override
    public String component5() {
        return getSenderAs2Identifier();
    }

    @Override
    public Long component6() {
        return getSenderKeyPairId();
    }

    @Override
    public Long component7() {
        return getRecipientId();
    }

    @Override
    public String component8() {
        return getRecipientAs2Identifier();
    }

    @Override
    public Long component9() {
        return getRecipientKeyPairId();
    }

    @Override
    public Boolean component10() {
        return getAllowBodyCertificateForVerification();
    }

    @Override
    public String component11() {
        return getRecipientMessageUrl();
    }

    @Override
    public TsTzRange component12() {
        return getValidity();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public TradingChannelType value3() {
        return getType();
    }

    @Override
    public Long value4() {
        return getSenderId();
    }

    @Override
    public String value5() {
        return getSenderAs2Identifier();
    }

    @Override
    public Long value6() {
        return getSenderKeyPairId();
    }

    @Override
    public Long value7() {
        return getRecipientId();
    }

    @Override
    public String value8() {
        return getRecipientAs2Identifier();
    }

    @Override
    public Long value9() {
        return getRecipientKeyPairId();
    }

    @Override
    public Boolean value10() {
        return getAllowBodyCertificateForVerification();
    }

    @Override
    public String value11() {
        return getRecipientMessageUrl();
    }

    @Override
    public TsTzRange value12() {
        return getValidity();
    }

    @Override
    public TradingChannelRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public TradingChannelRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public TradingChannelRecord value3(TradingChannelType value) {
        setType(value);
        return this;
    }

    @Override
    public TradingChannelRecord value4(Long value) {
        setSenderId(value);
        return this;
    }

    @Override
    public TradingChannelRecord value5(String value) {
        setSenderAs2Identifier(value);
        return this;
    }

    @Override
    public TradingChannelRecord value6(Long value) {
        setSenderKeyPairId(value);
        return this;
    }

    @Override
    public TradingChannelRecord value7(Long value) {
        setRecipientId(value);
        return this;
    }

    @Override
    public TradingChannelRecord value8(String value) {
        setRecipientAs2Identifier(value);
        return this;
    }

    @Override
    public TradingChannelRecord value9(Long value) {
        setRecipientKeyPairId(value);
        return this;
    }

    @Override
    public TradingChannelRecord value10(Boolean value) {
        setAllowBodyCertificateForVerification(value);
        return this;
    }

    @Override
    public TradingChannelRecord value11(String value) {
        setRecipientMessageUrl(value);
        return this;
    }

    @Override
    public TradingChannelRecord value12(TsTzRange value) {
        setValidity(value);
        return this;
    }

    @Override
    public TradingChannelRecord values(Long value1, String value2, TradingChannelType value3, Long value4, String value5, Long value6, Long value7, String value8, Long value9, Boolean value10, String value11, TsTzRange value12) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TradingChannelRecord
     */
    public TradingChannelRecord() {
        super(TradingChannel.TRADING_CHANNEL);
    }

    /**
     * Create a detached, initialised TradingChannelRecord
     */
    public TradingChannelRecord(Long id, String name, TradingChannelType type, Long senderId, String senderAs2Identifier, Long senderKeyPairId, Long recipientId, String recipientAs2Identifier, Long recipientKeyPairId, Boolean allowBodyCertificateForVerification, String recipientMessageUrl, TsTzRange validity) {
        super(TradingChannel.TRADING_CHANNEL);

        setId(id);
        setName(name);
        setType(type);
        setSenderId(senderId);
        setSenderAs2Identifier(senderAs2Identifier);
        setSenderKeyPairId(senderKeyPairId);
        setRecipientId(recipientId);
        setRecipientAs2Identifier(recipientAs2Identifier);
        setRecipientKeyPairId(recipientKeyPairId);
        setAllowBodyCertificateForVerification(allowBodyCertificateForVerification);
        setRecipientMessageUrl(recipientMessageUrl);
        setValidity(validity);
    }
}