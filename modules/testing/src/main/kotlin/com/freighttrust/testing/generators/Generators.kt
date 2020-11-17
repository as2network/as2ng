package com.freighttrust.testing.generators

import com.github.javafaker.Faker
import io.kotest.property.arbitrary.arbitrary
import javax.activation.DataHandler
import javax.mail.util.ByteArrayDataSource
import kotlin.random.asJavaRandom

private val bytesGenerator = { minSize: Int, maxSize: Int ->
  arbitrary { rs ->
    // random number of bytes between 1mb and 10mb
    val size = rs.random.nextInt(1024 * 1024, 1024 * 1024 * 10)
    ByteArray(size)
      .apply { rs.random.nextBytes(this) }
  }
}

val textGenerator = { minWordCount: Int, maxWordCount: Int ->
  arbitrary { rs ->

    val wordCount = rs.random.nextInt(minWordCount, maxWordCount)

    Faker(rs.random.asJavaRandom())
      .lorem()
      .sentence(wordCount)
  }
}

val textDataSourceGenerator = { minWordCount: Int, maxWordCount: Int ->
  arbitrary { rs ->
    ByteArrayDataSource(
      textGenerator(minWordCount, maxWordCount)
        .sample(rs)
        .value,
      "application/text"
    )
  }
}

val textDataHandlerGenerator = { minWordCount: Int, maxWordCount: Int ->
  arbitrary { rs ->
    DataHandler(
      textDataSourceGenerator(minWordCount, maxWordCount)
        .sample(rs)
        .value
    )
  }
}

private val dataSourceGenerator = { minSize: Int, maxSize: Int ->
  arbitrary { rs ->
    ByteArrayDataSource(bytesGenerator(minSize, maxSize).sample(rs).value, "application/foo")
  }
}

private val dataHandlerGenerator = { minSize: Int, maxSize: Int ->
  arbitrary { rs ->
    DataHandler(dataSourceGenerator(minSize, maxSize).sample(rs).value)
  }
}
