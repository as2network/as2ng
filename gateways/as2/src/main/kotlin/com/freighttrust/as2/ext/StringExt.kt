package com.freighttrust.as2.ext

import com.freighttrust.as2.domain.Disposition

fun String.disposition() = Disposition.parse(this)
