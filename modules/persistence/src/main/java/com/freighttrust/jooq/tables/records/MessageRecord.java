/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.records;


import com.freighttrust.jooq.tables.Message;

import java.util.UUID;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
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
public class MessageRecord extends UpdatableRecordImpl<MessageRecord> implements Record7<UUID, String, String, String[], Boolean, Boolean, String> {

    private static final long serialVersionUID = -139222199;

    /**
     * Setter for <code>public.message.request_id</code>.
     */
    public MessageRecord setRequestId(UUID value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.message.request_id</code>.
     */
    public UUID getRequestId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.message.encryption_algorithm</code>.
     */
    public MessageRecord setEncryptionAlgorithm(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.message.encryption_algorithm</code>.
     */
    public String getEncryptionAlgorithm() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.message.compression_algorithm</code>.
     */
    public MessageRecord setCompressionAlgorithm(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.message.compression_algorithm</code>.
     */
    public String getCompressionAlgorithm() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.message.mics</code>.
     */
    public MessageRecord setMics(String... value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.message.mics</code>.
     */
    public String[] getMics() {
        return (String[]) get(3);
    }

    /**
     * Setter for <code>public.message.is_mdn_requested</code>.
     */
    public MessageRecord setIsMdnRequested(Boolean value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.message.is_mdn_requested</code>.
     */
    public Boolean getIsMdnRequested() {
        return (Boolean) get(4);
    }

    /**
     * Setter for <code>public.message.is_mdn_async</code>.
     */
    public MessageRecord setIsMdnAsync(Boolean value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.message.is_mdn_async</code>.
     */
    public Boolean getIsMdnAsync() {
        return (Boolean) get(5);
    }

    /**
     * Setter for <code>public.message.receipt_delivery_option</code>.
     */
    public MessageRecord setReceiptDeliveryOption(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.message.receipt_delivery_option</code>.
     */
    public String getReceiptDeliveryOption() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<UUID, String, String, String[], Boolean, Boolean, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<UUID, String, String, String[], Boolean, Boolean, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return Message.MESSAGE.REQUEST_ID;
    }

    @Override
    public Field<String> field2() {
        return Message.MESSAGE.ENCRYPTION_ALGORITHM;
    }

    @Override
    public Field<String> field3() {
        return Message.MESSAGE.COMPRESSION_ALGORITHM;
    }

    @Override
    public Field<String[]> field4() {
        return Message.MESSAGE.MICS;
    }

    @Override
    public Field<Boolean> field5() {
        return Message.MESSAGE.IS_MDN_REQUESTED;
    }

    @Override
    public Field<Boolean> field6() {
        return Message.MESSAGE.IS_MDN_ASYNC;
    }

    @Override
    public Field<String> field7() {
        return Message.MESSAGE.RECEIPT_DELIVERY_OPTION;
    }

    @Override
    public UUID component1() {
        return getRequestId();
    }

    @Override
    public String component2() {
        return getEncryptionAlgorithm();
    }

    @Override
    public String component3() {
        return getCompressionAlgorithm();
    }

    @Override
    public String[] component4() {
        return getMics();
    }

    @Override
    public Boolean component5() {
        return getIsMdnRequested();
    }

    @Override
    public Boolean component6() {
        return getIsMdnAsync();
    }

    @Override
    public String component7() {
        return getReceiptDeliveryOption();
    }

    @Override
    public UUID value1() {
        return getRequestId();
    }

    @Override
    public String value2() {
        return getEncryptionAlgorithm();
    }

    @Override
    public String value3() {
        return getCompressionAlgorithm();
    }

    @Override
    public String[] value4() {
        return getMics();
    }

    @Override
    public Boolean value5() {
        return getIsMdnRequested();
    }

    @Override
    public Boolean value6() {
        return getIsMdnAsync();
    }

    @Override
    public String value7() {
        return getReceiptDeliveryOption();
    }

    @Override
    public MessageRecord value1(UUID value) {
        setRequestId(value);
        return this;
    }

    @Override
    public MessageRecord value2(String value) {
        setEncryptionAlgorithm(value);
        return this;
    }

    @Override
    public MessageRecord value3(String value) {
        setCompressionAlgorithm(value);
        return this;
    }

    @Override
    public MessageRecord value4(String... value) {
        setMics(value);
        return this;
    }

    @Override
    public MessageRecord value5(Boolean value) {
        setIsMdnRequested(value);
        return this;
    }

    @Override
    public MessageRecord value6(Boolean value) {
        setIsMdnAsync(value);
        return this;
    }

    @Override
    public MessageRecord value7(String value) {
        setReceiptDeliveryOption(value);
        return this;
    }

    @Override
    public MessageRecord values(UUID value1, String value2, String value3, String[] value4, Boolean value5, Boolean value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MessageRecord
     */
    public MessageRecord() {
        super(Message.MESSAGE);
    }

    /**
     * Create a detached, initialised MessageRecord
     */
    public MessageRecord(UUID requestId, String encryptionAlgorithm, String compressionAlgorithm, String[] mics, Boolean isMdnRequested, Boolean isMdnAsync, String receiptDeliveryOption) {
        super(Message.MESSAGE);

        set(0, requestId);
        set(1, encryptionAlgorithm);
        set(2, compressionAlgorithm);
        set(3, mics);
        set(4, isMdnRequested);
        set(5, isMdnAsync);
        set(6, receiptDeliveryOption);
    }
}