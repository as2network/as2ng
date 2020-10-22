package com.freighttrust.common.util

sealed class Either<out T> {
  data class Error(val message: String?, val e: Exception) : Either<Nothing>()
  data class Success<T>(val value: T) : Either<T>()
}
