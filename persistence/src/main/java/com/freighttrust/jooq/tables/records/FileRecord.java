/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.records;


import com.freighttrust.jooq.tables.File;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
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
public class FileRecord extends UpdatableRecordImpl<FileRecord> implements Record3<Long, String, String> {

    private static final long serialVersionUID = 735813340;

    /**
     * Setter for <code>public.file.id</code>.
     */
    public FileRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.file.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.file.bucket</code>.
     */
    public FileRecord setBucket(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.file.bucket</code>.
     */
    public String getBucket() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.file.key</code>.
     */
    public FileRecord setKey(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.file.key</code>.
     */
    public String getKey() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<Long, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Long, String, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return File.FILE.ID;
    }

    @Override
    public Field<String> field2() {
        return File.FILE.BUCKET;
    }

    @Override
    public Field<String> field3() {
        return File.FILE.KEY;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getBucket();
    }

    @Override
    public String component3() {
        return getKey();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getBucket();
    }

    @Override
    public String value3() {
        return getKey();
    }

    @Override
    public FileRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public FileRecord value2(String value) {
        setBucket(value);
        return this;
    }

    @Override
    public FileRecord value3(String value) {
        setKey(value);
        return this;
    }

    @Override
    public FileRecord values(Long value1, String value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FileRecord
     */
    public FileRecord() {
        super(File.FILE);
    }

    /**
     * Create a detached, initialised FileRecord
     */
    public FileRecord(Long id, String bucket, String key) {
        super(File.FILE);

        set(0, id);
        set(1, bucket);
        set(2, key);
    }
}
