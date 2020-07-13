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
values ('OpenAS2A', 'OpenAS2B', 'as2', 'http://localhost:10082', 'http://localhost:10081',
        'signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, md5',
        '3des',
        'md5'),
       ('OpenAS2B', 'OpenAS2A', 'as2', 'http://localhost:10080', 'http://localhost:10083',
        'signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, md5',
        '3des',
        'sha1');
