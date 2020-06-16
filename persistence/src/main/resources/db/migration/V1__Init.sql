
create table trading_partner (
    id serial primary key,
    name varchar(128) unique,
    filer_code char(3) unique
);

create table port (
    id smallint primary key,
    name varchar(64) unique,
    supports_vessel boolean,
    supports_air boolean,
    supports_rail boolean,
    supports_road boolean,
    supports_fixed boolean
);

create type entity as enum (
    'TradingPartner',
    'DIS'
);

create type transport as enum (
    'AS2',
    'MQ'
);

create type message_type as enum (
    'DocumentSubmissionPackage',
    'DocumentWithdrawal',
    'DocumentValidationResponse',
    'DateRequestPackage',
    'RequestedDataPackage',
    'DocumentReviewResponse'
);

create table document_message (
    id varchar(50) primary key,
    type message_type,
    sent_date_time timestamp,
    transmitter_id char(3) references trading_partner(filer_code),
    transmitter_site_code smallint reference port(id),
    preparer_id char(3),
    preparer_site_code smallint references port(id),
    payload_url varchar(128),
    comment varchar(50) null,
    received_at timestamp;
    received_via transport;
    received_from entity;
);

create table document_submission () inherits (document_message);

create table document_validation_response (
    document_submission_id varchar(50) references document_submission(id)
) inherits (document_message);

create table document_withdrawal () inherits (document_message);

create table data_request_package () inherits (document_message);

create table requested_data_package (
    data_request_package_id varchar(50) references data_request_package(id)
) inherits (document_message)

create table document_review_response (
    document_submission_id varchar(50) references document_submission(id)
) inherits (document_message);

