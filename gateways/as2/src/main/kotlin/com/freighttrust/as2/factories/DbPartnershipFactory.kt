package com.freighttrust.as2.factories

import com.helger.as2lib.message.IMessage
import com.helger.as2lib.message.IMessageMDN
import com.helger.as2lib.partner.IPartnershipFactory
import com.helger.as2lib.partner.Partnership
import com.helger.as2lib.session.IAS2Session
import com.helger.commons.collection.attr.IStringMap
import com.helger.commons.collection.impl.ICommonsList
import com.helger.commons.collection.impl.ICommonsSet
import com.helger.commons.state.EChange
import org.jooq.DSLContext


class DbPartnershipFactory(
  private val dbContext: DSLContext
) : IPartnershipFactory {

  override fun attrs(): IStringMap {
    TODO("Not yet implemented")
  }

  override fun getName(): String? {
    TODO("Not yet implemented")
  }

  override fun addPartnership(aPartnership: Partnership): EChange {
    TODO("Not yet implemented")
  }

  override fun getAllPartnershipNames(): ICommonsSet<String> {
    TODO("Not yet implemented")
  }

  override fun getAllPartnerships(): ICommonsList<Partnership> {
    TODO("Not yet implemented")
  }

  override fun removePartnership(aPartnership: Partnership): EChange {
    TODO("Not yet implemented")
  }

  override fun getPartnershipByName(sName: String?): Partnership? {
    TODO("Not yet implemented")
  }

  override fun initDynamicComponent(aSession: IAS2Session, aParameters: IStringMap?) {
    TODO("Not yet implemented")
  }

  override fun updatePartnership(aMsg: IMessage, bOverwrite: Boolean) {
    TODO("Not yet implemented")
  }

  override fun updatePartnership(aMdn: IMessageMDN, bOverwrite: Boolean) {
    TODO("Not yet implemented")
  }

  override fun getSession(): IAS2Session {
    TODO("Not yet implemented")
  }

  override fun getPartnership(aPartnership: Partnership): Partnership {
    TODO("Not yet implemented")
  }

}

