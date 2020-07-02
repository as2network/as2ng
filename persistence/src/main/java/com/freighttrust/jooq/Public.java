/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq;


import com.freighttrust.jooq.tables.*;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
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
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

  private static final long serialVersionUID = -1266936214;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

  /**
   * The table <code>public.as2_mdn</code>.
   */
  public final As2Mdn AS2_MDN = com.freighttrust.jooq.tables.As2Mdn.AS2_MDN;

  /**
   * The table <code>public.as2_message</code>.
   */
  public final As2Message AS2_MESSAGE = com.freighttrust.jooq.tables.As2Message.AS2_MESSAGE;

  /**
   * The table <code>public.certificate</code>.
   */
  public final Certificate CERTIFICATE = com.freighttrust.jooq.tables.Certificate.CERTIFICATE;

  /**
   * The table <code>public.flyway_schema_history</code>.
   */
  public final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = com.freighttrust.jooq.tables.FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

  /**
   * The table <code>public.trading_channel</code>.
   */
  public final TradingChannel TRADING_CHANNEL = com.freighttrust.jooq.tables.TradingChannel.TRADING_CHANNEL;

    /**
     * The table <code>public.trading_partner</code>.
     */
    public final TradingPartner TRADING_PARTNER = com.freighttrust.jooq.tables.TradingPartner.TRADING_PARTNER;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
          As2Mdn.AS2_MDN,
          As2Message.AS2_MESSAGE,
          Certificate.CERTIFICATE,
          FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY,
          TradingChannel.TRADING_CHANNEL,
          TradingPartner.TRADING_PARTNER);
    }
}
