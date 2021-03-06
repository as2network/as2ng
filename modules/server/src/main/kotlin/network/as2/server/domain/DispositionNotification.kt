package network.as2.server.domain

import network.as2.server.ext.calculateMic
import network.as2.server.ext.getAs2Header
import network.as2.server.ext.setAs2Header
import network.as2.server.handlers.as2Context
import network.as2.server.util.AS2Header
import com.helger.as2lib.util.AS2IOHelper
import com.helger.commons.http.CHttp
import com.helger.commons.http.CHttpHeader
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext
import network.as2.jooq.tables.pojos.DispositionNotification
import java.text.ParseException
import javax.mail.internet.InternetHeaders
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMultipart

/**
 * Use an action-mode of "automatic-action" when the disposition
 * described by the disposition type was a result of an automatic
 * action rather than that of an explicit instruction by the user for
 * this message
 *
 * Use an action-mode of "manual-action" when the disposition
 * described by the disposition type was a result of an explicit
 * instruction by the user rather than some sort of automatically
 * performed action.
 */
enum class DispositionActionMode(val key: String) {

  ManualAction("manual-action"),
  AutomaticAction("automatic-action");

  companion object {
    fun parse(str: String) = values().firstOrNull { it.key == str }
  }
}

/**
 * Use a sending-mode of "MDN-sent-automatically" when the MDN is
 * sent because the UA had previously been configured to do so.
 *
 * Use a sending-mode of "MDN-sent-manually" when the user explicitly
 * gave permission for this particular MDN to be sent.
 *
 * The sending-mode "MDN-sent-manually" is meaningful ONLY with
 * "manual-action", not with "automatic-action".
 */
enum class DispositionSendingMode(val key: String) {

  SentAutomatically("MDN-sent-automatically"),
  SentManually("MDN-sent-manually");

  companion object {
    fun parse(str: String) = values().firstOrNull { it.key == str }
  }
}

/**
 * The "failed" disposition type MUST NOT be used for the situation
 * in which there is some problem in processing the message other
 * than interpreting the request for an MDN.  The "processed" or
 * other disposition type with appropriate disposition modifiers is
 * to be used in such situations.
 */
enum class DispositionType(val key: String) {

  Processed("processed"),
  Failed("failed");

  companion object {
    fun parse(str: String) = values().firstOrNull { it.key == str }
  }
}

enum class DispositionModifier(val key: String) {

  Error("error"),
  Warning("warning"),
  Failure("failure");

  companion object {
    fun parse(str: String) = values().firstOrNull { it.key == str }
  }
}

data class Disposition(
  val actionMode: DispositionActionMode,
  val sendingMode: DispositionSendingMode,
  val type: DispositionType,
  val modifier: DispositionModifier? = null,
  val modifierText: String? = null
) {

  companion object {

    val regex =
      Regex("(manual-action|automatic-action)/(MDN-sent-manually|MDN-sent-automatically); (processed|failed)(/(error|warning|failure): (.*))?")

    fun parse(str: String): Disposition {
      val matches = regex.findAll(str).toList()
      if (matches.isEmpty()) throw ParseException("Malformed disposition string", 0)
      matches[0]
        .let { match ->
          val actionMode = DispositionActionMode.parse(match.groups[1]!!.value)!!
          val sendingMode = DispositionSendingMode.parse(match.groups[2]!!.value)!!
          val type = DispositionType.parse(match.groups[3]!!.value)!!

          val (modifierStr, modifierText) = Pair(
            match.groups[5]?.value,
            match.groups[6]?.value
          )

          val modifier = DispositionModifier.values().find { it.key == modifierStr }

          return Disposition(actionMode, sendingMode, type, modifier, modifierText)
        }
    }

    val automaticProcessed = Disposition(
      DispositionActionMode.AutomaticAction,
      DispositionSendingMode.SentAutomatically,
      DispositionType.Processed
    )

    /**
     * The "failed" AS2-disposition-type MUST be used when a failure occurs
     * that prevents the proper generation of an MDN.  For example, this
     * disposition-type would apply if the sender of the message requested
     * the application of an unsupported message-integrity-check (MIC)
     * algorithm.
     *
     * The "failure:" AS2-disposition-modifier-extension SHOULD be used with
     * an implementation-defined description of the failure.  Further
     * information about the failure may be contained in a failure-field.
     */
    fun automaticFailure(reason: String) = Disposition(
      DispositionActionMode.AutomaticAction,
      DispositionSendingMode.SentAutomatically,
      DispositionType.Failed,
      DispositionModifier.Failure,
      reason
    )

    /**
     * When errors occur in processing the received message (other than
     * content), the "disposition-field" MUST be set to the "processed"
     * value for disposition-type and the "error" value for disposition-
     * modifier.
     *
     * The "error" AS2-disposition-modifier with the "processed"
     * disposition-type MUST be used to indicate that an error of some sort
     * occurred that prevented successful processing of the message.
     * Further information may be contained in an error-field.
     *
     * An "error:" AS2-disposition-modifier-extension SHOULD be used to
     * combine the indication of an error with a predefined description of a
     * specific, well-known error.  Further information about the error may
     * be contained in an error field.
     *
     * For internet EDI use, the following "error" AS2-disposition-modifier
     * values are defined:
     *
     * o "Error: decryption-failed"           - the receiver could not
     * decrypt the message
     * contents.
     *
     * o "Error: authentication-failed"       - the receiver could not
     * authenticate the sender.
     *
     * o "Error: integrity-check-failed"      - the receiver could not
     * verify content integrity.
     *
     * o "Error: unexpected-processing-error" - a catch-all for any
     * additional processing
     * errors.
     */
    fun automaticError(reason: String) = Disposition(
      DispositionActionMode.AutomaticAction,
      DispositionSendingMode.SentAutomatically,
      DispositionType.Processed,
      DispositionModifier.Error,
      reason
    )

    val automaticAuthenticationFailedError = automaticError("authentication-failed")
    val automaticDecompressionFailedError = automaticError("decompress-failed")
    val automaticDecryptionFailedError = automaticError("decryption-failed")
    val automaticInsufficientMessageSecurityError = automaticError("insufficient-message-security")
    val automaticIntegrityCheckFailedError = automaticError("integrity-check-failed")
    val automaticUnexpectedProcessingError = automaticError("unexpected-processing-error")
  }

  override fun toString(): String = StringBuilder()
    .apply {
      append("${actionMode.key}/${sendingMode.key}; ${type.key}")

      // TODO enforce only one error type is set
      if (modifier != null) append("/${modifier.key}")
      if (modifierText != null) append(": $modifierText")
    }.toString()
}

