package com.freighttrust.dis.dsl

import gov.dhs.cbp.dis.MessageBody
import gov.dhs.cbp.dis.MessageEnvelope
import gov.dhs.cbp.dis.MessageHeader

fun messageEnvelope(action: MessageEnvelope.() -> Unit): MessageEnvelope {
  val me = MessageEnvelope()
  me.action()
  return me
}

fun messageHeader(action: MessageHeader.() -> Unit): MessageHeader {
  val mh = MessageHeader()
  mh.action()
  return mh
}

fun messageBody(action: MessageBody.() -> Unit): MessageBody {
  val mb = MessageBody()
  mb.action()
  return mb
}
