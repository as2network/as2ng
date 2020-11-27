package network.as2.server.handlers.edi

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext
import io.xlate.edi.stream.EDIInputFactory
import network.as2.server.domain.As2RequestContext
import network.as2.server.domain.Disposition
import network.as2.server.exceptions.DispositionException
import network.as2.server.handlers.as2Context
import network.as2.server.util.EdiFact
import network.as2.server.util.EdiX12
import org.apache.http.HttpHeaders.CONTENT_TYPE
import javax.activation.DataHandler
import javax.mail.internet.MimeBodyPart
import javax.mail.util.ByteArrayDataSource

class EdiValidationHandlerSpec : BehaviorSpec() {

  val handler = EDIValidationHandler(EDIInputFactory.newFactory())

  init {

    // common mocks and dependencies
    val vertx = Vertx.vertx()

    val ctx = mockk<RoutingContext>()
    val as2Ctx = mockk<As2RequestContext>()

    every { ctx.vertx() } returns vertx
    justRun { ctx.next() }
    every { ctx.as2Context } returns as2Ctx

    given("a valid edifact message") {

      val dataHandler = DataHandler(
        ByteArrayDataSource(
          EdiFact.sampleInvoice.toByteArray(),
          "application/edifact"
        )
      )

      every { as2Ctx.body } returns MimeBodyPart()
        .apply {
          setDataHandler(dataHandler)
          setHeader(CONTENT_TYPE, dataHandler.contentType)
        }

      `when`("we attempt to validate it") {
        handler.coroutineHandle(ctx)

        then("the handler should call ctx.next()") {
          verify { ctx.next() }
        }
      }

    }

    given("an invalid edifact message") {

      val dataHandler = DataHandler(
        ByteArrayDataSource(
          "Foo bar",
          "application/edifact"
        )
      )

      every { as2Ctx.body } returns MimeBodyPart()
        .apply {
          setDataHandler(dataHandler)
          setHeader(CONTENT_TYPE, dataHandler.contentType)
        }

      `when`("we attempt to validate it") {
        then("it should throw a disposition exception") {
          val exception = shouldThrow<DispositionException> {
            handler.coroutineHandle(ctx)
          }
          with(exception) {
            text shouldBe "Invalid EDI content found"
            disposition shouldBe Disposition.automaticError("invalid-edi")
          }
        }
      }
    }

    given("a valid edi x12 message") {

      val dataHandler = DataHandler(
        ByteArrayDataSource(
          EdiX12.sampleInvoice.toByteArray(),
          "application/edi-x12"
        )
      )

      every { as2Ctx.body } returns MimeBodyPart()
        .apply {
          setDataHandler(dataHandler)
          setHeader(CONTENT_TYPE, dataHandler.contentType)
        }

      `when`("we attempt to validate it") {
        handler.handle(ctx)

        then("the handler should call ctx.next()") {
          verify { ctx.next() }
        }
      }

    }

    given("an invalid edi x12 message") {

      val dataHandler = DataHandler(
        ByteArrayDataSource(
          "Foo bar",
          "application/edi-x12"
        )
      )

      every { as2Ctx.body } returns MimeBodyPart()
        .apply {
          setDataHandler(dataHandler)
          setHeader(CONTENT_TYPE, dataHandler.contentType)
        }

      `when`("we attempt to validate it") {
        then("it should throw a disposition exception") {
          val exception = shouldThrow<DispositionException> {
            handler.coroutineHandle(ctx)
          }
          with(exception) {
            text shouldBe "Invalid EDI content found"
            disposition shouldBe Disposition.automaticError("invalid-edi")
          }
        }
      }
    }

  }

}
