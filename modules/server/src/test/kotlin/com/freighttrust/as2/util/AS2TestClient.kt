package com.freighttrust.as2.util

import com.helger.as2lib.client.AS2Client
import com.helger.as2lib.client.AS2ClientSettings
import com.helger.as2lib.message.IMessage
import com.helger.as2lib.session.AS2Session

class As2TestClient : AS2Client() {

  var includeSigningCertificateInBody = true

  override fun beforeSend(settings: AS2ClientSettings, session: AS2Session, message: IMessage) {
    session.isCryptoSignIncludeCertificateInBodyPart = includeSigningCertificateInBody
  }
}
