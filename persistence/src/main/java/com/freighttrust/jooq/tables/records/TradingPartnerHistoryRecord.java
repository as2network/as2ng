/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.records;


import com.freighttrust.jooq.tables.TradingPartnerHistory;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.TableRecordImpl;


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
public class TradingPartnerHistoryRecord extends TableRecordImpl<TradingPartnerHistoryRecord> implements Record5<Long, String, String, Long, Object> {

    private static final long serialVersionUID = 769740206;

    /**
     * Setter for <code>public.trading_partner_history.id</code>.
     */
    public TradingPartnerHistoryRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partner_history.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.trading_partner_history.name</code>.
     */
    public TradingPartnerHistoryRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partner_history.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.trading_partner_history.email</code>.
     */
    public TradingPartnerHistoryRecord setEmail(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partner_history.email</code>.
     */
    public String getEmail() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.trading_partner_history.key_pair_id</code>.
     */
    public TradingPartnerHistoryRecord setKeyPairId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partner_history.key_pair_id</code>.
     */
    public Long getKeyPairId() {
        return (Long) get(3);
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    public TradingPartnerHistoryRecord setValidity(Object value) {
        set(4, value);
        return this;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    public Object getValidity() {
        return get(4);
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, String, String, Long, Object> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, String, String, Long, Object> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return TradingPartnerHistory.TRADING_PARTNER_HISTORY.ID;
    }

    @Override
    public Field<String> field2() {
        return TradingPartnerHistory.TRADING_PARTNER_HISTORY.NAME;
    }

    @Override
    public Field<String> field3() {
        return TradingPartnerHistory.TRADING_PARTNER_HISTORY.EMAIL;
    }

    @Override
    public Field<Long> field4() {
        return TradingPartnerHistory.TRADING_PARTNER_HISTORY.KEY_PAIR_ID;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Field<Object> field5() {
        return TradingPartnerHistory.TRADING_PARTNER_HISTORY.VALIDITY;
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

    @Override
    public Long component4() {
        return getKeyPairId();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Object component5() {
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

    @Override
    public Long value4() {
        return getKeyPairId();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Object value5() {
        return getValidity();
    }

    @Override
    public TradingPartnerHistoryRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public TradingPartnerHistoryRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public TradingPartnerHistoryRecord value3(String value) {
        setEmail(value);
        return this;
    }

    @Override
    public TradingPartnerHistoryRecord value4(Long value) {
        setKeyPairId(value);
        return this;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public TradingPartnerHistoryRecord value5(Object value) {
        setValidity(value);
        return this;
    }

    @Override
    public TradingPartnerHistoryRecord values(Long value1, String value2, String value3, Long value4, Object value5) {
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
     * Create a detached TradingPartnerHistoryRecord
     */
    public TradingPartnerHistoryRecord() {
        super(TradingPartnerHistory.TRADING_PARTNER_HISTORY);
    }

    /**
     * Create a detached, initialised TradingPartnerHistoryRecord
     */
    public TradingPartnerHistoryRecord(Long id, String name, String email, Long keyPairId, Object validity) {
        super(TradingPartnerHistory.TRADING_PARTNER_HISTORY);

        set(0, id);
        set(1, name);
        set(2, email);
        set(3, keyPairId);
        set(4, validity);
    }
}
