package com.freighttrust.as2.ext

import com.freighttrust.as2.util.AS2Header
import io.vertx.core.MultiMap

fun MultiMap.contains(header: AS2Header): Boolean = contains(header.key)

fun MultiMap.get(header: AS2Header): String? = get(header.key) as String?

fun MultiMap.getAll(header: AS2Header): List<String>? = getAll(header.key) as List<String>?
