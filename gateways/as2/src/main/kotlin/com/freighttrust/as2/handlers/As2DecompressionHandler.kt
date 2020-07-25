package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.ext.isCompressed
import com.helger.as2lib.disposition.AS2DispositionException
import com.helger.as2lib.disposition.DispositionType
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import org.bouncycastle.cms.CMSException
import org.bouncycastle.cms.jcajce.ZlibExpanderProvider
import org.bouncycastle.mail.smime.SMIMECompressedParser
import org.bouncycastle.mail.smime.SMIMEException
import org.bouncycastle.mail.smime.SMIMEUtil
import org.slf4j.LoggerFactory
import javax.mail.MessagingException
import javax.mail.internet.MimeBodyPart

class As2DecompressionHandler : Handler<RoutingContext> {

  private val logger = LoggerFactory.getLogger(As2DecompressionHandler::class.java)

  override fun handle(ctx: RoutingContext) {

    val as2Context = ctx.as2Context()

    val bodyPart = as2Context.bodyPart
    val decompressedContentType = as2Context.decompressedContentType

    // TODO improve error handling

    // As defined by RFC5402 compression is always before encryption but can be before
    // or after signing of message, but only in one place

    when {

      decompressedContentType != null && as2Context.bodyPart.isCompressed() ->
        throw Error("Decompression has already occurred")

      // TODO configurable decompression override from trading channel
      bodyPart.isCompressed() ->
        decompress(ctx, bodyPart)
          .also { decompressedBodyPart ->

            as2Context.bodyPart = decompressedBodyPart
            as2Context.decompressedContentType = decompressedBodyPart.contentType

          }
    }

    ctx.next()
  }

  @Throws(AS2DispositionException::class)

  private fun decompress(ctx: RoutingContext, bodyPart: MimeBodyPart): MimeBodyPart {
    try {

      logger.debug("Decompressing a compressed AS2 message")

      // Compress using stream
      if (logger.isDebugEnabled) {

        val str = StringBuilder()
          .apply {
            append("Headers before uncompress\n")
            bodyPart
              .allHeaderLines
              .toList()
              .forEach { str -> append("$str\n") }
            append("done")
          }.toString()

        logger.debug(str)
      }

      // TODO: get buffer from configuration
      return SMIMECompressedParser(bodyPart, 8 * 1024)
        .let { parser -> parser.getContent(ZlibExpanderProvider()) }
        .let { typedStream -> SMIMEUtil.toMimeBodyPart(typedStream, ctx.newTempFile()) }
        .also {
          logger.info("Successfully decompressed incoming AS2 message")
        }

    } catch (ex: SMIMEException) {
      logger.error("Error decompressing received message", ex)
      throw AS2DispositionException(
        DispositionType.createError("unexpected-processing-error"),
        AbstractActiveNetModule.DISP_DECOMPRESSION_ERROR,
        ex
      )
    } catch (ex: CMSException) {
      logger.error("Error decompressing received message", ex)
      throw AS2DispositionException(
        DispositionType.createError("unexpected-processing-error"),
        AbstractActiveNetModule.DISP_DECOMPRESSION_ERROR,
        ex
      )
    } catch (ex: MessagingException) {
      logger.error("Error decompressing received message", ex)
      throw AS2DispositionException(
        DispositionType.createError("unexpected-processing-error"),
        AbstractActiveNetModule.DISP_DECOMPRESSION_ERROR,
        ex
      )
    }
  }
}
