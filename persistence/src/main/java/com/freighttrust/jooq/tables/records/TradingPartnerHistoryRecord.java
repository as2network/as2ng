/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.records;


import com.freighttrust.jooq.tables.TradingPartnerHistory;
import org.jooq.Field;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.TableRecordImpl;

import javax.annotation.processing.Generated;


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
public class TradingPartnerHistoryRecord extends TableRecordImpl<TradingPartnerHistoryRecord> implements Record4<String, String, String, Object> {

    private static final long serialVersionUID = -714751203;

    /**
     * Setter for <code>public.trading_partner_history.id</code>.
     */
    public TradingPartnerHistoryRecord setId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partner_history.id</code>.
     */
    public String getId() {
        return (String) get(0);
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
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    public TradingPartnerHistoryRecord setValidity(Object value) {
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
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, String, Object> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, String, String, Object> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
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

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Field<Object> field4() {
        return TradingPartnerHistory.TRADING_PARTNER_HISTORY.VALIDITY;
    }

    @Override
    public String component1() {
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
    public String value1() {
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
    public TradingPartnerHistoryRecord value1(String value) {
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

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public TradingPartnerHistoryRecord value4(Object value) {
        setValidity(value);
        return this;
    }

    @Override
    public TradingPartnerHistoryRecord values(String value1, String value2, String value3, Object value4) {
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
     * Create a detached TradingPartnerHistoryRecord
     */
    public TradingPartnerHistoryRecord() {
        super(TradingPartnerHistory.TRADING_PARTNER_HISTORY);
    }

    /**
     * Create a detached, initialised TradingPartnerHistoryRecord
     */
    public TradingPartnerHistoryRecord(String id, String name, String email, Object validity) {
        super(TradingPartnerHistory.TRADING_PARTNER_HISTORY);

        set(0, id);
        set(1, name);
        set(2, email);
        set(3, validity);
    }
}
