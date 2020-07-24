package com.freighttrust.as2.domain

data class DispositionNotification(
  val originalMessageId: String,
  val originalRecipient: String,
  val finalRecipient: String,
  val reportingUA: String,
  val disposition: String,
  val receivedContentMic: String?
)
