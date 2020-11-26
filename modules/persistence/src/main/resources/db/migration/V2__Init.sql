/*****************************************************************************/
/* Key Pair                                                                  */
/*****************************************************************************/

create table key_pair
(
    id               bigserial primary key,
    serial_number    varchar(60)          not null,
    certificate      varchar(4096) unique not null,
    private_key      varchar(4096) unique,
    private_key_type varchar(16),
    issuing_ca       varchar(4096),
    ca_chain         varchar(4096)[],
    expires_at       timestamptz
);

/*****************************************************************************/
/* Trading Partner                                                           */
/*****************************************************************************/

create table trading_partner
(
    id          bigserial primary key,
    name        varchar(128) unique             not null,
    email       varchar(128)                    not null,
    key_pair_id bigint references key_pair (id) not null,
    validity    tstzrange default tstzrange(current_timestamp, null),
    unique (key_pair_id)
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

create type trading_channel_type as enum ('receiving', 'forwarding');

create table trading_channel
(
    id                                      bigserial primary key,
    name                                    varchar(64) unique                     not null,
    type                                    trading_channel_type                   not null,

    /* common options */

    sender_id                               bigint references trading_partner (id) not null,
    sender_as2_identifier                   varchar(64)                            not null,
    sender_key_pair_id                      bigint                                 null references key_pair (id),

    recipient_id                            bigint references trading_partner (id) not null,
    recipient_as2_identifier                varchar(64)                            not null,
    recipient_key_pair_id                   bigint                                 null references key_pair (id),

    allow_body_certificate_for_verification boolean   default false                not null,

    /* options for forwarding channel type */
    recipient_message_url                   varchar(128)                           null,

    /* options for receiving trading channel type */

    validity                                tstzrange default tstzrange(current_timestamp, null),
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
        'validity', 'trading_channel_history', true
    );

/*****************************************************************************/
/* File                                                                      */
/*****************************************************************************/

create type file_provider as enum ('filesystem', 's3');

create table file
(
    id       bigserial primary key,
    provider file_provider not null,
    metadata jsonb         not null,
    unique (provider, metadata)
);

/*****************************************************************************/
/* Request                                                                   */
/*****************************************************************************/

create type request_type as enum ('message', 'mdn');

create table request
(
    id                  uuid primary key,

    type                request_type                           not null,
    headers             jsonb                                  not null,
    body_file_id        bigint                                 not null,

    message_id          varchar(128) unique                    not null,
    subject             varchar(128)                           not null,

    trading_channel_id  bigint references trading_channel (id) not null,
    original_request_id uuid references request (id)           null,

    received_at         timestamptz default current_timestamp  not null,

    forwarded_at        timestamptz                            null,
    forwarded_to        varchar(128)                           null,

    error_message       varchar(512)                           null,
    error_stack_trace   text                                   null
);

/*****************************************************************************/
/* Message                                                                   */
/*****************************************************************************/

create table message
(
    request_id              uuid primary key references request (id),

    encryption_algorithm    varchar(32)    null,
    encryption_key_pair_id  bigint         null,

    signature_key_pair_id   bigint         null,

    compression_algorithm   varchar(16)    null,

    mics                    varchar(512)[] null,

    is_mdn_requested        bool           not null,
    is_mdn_async            bool           not null,
    receipt_delivery_option varchar(128)   null
);

/*****************************************************************************/
/* Message Disposition Notification                                          */
/*****************************************************************************/

create table disposition_notification
(
    request_id            uuid primary key references request (id),

    original_message_id   varchar(128) references request (message_id) not null,
    original_recipient    varchar(64)                                  not null,
    final_recipient       varchar(64)                                  not null,
    reporting_ua          varchar(64)                                  not null,
    disposition           varchar(128)                                 not null,
    received_content_mic  varchar(512)                                 null,
    digest_algorithm      varchar(16)                                  null,

    signature_key_pair_id bigint                                       null
);
