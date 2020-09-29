package com.freighttrust.as2.domain

import io.vertx.core.MultiMap
import javax.mail.internet.MimeBodyPart

class MessageDispositionNotification(
  val headers: MultiMap,
  val body: MimeBodyPart
)
