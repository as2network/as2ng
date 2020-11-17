package com.freighttrust.testing.generators

import com.github.javafaker.Faker
import io.kotest.property.arbitrary.arbitrary
import javax.activation.DataHandler
import javax.mail.util.ByteArrayDataSource
import kotlin.random.asJavaRandom

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
