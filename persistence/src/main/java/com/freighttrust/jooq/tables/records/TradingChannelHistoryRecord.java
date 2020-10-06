/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.records;


import com.freighttrust.jooq.tables.TradingChannelHistory;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.TableRecordImpl;


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
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class TradingChannelHistoryRecord extends TableRecordImpl<TradingChannelHistoryRecord> implements Record10<String, String, String, String, String, String, String, String, Boolean, Object> {

  private static final long serialVersionUID = 128096982;

  /**
   * Setter for <code>public.trading_channel_history.sender_id</code>.
   */
  public TradingChannelHistoryRecord setSenderId(String value) {
    set(0, value);
    return this;
  }

  /**
   * Getter for <code>public.trading_channel_history.sender_id</code>.
   */
  public String getSenderId() {
    return (String) get(0);
  }

  /**
   * Setter for <code>public.trading_channel_history.recipient_id</code>.
   */
  public TradingChannelHistoryRecord setRecipientId(String value) {
    set(1, value);
    return this;
  }

  /**
   * Getter for <code>public.trading_channel_history.recipient_id</code>.
   */
  public String getRecipientId() {
    return (String) get(1);
  }

  /**
   * Setter for <code>public.trading_channel_history.protocol</code>.
   */
  public TradingChannelHistoryRecord setProtocol(String value) {
    set(2, value);
    return this;
  }

  /**
   * Getter for <code>public.trading_channel_history.protocol</code>.
   */
  public String getProtocol() {
    return (String) get(2);
  }

  /**
   * Setter for <code>public.trading_channel_history.as2_url</code>.
   */
  public TradingChannelHistoryRecord setAs2Url(String value) {
    set(3, value);
    return this;
  }

  /**
   * Getter for <code>public.trading_channel_history.as2_url</code>.
   */
  public String getAs2Url() {
    return (String) get(3);
  }

  /**
   * Setter for <code>public.trading_channel_history.as2_mdn_to</code>.
   */
  public TradingChannelHistoryRecord setAs2MdnTo(String value) {
    set(4, value);
    return this;
  }

  /**
   * Getter for <code>public.trading_channel_history.as2_mdn_to</code>.
   */
  public String getAs2MdnTo() {
    return (String) get(4);
  }

  /**
   * Setter for <code>public.trading_channel_history.as2_mdn_options</code>.
   */
  public TradingChannelHistoryRecord setAs2MdnOptions(String value) {
    set(5, value);
    return this;
  }

  /**
   * Getter for <code>public.trading_channel_history.as2_mdn_options</code>.
   */
  public String getAs2MdnOptions() {
    return (String) get(5);
  }

  /**
   * Setter for <code>public.trading_channel_history.encryption_algorithm</code>.
   */
  public TradingChannelHistoryRecord setEncryptionAlgorithm(String value) {
    set(6, value);
    return this;
  }

  /**
   * Getter for <code>public.trading_channel_history.encryption_algorithm</code>.
   */
  public String getEncryptionAlgorithm() {
    return (String) get(6);
  }

  /**
   * Setter for <code>public.trading_channel_history.signing_algorithm</code>.
   */
  public TradingChannelHistoryRecord setSigningAlgorithm(String value) {
    set(7, value);
    return this;
  }

  /**
   * Getter for <code>public.trading_channel_history.signing_algorithm</code>.
   */
  public String getSigningAlgorithm() {
    return (String) get(7);
  }

  /**
   * Setter for <code>public.trading_channel_history.rfc_3851_mic_algorithms_enabled</code>.
   */
  public TradingChannelHistoryRecord setRfc_3851MicAlgorithmsEnabled(Boolean value) {
    set(8, value);
    return this;
  }

  /**
   * Getter for <code>public.trading_channel_history.rfc_3851_mic_algorithms_enabled</code>.
   */
  public Boolean getRfc_3851MicAlgorithmsEnabled() {
    return (Boolean) get(8);
  }

  /**
   * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
   */
  @java.lang.Deprecated
  public TradingChannelHistoryRecord setValidity(Object value) {
    set(9, value);
    return this;
  }

  /**
   * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
   */
  @java.lang.Deprecated
  public Object getValidity() {
    return get(9);
  }

  // -------------------------------------------------------------------------
  // Record10 type implementation
  // -------------------------------------------------------------------------

  @Override
  public Row10<String, String, String, String, String, String, String, String, Boolean, Object> fieldsRow() {
    return (Row10) super.fieldsRow();
  }

  @Override
  public Row10<String, String, String, String, String, String, String, String, Boolean, Object> valuesRow() {
    return (Row10) super.valuesRow();
  }

  @Override
  public Field<String> field1() {
    return TradingChannelHistory.TRADING_CHANNEL_HISTORY.SENDER_ID;
  }

  @Override
  public Field<String> field2() {
    return TradingChannelHistory.TRADING_CHANNEL_HISTORY.RECIPIENT_ID;
  }

  @Override
  public Field<String> field3() {
    return TradingChannelHistory.TRADING_CHANNEL_HISTORY.PROTOCOL;
  }

  @Override
  public Field<String> field4() {
    return TradingChannelHistory.TRADING_CHANNEL_HISTORY.AS2_URL;
  }

  @Override
  public Field<String> field5() {
    return TradingChannelHistory.TRADING_CHANNEL_HISTORY.AS2_MDN_TO;
  }

  @Override
  public Field<String> field6() {
    return TradingChannelHistory.TRADING_CHANNEL_HISTORY.AS2_MDN_OPTIONS;
  }

  @Override
  public Field<String> field7() {
    return TradingChannelHistory.TRADING_CHANNEL_HISTORY.ENCRYPTION_ALGORITHM;
  }

  @Override
  public Field<String> field8() {
    return TradingChannelHistory.TRADING_CHANNEL_HISTORY.SIGNING_ALGORITHM;
  }

  @Override
  public Field<Boolean> field9() {
    return TradingChannelHistory.TRADING_CHANNEL_HISTORY.RFC_3851_MIC_ALGORITHMS_ENABLED;
  }

  /**
   * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
   */
  @java.lang.Deprecated
  @Override
  public Field<Object> field10() {
    return TradingChannelHistory.TRADING_CHANNEL_HISTORY.VALIDITY;
  }

  @Override
  public String component1() {
    return getSenderId();
  }

  @Override
  public String component2() {
    return getRecipientId();
  }

  @Override
  public String component3() {
    return getProtocol();
  }

  @Override
  public String component4() {
    return getAs2Url();
  }

  @Override
  public String component5() {
    return getAs2MdnTo();
  }

  @Override
  public String component6() {
    return getAs2MdnOptions();
  }

  @Override
  public String component7() {
    return getEncryptionAlgorithm();
  }

  @Override
  public String component8() {
    return getSigningAlgorithm();
  }

  @Override
  public Boolean component9() {
    return getRfc_3851MicAlgorithmsEnabled();
  }

  /**
   * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
   */
  @java.lang.Deprecated
  @Override
  public Object component10() {
    return getValidity();
  }

  @Override
  public String value1() {
    return getSenderId();
  }

  @Override
  public String value2() {
    return getRecipientId();
  }

  @Override
  public String value3() {
    return getProtocol();
  }

  @Override
  public String value4() {
    return getAs2Url();
  }

  @Override
  public String value5() {
    return getAs2MdnTo();
  }

  @Override
  public String value6() {
    return getAs2MdnOptions();
  }

  @Override
  public String value7() {
    return getEncryptionAlgorithm();
  }

  @Override
  public String value8() {
    return getSigningAlgorithm();
  }

  @Override
  public Boolean value9() {
    return getRfc_3851MicAlgorithmsEnabled();
  }

  /**
   * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
   */
  @java.lang.Deprecated
  @Override
  public Object value10() {
    return getValidity();
  }

  @Override
  public TradingChannelHistoryRecord value1(String value) {
    setSenderId(value);
    return this;
  }

  @Override
  public TradingChannelHistoryRecord value2(String value) {
    setRecipientId(value);
    return this;
  }

  @Override
  public TradingChannelHistoryRecord value3(String value) {
    setProtocol(value);
    return this;
  }

  @Override
  public TradingChannelHistoryRecord value4(String value) {
    setAs2Url(value);
    return this;
  }

  @Override
  public TradingChannelHistoryRecord value5(String value) {
    setAs2MdnTo(value);
    return this;
  }

  @Override
  public TradingChannelHistoryRecord value6(String value) {
    setAs2MdnOptions(value);
    return this;
  }

  @Override
  public TradingChannelHistoryRecord value7(String value) {
    setEncryptionAlgorithm(value);
    return this;
  }

  @Override
  public TradingChannelHistoryRecord value8(String value) {
    setSigningAlgorithm(value);
    return this;
  }

  @Override
  public TradingChannelHistoryRecord value9(Boolean value) {
    setRfc_3851MicAlgorithmsEnabled(value);
    return this;
  }

  /**
   * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
   */
  @java.lang.Deprecated
  @Override
  public TradingChannelHistoryRecord value10(Object value) {
    setValidity(value);
    return this;
  }

  @Override
  public TradingChannelHistoryRecord values(String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, Boolean value9, Object value10) {
    value1(value1);
    value2(value2);
    value3(value3);
    value4(value4);
    value5(value5);
    value6(value6);
    value7(value7);
    value8(value8);
    value9(value9);
    value10(value10);
    return this;
  }

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Create a detached TradingChannelHistoryRecord
   */
  public TradingChannelHistoryRecord() {
    super(TradingChannelHistory.TRADING_CHANNEL_HISTORY);
  }

  /**
   * Create a detached, initialised TradingChannelHistoryRecord
   */
  public TradingChannelHistoryRecord(String senderId, String recipientId, String protocol, String as2Url, String as2MdnTo, String as2MdnOptions, String encryptionAlgorithm, String signingAlgorithm, Boolean rfc_3851MicAlgorithmsEnabled, Object validity) {
    super(TradingChannelHistory.TRADING_CHANNEL_HISTORY);

    set(0, senderId);
    set(1, recipientId);
    set(2, protocol);
    set(3, as2Url);
    set(4, as2MdnTo);
    set(5, as2MdnOptions);
    set(6, encryptionAlgorithm);
    set(7, signingAlgorithm);
    set(8, rfc_3851MicAlgorithmsEnabled);
    set(9, validity);
  }
}
