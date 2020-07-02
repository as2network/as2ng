/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables;


import com.freighttrust.jooq.Indexes;
import com.freighttrust.jooq.Keys;
import com.freighttrust.jooq.Public;
import com.freighttrust.jooq.tables.records.CertificateRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import java.util.List;


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
public class Certificate extends TableImpl<CertificateRecord> {

  /**
   * The reference instance of <code>public.certificate</code>
   */
  public static final Certificate CERTIFICATE = new Certificate();
  private static final long serialVersionUID = -1518464228;
  /**
   * The column <code>public.certificate.trading_partner_id</code>.
   */
  public final TableField<CertificateRecord, String> TRADING_PARTNER_ID = createField(DSL.name("trading_partner_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");
  /**
   * The column <code>public.certificate.private_key</code>.
   */
  public final TableField<CertificateRecord, String> PRIVATE_KEY = createField(DSL.name("private_key"), org.jooq.impl.SQLDataType.VARCHAR(1628), this, "");
  /**
   * The column <code>public.certificate.x509_certificate</code>.
   */
  public final TableField<CertificateRecord, String> X509_CERTIFICATE = createField(DSL.name("x509_certificate"), org.jooq.impl.SQLDataType.VARCHAR(1628), this, "");

  /**
   * Create a <code>public.certificate</code> table reference
   */
  public Certificate() {
    this(DSL.name("certificate"), null);
  }

  /**
   * Create an aliased <code>public.certificate</code> table reference
   */
  public Certificate(String alias) {
    this(DSL.name(alias), CERTIFICATE);
  }

  /**
   * Create an aliased <code>public.certificate</code> table reference
   */
  public Certificate(Name alias) {
    this(alias, CERTIFICATE);
  }

  private Certificate(Name alias, Table<CertificateRecord> aliased) {
    this(alias, aliased, null);
  }

  private Certificate(Name alias, Table<CertificateRecord> aliased, Field<?>[] parameters) {
    super(alias, null, aliased, parameters, DSL.comment(""));
  }

  public <O extends Record> Certificate(Table<O> child, ForeignKey<O, CertificateRecord> key) {
    super(child, key, CERTIFICATE);
  }

  /**
   * The class holding records for this type
   */
  @Override
  public Class<CertificateRecord> getRecordType() {
    return CertificateRecord.class;
  }

  @Override
  public Schema getSchema() {
    return Public.PUBLIC;
  }

  @Override
  public List<Index> getIndexes() {
    return Arrays.<Index>asList(Indexes.CERTIFICATE_PKEY);
  }

  @Override
  public UniqueKey<CertificateRecord> getPrimaryKey() {
    return Keys.CERTIFICATE_PKEY;
  }

  @Override
  public List<UniqueKey<CertificateRecord>> getKeys() {
    return Arrays.<UniqueKey<CertificateRecord>>asList(Keys.CERTIFICATE_PKEY);
  }

  @Override
  public List<ForeignKey<CertificateRecord, ?>> getReferences() {
    return Arrays.<ForeignKey<CertificateRecord, ?>>asList(Keys.CERTIFICATE__CERTIFICATE_TRADING_PARTNER_ID_FKEY);
  }

  public TradingPartner tradingPartner() {
    return new TradingPartner(this, Keys.CERTIFICATE__CERTIFICATE_TRADING_PARTNER_ID_FKEY);
  }

  @Override
  public Certificate as(String alias) {
    return new Certificate(DSL.name(alias), this);
  }

  @Override
  public Certificate as(Name alias) {
    return new Certificate(alias, this);
  }

  /**
   * Rename this table
   */
  @Override
  public Certificate rename(String name) {
    return new Certificate(DSL.name(name), null);
  }

  /**
   * Rename this table
   */
  @Override
  public Certificate rename(Name name) {
    return new Certificate(name, null);
  }

  // -------------------------------------------------------------------------
  // Row3 type methods
  // -------------------------------------------------------------------------

  @Override
  public Row3<String, String, String> fieldsRow() {
    return (Row3) super.fieldsRow();
  }
}