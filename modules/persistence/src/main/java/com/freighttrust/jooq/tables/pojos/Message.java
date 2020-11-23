/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.pojos;


import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID     requestId;
    private String   encryptionAlgorithm;
    private Long     encryptionKeyPairId;
    private Long     signatureKeyPairId;
    private String   compressionAlgorithm;
    private String[] mics;
    private Boolean  isMdnRequested;
    private Boolean  isMdnAsync;
    private String   receiptDeliveryOption;

    public Message() {}

    public Message(Message value) {
        this.requestId = value.requestId;
        this.encryptionAlgorithm = value.encryptionAlgorithm;
        this.encryptionKeyPairId = value.encryptionKeyPairId;
        this.signatureKeyPairId = value.signatureKeyPairId;
        this.compressionAlgorithm = value.compressionAlgorithm;
        this.mics = value.mics;
        this.isMdnRequested = value.isMdnRequested;
        this.isMdnAsync = value.isMdnAsync;
        this.receiptDeliveryOption = value.receiptDeliveryOption;
    }

    public Message(
        UUID     requestId,
        String   encryptionAlgorithm,
        Long     encryptionKeyPairId,
        Long     signatureKeyPairId,
        String   compressionAlgorithm,
        String[] mics,
        Boolean  isMdnRequested,
        Boolean  isMdnAsync,
        String   receiptDeliveryOption
    ) {
        this.requestId = requestId;
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.encryptionKeyPairId = encryptionKeyPairId;
        this.signatureKeyPairId = signatureKeyPairId;
        this.compressionAlgorithm = compressionAlgorithm;
        this.mics = mics;
        this.isMdnRequested = isMdnRequested;
        this.isMdnAsync = isMdnAsync;
        this.receiptDeliveryOption = receiptDeliveryOption;
    }

    /**
     * Getter for <code>public.message.request_id</code>.
     */
    public UUID getRequestId() {
        return this.requestId;
    }

    /**
     * Setter for <code>public.message.request_id</code>.
     */
    public Message setRequestId(UUID requestId) {
        this.requestId = requestId;
        return this;
    }

    /**
     * Getter for <code>public.message.encryption_algorithm</code>.
     */
    public String getEncryptionAlgorithm() {
        return this.encryptionAlgorithm;
    }

    /**
     * Setter for <code>public.message.encryption_algorithm</code>.
     */
    public Message setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
        return this;
    }

    /**
     * Getter for <code>public.message.encryption_key_pair_id</code>.
     */
    public Long getEncryptionKeyPairId() {
        return this.encryptionKeyPairId;
    }

    /**
     * Setter for <code>public.message.encryption_key_pair_id</code>.
     */
    public Message setEncryptionKeyPairId(Long encryptionKeyPairId) {
        this.encryptionKeyPairId = encryptionKeyPairId;
        return this;
    }

    /**
     * Getter for <code>public.message.signature_key_pair_id</code>.
     */
    public Long getSignatureKeyPairId() {
        return this.signatureKeyPairId;
    }

    /**
     * Setter for <code>public.message.signature_key_pair_id</code>.
     */
    public Message setSignatureKeyPairId(Long signatureKeyPairId) {
        this.signatureKeyPairId = signatureKeyPairId;
        return this;
    }

    /**
     * Getter for <code>public.message.compression_algorithm</code>.
     */
    public String getCompressionAlgorithm() {
        return this.compressionAlgorithm;
    }

    /**
     * Setter for <code>public.message.compression_algorithm</code>.
     */
    public Message setCompressionAlgorithm(String compressionAlgorithm) {
        this.compressionAlgorithm = compressionAlgorithm;
        return this;
    }

    /**
     * Getter for <code>public.message.mics</code>.
     */
    public String[] getMics() {
        return this.mics;
    }

    /**
     * Setter for <code>public.message.mics</code>.
     */
    public Message setMics(String[] mics) {
        this.mics = mics;
        return this;
    }

    /**
     * Getter for <code>public.message.is_mdn_requested</code>.
     */
    public Boolean getIsMdnRequested() {
        return this.isMdnRequested;
    }

    /**
     * Setter for <code>public.message.is_mdn_requested</code>.
     */
    public Message setIsMdnRequested(Boolean isMdnRequested) {
        this.isMdnRequested = isMdnRequested;
        return this;
    }

    /**
     * Getter for <code>public.message.is_mdn_async</code>.
     */
    public Boolean getIsMdnAsync() {
        return this.isMdnAsync;
    }

    /**
     * Setter for <code>public.message.is_mdn_async</code>.
     */
    public Message setIsMdnAsync(Boolean isMdnAsync) {
        this.isMdnAsync = isMdnAsync;
        return this;
    }

    /**
     * Getter for <code>public.message.receipt_delivery_option</code>.
     */
    public String getReceiptDeliveryOption() {
        return this.receiptDeliveryOption;
    }

    /**
     * Setter for <code>public.message.receipt_delivery_option</code>.
     */
    public Message setReceiptDeliveryOption(String receiptDeliveryOption) {
        this.receiptDeliveryOption = receiptDeliveryOption;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Message other = (Message) obj;
        if (requestId == null) {
            if (other.requestId != null)
                return false;
        }
        else if (!requestId.equals(other.requestId))
            return false;
        if (encryptionAlgorithm == null) {
            if (other.encryptionAlgorithm != null)
                return false;
        }
        else if (!encryptionAlgorithm.equals(other.encryptionAlgorithm))
            return false;
        if (encryptionKeyPairId == null) {
            if (other.encryptionKeyPairId != null)
                return false;
        }
        else if (!encryptionKeyPairId.equals(other.encryptionKeyPairId))
            return false;
        if (signatureKeyPairId == null) {
            if (other.signatureKeyPairId != null)
                return false;
        }
        else if (!signatureKeyPairId.equals(other.signatureKeyPairId))
            return false;
        if (compressionAlgorithm == null) {
            if (other.compressionAlgorithm != null)
                return false;
        }
        else if (!compressionAlgorithm.equals(other.compressionAlgorithm))
            return false;
        if (mics == null) {
            if (other.mics != null)
                return false;
        }
        else if (!Arrays.equals(mics, other.mics))
            return false;
        if (isMdnRequested == null) {
            if (other.isMdnRequested != null)
                return false;
        }
        else if (!isMdnRequested.equals(other.isMdnRequested))
            return false;
        if (isMdnAsync == null) {
            if (other.isMdnAsync != null)
                return false;
        }
        else if (!isMdnAsync.equals(other.isMdnAsync))
            return false;
        if (receiptDeliveryOption == null) {
            if (other.receiptDeliveryOption != null)
                return false;
        }
        else if (!receiptDeliveryOption.equals(other.receiptDeliveryOption))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.requestId == null) ? 0 : this.requestId.hashCode());
        result = prime * result + ((this.encryptionAlgorithm == null) ? 0 : this.encryptionAlgorithm.hashCode());
        result = prime * result + ((this.encryptionKeyPairId == null) ? 0 : this.encryptionKeyPairId.hashCode());
        result = prime * result + ((this.signatureKeyPairId == null) ? 0 : this.signatureKeyPairId.hashCode());
        result = prime * result + ((this.compressionAlgorithm == null) ? 0 : this.compressionAlgorithm.hashCode());
        result = prime * result + ((this.mics == null) ? 0 : Arrays.hashCode(this.mics));
        result = prime * result + ((this.isMdnRequested == null) ? 0 : this.isMdnRequested.hashCode());
        result = prime * result + ((this.isMdnAsync == null) ? 0 : this.isMdnAsync.hashCode());
        result = prime * result + ((this.receiptDeliveryOption == null) ? 0 : this.receiptDeliveryOption.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Message (");

        sb.append(requestId);
        sb.append(", ").append(encryptionAlgorithm);
        sb.append(", ").append(encryptionKeyPairId);
        sb.append(", ").append(signatureKeyPairId);
        sb.append(", ").append(compressionAlgorithm);
        sb.append(", ").append(Arrays.toString(mics));
        sb.append(", ").append(isMdnRequested);
        sb.append(", ").append(isMdnAsync);
        sb.append(", ").append(receiptDeliveryOption);

        sb.append(")");
        return sb.toString();
    }
}
