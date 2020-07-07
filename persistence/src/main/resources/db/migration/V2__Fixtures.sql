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

/* TODO: Remove this file once we have a better story on fixtures */

insert into trading_partner
values ('OpenAS2A', 'OpenAS2A', 'OpenAS2 A email'),
       ('OpenAS2B', 'OpenAS2B', 'OpenAS2 B email');

-- insert into certificate
-- values ('OpenAS2A',
--         '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'),
--        ('OpenAS2B',
--         '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');

insert into trading_channel
values ('OpenAS2A', 'OpenAS2B', 'as2', 'http://localhost:10082', 'http://localhost:10080',
        'signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, md5',
        '3des',
        'md5'),
       ('OpenAS2B', 'OpenAS2A', 'as2', 'http://localhost:10081', 'http://localhost:10080',
        'signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, sha1',
        '3des',
        'sha1');
