/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.records;


import com.freighttrust.jooq.tables.As2Mdn;
import org.jooq.*;
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
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class As2MdnRecord extends UpdatableRecordImpl<As2MdnRecord> implements Record6<String, String, String, JSONB, JSONB, Integer> {

  private static final long serialVersionUID = 65554202;

  /**
   * Setter for <code>public.as2_mdn.id</code>.
   */
  public As2MdnRecord setId(String value) {
    set(0, value);
    return this;
  }

    /**
     * Getter for <code>public.as2_mdn.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.as2_mdn.message_id</code>.
     */
    public As2MdnRecord setMessageId(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.as2_mdn.message_id</code>.
     */
    public String getMessageId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.as2_mdn.text</code>.
     */
    public As2MdnRecord setText(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.as2_mdn.text</code>.
     */
    public String getText() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.as2_mdn.headers</code>.
     */
    public As2MdnRecord setHeaders(JSONB value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.as2_mdn.headers</code>.
     */
    public JSONB getHeaders() {
        return (JSONB) get(3);
    }

    /**
     * Setter for <code>public.as2_mdn.attributes</code>.
     */
    public As2MdnRecord setAttributes(JSONB value) {
        set(4, value);
      return this;
    }

  /**
   * Getter for <code>public.as2_mdn.attributes</code>.
   */
  public JSONB getAttributes() {
    return (JSONB) get(4);
  }

  /**
   * Create a detached, initialised As2MdnRecord
   */
  public As2MdnRecord(String id, String messageId, String text, JSONB headers, JSONB attributes, Integer bodyFileId) {
    super(As2Mdn.AS2_MDN);

    set(0, id);
    set(1, messageId);
    set(2, text);
    set(3, headers);
    set(4, attributes);
    set(5, bodyFileId);
  }

  /**
   * Getter for <code>public.as2_mdn.body_file_id</code>.
   */
  public Integer getBodyFileId() {
    return (Integer) get(5);
  }

  // -------------------------------------------------------------------------
  // Primary key information
  // -------------------------------------------------------------------------

  @Override
  public Record1<String> key() {
    return (Record1) super.key();
  }

  // -------------------------------------------------------------------------
  // Record6 type implementation
  // -------------------------------------------------------------------------

  /**
   * Setter for <code>public.as2_mdn.body_file_id</code>.
   */
  public As2MdnRecord setBodyFileId(Integer value) {
    set(5, value);
    return this;
  }

  @Override
  public Row6<String, String, String, JSONB, JSONB, Integer> fieldsRow() {
    return (Row6) super.fieldsRow();
  }

  @Override
  public Field<String> field1() {
    return As2Mdn.AS2_MDN.ID;
  }

    @Override
    public Field<String> field2() {
        return As2Mdn.AS2_MDN.MESSAGE_ID;
    }

    @Override
    public Field<String> field3() {
        return As2Mdn.AS2_MDN.TEXT;
    }

    @Override
    public Field<JSONB> field4() {
      return As2Mdn.AS2_MDN.HEADERS;
    }

  @Override
  public Field<JSONB> field5() {
    return As2Mdn.AS2_MDN.ATTRIBUTES;
  }

  @Override
  public Row6<String, String, String, JSONB, JSONB, Integer> valuesRow() {
    return (Row6) super.valuesRow();
  }

  @Override
  public String component1() {
    return getId();
  }

  @Override
  public String component2() {
    return getMessageId();
  }

  @Override
  public String component3() {
        return getText();
    }

    @Override
    public JSONB component4() {
      return getHeaders();
    }

  @Override
  public JSONB component5() {
    return getAttributes();
  }

  @Override
  public Field<Integer> field6() {
    return As2Mdn.AS2_MDN.BODY_FILE_ID;
  }

  @Override
  public String value1() {
    return getId();
  }

  @Override
  public String value2() {
    return getMessageId();
  }

  @Override
  public String value3() {
        return getText();
    }

    @Override
    public JSONB value4() {
      return getHeaders();
    }

  @Override
  public JSONB value5() {
    return getAttributes();
  }

  @Override
  public Integer component6() {
    return getBodyFileId();
  }

  @Override
  public As2MdnRecord value1(String value) {
    setId(value);
    return this;
  }

  @Override
  public As2MdnRecord value2(String value) {
    setMessageId(value);
    return this;
    }

    @Override
    public As2MdnRecord value3(String value) {
        setText(value);
        return this;
    }

    @Override
    public As2MdnRecord value4(JSONB value) {
        setHeaders(value);
      return this;
    }

  @Override
  public As2MdnRecord value5(JSONB value) {
    setAttributes(value);
    return this;
  }

  @Override
  public Integer value6() {
    return getBodyFileId();
  }

  @Override
  public As2MdnRecord value6(Integer value) {
    setBodyFileId(value);
    return this;
  }

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Create a detached As2MdnRecord
   */
  public As2MdnRecord() {
    super(As2Mdn.AS2_MDN);
  }

  @Override
  public As2MdnRecord values(String value1, String value2, String value3, JSONB value4, JSONB value5, Integer value6) {
    value1(value1);
    value2(value2);
    value3(value3);
    value4(value4);
    value5(value5);
    value6(value6);
    return this;
  }
}