fun DispositionNotification.fromMimeBodyPart(bodyPart: MimeBodyPart): DispositionNotification =
  with(bodyPart) {

    require(isMimeType("multipart/report")) { "Must be a multipart/report body part" }

    val multipartBody = (content as MimeMultipart)

    var dispositionBodyPart: MimeBodyPart? = null

    for (idx in 0..multipartBody.count) {
      val part = multipartBody.getBodyPart(idx) as MimeBodyPart
      if (part.isMimeType("message/disposition-notification")) {
        dispositionBodyPart = part
        break
      }
    }

    requireNotNull(dispositionBodyPart) { "disposition body part not found" }

    dispositionBodyPart
      .getHeader(CHttpHeader.CONTENT_TRANSFER_ENCODING, null)
      .let { contentTransferEncoding ->
        AS2IOHelper.getContentTransferEncodingAwareInputStream(
          dispositionBodyPart.inputStream,
          contentTransferEncoding
        )
      }.use { inputStream ->

        InternetHeaders(inputStream)
          .let { headers ->

            this@fromMimeBodyPart.originalMessageId = headers.getAs2Header(AS2Header.OriginalMessageID)
            this@fromMimeBodyPart.originalRecipient = headers.getAs2Header(AS2Header.OriginalRecipient)
            this@fromMimeBodyPart.finalRecipient = headers.getAs2Header(AS2Header.FinalRecipient)
            this@fromMimeBodyPart.reportingUa = headers.getAs2Header(AS2Header.ReportingUA)
            this@fromMimeBodyPart.disposition = headers.getAs2Header(AS2Header.Disposition)
            this@fromMimeBodyPart.receivedContentMic = headers.getAs2Header(AS2Header.ReceivedContentMIC)
            this@fromMimeBodyPart.digestAlgorithm = headers.getAs2Header(AS2Header.DigestAlgorithmId)

            this@fromMimeBodyPart
          }
      }
  }


fun DispositionNotification.toMimeBodyPart(ctx: RoutingContext): MimeBodyPart =
  with(ctx.as2Context) {

    InternetHeaders()
      .apply {
        setAs2Header(AS2Header.ReportingUA, reportingUa)
        setAs2Header(AS2Header.OriginalRecipient, "rfc822; $originalRecipient")
        setAs2Header(AS2Header.FinalRecipient, "rfc822; $finalRecipient")
        setAs2Header(AS2Header.OriginalMessageID, originalMessageId)
        setAs2Header(AS2Header.Disposition, disposition.toString())

        val signingAlgorithm =
          dispositionNotificationOptions?.firstMICAlg ?: throw Error("Disposition notification options not set")

        val includeHeaders = with(bodyContext) { hasBeenDecrypted || hasBeenVerified || hasBeenDecompressed }

        signingAlgorithm.apply {
          val mic = body.calculateMic(includeHeaders, signingAlgorithm)
          setAs2Header(AS2Header.ReceivedContentMIC, mic)
        }
      }
      .let { headers ->
        val builder = StringBuilder()
        for (line in headers.allHeaderLines) {
          builder
            .append(line)
            .append(CHttp.EOL)
        }
        val content = builder.append(CHttp.EOL).toString()

        MimeBodyPart()
          .apply {
            setContent(content, "message/disposition-notification")
            // TODO is this necessary in addition to above?
            setHeader(HttpHeaders.CONTENT_TYPE.toString(), "message/disposition-notification")
          }
      }
  }
