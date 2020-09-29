package com.freighttrust.as2.util

import com.freighttrust.as2.domain.Message
import com.helger.as2lib.disposition.AS2DispositionException
import com.helger.as2lib.disposition.DispositionType
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule
import org.bouncycastle.cms.CMSException
import org.bouncycastle.cms.jcajce.ZlibExpanderProvider
import org.bouncycastle.mail.smime.SMIMECompressedParser
import org.bouncycastle.mail.smime.SMIMEException
import org.bouncycastle.mail.smime.SMIMEUtil
import org.slf4j.LoggerFactory
import javax.mail.MessagingException

object CompressionUtil {

  private val logger = LoggerFactory.getLogger(CompressionUtil::class.java)

  fun decompress(message: Message, tempFileHelper: TempFileHelper): Message =
    try {

      val bodyPart = message.body

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
      val decompressedBodyPart =
        SMIMECompressedParser(bodyPart, 8 * 1024)
          .let { parser -> parser.getContent(ZlibExpanderProvider()) }
          .let { typedStream -> SMIMEUtil.toMimeBodyPart(typedStream, tempFileHelper.newFile()) }
          .also {
            logger.info("Successfully decompressed incoming AS2 message")
          }

      message.copy(
        body = decompressedBodyPart,
        context = message.context.copy(decompressedBody = Pair(decompressedBodyPart, "zlib"))
      )

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
