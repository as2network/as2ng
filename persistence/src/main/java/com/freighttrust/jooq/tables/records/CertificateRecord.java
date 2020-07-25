/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.records;


import com.freighttrust.jooq.tables.Certificate;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;

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
public class CertificateRecord extends UpdatableRecordImpl<CertificateRecord> implements Record4<String, String, String, Object> {

    private static final long serialVersionUID = 20172848;

    /**
     * Setter for <code>public.certificate.trading_partner_id</code>.
     */
    public CertificateRecord setTradingPartnerId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.certificate.trading_partner_id</code>.
     */
    public String getTradingPartnerId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.certificate.private_key</code>.
     */
    public CertificateRecord setPrivateKey(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.certificate.private_key</code>.
     */
    public String getPrivateKey() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.certificate.x509_certificate</code>.
     */
    public CertificateRecord setX509Certificate(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.certificate.x509_certificate</code>.
     */
    public String getX509Certificate() {
        return (String) get(2);
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    public CertificateRecord setValidity(Object value) {
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
    public Record1<String> key() {
        return (Record1) super.key();
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
        return Certificate.CERTIFICATE.TRADING_PARTNER_ID;
    }

    @Override
    public Field<String> field2() {
        return Certificate.CERTIFICATE.PRIVATE_KEY;
    }

    @Override
    public Field<String> field3() {
        return Certificate.CERTIFICATE.X509_CERTIFICATE;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Field<Object> field4() {
        return Certificate.CERTIFICATE.VALIDITY;
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
    public CertificateRecord value1(String value) {
        setTradingPartnerId(value);
        return this;
    }

    @Override
    public CertificateRecord value2(String value) {
        setPrivateKey(value);
        return this;
    }

    @Override
    public CertificateRecord value3(String value) {
        setX509Certificate(value);
        return this;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public CertificateRecord value4(Object value) {
        setValidity(value);
        return this;
    }

    @Override
    public CertificateRecord values(String value1, String value2, String value3, Object value4) {
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
     * Create a detached CertificateRecord
     */
    public CertificateRecord() {
        super(Certificate.CERTIFICATE);
    }

    /**
     * Create a detached, initialised CertificateRecord
     */
    public CertificateRecord(String tradingPartnerId, String privateKey, String x509Certificate, Object validity) {
        super(Certificate.CERTIFICATE);

        set(0, tradingPartnerId);
        set(1, privateKey);
        set(2, x509Certificate);
        set(3, validity);
    }
}
