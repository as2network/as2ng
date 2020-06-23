package com.freighttrust.as2.session

import com.freighttrust.as2.processor.module.JmsStorageProcessorModule
import com.helger.as2.app.cert.ServerCertificateFactory
import com.helger.as2.app.partner.ServerXMLPartnershipFactory
import com.helger.as2lib.partner.xml.XMLPartnershipFactory
import com.helger.as2lib.processor.DefaultMessageProcessor
import com.helger.as2lib.processor.receiver.AS2MDNReceiverModule
import com.helger.as2lib.processor.receiver.AS2ReceiverModule
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule
import com.helger.as2lib.session.AS2Session
import com.typesafe.config.Config
import java.io.File

object As2SessionFactory {

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
            attrs()[ServerCertificateFactory.ATTR_FILENAME] = File(config.getString("KeyStoreFilename")).absolutePath
            attrs()[ServerCertificateFactory.ATTR_TYPE] = config.getString("KeystoreType")
            attrs()[ServerCertificateFactory.ATTR_PASSWORD] = config.getString("KeyStorePassword")
            attrs()[ServerCertificateFactory.ATTR_INTERVAL] = config.getInt("KeyStoreMonitoringInterval").toString()
            initDynamicComponent(self, attrs())
          }

        // Processors
        messageProcessor = DefaultMessageProcessor()
          .apply {
            attrs()[DefaultMessageProcessor.ATTR_PENDINGMDN] = config.getString("MessageProcessorPendingMdn")
            attrs()[DefaultMessageProcessor.ATTR_PENDINGMDNINFO] = config.getString("MessageProcessorPendingMdnInfo")
            initDynamicComponent(self, attrs())

            addModule(
              AS2ReceiverModule()
                .apply {
                  attrs()[AbstractActiveNetModule.ATTR_PORT] = config.getInt("AS2ReceiverModulePort").toString()
                  attrs()[AbstractActiveNetModule.ATTR_ERROR_DIRECTORY] = config.getString("AS2ReceiverModuleErrorDir")
                  attrs()[AbstractActiveNetModule.ATTR_ERROR_FORMAT] = config.getString("AS2ReceiveModuleErrorFormat")
                  initDynamicComponent(self, attrs())
                }
            )

            addModule(
              AS2MDNReceiverModule()
                .apply {
                  attrs()[AS2MDNReceiverModule.ATTR_PORT] = config.getInt("AS2MDNReceiverModulePort").toString()
                  initDynamicComponent(self, attrs())
                }
            )

            addModule(
              JmsStorageProcessorModule()
                .apply {
                  initDynamicComponent(self, attrs())
                }
            )
          }

        // Partnerships
        // TODO: Replace this with our factory
        partnershipFactory = ServerXMLPartnershipFactory()
          .apply {
            attrs()[XMLPartnershipFactory.ATTR_FILENAME] = "gateways/as2/src/main/resources/config/partnerships.xml"
            initDynamicComponent(self, attrs())
          }
      }
    }
}
