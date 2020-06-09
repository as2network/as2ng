
create type entity as enum (
    'TradingPartner',
    'DIS'
);

create type transport as enum (
    'AS2',
    'MQ'
);

create type message_type as enum (
    'DocumentSubmission',
    'DocumentValidationResponse',
    'DocumentReviewResponse'
);

create table message (
    id varchar(50) primary key,
    payload bytea
);

create table message_origin (
    id varchar(50) primary key references message on delete cascade,
    entity entity,
    transport transport,
    received_at timestamp,
    ip_address cidr null
);

create table message_header (
    id varchar(50) primary key references message on delete cascade,
    type message_type,
    sent_date_time timestamp,
    transmitter_id char(3),
    transmitter_site_code smallint,
    preparer_id char(3),
    preparer_site_code smallint,
    comment varchar(50) null
);

