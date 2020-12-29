/*
 * This file is generated by jOOQ.
 */
package network.as2.jooq.enums;


import network.as2.jooq.Public;

import org.jooq.Catalog;
import org.jooq.EnumType;
import org.jooq.Schema;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public enum RequestType implements EnumType {

    message("message"),

    mdn("mdn");

    private final String literal;

    private RequestType(String literal) {
        this.literal = literal;
    }

    @Override
    public Catalog getCatalog() {
        return getSchema().getCatalog();
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public String getName() {
        return "request_type";
    }

    @Override
    public String getLiteral() {
        return literal;
    }
}