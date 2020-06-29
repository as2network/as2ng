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
values ('OpenAS2A', 'OpenAS2A', 'OpenAS2 A email');

insert into trading_partner
values ('OpenAS2B', 'OpenAS2B', 'OpenAS2 B email');

insert into trading_channel
values ('OpenAS2A',
        'OpenAS2B',
        'OpenAS2A_alias',
        'OpenAS2B_alias',
        'as2',
        'http://localhost:10080',
        'http://localhost:10080',
        'signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, md5',
        '3des',
        'md5');

insert into trading_channel
values ('OpenAS2B',
        'OpenAS2A',
        'OpenAS2B_alias',
        'OpenAS2A_alias',
        'as2',
        'http://localhost:10080',
        'http://localhost:10080',
        'signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, sha1',
        '3des',
        'sha1');
