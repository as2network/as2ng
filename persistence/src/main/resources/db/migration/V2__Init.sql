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

create table trading_partner
(
    id       varchar(64) primary key,
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

create table certificate
(
    trading_partner_id varchar(64) primary key references trading_partner (id),
    private_key        varchar(4096),
    x509_certificate   varchar(4096),
    validity           tstzrange default tstzrange(current_timestamp, null)
);

create table certificate_history
(
    LIKE certificate
);

create trigger certificate_history_versioning_trigger
    before insert or update or delete
    on certificate_history
    for each row
execute procedure versioning(
        'validity', 'certificate_history', true
    );


create table trading_channel
(
    sender_id                       varchar(64) references trading_partner (id),
    recipient_id                    varchar(64) references trading_partner (id),
    protocol                        varchar(16),
    as2_url                         varchar(128),
    as2_mdn_to                      varchar(128) null,
    as2_mdn_options                 varchar(128),
    encryption_algorithm            varchar(16)  null,
    signing_algorithm               varchar(16)  null,
    rfc_3851_mic_algorithms_enabled bool,
    validity                        tstzrange default tstzrange(current_timestamp, null),
    primary key (sender_id, recipient_id)
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


create table file
(
    id     bigserial primary key,
    bucket varchar(128),
    key    varchar(128),
    unique (bucket, key)
);


create table request
(
    id                       uuid primary key,
    headers                  jsonb,
    body_file_id             bigint                       not null,
    sender_id                varchar(64) references trading_partner (id),
    recipient_id             varchar(64) references trading_partner (id),
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


create table message
(
    request_id              uuid primary key references request (id),

    encryption_algorithm    varchar(16)  null,
    compression_algorithm   varchar(16)  null,
    mic                     varchar(64)  null,

    is_mdn_requested        bool,
    is_mdn_async            bool,
    receipt_delivery_option varchar(128) null
);

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
