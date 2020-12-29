/*
 * This file is generated by jOOQ.
 */
package network.as2.jooq;


import org.jooq.Sequence;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;


/**
 * Convenience access to all sequences in public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

    /**
     * The sequence <code>public.file_id_seq</code>
     */
    public static final Sequence<Long> FILE_ID_SEQ = Internal.createSequence("file_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);

    /**
     * The sequence <code>public.key_pair_id_seq</code>
     */
    public static final Sequence<Long> KEY_PAIR_ID_SEQ = Internal.createSequence("key_pair_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);

    /**
     * The sequence <code>public.trading_channel_id_seq</code>
     */
    public static final Sequence<Long> TRADING_CHANNEL_ID_SEQ = Internal.createSequence("trading_channel_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);

    /**
     * The sequence <code>public.trading_partner_id_seq</code>
     */
    public static final Sequence<Long> TRADING_PARTNER_ID_SEQ = Internal.createSequence("trading_partner_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);
}