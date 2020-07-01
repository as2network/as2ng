package com.freighttrust.as2.session

import com.freighttrust.as2.factories.PostgresCertificateFactory
import com.freighttrust.as2.factories.PostgresTradingChannelFactory
import com.freighttrust.as2.processor.module.JmsStorageProcessorModule
import com.helger.as2lib.processor.DefaultMessageProcessor
import com.helger.as2lib.processor.receiver.AS2MDNReceiverModule
import com.helger.as2lib.processor.receiver.AS2ReceiverModule
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule
import com.helger.as2lib.session.AS2Session
import com.typesafe.config.Config
import org.koin.core.Koin

object As2SessionFactory {

  fun create(koin: Koin, config: Config): AS2Session =
    object : AS2Session() {
      init {
        val self = this

        // Global attributes
        isCryptoVerifyUseCertificateInBodyPart = config.getBoolean("as2.CryptoVerifyUseCertificateInBodyPartEnabled")
        isCryptoSignIncludeCertificateInBodyPart = config.getBoolean("as2.CryptoSignIncludeCertificateInBodyPartEnabled")

        // Certificates

        certificateFactory = PostgresCertificateFactory(koin.get())
          .apply {
            initDynamicComponent(self, attrs())
          }

//        certificateFactory = ServerCertificateFactory()
//          .apply {
//            attrs()[ServerCertificateFactory.ATTR_FILENAME] = File(config.getString("as2.KeyStoreFilename")).absolutePath
//            attrs()[ServerCertificateFactory.ATTR_TYPE] = config.getString("as2.KeystoreType")
//            attrs()[ServerCertificateFactory.ATTR_PASSWORD] = config.getString("as2.KeyStorePassword")
//            attrs()[ServerCertificateFactory.ATTR_INTERVAL] = config.getInt("as2.KeyStoreMonitoringInterval").toString()
//            initDynamicComponent(self, attrs())
//          }

        // Processors
        messageProcessor = DefaultMessageProcessor()
          .apply {
            attrs()[DefaultMessageProcessor.ATTR_PENDINGMDN] = config.getString("as2.MessageProcessorPendingMdn")
            attrs()[DefaultMessageProcessor.ATTR_PENDINGMDNINFO] = config.getString("as2.MessageProcessorPendingMdnInfo")
            initDynamicComponent(self, attrs())

            addModule(
              AS2ReceiverModule()
                .apply {
                  attrs()[AbstractActiveNetModule.ATTR_PORT] = config.getInt("as2.AS2ReceiverModulePort").toString()
                  attrs()[AbstractActiveNetModule.ATTR_ERROR_DIRECTORY] = config.getString("as2.AS2ReceiverModuleErrorDir")
                  attrs()[AbstractActiveNetModule.ATTR_ERROR_FORMAT] = config.getString("as2.AS2ReceiveModuleErrorFormat")
                  initDynamicComponent(self, attrs())
                }
            )

            addModule(
              AS2MDNReceiverModule()
                .apply {
                  attrs()[AS2MDNReceiverModule.ATTR_PORT] = config.getInt("as2.AS2MDNReceiverModulePort").toString()
                  initDynamicComponent(self, attrs())
                }
            )

            addModule(
              JmsStorageProcessorModule()
                .apply {
                  attrs()[JmsStorageProcessorModule.ATTR_BROKER_URL] = config.getString("activemq.brokerUrl")
                  initDynamicComponent(self, attrs())
                }
            )
          }

        // Partnerships
        partnershipFactory = PostgresTradingChannelFactory(koin.get())
          .apply {
            initDynamicComponent(self, attrs())
          }
      }
    }
}
