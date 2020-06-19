/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, FreightTrust & Clearing Corporation
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables;


import com.freighttrust.jooq.Indexes;
import com.freighttrust.jooq.Keys;
import com.freighttrust.jooq.Public;
import com.freighttrust.jooq.tables.records.As2MessageRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row9;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


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
public class As2Message extends TableImpl<As2MessageRecord> {

    private static final long serialVersionUID = 675655910;

    /**
     * The reference instance of <code>public.as2_message</code>
     */
    public static final As2Message AS2_MESSAGE = new As2Message();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<As2MessageRecord> getRecordType() {
        return As2MessageRecord.class;
    }

    /**
     * The column <code>public.as2_message.id</code>.
     */
    public final TableField<As2MessageRecord, String> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>public.as2_message.from</code>.
     */
    public final TableField<As2MessageRecord, String> FROM = createField(DSL.name("from"), org.jooq.impl.SQLDataType.VARCHAR(32), this, "");

    /**
     * The column <code>public.as2_message.to</code>.
     */
    public final TableField<As2MessageRecord, String> TO = createField(DSL.name("to"), org.jooq.impl.SQLDataType.VARCHAR(32), this, "");

    /**
     * The column <code>public.as2_message.subject</code>.
     */
    public final TableField<As2MessageRecord, String> SUBJECT = createField(DSL.name("subject"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.as2_message.contenttype</code>.
     */
    public final TableField<As2MessageRecord, String> CONTENTTYPE = createField(DSL.name("contenttype"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.as2_message.contentdisposition</code>.
     */
    public final TableField<As2MessageRecord, String> CONTENTDISPOSITION = createField(DSL.name("contentdisposition"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>public.as2_message.headers</code>.
     */
    public final TableField<As2MessageRecord, JSONB> HEADERS = createField(DSL.name("headers"), org.jooq.impl.SQLDataType.JSONB, this, "");

    /**
     * The column <code>public.as2_message.attributes</code>.
     */
    public final TableField<As2MessageRecord, JSONB> ATTRIBUTES = createField(DSL.name("attributes"), org.jooq.impl.SQLDataType.JSONB, this, "");

    /**
     * The column <code>public.as2_message.data</code>.
     */
    public final TableField<As2MessageRecord, byte[]> DATA = createField(DSL.name("data"), org.jooq.impl.SQLDataType.BLOB, this, "");

    /**
     * Create a <code>public.as2_message</code> table reference
     */
    public As2Message() {
        this(DSL.name("as2_message"), null);
    }

    /**
     * Create an aliased <code>public.as2_message</code> table reference
     */
    public As2Message(String alias) {
        this(DSL.name(alias), AS2_MESSAGE);
    }

    /**
     * Create an aliased <code>public.as2_message</code> table reference
     */
    public As2Message(Name alias) {
        this(alias, AS2_MESSAGE);
    }

    private As2Message(Name alias, Table<As2MessageRecord> aliased) {
        this(alias, aliased, null);
    }

    private As2Message(Name alias, Table<As2MessageRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> As2Message(Table<O> child, ForeignKey<O, As2MessageRecord> key) {
        super(child, key, AS2_MESSAGE);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.AS2_MESSAGE_PKEY);
    }

    @Override
    public UniqueKey<As2MessageRecord> getPrimaryKey() {
        return Keys.AS2_MESSAGE_PKEY;
    }

    @Override
    public List<UniqueKey<As2MessageRecord>> getKeys() {
        return Arrays.<UniqueKey<As2MessageRecord>>asList(Keys.AS2_MESSAGE_PKEY);
    }

    @Override
    public As2Message as(String alias) {
        return new As2Message(DSL.name(alias), this);
    }

    @Override
    public As2Message as(Name alias) {
        return new As2Message(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public As2Message rename(String name) {
        return new As2Message(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public As2Message rename(Name name) {
        return new As2Message(name, null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<String, String, String, String, String, String, JSONB, JSONB, byte[]> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}
