

create table trading_partner (
    id serial primary key,
    name varchar(128) unique,
    x509_certificate bytea,
    email varchar(128)
);

create table partnership (
    sender_id integer references trading_partner(id),
    recipient_id integer references trading_partner(id),
    protocol varchar(16),
    subject varchar(128),
    as2_url varchar(128),
    as2_mdn_to varchar(128) null,
    as2_mdn_options varchar(64)[],
    encryption_algorithm varchar(16) null,
    signing_algorithm varchar(16),
    primary key (sender_id, recipient_id)
);
