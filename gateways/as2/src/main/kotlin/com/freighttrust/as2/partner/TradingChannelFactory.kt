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

package com.freighttrust.as2.partner

import com.freighttrust.db.repositories.TradingChannelRepository
import com.helger.as2lib.AbstractDynamicComponent
import com.helger.as2lib.message.IMessage
import com.helger.as2lib.message.IMessageMDN
import com.helger.as2lib.partner.IPartnershipFactory
import com.helger.as2lib.partner.Partnership
import com.helger.as2lib.session.IAS2Session
import com.helger.commons.collection.attr.IStringMap
import com.helger.commons.collection.impl.ICommonsList
import com.helger.commons.collection.impl.ICommonsSet
import com.helger.commons.state.EChange

class TradingChannelFactory(
  private val repository: TradingChannelRepository
) : AbstractDynamicComponent(), IPartnershipFactory {

  override fun addPartnership(partnership: Partnership): EChange {
    TODO("Not yet implemented")
  }

  override fun getAllPartnershipNames(): ICommonsSet<String> {
    TODO("Not yet implemented")
  }

  override fun getAllPartnerships(): ICommonsList<Partnership> {
    TODO("Not yet implemented")
  }

  override fun removePartnership(partnership: Partnership): EChange {
    TODO("Not yet implemented")
  }

  override fun getPartnershipByName(name: String?): Partnership? {
    TODO("Not yet implemented")
  }

  override fun initDynamicComponent(aSession: IAS2Session, params: IStringMap?) {
    TODO("Not yet implemented")
  }

  override fun updatePartnership(aMsg: IMessage, overwrite: Boolean) {
    TODO("Not yet implemented")
  }

  override fun updatePartnership(aMdn: IMessageMDN, overwrite: Boolean) {
    TODO("Not yet implemented")
  }

  override fun getPartnership(partnership: Partnership): Partnership {
    TODO("Not yet implemented")
  }

}

