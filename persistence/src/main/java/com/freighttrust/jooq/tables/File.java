/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables;


import com.freighttrust.jooq.Indexes;
import com.freighttrust.jooq.Keys;
import com.freighttrust.jooq.Public;
import com.freighttrust.jooq.tables.records.FileRecord;
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
public class File extends TableImpl<FileRecord> {

  /**
   * The reference instance of <code>public.file</code>
   */
  public static final File FILE = new File();
  private static final long serialVersionUID = 1876802401;
  /**
   * The column <code>public.file.id</code>.
   */
  public final TableField<FileRecord, Integer> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('file_id_seq'::regclass)", org.jooq.impl.SQLDataType.INTEGER)), this, "");
  /**
   * The column <code>public.file.bucket</code>.
   */
  public final TableField<FileRecord, String> BUCKET = createField(DSL.name("bucket"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");
  /**
   * The column <code>public.file.key</code>.
   */
  public final TableField<FileRecord, String> KEY = createField(DSL.name("key"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

  /**
   * Create a <code>public.file</code> table reference
   */
  public File() {
    this(DSL.name("file"), null);
  }

  /**
   * Create an aliased <code>public.file</code> table reference
   */
  public File(String alias) {
    this(DSL.name(alias), FILE);
  }

  /**
   * Create an aliased <code>public.file</code> table reference
   */
  public File(Name alias) {
    this(alias, FILE);
  }

  private File(Name alias, Table<FileRecord> aliased) {
    this(alias, aliased, null);
  }

  private File(Name alias, Table<FileRecord> aliased, Field<?>[] parameters) {
    super(alias, null, aliased, parameters, DSL.comment(""));
  }

  public <O extends Record> File(Table<O> child, ForeignKey<O, FileRecord> key) {
    super(child, key, FILE);
  }

  /**
   * The class holding records for this type
   */
  @Override
  public Class<FileRecord> getRecordType() {
    return FileRecord.class;
  }

  @Override
  public Schema getSchema() {
    return Public.PUBLIC;
  }

  @Override
  public List<Index> getIndexes() {
    return Arrays.<Index>asList(Indexes.FILE_BUCKET_KEY_KEY, Indexes.FILE_PKEY);
  }

  @Override
  public Identity<FileRecord, Integer> getIdentity() {
    return Keys.IDENTITY_FILE;
  }

  @Override
  public UniqueKey<FileRecord> getPrimaryKey() {
    return Keys.FILE_PKEY;
  }

  @Override
  public List<UniqueKey<FileRecord>> getKeys() {
    return Arrays.<UniqueKey<FileRecord>>asList(Keys.FILE_PKEY, Keys.FILE_BUCKET_KEY_KEY);
  }

  @Override
  public File as(String alias) {
    return new File(DSL.name(alias), this);
  }

  @Override
  public File as(Name alias) {
    return new File(alias, this);
  }

  /**
   * Rename this table
   */
  @Override
  public File rename(String name) {
    return new File(DSL.name(name), null);
  }

  /**
   * Rename this table
   */
  @Override
  public File rename(Name name) {
    return new File(name, null);
  }

  // -------------------------------------------------------------------------
  // Row3 type methods
  // -------------------------------------------------------------------------

  @Override
  public Row3<Integer, String, String> fieldsRow() {
    return (Row3) super.fieldsRow();
  }
}
