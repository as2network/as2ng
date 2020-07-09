package com.freighttrust.as2.session

import com.freighttrust.as2.factories.PostgresCertificateFactory
import com.freighttrust.as2.factories.PostgresTradingChannelFactory
import com.freighttrust.as2.processor.module.JmsStorageProcessorModule
import com.freighttrust.as2.processor.module.LoggingProcessorModule
import com.freighttrust.as2.receivers.AS2ForwardingReceiverModule
import com.freighttrust.as2.receivers.AS2MDNForwardingReceiverModule
import com.helger.as2.app.cert.ServerCertificateFactory
import com.helger.as2.app.cert.ServerCertificateFactory.*
import com.helger.as2lib.processor.DefaultMessageProcessor
import com.helger.as2lib.processor.DefaultMessageProcessor.ATTR_PENDINGMDN
import com.helger.as2lib.processor.DefaultMessageProcessor.ATTR_PENDINGMDNINFO
import com.helger.as2lib.processor.receiver.AS2MDNReceiverModule
import com.helger.as2lib.processor.receiver.AS2ReceiverModule
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule.*
import com.helger.as2lib.processor.sender.AsynchMDNSenderModule
import com.helger.as2lib.session.AS2Session
import com.typesafe.config.Config
import org.koin.core.Koin
import java.io.File

object As2SessionFactory {

  fun create(k: Koin, c: Config): AS2Session =
    object : AS2Session() {
      init {
        val self = this

        // Global attributes
        isCryptoVerifyUseCertificateInBodyPart = c.getBoolean("as2.CryptoVerifyUseCertificateInBodyPartEnabled")
        isCryptoSignIncludeCertificateInBodyPart = c.getBoolean("as2.CryptoSignIncludeCertificateInBodyPartEnabled")

        // Certificates

        val cf = c.getString("as2.CertificateFactory.Name")
        certificateFactory = when (cf) {
          "PostgresCertificateFactory" -> {
            PostgresCertificateFactory(k.get())
              .apply {
                initDynamicComponent(self, attrs())
              }
          }
          "ServerCertificateFactory" -> {
            ServerCertificateFactory()
              .apply {
                attrs()[ATTR_FILENAME] = File(c.getString("as2.ServerCertificateFactory.KeyStoreFilename")).absolutePath
                attrs()[ATTR_TYPE] = c.getString("as2.ServerCertificateFactory.KeystoreType")
                attrs()[ATTR_PASSWORD] = c.getString("as2.ServerCertificateFactory.KeyStorePassword")
                attrs()[ATTR_INTERVAL] = c.getInt("as2.ServerCertificateFactory.KeyStoreMonitoringInterval").toString()
                initDynamicComponent(self, attrs())
              }
          }
          else -> throw IllegalArgumentException("Invalid CertificateFactory value $cf passed!")
        }

        // Processors
        messageProcessor = DefaultMessageProcessor()
          .apply {
            attrs()[ATTR_PENDINGMDN] = c.getString("as2.DefaultMessageProcessor.PendingMdn")
            attrs()[ATTR_PENDINGMDNINFO] = c.getString("as2.DefaultMessageProcessor.PendingMdnInfo")
            initDynamicComponent(self, attrs())

            val modules = c.getStringList("as2.DefaultMessageProcessor.EnabledModules")
            modules.forEach { module ->
              when (module) {
                "AS2ReceiverModule" -> {
                  addModule(
                    AS2ReceiverModule()
                      .apply {
                        attrs()[ATTR_PORT] = c.getInt("as2.DefaultMessageProcessor.AS2ReceiverModule.Port").toString()
                        attrs()[ATTR_ERROR_DIRECTORY] = c.getString("as2.DefaultMessageProcessor.AS2ReceiverModule.ErrorDir")
                        attrs()[ATTR_ERROR_FORMAT] = c.getString("as2.DefaultMessageProcessor.AS2ReceiveModuleError.ErrorFormat")
                        initDynamicComponent(self, attrs())
                      }
                  )
                }
                "AsynchMDNSenderModule" -> {
                  addModule(
                    AsynchMDNSenderModule()
                      .apply {
                        initDynamicComponent(self, attrs())
                      }
                  )
                }
                "AS2ForwardingReceiverModule" -> {
                  addModule(
                    AS2ForwardingReceiverModule(k.get(), k.get(), k.get(), k.get())
                      .apply {
                        attrs()[ATTR_PORT] = c.getInt("as2.DefaultMessageProcessor.AS2ForwardingReceiverModule.Port").toString()
                        attrs()[ATTR_ERROR_DIRECTORY] = c.getString("as2.DefaultMessageProcessor.AS2ForwardingReceiverModule.ErrorDir")
                        attrs()[ATTR_ERROR_FORMAT] = c.getString("as2.DefaultMessageProcessor.AS2ForwardingReceiverModule.ErrorFormat")
                        initDynamicComponent(self, attrs())
                      }
                  )
                }
                "AS2MDNForwardingReceiverModule" -> {
                  addModule(
                    AS2MDNForwardingReceiverModule(k.get(), k.get(), k.get())
                      .apply {
                        attrs()[ATTR_PORT] = c.getInt("as2.DefaultMessageProcessor.AS2MDNForwardingReceiverModule.Port").toString()
                        initDynamicComponent(self, attrs())
                      }
                  )
                }
                "AS2MDNReceiverModule" -> {
                  addModule(
                    AS2MDNReceiverModule()
                      .apply {
                        attrs()[ATTR_PORT] = c.getInt("as2.DefaultMessageProcessor.AS2MDNReceiverModule.Port").toString()
                        initDynamicComponent(self, attrs())
                      }
                  )
                }
                "JmsStorageProcessorModule" -> {
                  addModule(
                    JmsStorageProcessorModule()
                      .apply {
                        attrs()[JmsStorageProcessorModule.ATTR_BROKER_URL] = c.getString("activemq.brokerUrl")
                        initDynamicComponent(self, attrs())
                      }
                  )
                }
                "LoggingProcessorModule" -> {
                  addModule(
                    LoggingProcessorModule()
                      .apply {
                        initDynamicComponent(self, attrs())
                      }
                  )
                }
              }
            }
          }

        // Partnerships
        val pf = c.getString("as2.PartnershipsFactory.Name")
        partnershipFactory = when (pf) {
          "PostgresTradingChannelFactory" -> {
            PostgresTradingChannelFactory(k.get())
              .apply {
                initDynamicComponent(self, attrs())
              }
          }
          else -> throw IllegalArgumentException("Invalid PartnershipsFactory value $cf passed!")
        }
      }
    }
}
