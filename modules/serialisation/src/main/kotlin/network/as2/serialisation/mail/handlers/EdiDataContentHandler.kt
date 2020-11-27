package network.as2.serialisation.mail.handlers

import com.sun.mail.handlers.text_plain
import java.io.OutputStream
import javax.activation.ActivationDataFlavor
import javax.activation.DataSource

class EdiDataContentHandler : text_plain() {

  override fun getDataFlavors() = arrayOf(
    ActivationDataFlavor(String::class.java, "application/edifact", "EDIFACT"),
    ActivationDataFlavor(String::class.java, "application/edi-x12", "EDI X12")
  )


  override fun writeTo(obj: Any?, type: String?, os: OutputStream?) {
    super.writeTo(obj, type, os)
  }

  override fun getContent(ds: DataSource?): Any {
    return super.getContent(ds)
  }
}
