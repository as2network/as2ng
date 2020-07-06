package com.freighttrust.as2.processor.module

import com.freighttrust.db.extensions.toJSONB
import com.freighttrust.db.repositories.As2MdnRepository
import com.freighttrust.db.repositories.As2MessageRepository
import com.freighttrust.jooq.tables.records.As2MessageRecord
import com.helger.as2lib.message.AS2Message
import com.helger.as2lib.message.IMessage
import com.helger.as2lib.processor.module.AbstractProcessorModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule.DO_STORE
import com.helger.as2lib.processor.storage.IProcessorStorageModule.DO_STOREMDN
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.as2lib.util.http.IAS2HttpResponseHandler
import com.helger.commons.http.CHttp
import com.helger.commons.http.HttpHeaderMap
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream
import com.helger.commons.io.stream.StreamHelper
import io.vertx.core.MultiMap
import io.vertx.ext.web.client.WebClient
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.jooq.tools.json.JSONObject
import javax.mail.internet.MimeBodyPart

fun AS2Message.toAs2MessageRecord(): As2MessageRecord {
  val self = this
  return As2MessageRecord()
    .apply {
      id = self.messageID
      from = self.aS2From
      to = self.aS2To
      subject = self.subject
      contenttype = self.contentType
      contentdisposition = self.contentDisposition
      headers = JSONObject(
        self.headers()
          .map { h -> h.key to h.value }
          .toMap()
      ).toJSONB()
      attributes = JSONObject(
        self.attrs()
          .map { a -> a.key to a.value }
          .toMap()
      ).toJSONB()
    }
}

fun MultiMap.toHttpHeaderMap(): HttpHeaderMap {
  val map = HttpHeaderMap()
  forEach { e -> map.addHeader(e.key, e.value) }
  return map
}

fun Headers.toHttpHeaderMap(): HttpHeaderMap {
  val map = HttpHeaderMap()
  forEach { e -> map.addHeader(e.first, e.second) }
  return map
}

class ProxyProcessorModule(
  private val as2MessageRepository: As2MessageRepository,
  private val as2MdnRepository: As2MdnRepository,
  private val webClient: WebClient
) : AbstractProcessorModule(), IProcessorStorageModule {

  private val okHttpClient = OkHttpClient()

  override fun canHandle(action: String, msg: IMessage, options: MutableMap<String, Any>?): Boolean =
    supportedActions.contains(action)

  override fun handle(action: String, msg: IMessage, options: MutableMap<String, Any>?) {
    when (action) {
      DO_STORE -> {
        as2MessageRepository.insert((msg as AS2Message).toAs2MessageRecord())

        val url = msg.partnership().aS2URL!!
        val data = msg.data!!.rawInputStream.readAllBytes()
        val mediaType = msg.data!!.contentType.toMediaType()

        val body = data.toRequestBody(mediaType)

        val request = Request.Builder()
          .url(url)
          .apply {
            val headers = msg.headers().allHeaders
            headers.forEach { h -> header(h.key, h.value.first!!) }
          }
          .post(body)
          .apply {
            removeHeader("Content-Length")
          }
          .build()

        okHttpClient
          .newCall(request)
          .execute()
          .use { response ->
            when {
              response.isSuccessful -> {
                if (!msg.isRequestingMDN) return

                when {
                  // As the Socket is open, we are forced to use the ResponseHandler directly to send back
                  // the information to the client
                  !msg.isRequestingAsynchMDN -> {

                    // If the request is synchronous we can send directly using the open connection
                    NonBlockingByteArrayOutputStream().use { data ->
                      val headers = response.headers.toHttpHeaderMap()

                      val inputStream = MimeBodyPart(
                        AS2HttpHelper.getAsInternetHeaders(headers),
                        response.body!!.bytes()
                      ).inputStream
                      StreamHelper.copyInputStreamToOutputStream(inputStream, data)
                      headers.setContentLength(data.size().toLong())

                      // start HTTP response
                      val responseHandler = options!!["ResponseHandler"] as IAS2HttpResponseHandler
                      responseHandler.sendHttpResponse(CHttp.HTTP_OK, headers, data)
                    }
                  }
                }
              }
              else -> {
              }
            }
          }

        // val latch = CountDownLatch(1)
        // webClient
        //   .postAbs(msg.partnership().aS2URL)
        //   .apply {
        //     msg.headers().allHeaders.forEach { h -> headers()[h.key] = h.value.first }
        //   }
        //   .sendBuffer(Buffer.buffer(data)) { res ->
        //     when {
        //       res.succeeded() -> {
        //         val raw = res.result()
        //
        //         // as2MdnRepository.insert(mdn.toAs2MdnRecord())
        //
        //         // TODO: Check if the AS2 request is synchronous or not and return directly the answer
        //         if (!msg.isRequestingMDN) return@sendBuffer
        //
        //         when {
        //           // As the Socket is open, we are forced to use the ResponseHandler directly to send back
        //           // the information to the client
        //           !msg.isRequestingAsynchMDN -> {
        //
        //             // If the request is synchronous we can send directly using the open connection
        //             NonBlockingByteArrayOutputStream().use { outputStream ->
        //               val headers = raw.headers().toHttpHeaderMap()
        //
        //               val inputStream = MimeBodyPart(
        //                 AS2HttpHelper.getAsInternetHeaders(headers),
        //                 raw.bodyAsBuffer().bytes
        //               ).inputStream
        //               StreamHelper.copyInputStreamToOutputStream(inputStream, outputStream)
        //               headers.setContentLength(outputStream.size().toLong())
        //
        //               // start HTTP response
        //               val responseHandler = options!!["ResponseHandler"] as IAS2HttpResponseHandler
        //               responseHandler.sendHttpResponse(CHttp.HTTP_OK, headers, outputStream)
        //             }
        //           }
        //         }
        //
        //         latch.countDown()
        //       }
        //       res.failed() -> {
        //         val cause = res.cause()
        //       }
        //     }
        //   }
        //
        // latch.await()
      }
      // DO_STOREMDN -> {
      //   as2MdnRepository.insert((msg.mdn as AS2MessageMDN).toAs2MdnRecord())
      // }
      else -> throw IllegalStateException("Unhandled action type: $action")
    }
  }

  companion object {

    val supportedActions = setOf(DO_STORE, DO_STOREMDN)
  }
}
