/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.pojos;


import com.freighttrust.common.util.TsTzRange;

import java.io.Serializable;

import javax.annotation.processing.Generated;


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
public class TradingChannel implements Serializable {

    private static final long serialVersionUID = -677792448;

    private Long      id;
    private String    name;
    private Long      senderId;
    private String    senderAs2Identifier;
    private Long      recipientId;
    private String    recipientAs2Identifier;
    private String    recipientMessageUrl;
    private TsTzRange validity;

    public TradingChannel() {}

    public TradingChannel(TradingChannel value) {
        this.id = value.id;
        this.name = value.name;
        this.senderId = value.senderId;
        this.senderAs2Identifier = value.senderAs2Identifier;
        this.recipientId = value.recipientId;
        this.recipientAs2Identifier = value.recipientAs2Identifier;
        this.recipientMessageUrl = value.recipientMessageUrl;
        this.validity = value.validity;
    }

    public TradingChannel(
        Long      id,
        String    name,
        Long      senderId,
        String    senderAs2Identifier,
        Long      recipientId,
        String    recipientAs2Identifier,
        String    recipientMessageUrl,
        TsTzRange validity
    ) {
        this.id = id;
        this.name = name;
        this.senderId = senderId;
        this.senderAs2Identifier = senderAs2Identifier;
        this.recipientId = recipientId;
        this.recipientAs2Identifier = recipientAs2Identifier;
        this.recipientMessageUrl = recipientMessageUrl;
        this.validity = validity;
    }

    public Long getId() {
        return this.id;
    }

    public TradingChannel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public TradingChannel setName(String name) {
        this.name = name;
        return this;
    }

    public Long getSenderId() {
        return this.senderId;
    }

    public TradingChannel setSenderId(Long senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getSenderAs2Identifier() {
        return this.senderAs2Identifier;
    }

    public TradingChannel setSenderAs2Identifier(String senderAs2Identifier) {
        this.senderAs2Identifier = senderAs2Identifier;
        return this;
    }

    public Long getRecipientId() {
        return this.recipientId;
    }

    public TradingChannel setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getRecipientAs2Identifier() {
        return this.recipientAs2Identifier;
    }

    public TradingChannel setRecipientAs2Identifier(String recipientAs2Identifier) {
        this.recipientAs2Identifier = recipientAs2Identifier;
        return this;
    }

    public String getRecipientMessageUrl() {
        return this.recipientMessageUrl;
    }

    public TradingChannel setRecipientMessageUrl(String recipientMessageUrl) {
        this.recipientMessageUrl = recipientMessageUrl;
        return this;
    }

    public TsTzRange getValidity() {
        return this.validity;
    }

    public TradingChannel setValidity(TsTzRange validity) {
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
        final TradingChannel other = (TradingChannel) obj;
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
        result = prime * result + ((this.senderId == null) ? 0 : this.senderId.hashCode());
        result = prime * result + ((this.senderAs2Identifier == null) ? 0 : this.senderAs2Identifier.hashCode());
        result = prime * result + ((this.recipientId == null) ? 0 : this.recipientId.hashCode());
        result = prime * result + ((this.recipientAs2Identifier == null) ? 0 : this.recipientAs2Identifier.hashCode());
        result = prime * result + ((this.recipientMessageUrl == null) ? 0 : this.recipientMessageUrl.hashCode());
        result = prime * result + ((this.validity == null) ? 0 : this.validity.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TradingChannel (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(senderId);
        sb.append(", ").append(senderAs2Identifier);
        sb.append(", ").append(recipientId);
        sb.append(", ").append(recipientAs2Identifier);
        sb.append(", ").append(recipientMessageUrl);
        sb.append(", ").append(validity);

        sb.append(")");
        return sb.toString();
    }
}
