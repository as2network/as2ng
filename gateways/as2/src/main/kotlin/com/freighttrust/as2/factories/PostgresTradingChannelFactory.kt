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

package com.freighttrust.as2.factories

import com.freighttrust.as2.ext.toPartnership
import com.freighttrust.as2.ext.toTradingChannelRecord
import com.freighttrust.postgres.repositories.TradingChannelRepository
import com.helger.as2lib.AbstractDynamicComponent
import com.helger.as2lib.message.IMessage
import com.helger.as2lib.message.IMessageMDN
import com.helger.as2lib.params.MessageParameters
import com.helger.as2lib.partner.AS2PartnershipNotFoundException
import com.helger.as2lib.partner.IPartnershipFactory
import com.helger.as2lib.partner.Partnership
import com.helger.commons.ValueEnforcer
import com.helger.commons.collection.impl.ICommonsList
import com.helger.commons.collection.impl.ICommonsSet
import com.helger.commons.state.EChange

class PostgresTradingChannelFactory(
  private val tradingChannelRepository: TradingChannelRepository
) : AbstractDynamicComponent(), IPartnershipFactory {

  override fun addPartnership(partnership: Partnership): EChange {
    throw NotImplementedError("This method is not supported!")
  }

  override fun getAllPartnershipNames(): ICommonsSet<String> {
    throw NotImplementedError("This method is not supported!")
  }

  override fun getAllPartnerships(): ICommonsList<Partnership> {
    throw NotImplementedError("This method is not supported!")
  }

  override fun removePartnership(partnership: Partnership): EChange {
    throw NotImplementedError("This method is not supported!")
  }

  override fun getPartnershipByName(name: String?): Partnership? {
    throw NotImplementedError("This method is not supported!")
  }

  // Copied from AbstractPartnershipFactory
  override fun updatePartnership(msg: IMessage, overwrite: Boolean) {
    ValueEnforcer.notNull<IMessage>(msg, "Message")

    val partnership = getPartnership(msg.partnership())
    msg.partnership().copyFrom(partnership)

    if (overwrite) {
      partnership.subject?.let {
        msg.subject = MessageParameters(msg).format(it)
      }
    }
  }

  // Copied from AbstractPartnershipFactory
  override fun updatePartnership(mdn: IMessageMDN, overwrite: Boolean) {
    ValueEnforcer.notNull<IMessageMDN>(mdn, "MessageMDN")
    val partnership = getPartnership(mdn.partnership())
    mdn.partnership().copyFrom(partnership)
  }

  override fun getPartnership(partnership: Partnership): Partnership {
    val found = tradingChannelRepository
      .findOne(partnership.toTradingChannelRecord())
      ?: throw AS2PartnershipNotFoundException(partnership)

    return found.toPartnership()
  }
}
