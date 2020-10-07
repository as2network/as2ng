/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.records;


import com.freighttrust.jooq.tables.TradingPartner;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
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
public class TradingPartnerRecord extends UpdatableRecordImpl<TradingPartnerRecord> implements Record4<Long, String, String, Object> {

    private static final long serialVersionUID = -1258127669;

    /**
     * Setter for <code>public.trading_partner.id</code>.
     */
    public TradingPartnerRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partner.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.trading_partner.name</code>.
     */
    public TradingPartnerRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partner.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.trading_partner.email</code>.
     */
    public TradingPartnerRecord setEmail(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partner.email</code>.
     */
    public String getEmail() {
        return (String) get(2);
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    public TradingPartnerRecord setValidity(Object value) {
        set(3, value);
        return this;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    public Object getValidity() {
        return get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, String, String, Object> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Long, String, String, Object> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return TradingPartner.TRADING_PARTNER.ID;
    }

    @Override
    public Field<String> field2() {
        return TradingPartner.TRADING_PARTNER.NAME;
    }

    @Override
    public Field<String> field3() {
        return TradingPartner.TRADING_PARTNER.EMAIL;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Field<Object> field4() {
        return TradingPartner.TRADING_PARTNER.VALIDITY;
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
    public String component3() {
        return getEmail();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Object component4() {
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
    public String value3() {
        return getEmail();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Object value4() {
        return getValidity();
    }

    @Override
    public TradingPartnerRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public TradingPartnerRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public TradingPartnerRecord value3(String value) {
        setEmail(value);
        return this;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public TradingPartnerRecord value4(Object value) {
        setValidity(value);
        return this;
    }

    @Override
    public TradingPartnerRecord values(Long value1, String value2, String value3, Object value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TradingPartnerRecord
     */
    public TradingPartnerRecord() {
        super(TradingPartner.TRADING_PARTNER);
    }

    /**
     * Create a detached, initialised TradingPartnerRecord
     */
    public TradingPartnerRecord(Long id, String name, String email, Object validity) {
        super(TradingPartner.TRADING_PARTNER);

        set(0, id);
        set(1, name);
        set(2, email);
        set(3, validity);
    }
}
