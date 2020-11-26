package network.as2.server.extensions

import network.as2.server.ext.dataSource
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.commons.http.CHttpHeader
import io.vertx.core.http.HttpHeaders
import okhttp3.mockwebserver.RecordedRequest
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.mail.internet.MimeBodyPart

fun RecordedRequest.bodyDataSource(): DataSource =
  body.dataSource(
    headers[HttpHeaders.CONTENT_TYPE.toString()] ?: throw Error("Content-Type not found"),
    headers[HttpHeaders.CONTENT_TRANSFER_ENCODING.toString()] ?: throw Error("Content-Transfer-Encoding not found")
  )


fun RecordedRequest.bodyAsMimeBodyPart() =
  MimeBodyPart()
    .let { bodyPart ->

      val receivedContentType = AS2HttpHelper
        // TODO look into exactly how content type might be encoded
        .getCleanContentType(headers[HttpHeaders.CONTENT_TYPE.toString()])

      bodyPart.dataHandler = DataHandler(bodyDataSource())
      // Header must be set AFTER the DataHandler!
      bodyPart.setHeader(CHttpHeader.CONTENT_TYPE, receivedContentType)

      bodyPart
    }
