package network.as2.server.handlers

import network.as2.server.ext.contentLength
import io.netty.buffer.Unpooled
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.RoutingContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.channels.FileChannel

class BodyHandler : Handler<RoutingContext> {

  private fun newFileOutputStream(ctx: RoutingContext): Pair<File, FileOutputStream> =
    ctx.tempFileHelper.newFile()
      .let { file ->
        Pair(file, FileOutputStream(file))
      }

  override fun handle(ctx: RoutingContext) {

    val request = ctx.request()

    val (file, outputStream) = request.contentLength()
      ?.let { length ->
        // 1 mb
        if (length > (1024 * 1024)) {
          newFileOutputStream(ctx)
        } else {
          Pair(null, ByteArrayOutputStream(length))
        }
      } ?: newFileOutputStream(ctx)

    // drain the body into the output stream

    var size = 0L

    request.handler { buffer ->
      outputStream.write(buffer.bytes)
      size += buffer.length()
    }

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
