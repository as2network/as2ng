
/*****************************************************************************/
/* Key Pair                                                                  */
/*****************************************************************************/

create table key_pair
(
    id              bigserial primary key,
    private_key     varchar(4096) null,
    certificate     varchar(4096),
    expires_at      timestamptz
);

/*****************************************************************************/
/* Trading Partner                                                           */
/*****************************************************************************/

create table trading_partner
(
    id       bigserial primary key,
    name     varchar(128) unique,
    email    varchar(128),
    validity tstzrange default tstzrange(current_timestamp, null)
);

create table trading_partner_history
(
    LIKE trading_partner
);

create trigger trading_partner_versioning_trigger
    before insert or update or delete
    on trading_partner
    for each row
execute procedure versioning(
        'validity', 'trading_partner_history', true
    );

/*****************************************************************************/
/* Trading Channel                                                           */
/*****************************************************************************/

create table trading_channel
(
    id                              bigserial primary key,
    name                            varchar (64),

    sender_id                       bigint references trading_partner (id),
    sender_as2_identifier           varchar (64),

    recipient_id                    bigint references trading_partner (id),
    recipient_as2_identifier        varchar (64),
    recipient_message_url           varchar (128),

    encryption_algorithm            varchar(16) null,
    encryption_key_pair_id          bigint null references key_pair(id),

    validity                        tstzrange default tstzrange(current_timestamp, null),
    unique (sender_id, recipient_id),
    unique (sender_as2_identifier, recipient_as2_identifier)
);

create table trading_channel_history
(
    LIKE trading_channel
);

create trigger trading_channel_versioning_trigger
    before insert or update or delete
    on trading_channel
    for each row
execute procedure versioning(
        'validity', 'trading_channel', true
    );


/*****************************************************************************/
/* File                                                                      */
/*****************************************************************************/

create table file
(
    id     bigserial primary key,
    bucket varchar(128),
    key    varchar(128),
    unique (bucket, key)
);

/*****************************************************************************/
/* Request                                                                   */
/*****************************************************************************/

create table request
(
    id                       uuid primary key,
    headers                  jsonb,
    body_file_id             bigint                       not null,
    trading_channel_id       bigint references trading_channel(id),
    message_id               varchar(64) unique,
    subject                  varchar(128),
    received_at              timestamptz default current_timestamp,

    original_request_id      uuid references request (id) null,

    processed_at             timestamptz                  null,
    processing_error         bool,
    processing_error_message varchar(128)                 null,

    delivered_at             timestamptz                  null,
    delivered_to             varchar(128)                 null
);

/*****************************************************************************/
/* Message                                                                   */
/*****************************************************************************/

create table message
(
    request_id              uuid primary key references request (id),

    encryption_algorithm    varchar(16)  null,
    compression_algorithm   varchar(16)  null,

    mics                    varchar(64)[]  null,

    is_mdn_requested        bool,
    is_mdn_async            bool,
    receipt_delivery_option varchar(128) null
);

/*****************************************************************************/
/* Message Disposition Notification                                          */
/*****************************************************************************/

create table message_disposition_notification
(
    request_id           uuid primary key references request (id),

    original_message_id  varchar(64),
    original_recipient   varchar(64),
    final_recipient      varchar(64),
    reporting_ua         varchar(64),
    disposition          varchar(128),
    received_content_mic varchar(64) null
);
