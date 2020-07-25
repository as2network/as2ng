package com.freighttrust.as2.domain

import java.text.ParseException

enum class DispositionActionMode(val key: String) {

  ManualAction("manual-action"),
  AutomaticAction("automatic-action");

  companion object {
    fun parse(str: String) = values().firstOrNull { it.key == str }
  }
}

enum class DispositionSendingMode(val key: String) {

  SentAutomatically("MDN-sent-automatically"),
  SentManually("MDN-sent-manually");

  companion object {
    fun parse(str: String) = values().firstOrNull { it.key == str }
  }

}

enum class DispositionType(val key: String) {

  Processed("processed"),
  Failed("failed");

  companion object {
    fun parse(str: String) = values().firstOrNull { it.key == str }
  }

}

enum class DispositionModifier(val key: String) {

  Error("error"),
  Warning("warning");

  companion object {
    fun parse(str: String) = values().firstOrNull { it.key == str }
  }

}


data class Disposition(
  val actionMode: DispositionActionMode,
  val sendingMode: DispositionSendingMode,
  val type: DispositionType,
  val error: String? = null,
  val warning: String? = null,
  val failure: String? = null
) {

  companion object {

    val regex = Regex("(manual\\-action|automatic\\-action)\\/(MDN\\-sent\\-manually|MDN\\-sent\\-automatically); (processed|failed)(\\/(error|warning|failure): (.*))?")

    fun parse(str: String): Disposition {
      val matches = regex.findAll(str).toList()
      if (matches.isEmpty()) throw ParseException("Malformed disposition string", 0)
      matches[0]
        .let { match ->
          val actionMode = DispositionActionMode.parse(match.groups[1]!!.value)!!
          val sendingMode = DispositionSendingMode.parse(match.groups[2]!!.value)!!
          val type = DispositionType.parse(match.groups[3]!!.value)!!

          val (typeExtension, message) = Pair(
            match.groups[5]?.value,
            match.groups[6]?.value
          )

          val error = if ("error" == typeExtension) message else null
          val warning = if ("warning" == typeExtension) message else null
          val failure = if ("failure" == typeExtension) message else null

          return Disposition(actionMode, sendingMode, type, error, warning, failure)
        }
    }

  }

}

data class DispositionNotification(
  val originalMessageId: String,
  val originalRecipient: String,
  val finalRecipient: String,
  val reportingUA: String,
  val disposition: Disposition,
  val receivedContentMic: String?
)
