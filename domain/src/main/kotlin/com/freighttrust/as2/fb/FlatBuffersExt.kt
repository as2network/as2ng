package com.freighttrust.as2.fb

import com.google.common.base.MoreObjects

fun As2Mdn.toNiceString(): String =
  MoreObjects
    .toStringHelper("As2Mdn")
    .add("id", id())
    .add("messageId", messageId())
    .add("text", text())
    .toString()

fun As2Message.toNiceString(): String =
  MoreObjects
    .toStringHelper("As2Message")
    .add("id", id())
    .add("from", from())
    .add("to", to())
    .add("subject", subject())
    .add("contentType", contentType())
    .add("contentDisposition", contentDisposition())
    .toString()
