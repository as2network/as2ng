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

    val regex = Regex("(manual-action|automatic-action)/(MDN-sent-manually|MDN-sent-automatically); (processed|failed)(/(error|warning|failure): (.*))?")

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

    fun automaticFailure(reason: String) = Disposition(
      DispositionActionMode.AutomaticAction,
      DispositionSendingMode.SentAutomatically,
      DispositionType.Failed,
      DispositionModifier.Failure,
      reason
    )

    fun automaticError(reason: String) = Disposition(
      DispositionActionMode.AutomaticAction,
      DispositionSendingMode.SentAutomatically,
      DispositionType.Failed,
      DispositionModifier.Error,
      reason
    )

  }

  override fun toString(): String = StringBuilder()
    .apply {
      append("${actionMode.key}/${sendingMode.key}; ${type.key}")

      // TODO enforce only one error type is set
      if (modifier != null) append("/${modifier.key}")
      if (modifierText != null) append(": $modifierText")

    }.toString()
}

data class DispositionNotification(
  val originalMessageId: String,
  val originalRecipient: String,
  val finalRecipient: String,
  val reportingUA: String,
  val disposition: Disposition,
  val receivedContentMic: String?,
  val digestAlgorithmId: String?
)
