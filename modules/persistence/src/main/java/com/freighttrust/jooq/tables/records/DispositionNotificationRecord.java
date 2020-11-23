/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.records;


import com.freighttrust.jooq.tables.DispositionNotification;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DispositionNotificationRecord extends UpdatableRecordImpl<DispositionNotificationRecord> implements Record9<UUID, String, String, String, String, String, String, String, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.disposition_notification.request_id</code>.
     */
    public DispositionNotificationRecord setRequestId(UUID value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.disposition_notification.request_id</code>.
     */
    public UUID getRequestId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.disposition_notification.original_message_id</code>.
     */
    public DispositionNotificationRecord setOriginalMessageId(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.disposition_notification.original_message_id</code>.
     */
    public String getOriginalMessageId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.disposition_notification.original_recipient</code>.
     */
    public DispositionNotificationRecord setOriginalRecipient(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.disposition_notification.original_recipient</code>.
     */
    public String getOriginalRecipient() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.disposition_notification.final_recipient</code>.
     */
    public DispositionNotificationRecord setFinalRecipient(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.disposition_notification.final_recipient</code>.
     */
    public String getFinalRecipient() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.disposition_notification.reporting_ua</code>.
     */
    public DispositionNotificationRecord setReportingUa(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.disposition_notification.reporting_ua</code>.
     */
    public String getReportingUa() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.disposition_notification.disposition</code>.
     */
    public DispositionNotificationRecord setDisposition(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.disposition_notification.disposition</code>.
     */
    public String getDisposition() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.disposition_notification.received_content_mic</code>.
     */
    public DispositionNotificationRecord setReceivedContentMic(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.disposition_notification.received_content_mic</code>.
     */
    public String getReceivedContentMic() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.disposition_notification.digest_algorithm</code>.
     */
    public DispositionNotificationRecord setDigestAlgorithm(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>public.disposition_notification.digest_algorithm</code>.
     */
    public String getDigestAlgorithm() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.disposition_notification.signature_key_pair_id</code>.
     */
    public DispositionNotificationRecord setSignatureKeyPairId(Long value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>public.disposition_notification.signature_key_pair_id</code>.
     */
    public Long getSignatureKeyPairId() {
        return (Long) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<UUID, String, String, String, String, String, String, String, Long> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<UUID, String, String, String, String, String, String, String, Long> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return DispositionNotification.DISPOSITION_NOTIFICATION.REQUEST_ID;
    }

    @Override
    public Field<String> field2() {
        return DispositionNotification.DISPOSITION_NOTIFICATION.ORIGINAL_MESSAGE_ID;
    }

    @Override
    public Field<String> field3() {
        return DispositionNotification.DISPOSITION_NOTIFICATION.ORIGINAL_RECIPIENT;
    }

    @Override
    public Field<String> field4() {
        return DispositionNotification.DISPOSITION_NOTIFICATION.FINAL_RECIPIENT;
    }

    @Override
    public Field<String> field5() {
        return DispositionNotification.DISPOSITION_NOTIFICATION.REPORTING_UA;
    }

    @Override
    public Field<String> field6() {
        return DispositionNotification.DISPOSITION_NOTIFICATION.DISPOSITION;
    }

    @Override
    public Field<String> field7() {
        return DispositionNotification.DISPOSITION_NOTIFICATION.RECEIVED_CONTENT_MIC;
    }

    @Override
    public Field<String> field8() {
        return DispositionNotification.DISPOSITION_NOTIFICATION.DIGEST_ALGORITHM;
    }

    @Override
    public Field<Long> field9() {
        return DispositionNotification.DISPOSITION_NOTIFICATION.SIGNATURE_KEY_PAIR_ID;
    }

    @Override
    public UUID component1() {
        return getRequestId();
    }

    @Override
    public String component2() {
        return getOriginalMessageId();
    }

    @Override
    public String component3() {
        return getOriginalRecipient();
    }

    @Override
    public String component4() {
        return getFinalRecipient();
    }

    @Override
    public String component5() {
        return getReportingUa();
    }

    @Override
    public String component6() {
        return getDisposition();
    }

    @Override
    public String component7() {
        return getReceivedContentMic();
    }

    @Override
    public String component8() {
        return getDigestAlgorithm();
    }

    @Override
    public Long component9() {
        return getSignatureKeyPairId();
    }

    @Override
    public UUID value1() {
        return getRequestId();
    }

    @Override
    public String value2() {
        return getOriginalMessageId();
    }

    @Override
    public String value3() {
        return getOriginalRecipient();
    }

    @Override
    public String value4() {
        return getFinalRecipient();
    }

    @Override
    public String value5() {
        return getReportingUa();
    }

    @Override
    public String value6() {
        return getDisposition();
    }

    @Override
    public String value7() {
        return getReceivedContentMic();
    }

    @Override
    public String value8() {
        return getDigestAlgorithm();
    }

    @Override
    public Long value9() {
        return getSignatureKeyPairId();
    }

    @Override
    public DispositionNotificationRecord value1(UUID value) {
        setRequestId(value);
        return this;
    }

    @Override
    public DispositionNotificationRecord value2(String value) {
        setOriginalMessageId(value);
        return this;
    }

    @Override
    public DispositionNotificationRecord value3(String value) {
        setOriginalRecipient(value);
        return this;
    }

    @Override
    public DispositionNotificationRecord value4(String value) {
        setFinalRecipient(value);
        return this;
    }

    @Override
    public DispositionNotificationRecord value5(String value) {
        setReportingUa(value);
        return this;
    }

    @Override
    public DispositionNotificationRecord value6(String value) {
        setDisposition(value);
        return this;
    }

    @Override
    public DispositionNotificationRecord value7(String value) {
        setReceivedContentMic(value);
        return this;
    }

    @Override
    public DispositionNotificationRecord value8(String value) {
        setDigestAlgorithm(value);
        return this;
    }

    @Override
    public DispositionNotificationRecord value9(Long value) {
        setSignatureKeyPairId(value);
        return this;
    }

    @Override
    public DispositionNotificationRecord values(UUID value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, Long value9) {
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
     * Create a detached DispositionNotificationRecord
     */
    public DispositionNotificationRecord() {
        super(DispositionNotification.DISPOSITION_NOTIFICATION);
    }

    /**
     * Create a detached, initialised DispositionNotificationRecord
     */
    public DispositionNotificationRecord(UUID requestId, String originalMessageId, String originalRecipient, String finalRecipient, String reportingUa, String disposition, String receivedContentMic, String digestAlgorithm, Long signatureKeyPairId) {
        super(DispositionNotification.DISPOSITION_NOTIFICATION);

        setRequestId(requestId);
        setOriginalMessageId(originalMessageId);
        setOriginalRecipient(originalRecipient);
        setFinalRecipient(finalRecipient);
        setReportingUa(reportingUa);
        setDisposition(disposition);
        setReceivedContentMic(receivedContentMic);
        setDigestAlgorithm(digestAlgorithm);
        setSignatureKeyPairId(signatureKeyPairId);
    }
}
