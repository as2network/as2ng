/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.pojos;


import com.freighttrust.common.util.TsTzRange;
import com.freighttrust.jooq.enums.TradingChannelType;

import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TradingChannelHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long               id;
    private String             name;
    private TradingChannelType type;
    private Long               senderId;
    private String             senderAs2Identifier;
    private Long               senderKeyPairId;
    private Long               recipientId;
    private String             recipientAs2Identifier;
    private Long               recipientKeyPairId;
    private Boolean            allowBodyCertificateForVerification;
    private String             recipientMessageUrl;
    private TsTzRange          validity;

    public TradingChannelHistory() {}

    public TradingChannelHistory(TradingChannelHistory value) {
        this.id = value.id;
        this.name = value.name;
        this.type = value.type;
        this.senderId = value.senderId;
        this.senderAs2Identifier = value.senderAs2Identifier;
        this.senderKeyPairId = value.senderKeyPairId;
        this.recipientId = value.recipientId;
        this.recipientAs2Identifier = value.recipientAs2Identifier;
        this.recipientKeyPairId = value.recipientKeyPairId;
        this.allowBodyCertificateForVerification = value.allowBodyCertificateForVerification;
        this.recipientMessageUrl = value.recipientMessageUrl;
        this.validity = value.validity;
    }

    public TradingChannelHistory(
        Long               id,
        String             name,
        TradingChannelType type,
        Long               senderId,
        String             senderAs2Identifier,
        Long               senderKeyPairId,
        Long               recipientId,
        String             recipientAs2Identifier,
        Long               recipientKeyPairId,
        Boolean            allowBodyCertificateForVerification,
        String             recipientMessageUrl,
        TsTzRange          validity
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.senderId = senderId;
        this.senderAs2Identifier = senderAs2Identifier;
        this.senderKeyPairId = senderKeyPairId;
        this.recipientId = recipientId;
        this.recipientAs2Identifier = recipientAs2Identifier;
        this.recipientKeyPairId = recipientKeyPairId;
        this.allowBodyCertificateForVerification = allowBodyCertificateForVerification;
        this.recipientMessageUrl = recipientMessageUrl;
        this.validity = validity;
    }

    /**
     * Getter for <code>public.trading_channel_history.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>public.trading_channel_history.id</code>.
     */
    public TradingChannelHistory setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Getter for <code>public.trading_channel_history.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>public.trading_channel_history.name</code>.
     */
    public TradingChannelHistory setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>public.trading_channel_history.type</code>.
     */
    public TradingChannelType getType() {
        return this.type;
    }

    /**
     * Setter for <code>public.trading_channel_history.type</code>.
     */
    public TradingChannelHistory setType(TradingChannelType type) {
        this.type = type;
        return this;
    }

    /**
     * Getter for <code>public.trading_channel_history.sender_id</code>.
     */
    public Long getSenderId() {
        return this.senderId;
    }

    /**
     * Setter for <code>public.trading_channel_history.sender_id</code>.
     */
    public TradingChannelHistory setSenderId(Long senderId) {
        this.senderId = senderId;
        return this;
    }

    /**
     * Getter for <code>public.trading_channel_history.sender_as2_identifier</code>.
     */
    public String getSenderAs2Identifier() {
        return this.senderAs2Identifier;
    }

    /**
     * Setter for <code>public.trading_channel_history.sender_as2_identifier</code>.
     */
    public TradingChannelHistory setSenderAs2Identifier(String senderAs2Identifier) {
        this.senderAs2Identifier = senderAs2Identifier;
        return this;
    }

    /**
     * Getter for <code>public.trading_channel_history.sender_key_pair_id</code>.
     */
    public Long getSenderKeyPairId() {
        return this.senderKeyPairId;
    }

    /**
     * Setter for <code>public.trading_channel_history.sender_key_pair_id</code>.
     */
    public TradingChannelHistory setSenderKeyPairId(Long senderKeyPairId) {
        this.senderKeyPairId = senderKeyPairId;
        return this;
    }

    /**
     * Getter for <code>public.trading_channel_history.recipient_id</code>.
     */
    public Long getRecipientId() {
        return this.recipientId;
    }

    /**
     * Setter for <code>public.trading_channel_history.recipient_id</code>.
     */
    public TradingChannelHistory setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    /**
     * Getter for <code>public.trading_channel_history.recipient_as2_identifier</code>.
     */
    public String getRecipientAs2Identifier() {
        return this.recipientAs2Identifier;
    }

    /**
     * Setter for <code>public.trading_channel_history.recipient_as2_identifier</code>.
     */
    public TradingChannelHistory setRecipientAs2Identifier(String recipientAs2Identifier) {
        this.recipientAs2Identifier = recipientAs2Identifier;
        return this;
    }

    /**
     * Getter for <code>public.trading_channel_history.recipient_key_pair_id</code>.
     */
    public Long getRecipientKeyPairId() {
        return this.recipientKeyPairId;
    }

    /**
     * Setter for <code>public.trading_channel_history.recipient_key_pair_id</code>.
     */
    public TradingChannelHistory setRecipientKeyPairId(Long recipientKeyPairId) {
        this.recipientKeyPairId = recipientKeyPairId;
        return this;
    }

    /**
     * Getter for <code>public.trading_channel_history.allow_body_certificate_for_verification</code>.
     */
    public Boolean getAllowBodyCertificateForVerification() {
        return this.allowBodyCertificateForVerification;
    }

    /**
     * Setter for <code>public.trading_channel_history.allow_body_certificate_for_verification</code>.
     */
    public TradingChannelHistory setAllowBodyCertificateForVerification(Boolean allowBodyCertificateForVerification) {
        this.allowBodyCertificateForVerification = allowBodyCertificateForVerification;
        return this;
    }

    /**
     * Getter for <code>public.trading_channel_history.recipient_message_url</code>.
     */
    public String getRecipientMessageUrl() {
        return this.recipientMessageUrl;
    }

    /**
     * Setter for <code>public.trading_channel_history.recipient_message_url</code>.
     */
    public TradingChannelHistory setRecipientMessageUrl(String recipientMessageUrl) {
        this.recipientMessageUrl = recipientMessageUrl;
        return this;
    }

    /**
     * Getter for <code>public.trading_channel_history.validity</code>.
     */
    public TsTzRange getValidity() {
        return this.validity;
    }

    /**
     * Setter for <code>public.trading_channel_history.validity</code>.
     */
    public TradingChannelHistory setValidity(TsTzRange validity) {
        this.validity = validity;
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
        final TradingChannelHistory other = (TradingChannelHistory) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        }
        else if (!type.equals(other.type))
            return false;
        if (senderId == null) {
            if (other.senderId != null)
                return false;
        }
        else if (!senderId.equals(other.senderId))
            return false;
        if (senderAs2Identifier == null) {
            if (other.senderAs2Identifier != null)
                return false;
        }
        else if (!senderAs2Identifier.equals(other.senderAs2Identifier))
            return false;
        if (senderKeyPairId == null) {
            if (other.senderKeyPairId != null)
                return false;
        }
        else if (!senderKeyPairId.equals(other.senderKeyPairId))
            return false;
        if (recipientId == null) {
            if (other.recipientId != null)
                return false;
        }
        else if (!recipientId.equals(other.recipientId))
            return false;
        if (recipientAs2Identifier == null) {
            if (other.recipientAs2Identifier != null)
                return false;
        }
        else if (!recipientAs2Identifier.equals(other.recipientAs2Identifier))
            return false;
        if (recipientKeyPairId == null) {
            if (other.recipientKeyPairId != null)
                return false;
        }
        else if (!recipientKeyPairId.equals(other.recipientKeyPairId))
            return false;
        if (allowBodyCertificateForVerification == null) {
            if (other.allowBodyCertificateForVerification != null)
                return false;
        }
        else if (!allowBodyCertificateForVerification.equals(other.allowBodyCertificateForVerification))
            return false;
        if (recipientMessageUrl == null) {
            if (other.recipientMessageUrl != null)
                return false;
        }
        else if (!recipientMessageUrl.equals(other.recipientMessageUrl))
            return false;
        if (validity == null) {
            if (other.validity != null)
                return false;
        }
        else if (!validity.equals(other.validity))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result + ((this.senderId == null) ? 0 : this.senderId.hashCode());
        result = prime * result + ((this.senderAs2Identifier == null) ? 0 : this.senderAs2Identifier.hashCode());
        result = prime * result + ((this.senderKeyPairId == null) ? 0 : this.senderKeyPairId.hashCode());
        result = prime * result + ((this.recipientId == null) ? 0 : this.recipientId.hashCode());
        result = prime * result + ((this.recipientAs2Identifier == null) ? 0 : this.recipientAs2Identifier.hashCode());
        result = prime * result + ((this.recipientKeyPairId == null) ? 0 : this.recipientKeyPairId.hashCode());
        result = prime * result + ((this.allowBodyCertificateForVerification == null) ? 0 : this.allowBodyCertificateForVerification.hashCode());
        result = prime * result + ((this.recipientMessageUrl == null) ? 0 : this.recipientMessageUrl.hashCode());
        result = prime * result + ((this.validity == null) ? 0 : this.validity.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TradingChannelHistory (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(type);
        sb.append(", ").append(senderId);
        sb.append(", ").append(senderAs2Identifier);
        sb.append(", ").append(senderKeyPairId);
        sb.append(", ").append(recipientId);
        sb.append(", ").append(recipientAs2Identifier);
        sb.append(", ").append(recipientKeyPairId);
        sb.append(", ").append(allowBodyCertificateForVerification);
        sb.append(", ").append(recipientMessageUrl);
        sb.append(", ").append(validity);

        sb.append(")");
        return sb.toString();
    }
}
