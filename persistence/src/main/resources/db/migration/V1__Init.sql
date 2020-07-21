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

create table trading_partner (
    id varchar(64) primary key,
    name varchar(128) unique,
    email varchar(128)
);

create table certificate (
    trading_partner_id varchar(64) primary key references trading_partner(id),
    private_key varchar(4096),
    x509_certificate varchar(4096)
);

create table trading_channel (
                                 sender_id            varchar(64) references trading_partner(id),
                                 recipient_id         varchar(64) references trading_partner (id),
                                 protocol             varchar(16),
                                 as2_url              varchar(128),
                                 as2_mdn_to           varchar(128) null,
                                 as2_mdn_options      varchar(128),
                                 encryption_algorithm varchar(16)  null,
                                 signing_algorithm    varchar(16),
                                 primary key (sender_id, recipient_id)
);

create table file
(
    id     serial primary key,
    bucket varchar(128),
    key    varchar(128),
    unique (bucket, key)
);

create table as2_message
(
    id                  varchar(64) primary key,
    "from"              varchar(64) references trading_partner (id),
    "to"                varchar(64) references trading_partner (id),
    subject             varchar(128),
    content_type        varchar(128),
    content_disposition varchar(128),
    mic                 varchar(64) null,
    /* store header and attributes as jsonb to allow for free form data but make it queryable */
    headers             jsonb,
    attributes          jsonb,
    body_file_id        int references file (id)
);

create table as2_mdn(
                        id           varchar(64) primary key,
                        message_id   varchar(64) references as2_message (id),
                        "text"       text,
    /* store header and attributes as jsonb to allow for free form data but make it queryable */
                        headers      jsonb,
                        attributes   jsonb,
                        body_file_id int references file (id)
);

