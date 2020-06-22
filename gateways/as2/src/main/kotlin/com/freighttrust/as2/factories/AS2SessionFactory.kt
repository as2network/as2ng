package com.freighttrust.as2.factories

import com.freighttrust.as2.modules.JmsStorageProcessorModule
import com.helger.as2.app.cert.ServerCertificateFactory
import com.helger.as2.app.partner.ServerXMLPartnershipFactory
import com.helger.as2lib.processor.DefaultMessageProcessor
import com.helger.as2lib.processor.receiver.AS2MDNReceiverModule
import com.helger.as2lib.processor.receiver.AS2ReceiverModule
import com.helger.as2lib.session.AS2Session
import com.typesafe.config.Config
import java.io.File

object AS2SessionFactory {

  fun create(config: Config): AS2Session =
    object : AS2Session() {
      init {
        val self = this

        // Global attributes
        isCryptoVerifyUseCertificateInBodyPart = config.getBoolean("CryptoVerifyUseCertificateInBodyPartEnabled")
        isCryptoSignIncludeCertificateInBodyPart = config.getBoolean("CryptoSignIncludeCertificateInBodyPartEnabled")

        // Certificates
        certificateFactory = ServerCertificateFactory()
          .apply {
            keyStoreType = config.getString("KeystoreType")
            filename = File(config.getString("KeyStoreFilename")).absolutePath
            setPassword(config.getString("KeyStorePassword"))

            // interval attr needs to be specified with a StringMap
            attrs().add("interval", config.getInt("KeyStoreMonitoringInterval"))
          }

        // Processors
        messageProcessor = DefaultMessageProcessor()
          .apply {
            addModule(
              AS2ReceiverModule()
                .apply {
                  port = config.getInt("AS2ReceiverModulePort")
                  errorDirectory = config.getString("AS2ReceiverModuleErrorDir")
                  setErrorFormat(config.getString("AS2ReceiveModuleErrorFormat"))
                }
            )

            addModule(
              AS2MDNReceiverModule()
                .apply {
                  port = config.getInt("AS2MDNReceiverModulePort")
                }
            )

            addModule(JmsStorageProcessorModule())
          }

        // Partnerships
        partnershipFactory = ServerXMLPartnershipFactory()
          .apply {
            filename = "%home%/partnerships.xml"
          }
      }
    }
}
