/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.customs.jooq.tables.records;


import com.freighttrust.customs.jooq.enums.Entity;
import com.freighttrust.customs.jooq.enums.Transport;
import com.freighttrust.customs.jooq.tables.MessageOrigin;

import java.sql.Timestamp;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
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
public class MessageOriginRecord extends UpdatableRecordImpl<MessageOriginRecord> implements Record5<String, Entity, Transport, Timestamp, Object> {

    private static final long serialVersionUID = -1921535796;

    /**
     * Setter for <code>public.message_origin.id</code>.
     */
    public MessageOriginRecord setId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.message_origin.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.message_origin.entity</code>.
     */
    public MessageOriginRecord setEntity(Entity value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.message_origin.entity</code>.
     */
    public Entity getEntity() {
        return (Entity) get(1);
    }

    /**
     * Setter for <code>public.message_origin.transport</code>.
     */
    public MessageOriginRecord setTransport(Transport value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.message_origin.transport</code>.
     */
    public Transport getTransport() {
        return (Transport) get(2);
    }

    /**
     * Setter for <code>public.message_origin.received_at</code>.
     */
    public MessageOriginRecord setReceivedAt(Timestamp value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.message_origin.received_at</code>.
     */
    public Timestamp getReceivedAt() {
        return (Timestamp) get(3);
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    public MessageOriginRecord setIpAddress(Object value) {
        set(4, value);
        return this;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    public Object getIpAddress() {
        return get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, Entity, Transport, Timestamp, Object> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<String, Entity, Transport, Timestamp, Object> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return MessageOrigin.MESSAGE_ORIGIN.ID;
    }

    @Override
    public Field<Entity> field2() {
        return MessageOrigin.MESSAGE_ORIGIN.ENTITY;
    }

    @Override
    public Field<Transport> field3() {
        return MessageOrigin.MESSAGE_ORIGIN.TRANSPORT;
    }

    @Override
    public Field<Timestamp> field4() {
        return MessageOrigin.MESSAGE_ORIGIN.RECEIVED_AT;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Field<Object> field5() {
        return MessageOrigin.MESSAGE_ORIGIN.IP_ADDRESS;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public Entity component2() {
        return getEntity();
    }

    @Override
    public Transport component3() {
        return getTransport();
    }

    @Override
    public Timestamp component4() {
        return getReceivedAt();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Object component5() {
        return getIpAddress();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public Entity value2() {
        return getEntity();
    }

    @Override
    public Transport value3() {
        return getTransport();
    }

    @Override
    public Timestamp value4() {
        return getReceivedAt();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Object value5() {
        return getIpAddress();
    }

    @Override
    public MessageOriginRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public MessageOriginRecord value2(Entity value) {
        setEntity(value);
        return this;
    }

    @Override
    public MessageOriginRecord value3(Transport value) {
        setTransport(value);
        return this;
    }

    @Override
    public MessageOriginRecord value4(Timestamp value) {
        setReceivedAt(value);
        return this;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public MessageOriginRecord value5(Object value) {
        setIpAddress(value);
        return this;
    }

    @Override
    public MessageOriginRecord values(String value1, Entity value2, Transport value3, Timestamp value4, Object value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MessageOriginRecord
     */
    public MessageOriginRecord() {
        super(MessageOrigin.MESSAGE_ORIGIN);
    }

    /**
     * Create a detached, initialised MessageOriginRecord
     */
    public MessageOriginRecord(String id, Entity entity, Transport transport, Timestamp receivedAt, Object ipAddress) {
        super(MessageOrigin.MESSAGE_ORIGIN);

        set(0, id);
        set(1, entity);
        set(2, transport);
        set(3, receivedAt);
        set(4, ipAddress);
    }
}
