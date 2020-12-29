/*
 * This file is generated by jOOQ.
 */
package network.as2.jooq.tables.records;


import network.as2.common.util.TsTzRange;
import network.as2.jooq.tables.TradingPartnerHistory;

import org.jooq.Field;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TradingPartnerHistoryRecord extends TableRecordImpl<TradingPartnerHistoryRecord> implements Record5<Long, String, String, Long, TsTzRange> {

    private static final long serialVersionUID = 1L;

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
     * Setter for <code>public.trading_partner_history.validity</code>.
     */
    public TradingPartnerHistoryRecord setValidity(TsTzRange value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.trading_partner_history.validity</code>.
     */
    public TsTzRange getValidity() {
        return (TsTzRange) get(4);
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, String, String, Long, TsTzRange> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, String, String, Long, TsTzRange> valuesRow() {
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

    @Override
    public Field<TsTzRange> field5() {
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

    @Override
    public TsTzRange component5() {
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

    @Override
    public TsTzRange value5() {
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

    @Override
    public TradingPartnerHistoryRecord value5(TsTzRange value) {
        setValidity(value);
        return this;
    }

    @Override
    public TradingPartnerHistoryRecord values(Long value1, String value2, String value3, Long value4, TsTzRange value5) {
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
    public TradingPartnerHistoryRecord(Long id, String name, String email, Long keyPairId, TsTzRange validity) {
        super(TradingPartnerHistory.TRADING_PARTNER_HISTORY);

        setId(id);
        setName(name);
        setEmail(email);
        setKeyPairId(keyPairId);
        setValidity(validity);
    }
}