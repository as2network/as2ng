/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.pojos;


import com.freighttrust.jooq.enums.RequestType;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.jooq.JSONB;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID           id;
    private RequestType    type;
    private JSONB          headers;
    private Long           bodyFileId;
    private String         messageId;
    private String         subject;
    private Long           tradingChannelId;
    private UUID           originalRequestId;
    private OffsetDateTime receivedAt;
    private OffsetDateTime deliveredAt;
    private String         deliveredTo;
    private String         errorMessage;
    private String         errorStackTrace;

    public Request() {}

    public Request(Request value) {
        this.id = value.id;
        this.type = value.type;
        this.headers = value.headers;
        this.bodyFileId = value.bodyFileId;
        this.messageId = value.messageId;
        this.subject = value.subject;
        this.tradingChannelId = value.tradingChannelId;
        this.originalRequestId = value.originalRequestId;
        this.receivedAt = value.receivedAt;
        this.deliveredAt = value.deliveredAt;
        this.deliveredTo = value.deliveredTo;
        this.errorMessage = value.errorMessage;
        this.errorStackTrace = value.errorStackTrace;
    }

    public Request(
        UUID           id,
        RequestType    type,
        JSONB          headers,
        Long           bodyFileId,
        String         messageId,
        String         subject,
        Long           tradingChannelId,
        UUID           originalRequestId,
        OffsetDateTime receivedAt,
        OffsetDateTime deliveredAt,
        String         deliveredTo,
        String         errorMessage,
        String         errorStackTrace
    ) {
        this.id = id;
        this.type = type;
        this.headers = headers;
        this.bodyFileId = bodyFileId;
        this.messageId = messageId;
        this.subject = subject;
        this.tradingChannelId = tradingChannelId;
        this.originalRequestId = originalRequestId;
        this.receivedAt = receivedAt;
        this.deliveredAt = deliveredAt;
        this.deliveredTo = deliveredTo;
        this.errorMessage = errorMessage;
        this.errorStackTrace = errorStackTrace;
    }

    /**
     * Getter for <code>public.request.id</code>.
     */
    public UUID getId() {
        return this.id;
    }

    /**
     * Setter for <code>public.request.id</code>.
     */
    public Request setId(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Getter for <code>public.request.type</code>.
     */
    public RequestType getType() {
        return this.type;
    }

    /**
     * Setter for <code>public.request.type</code>.
     */
    public Request setType(RequestType type) {
        this.type = type;
        return this;
    }

    /**
     * Getter for <code>public.request.headers</code>.
     */
    public JSONB getHeaders() {
        return this.headers;
    }

    /**
     * Setter for <code>public.request.headers</code>.
     */
    public Request setHeaders(JSONB headers) {
        this.headers = headers;
        return this;
    }

    /**
     * Getter for <code>public.request.body_file_id</code>.
     */
    public Long getBodyFileId() {
        return this.bodyFileId;
    }

    /**
     * Setter for <code>public.request.body_file_id</code>.
     */
    public Request setBodyFileId(Long bodyFileId) {
        this.bodyFileId = bodyFileId;
        return this;
    }

    /**
     * Getter for <code>public.request.message_id</code>.
     */
    public String getMessageId() {
        return this.messageId;
    }

    /**
     * Setter for <code>public.request.message_id</code>.
     */
    public Request setMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    /**
     * Getter for <code>public.request.subject</code>.
     */
    public String getSubject() {
        return this.subject;
    }

    /**
     * Setter for <code>public.request.subject</code>.
     */
    public Request setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Getter for <code>public.request.trading_channel_id</code>.
     */
    public Long getTradingChannelId() {
        return this.tradingChannelId;
    }

    /**
     * Setter for <code>public.request.trading_channel_id</code>.
     */
    public Request setTradingChannelId(Long tradingChannelId) {
        this.tradingChannelId = tradingChannelId;
        return this;
    }

    /**
     * Getter for <code>public.request.original_request_id</code>.
     */
    public UUID getOriginalRequestId() {
        return this.originalRequestId;
    }

    /**
     * Setter for <code>public.request.original_request_id</code>.
     */
    public Request setOriginalRequestId(UUID originalRequestId) {
        this.originalRequestId = originalRequestId;
        return this;
    }

    /**
     * Getter for <code>public.request.received_at</code>.
     */
    public OffsetDateTime getReceivedAt() {
        return this.receivedAt;
    }

    /**
     * Setter for <code>public.request.received_at</code>.
     */
    public Request setReceivedAt(OffsetDateTime receivedAt) {
        this.receivedAt = receivedAt;
        return this;
    }

    /**
     * Getter for <code>public.request.delivered_at</code>.
     */
    public OffsetDateTime getDeliveredAt() {
        return this.deliveredAt;
    }

    /**
     * Setter for <code>public.request.delivered_at</code>.
     */
    public Request setDeliveredAt(OffsetDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
        return this;
    }

    /**
     * Getter for <code>public.request.delivered_to</code>.
     */
    public String getDeliveredTo() {
        return this.deliveredTo;
    }

    /**
     * Setter for <code>public.request.delivered_to</code>.
     */
    public Request setDeliveredTo(String deliveredTo) {
        this.deliveredTo = deliveredTo;
        return this;
    }

    /**
     * Getter for <code>public.request.error_message</code>.
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Setter for <code>public.request.error_message</code>.
     */
    public Request setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * Getter for <code>public.request.error_stack_trace</code>.
     */
    public String getErrorStackTrace() {
        return this.errorStackTrace;
    }

    /**
     * Setter for <code>public.request.error_stack_trace</code>.
     */
    public Request setErrorStackTrace(String errorStackTrace) {
        this.errorStackTrace = errorStackTrace;
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
        final Request other = (Request) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        }
        else if (!type.equals(other.type))
            return false;
        if (headers == null) {
            if (other.headers != null)
                return false;
        }
        else if (!headers.equals(other.headers))
            return false;
        if (bodyFileId == null) {
            if (other.bodyFileId != null)
                return false;
        }
        else if (!bodyFileId.equals(other.bodyFileId))
            return false;
        if (messageId == null) {
            if (other.messageId != null)
                return false;
        }
        else if (!messageId.equals(other.messageId))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        }
        else if (!subject.equals(other.subject))
            return false;
        if (tradingChannelId == null) {
            if (other.tradingChannelId != null)
                return false;
        }
        else if (!tradingChannelId.equals(other.tradingChannelId))
            return false;
        if (originalRequestId == null) {
            if (other.originalRequestId != null)
                return false;
        }
        else if (!originalRequestId.equals(other.originalRequestId))
            return false;
        if (receivedAt == null) {
            if (other.receivedAt != null)
                return false;
        }
        else if (!receivedAt.equals(other.receivedAt))
            return false;
        if (deliveredAt == null) {
            if (other.deliveredAt != null)
                return false;
        }
        else if (!deliveredAt.equals(other.deliveredAt))
            return false;
        if (deliveredTo == null) {
            if (other.deliveredTo != null)
                return false;
        }
        else if (!deliveredTo.equals(other.deliveredTo))
            return false;
        if (errorMessage == null) {
            if (other.errorMessage != null)
                return false;
        }
        else if (!errorMessage.equals(other.errorMessage))
            return false;
        if (errorStackTrace == null) {
            if (other.errorStackTrace != null)
                return false;
        }
        else if (!errorStackTrace.equals(other.errorStackTrace))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result + ((this.headers == null) ? 0 : this.headers.hashCode());
        result = prime * result + ((this.bodyFileId == null) ? 0 : this.bodyFileId.hashCode());
        result = prime * result + ((this.messageId == null) ? 0 : this.messageId.hashCode());
        result = prime * result + ((this.subject == null) ? 0 : this.subject.hashCode());
        result = prime * result + ((this.tradingChannelId == null) ? 0 : this.tradingChannelId.hashCode());
        result = prime * result + ((this.originalRequestId == null) ? 0 : this.originalRequestId.hashCode());
        result = prime * result + ((this.receivedAt == null) ? 0 : this.receivedAt.hashCode());
        result = prime * result + ((this.deliveredAt == null) ? 0 : this.deliveredAt.hashCode());
        result = prime * result + ((this.deliveredTo == null) ? 0 : this.deliveredTo.hashCode());
        result = prime * result + ((this.errorMessage == null) ? 0 : this.errorMessage.hashCode());
        result = prime * result + ((this.errorStackTrace == null) ? 0 : this.errorStackTrace.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Request (");

        sb.append(id);
        sb.append(", ").append(type);
        sb.append(", ").append(headers);
        sb.append(", ").append(bodyFileId);
        sb.append(", ").append(messageId);
        sb.append(", ").append(subject);
        sb.append(", ").append(tradingChannelId);
        sb.append(", ").append(originalRequestId);
        sb.append(", ").append(receivedAt);
        sb.append(", ").append(deliveredAt);
        sb.append(", ").append(deliveredTo);
        sb.append(", ").append(errorMessage);
        sb.append(", ").append(errorStackTrace);

        sb.append(")");
        return sb.toString();
    }
}
