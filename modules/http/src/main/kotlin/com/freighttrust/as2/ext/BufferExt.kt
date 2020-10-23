package com.freighttrust.as2.ext

import com.helger.as2lib.util.AS2IOHelper
import io.netty.buffer.ByteBufInputStream
import io.vertx.core.buffer.Buffer
import java.io.InputStream
import java.io.OutputStream
import javax.activation.DataSource

fun Buffer.dataSource(contentType: String, contentTransferEncoding: String): DataSource =
  this.let { buffer ->
    object : DataSource {

      override fun getName(): String = "Buffer data source"

      override fun getContentType(): String = contentType

      override fun getOutputStream(): OutputStream = throw UnsupportedOperationException()

      override fun getInputStream(): InputStream =
        AS2IOHelper.getContentTransferEncodingAwareInputStream(
          ByteBufInputStream(buffer.byteBuf, false),
          contentTransferEncoding
        )
    }
  }

fun okio.Buffer.dataSource(contentType: String, contentTransferEncoding: String): DataSource =
  this.let { buffer ->

    object : DataSource {

      override fun getName(): String = "OKIO Buffer data source"

      override fun getContentType(): String = contentType

      override fun getOutputStream(): OutputStream = throw UnsupportedOperationException()

      override fun getInputStream(): InputStream =
        AS2IOHelper.getContentTransferEncodingAwareInputStream(
          buffer.inputStream(),
          contentTransferEncoding
        )
    }

  }
