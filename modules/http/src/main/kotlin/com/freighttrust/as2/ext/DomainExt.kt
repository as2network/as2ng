package com.freighttrust.as2.ext

import com.freighttrust.as2.fb.As2Mdn
import com.freighttrust.as2.fb.As2Message
import com.freighttrust.as2.fb.KeyValue
import com.google.common.io.ByteStreams
import com.google.flatbuffers.FlatBufferBuilder
import com.helger.as2lib.message.IBaseMessage
import com.helger.as2lib.message.IMessage
import com.helger.as2lib.message.IMessageMDN
import com.helger.commons.http.CHttpHeader

private val extractedHeaders = setOf(
  CHttpHeader.MESSAGE_ID,
  CHttpHeader.AS2_FROM,
  CHttpHeader.AS2_TO,
  CHttpHeader.SUBJECT,
  CHttpHeader.CONTENT_TYPE,
  CHttpHeader.CONTENT_DISPOSITION
)

fun IMessageMDN.toFlatBuffer(bb: FlatBufferBuilder, parent: IMessage): Int {

  val headersOffset = bb.createVectorOfTables(writeHeaders(this, extractedHeaders, bb))
  val attributesOffset = bb.createVectorOfTables(writeAttrs(this, bb))

  val idOffset = bb.createString(messageID)
  val messageIdOffset = bb.createString(parent.messageID)
  val textOffset = bb.createString(text)

  val dataBytes = ByteStreams.toByteArray(data!!.inputStream)
  val dataOffset = bb.createByteVector(dataBytes)

  As2Mdn.startAs2Mdn(bb)

  As2Mdn.addId(bb, idOffset)
  As2Mdn.addMessageId(bb, messageIdOffset)
  As2Mdn.addText(bb, textOffset)
  As2Mdn.addHeaders(bb, headersOffset)
  As2Mdn.addAttributes(bb, attributesOffset)
  As2Mdn.addData(bb, dataOffset)

  return As2Mdn.endAs2Mdn(bb)
}

fun IMessage.toFlatBuffer(bb: FlatBufferBuilder): Int {

  val headersOffset = bb.createVectorOfTables(writeHeaders(this, extractedHeaders, bb))
  val attributesOffset = bb.createVectorOfTables(writeAttrs(this, bb))

  val idOffset = bb.createString(messageID)
  val fromOffset = bb.createString(aS2From)
  val toOffset = bb.createString(aS2To)
  val subjectOffset = bb.createString(subject)
  val contentTypeOffset = bb.createString(contentType)
  val contentDispositionOffset = bb.createString(contentDisposition)

  val dataBytes = ByteStreams.toByteArray(data!!.inputStream)
  val dataOffset = bb.createByteVector(dataBytes)

  As2Message.startAs2Message(bb)

  As2Message.addId(bb, idOffset)
  As2Message.addFrom(bb, fromOffset)
  As2Message.addTo(bb, toOffset)
  As2Message.addSubject(bb, subjectOffset)
  As2Message.addContentType(bb, contentTypeOffset)
  As2Message.addContentDisposition(bb, contentDispositionOffset)
  As2Message.addHeaders(bb, headersOffset)
  As2Message.addAttributes(bb, attributesOffset)
  As2Message.addData(bb, dataOffset)

  return As2Message.endAs2Message(bb)
}

private fun writeHeaders(
  message: IBaseMessage,
  headersToFilter: Set<String>,
  bb: FlatBufferBuilder
): IntArray =
  message.headers()
    .filter { !headersToFilter.contains(it.key) }
    .map { entry ->
      val keyOffset = bb.createString(entry.key)

      val valueOffsets = entry.value.map { bb.createString(it) }
      val valueOffset = bb.createVectorOfTables(valueOffsets.toIntArray())

      KeyValue.startKeyValue(bb)

      KeyValue.addKey(bb, keyOffset)
      KeyValue.addValue(bb, valueOffset)

      KeyValue.endKeyValue(bb)
    }.toIntArray()

private fun writeAttrs(
  message: IBaseMessage,
  bb: FlatBufferBuilder
): IntArray =
  message.attrs()
    .entries
    .map { entry ->

      val keyOffset = bb.createString(entry.key)

      val valueOffsets = listOf(entry.value).map { bb.createString(it) }
      val valueOffset = bb.createVectorOfTables(valueOffsets.toIntArray())

      KeyValue.startKeyValue(bb)

      KeyValue.addKey(bb, keyOffset)
      KeyValue.addValue(bb, valueOffset)

      KeyValue.endKeyValue(bb)
    }.toIntArray()
