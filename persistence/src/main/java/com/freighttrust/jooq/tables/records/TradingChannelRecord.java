/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.records;


import com.freighttrust.jooq.tables.TradingChannel;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.13.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TradingChannelRecord extends UpdatableRecordImpl<TradingChannelRecord> implements Record8<Long, String, Long, String, Long, String, String, Object> {

    private static final long serialVersionUID = -967626049;

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
     * Setter for <code>public.trading_channel.sender_id</code>.
     */
    public TradingChannelRecord setSenderId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.sender_id</code>.
     */
    public Long getSenderId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.trading_channel.sender_as2_identifier</code>.
     */
    public TradingChannelRecord setSenderAs2Identifier(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.sender_as2_identifier</code>.
     */
    public String getSenderAs2Identifier() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.trading_channel.recipient_id</code>.
     */
    public TradingChannelRecord setRecipientId(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.recipient_id</code>.
     */
    public Long getRecipientId() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>public.trading_channel.recipient_as2_identifier</code>.
     */
    public TradingChannelRecord setRecipientAs2Identifier(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.recipient_as2_identifier</code>.
     */
    public String getRecipientAs2Identifier() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.trading_channel.recipient_message_url</code>.
     */
    public TradingChannelRecord setRecipientMessageUrl(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_channel.recipient_message_url</code>.
     */
    public String getRecipientMessageUrl() {
        return (String) get(6);
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    public TradingChannelRecord setValidity(Object value) {
        set(7, value);
        return this;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    public Object getValidity() {
        return get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<Long, String, Long, String, Long, String, String, Object> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<Long, String, Long, String, Long, String, String, Object> valuesRow() {
        return (Row8) super.valuesRow();
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
    public Field<Long> field3() {
        return TradingChannel.TRADING_CHANNEL.SENDER_ID;
    }

    @Override
    public Field<String> field4() {
        return TradingChannel.TRADING_CHANNEL.SENDER_AS2_IDENTIFIER;
    }

    @Override
    public Field<Long> field5() {
        return TradingChannel.TRADING_CHANNEL.RECIPIENT_ID;
    }

    @Override
    public Field<String> field6() {
        return TradingChannel.TRADING_CHANNEL.RECIPIENT_AS2_IDENTIFIER;
    }

    @Override
    public Field<String> field7() {
        return TradingChannel.TRADING_CHANNEL.RECIPIENT_MESSAGE_URL;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Field<Object> field8() {
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
    public Long component3() {
        return getSenderId();
    }

    @Override
    public String component4() {
        return getSenderAs2Identifier();
    }

    @Override
    public Long component5() {
        return getRecipientId();
    }

    @Override
    public String component6() {
        return getRecipientAs2Identifier();
    }

    @Override
    public String component7() {
        return getRecipientMessageUrl();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Object component8() {
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
    public Long value3() {
        return getSenderId();
    }

    @Override
    public String value4() {
        return getSenderAs2Identifier();
    }

    @Override
    public Long value5() {
        return getRecipientId();
    }

    @Override
    public String value6() {
        return getRecipientAs2Identifier();
    }

    @Override
    public String value7() {
        return getRecipientMessageUrl();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Object value8() {
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
    public TradingChannelRecord value3(Long value) {
        setSenderId(value);
        return this;
    }

    @Override
    public TradingChannelRecord value4(String value) {
        setSenderAs2Identifier(value);
        return this;
    }

    @Override
    public TradingChannelRecord value5(Long value) {
        setRecipientId(value);
        return this;
    }

    @Override
    public TradingChannelRecord value6(String value) {
        setRecipientAs2Identifier(value);
        return this;
    }

    @Override
    public TradingChannelRecord value7(String value) {
        setRecipientMessageUrl(value);
        return this;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public TradingChannelRecord value8(Object value) {
        setValidity(value);
        return this;
    }

    @Override
    public TradingChannelRecord values(Long value1, String value2, Long value3, String value4, Long value5, String value6, String value7, Object value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
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
    public TradingChannelRecord(Long id, String name, Long senderId, String senderAs2Identifier, Long recipientId, String recipientAs2Identifier, String recipientMessageUrl, Object validity) {
        super(TradingChannel.TRADING_CHANNEL);

        set(0, id);
        set(1, name);
        set(2, senderId);
        set(3, senderAs2Identifier);
        set(4, recipientId);
        set(5, recipientAs2Identifier);
        set(6, recipientMessageUrl);
        set(7, validity);
    }
}
