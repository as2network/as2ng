package com.freighttrust.as2.utils

import com.freighttrust.dis.dsl.messageBody
import com.freighttrust.dis.dsl.messageEnvelope
import com.freighttrust.dis.dsl.messageHeader
import gov.dhs.cbp.dis.CBPRequest
import gov.dhs.cbp.dis.DocumentSubmissionPackage
import gov.dhs.cbp.dis.PackageIdentifierType

object DISMessageEnvelopeGenerator {

  val emptyMessageEnvelope = messageEnvelope {
    messageHeader = messageHeader {}
    messageBody = messageBody {}
  }

  object DocumentSubmission {

    fun create(): DocumentSubmissionPackage =
      DocumentSubmissionPackage().apply {
        actionCode = ""
        cbpRequest = CBPRequest()
        packageIdentifier = PackageIdentifierType()
      }
  }
}
