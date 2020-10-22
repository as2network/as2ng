/*
 *
 *  * BSD 3-Clause License
 *  *
 *  * Copyright (c) 2020, FreightTrust & Clearing Corporation
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  *  Redistributions of source code must retain the above copyright notice, this
 *  *   list of conditions and the following disclaimer.
 *  *
 *  *  Redistributions in binary form must reproduce the above copyright notice,
 *  *   this list of conditions and the following disclaimer in the documentation
 *  *   and/or other materials provided with the distribution.
 *  *
 *  *  Neither the name of the copyright holder nor the names of its
 *  *   contributors may be used to endorse or promote products derived from
 *  *   this software without specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 *  * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *  * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.freighttrust.as2

import com.freighttrust.as2.utils.asPathResourceFile

object TestMessages {

  val UnencryptedDataNoReceipt = "/messages/text/plain/1-unencrypted-data-no-receipt.http".asPathResourceFile()!!

  val UnencryptedDataUnsignedReceipt = "/messages/text/plain/2-unencrypted-data-unsigned-receipt.http".asPathResourceFile()!!

  val UnencryptedDataSignedReceipt = "/messages/text/plain/3-unencrypted-data-signed-receipt.http".asPathResourceFile()!!

  val EncryptedDataNoReceipt = "/messages/text/plain/4-encrypted-data-no-receipt.http".asPathResourceFile()!!

  val EncryptedDataUnsignedReceipt = "/messages/text/plain/5-encrypted-data-unsigned-receipt.http".asPathResourceFile()!!

  val EncryptedDataSignedReceipt = "/messages/text/plain/6-encrypted-data-signed-receipt.http".asPathResourceFile()!!

  val SignedDataNoReceipt = "/messages/text/plain/7-signed-data-no-receipt.http".asPathResourceFile()!!

  val SignedDataUnsignedReceipt = "/messages/text/plain/8-signed-data-unsigned-receipt.http".asPathResourceFile()!!

  val SignedDataSignedReceipt = "/messages/text/plain/9-signed-data-signed-receipt.http".asPathResourceFile()!!

  val EncryptedAndSignedDataNoReceipt = "/messages/text/plain/10-encrypted-and-signed-data-no-receipt.http".asPathResourceFile()!!

  val EncryptedAndSignedDataUnsignedReceipt = "/messages/text/plain/11-encrypted-and-signed-data-unsigned-receipt.http".asPathResourceFile()!!

  val EncryptedAndSignedDataSignedReceipt = "/messages/text/plain/12-encrypted-and-signed-data-signed-receipt.http".asPathResourceFile()!!

  val ContentLengthRequest = "/messages/text/plain/request-with-content-length.http".asPathResourceFile()
}
