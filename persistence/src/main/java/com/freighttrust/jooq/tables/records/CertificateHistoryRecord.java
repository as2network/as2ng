/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.records;


import com.freighttrust.jooq.tables.CertificateHistory;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record4;
import org.jooq.Row4;
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
public class CertificateHistoryRecord extends TableRecordImpl<CertificateHistoryRecord> implements Record4<String, String, String, Object> {

  private static final long serialVersionUID = -426904059;

  /**
   * Setter for <code>public.certificate_history.trading_partner_id</code>.
   */
  public CertificateHistoryRecord setTradingPartnerId(String value) {
    set(0, value);
    return this;
  }

  /**
   * Getter for <code>public.certificate_history.trading_partner_id</code>.
   */
  public String getTradingPartnerId() {
    return (String) get(0);
  }

  /**
   * Setter for <code>public.certificate_history.private_key</code>.
   */
  public CertificateHistoryRecord setPrivateKey(String value) {
    set(1, value);
    return this;
  }

  /**
   * Getter for <code>public.certificate_history.private_key</code>.
   */
  public String getPrivateKey() {
    return (String) get(1);
  }

  /**
   * Setter for <code>public.certificate_history.x509_certificate</code>.
   */
  public CertificateHistoryRecord setX509Certificate(String value) {
    set(2, value);
    return this;
  }

  /**
   * Getter for <code>public.certificate_history.x509_certificate</code>.
   */
  public String getX509Certificate() {
    return (String) get(2);
  }

  /**
   * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
   */
  @java.lang.Deprecated
  public CertificateHistoryRecord setValidity(Object value) {
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
    return CertificateHistory.CERTIFICATE_HISTORY.TRADING_PARTNER_ID;
  }

  @Override
  public Field<String> field2() {
    return CertificateHistory.CERTIFICATE_HISTORY.PRIVATE_KEY;
  }

  @Override
  public Field<String> field3() {
    return CertificateHistory.CERTIFICATE_HISTORY.X509_CERTIFICATE;
  }

  /**
   * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
   */
  @java.lang.Deprecated
  @Override
  public Field<Object> field4() {
    return CertificateHistory.CERTIFICATE_HISTORY.VALIDITY;
  }

  @Override
  public String component1() {
    return getTradingPartnerId();
  }

  @Override
  public String component2() {
    return getPrivateKey();
  }

  @Override
  public String component3() {
    return getX509Certificate();
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
    return getTradingPartnerId();
  }

  @Override
  public String value2() {
    return getPrivateKey();
  }

  @Override
  public String value3() {
    return getX509Certificate();
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
  public CertificateHistoryRecord value1(String value) {
    setTradingPartnerId(value);
    return this;
  }

  @Override
  public CertificateHistoryRecord value2(String value) {
    setPrivateKey(value);
    return this;
  }

  @Override
  public CertificateHistoryRecord value3(String value) {
    setX509Certificate(value);
    return this;
  }

  /**
   * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
   */
  @java.lang.Deprecated
  @Override
  public CertificateHistoryRecord value4(Object value) {
    setValidity(value);
    return this;
  }

  @Override
  public CertificateHistoryRecord values(String value1, String value2, String value3, Object value4) {
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
   * Create a detached CertificateHistoryRecord
   */
  public CertificateHistoryRecord() {
    super(CertificateHistory.CERTIFICATE_HISTORY);
  }

  /**
   * Create a detached, initialised CertificateHistoryRecord
   */
  public CertificateHistoryRecord(String tradingPartnerId, String privateKey, String x509Certificate, Object validity) {
    super(CertificateHistory.CERTIFICATE_HISTORY);

    set(0, tradingPartnerId);
    set(1, privateKey);
    set(2, x509Certificate);
    set(3, validity);
  }
}
