package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.contentLength
import com.freighttrust.as2.ext.contentType
import com.freighttrust.as2.ext.isMultipart
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.RoutingContext
import java.io.*
import java.lang.UnsupportedOperationException
import java.nio.channels.FileChannel
import javax.activation.DataSource
import javax.mail.util.SharedByteArrayInputStream
import javax.mail.util.SharedFileInputStream

class As2BodyHandler : Handler<RoutingContext> {

  override fun handle(ctx: RoutingContext) {

    val fileOutputStreamFactory = {
      ctx.newTempFile()
        .let { file ->
          Pair(file, FileOutputStream(file))
        }
    }

    ctx.request()
      .let { request ->

        val (file, outputStream) = request
          .contentLength()
          ?.let { length ->
            // 1 mb
            if (length > (1024 * 1024)) {
              fileOutputStreamFactory()
            } else {
              Pair(null, ByteArrayOutputStream(length))
            }
          } ?: fileOutputStreamFactory()


        // drain the body into the output stream

        var size = 0L

        request.handler { buffer ->
          outputStream.write(buffer.bytes)
          size += buffer.length()
        }

        // when finished
        request.endHandler {

          outputStream.close()

          val byteBuffer =
            when (outputStream) {
              is ByteArrayOutputStream ->
                Unpooled.wrappedBuffer(outputStream.toByteArray())
              is FileOutputStream ->
                Unpooled.wrappedBuffer(
                  RandomAccessFile(file, "r")
                    .channel
                    .map(FileChannel.MapMode.READ_ONLY, 0, size)
                )
              else -> throw IllegalStateException()
            }

          ctx.body = Buffer.buffer(byteBuffer)

          ctx.next()
        }

      }
  }

}
