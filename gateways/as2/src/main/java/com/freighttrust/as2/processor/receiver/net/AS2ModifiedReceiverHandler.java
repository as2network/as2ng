package com.freighttrust.as2.processor.receiver.net;

import java.io.IOException;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import com.freighttrust.as2.processor.receiver.AS2ModifiedReceiverModule;
import com.helger.as2lib.processor.receiver.net.AS2NetException;
import com.helger.as2lib.processor.receiver.net.AbstractReceiverHandler;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.jcajce.ZlibExpanderProvider;
import org.bouncycastle.mail.smime.SMIMECompressedParser;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMEUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.as2lib.cert.ECertificatePartnershipType;
import com.helger.as2lib.cert.ICertificateFactory;
import com.helger.as2lib.crypto.ICryptoHelper;
import com.helger.as2lib.disposition.AS2DispositionException;
import com.helger.as2lib.disposition.DispositionType;
import com.helger.as2lib.exception.AS2Exception;
import com.helger.as2lib.exception.WrappedAS2Exception;
import com.helger.as2lib.message.AS2Message;
import com.helger.as2lib.message.IMessage;
import com.helger.as2lib.message.IMessageMDN;
import com.helger.as2lib.params.MessageParameters;
import com.helger.as2lib.processor.AS2NoModuleException;
import com.helger.as2lib.processor.AS2ProcessorException;
import com.helger.as2lib.processor.CNetAttribute;
import com.helger.as2lib.processor.receiver.AS2ReceiverModule;
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule;
import com.helger.as2lib.processor.sender.IProcessorSenderModule;
import com.helger.as2lib.processor.storage.IProcessorStorageModule;
import com.helger.as2lib.session.AS2ComponentNotFoundException;
import com.helger.as2lib.session.IAS2Session;
import com.helger.as2lib.util.AS2Helper;
import com.helger.as2lib.util.AS2HttpHelper;
import com.helger.as2lib.util.AS2IOHelper;
import com.helger.as2lib.util.AS2ResourceHelper;
import com.helger.as2lib.util.dump.IHTTPIncomingDumper;
import com.helger.as2lib.util.http.AS2HttpResponseHandlerSocket;
import com.helger.as2lib.util.http.AS2InputStreamProviderSocket;
import com.helger.as2lib.util.http.IAS2HttpResponseHandler;
import com.helger.as2lib.util.http.TempSharedFileInputStream;
import com.helger.commons.ValueEnforcer;
import com.helger.commons.functional.IConsumer;
import com.helger.commons.http.CHttp;
import com.helger.commons.http.CHttpHeader;
import com.helger.commons.http.HttpHeaderMap;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.commons.lang.StackTraceHelper;
import com.helger.commons.state.ESuccess;
import com.helger.commons.state.ETriState;
import com.helger.commons.string.StringHelper;
import com.helger.commons.timing.StopWatch;
import com.helger.commons.wrapper.Wrapper;
import com.helger.mail.datasource.ByteArrayDataSource;
import com.helger.security.certificate.CertificateHelper;

public class AS2ModifiedReceiverHandler extends AbstractReceiverHandler
{
  public static final boolean DEFAULT_SEND_EXCEPTIONS_IN_MDN = false;
  public static final boolean DEFAULT_SEND_EXCEPTION_STACKTRACE_IN_MDN = false;
  private static final Logger LOGGER = LoggerFactory.getLogger (AS2ModifiedReceiverHandler.class);

  private final AS2ModifiedReceiverModule m_aReceiverModule;
  private boolean m_bSendExceptionsInMDN = DEFAULT_SEND_EXCEPTIONS_IN_MDN;
  private boolean m_bSendExceptionStackTraceInMDN = DEFAULT_SEND_EXCEPTION_STACKTRACE_IN_MDN;

  public AS2ModifiedReceiverHandler(@Nonnull final AS2ModifiedReceiverModule aModule)
  {
    m_aReceiverModule = ValueEnforcer.notNull (aModule, "Module");
  }

  @Nonnull
  protected AS2Message createMessage (@Nonnull final Socket aSocket)
  {
    final AS2Message aMsg = new AS2Message ();
    aMsg.attrs ().putIn (CNetAttribute.MA_SOURCE_IP, aSocket.getInetAddress ().getHostAddress ());
    aMsg.attrs ().putIn (CNetAttribute.MA_SOURCE_PORT, aSocket.getPort ());
    aMsg.attrs ().putIn (CNetAttribute.MA_DESTINATION_IP, aSocket.getLocalAddress ().getHostAddress ());
    aMsg.attrs ().putIn (CNetAttribute.MA_DESTINATION_PORT, aSocket.getLocalPort ());
    aMsg.attrs ().putIn (AS2Message.ATTRIBUTE_RECEIVED, true);
    return aMsg;
  }

  protected void decrypt (@Nonnull final IMessage aMsg, @Nonnull final AS2ResourceHelper aResHelper) throws AS2Exception
  {
    final ICertificateFactory aCertFactory = m_aReceiverModule.getSession ().getCertificateFactory ();
    final ICryptoHelper aCryptoHelper = AS2Helper.getCryptoHelper ();

    try
    {
      final boolean bDisableDecrypt = aMsg.partnership ().isDisableDecrypt ();
      final boolean bMsgIsEncrypted = aCryptoHelper.isEncrypted (aMsg.getData ());
      final boolean bForceDecrypt = aMsg.partnership ().isForceDecrypt ();
      if (bMsgIsEncrypted && bDisableDecrypt)
      {
        if (LOGGER.isInfoEnabled ())
          LOGGER.info ("Message claims to be encrypted but decryption is disabled" + aMsg.getLoggingText ());
      }
      else
        if (bMsgIsEncrypted || bForceDecrypt)
        {
          // Decrypt
          if (bForceDecrypt && !bMsgIsEncrypted)
          {
            if (LOGGER.isInfoEnabled ())
              LOGGER.info ("Forced decrypting" + aMsg.getLoggingText ());
          }
          else
            if (LOGGER.isDebugEnabled ())
              LOGGER.debug ("Decrypting" + aMsg.getLoggingText ());

          final X509Certificate aReceiverCert = aCertFactory.getCertificate (aMsg,
                                                                             ECertificatePartnershipType.RECEIVER);
          final PrivateKey aReceiverKey = aCertFactory.getPrivateKey (aReceiverCert);
          final MimeBodyPart aDecryptedData = aCryptoHelper.decrypt (aMsg.getData (),
                                                                     aReceiverCert,
                                                                     aReceiverKey,
                                                                     bForceDecrypt,
                                                                     aResHelper);
          aMsg.setData (aDecryptedData);
          // Remember that message was encrypted
          aMsg.attrs ().putIn (AS2Message.ATTRIBUTE_RECEIVED_ENCRYPTED, true);

          if (LOGGER.isInfoEnabled ())
            LOGGER.info ("Successfully decrypted incoming AS2 message" + aMsg.getLoggingText ());
        }
    }
    catch (final Exception ex)
    {
      if (LOGGER.isErrorEnabled ())
        LOGGER.error ("Error decrypting " + aMsg.getLoggingText () + ": " + ex.getMessage ());

      throw new AS2DispositionException (DispositionType.createError ("decryption-failed"),
                                         AbstractActiveNetModule.DISP_DECRYPTION_ERROR,
                                         ex);
    }
  }

  protected void verify (@Nonnull final IMessage aMsg, @Nonnull final AS2ResourceHelper aResHelper) throws AS2Exception
  {
    final ICertificateFactory aCertFactory = m_aReceiverModule.getSession ().getCertificateFactory ();
    final ICryptoHelper aCryptoHelper = AS2Helper.getCryptoHelper ();

    try
    {
      final boolean bDisableVerify = aMsg.partnership ().isDisableVerify ();
      final boolean bMsgIsSigned = aCryptoHelper.isSigned (aMsg.getData ());
      final boolean bForceVerify = aMsg.partnership ().isForceVerify ();
      if (bMsgIsSigned && bDisableVerify)
      {
        if (LOGGER.isInfoEnabled ())
          LOGGER.info ("Message claims to be signed but signature validation is disabled" + aMsg.getLoggingText ());
      }
      else
        if (bMsgIsSigned || bForceVerify)
        {
          if (bForceVerify && !bMsgIsSigned)
          {
            if (LOGGER.isInfoEnabled ())
              LOGGER.info ("Forced verify signature" + aMsg.getLoggingText ());
          }
          else
            if (LOGGER.isDebugEnabled ())
              LOGGER.debug ("Verifying signature" + aMsg.getLoggingText ());

          final X509Certificate aSenderCert = aCertFactory.getCertificateOrNull (aMsg,
                                                                                 ECertificatePartnershipType.SENDER);
          boolean bUseCertificateInBodyPart;
          final ETriState eUseCertificateInBodyPart = aMsg.partnership ().getVerifyUseCertificateInBodyPart ();
          if (eUseCertificateInBodyPart.isDefined ())
          {
            // Use per partnership
            bUseCertificateInBodyPart = eUseCertificateInBodyPart.getAsBooleanValue ();
          }
          else
          {
            // Use global value
            bUseCertificateInBodyPart = m_aReceiverModule.getSession ().isCryptoVerifyUseCertificateInBodyPart ();
          }

          final Wrapper <X509Certificate> aCertHolder = new Wrapper <> ();
          final MimeBodyPart aVerifiedData = aCryptoHelper.verify (aMsg.getData (),
                                                                   aSenderCert,
                                                                   bUseCertificateInBodyPart,
                                                                   bForceVerify,
                                                                   aCertHolder::set,
                                                                   aResHelper);
          final IConsumer <X509Certificate> aExternalConsumer = getVerificationCertificateConsumer ();
          if (aExternalConsumer != null)
            aExternalConsumer.accept (aCertHolder.get ());

          aMsg.setData (aVerifiedData);
          // Remember that message was signed and verified
          aMsg.attrs ().putIn (AS2Message.ATTRIBUTE_RECEIVED_SIGNED, true);
          // Remember the PEM encoded version of the X509 certificate that was
          // used for verification
          aMsg.attrs ()
              .putIn (AS2Message.ATTRIBUTE_RECEIVED_SIGNATURE_CERTIFICATE,
                      CertificateHelper.getPEMEncodedCertificate (aCertHolder.get ()));

          if (LOGGER.isInfoEnabled ())
            LOGGER.info ("Successfully verified signature of incoming AS2 message" + aMsg.getLoggingText ());
        }
    }
    catch (final Exception ex)
    {
      if (LOGGER.isErrorEnabled ())
        LOGGER.error ("Error verifying signature " + aMsg.getLoggingText () + ": " + ex.getMessage ());

      throw new AS2DispositionException (DispositionType.createError ("integrity-check-failed"),
                                         AbstractActiveNetModule.DISP_VERIFY_SIGNATURE_FAILED,
                                         ex);
    }
  }

  protected void decompress (@Nonnull final IMessage aMsg) throws AS2DispositionException
  {
    try
    {
      if (aMsg.partnership ().isDisableDecompress ())
      {
        if (LOGGER.isInfoEnabled ())
          LOGGER.info ("Message claims to be compressed but decompression is disabled" + aMsg.getLoggingText ());
      }
      else
      {
        if (LOGGER.isDebugEnabled ())
          LOGGER.debug ("Decompressing a compressed AS2 message");

        MimeBodyPart aDecompressedPart;
        final ZlibExpanderProvider aExpander = new ZlibExpanderProvider ();

        // Compress using stream
        if (LOGGER.isDebugEnabled ())
        {
          final StringBuilder aSB = new StringBuilder ();
          aSB.append ("Headers before uncompress\n");
          final MimeBodyPart part = aMsg.getData ();
          final Enumeration <String> aHeaderLines = part.getAllHeaderLines ();
          while (aHeaderLines.hasMoreElements ())
          {
            aSB.append (aHeaderLines.nextElement ()).append ('\n');
          }
          aSB.append ("done");
          LOGGER.debug (aSB.toString ());
        }

        final SMIMECompressedParser aCompressedParser = new SMIMECompressedParser (aMsg.getData (), 8 * 1024);
        // TODO: get buffer from configuration
        aDecompressedPart = SMIMEUtil.toMimeBodyPart (aCompressedParser.getContent (aExpander));

        // Update the message object
        aMsg.setData (aDecompressedPart);
        // Remember that message was decompressed
        aMsg.attrs ().putIn (AS2Message.ATTRIBUTE_RECEIVED_COMPRESSED, true);

        if (LOGGER.isInfoEnabled ())
          LOGGER.info ("Successfully decompressed incoming AS2 message" + aMsg.getLoggingText ());
      }
    }
    catch (final SMIMEException | CMSException | MessagingException ex)
    {
      if (LOGGER.isErrorEnabled ())
        LOGGER.error ("Error decompressing received message", ex);

      throw new AS2DispositionException (DispositionType.createError ("unexpected-processing-error"),
                                         AbstractActiveNetModule.DISP_DECOMPRESSION_ERROR,
                                         ex);
    }
  }

  protected void sendMDN (@Nonnull final String sClientInfo,
                          @Nonnull final IAS2HttpResponseHandler aResponseHandler,
                          @Nonnull final AS2Message aMsg,
                          @Nonnull final DispositionType aDisposition,
                          @Nonnull final String sText,
                          @Nonnull final ESuccess eSuccess)
  {
    final boolean bAllowErrorMDN = !aMsg.partnership ().isBlockErrorMDN ();
    if (eSuccess.isSuccess () || bAllowErrorMDN)
    {
      try
      {
        final IAS2Session aSession = m_aReceiverModule.getSession ();
        final IMessageMDN aMdn = AS2Helper.createMDN (aSession, aMsg, aDisposition, sText);

        if (aMsg.isRequestingAsynchMDN ())
        {
          // if asyncMDN requested, close existing synchronous connection and
          // initiate separate MDN send
          final HttpHeaderMap aHeaders = new HttpHeaderMap ();
          aHeaders.setContentLength (0);
          try (final NonBlockingByteArrayOutputStream aData = new NonBlockingByteArrayOutputStream ())
          {
            // Empty data
            // Ideally this would be HTTP 204 (no content)
            aResponseHandler.sendHttpResponse (CHttp.HTTP_OK, aHeaders, aData);
          }

          if (LOGGER.isInfoEnabled ())
            LOGGER.info ("Setup to send async MDN [" +
                         aDisposition.getAsString () +
                         "] " +
                         sClientInfo +
                         aMsg.getLoggingText ());

          // trigger explicit async sending
          aSession.getMessageProcessor ().handle (IProcessorSenderModule.DO_SEND_ASYNC_MDN, aMsg, null);
        }
        else
        {
          // otherwise, send sync MDN back on same connection
          if (LOGGER.isInfoEnabled ())
            LOGGER.info ("Sending back sync MDN [" +
                         aDisposition.getAsString () +
                         "] " +
                         sClientInfo +
                         aMsg.getLoggingText ());

          // Get data and therefore content length for sync MDN
          try (final NonBlockingByteArrayOutputStream aData = new NonBlockingByteArrayOutputStream ())
          {
            final MimeBodyPart aPart = aMdn.getData ();
            StreamHelper.copyInputStreamToOutputStream (aPart.getInputStream (), aData);
            aMdn.headers ().setContentLength (aData.size ());

            // start HTTP response
            aResponseHandler.sendHttpResponse (CHttp.HTTP_OK, aMdn.headers (), aData);
          }

          // Save sent MDN for later examination
          try
          {
            aSession.getMessageProcessor ().handle (IProcessorStorageModule.DO_STOREMDN, aMsg, null);
          }
          catch (final AS2ComponentNotFoundException | AS2NoModuleException ex)
          {
            // No message processor found
            // or No module found in message processor
          }
          if (LOGGER.isInfoEnabled ())
            LOGGER.info ("sent MDN [" + aDisposition.getAsString () + "] " + sClientInfo + aMsg.getLoggingText ());
        }
      }
      catch (final Exception ex)
      {
        WrappedAS2Exception.wrap (ex).setSourceMsg (aMsg).terminate ();
      }
    }
  }

  @Nonnull
  private String _getDispositionText (@Nonnull final AS2Exception ex)
  {
    // Issue 90 - use CRLF as separator
    if (m_bSendExceptionsInMDN)
    {
      final String sExceptionText;
      if (m_bSendExceptionStackTraceInMDN)
      {
        // Message and stack trace
        sExceptionText = StackTraceHelper.getStackAsString (ex, true, CHttp.EOL);
      }
      else
      {
        // Exception message only
        if (ex instanceof AS2ProcessorException)
          sExceptionText = ((AS2ProcessorException) ex).getShortToString ();
        else
          sExceptionText = ex.toString ();
      }
      return CHttp.EOL + MessageParameters.getEscapedString (sExceptionText);
    }

    // No information at all
    return "";
  }

  public void handleIncomingMessage (@Nonnull final String sClientInfo,
                                     @Nullable final DataSource aMsgData,
                                     @Nonnull final AS2Message aMsg,
                                     @Nonnull final IAS2HttpResponseHandler aResponseHandler)
  {
    try (final AS2ResourceHelper aResHelper = new AS2ResourceHelper ())
    {
      final IAS2Session aSession = m_aReceiverModule.getSession ();

      try
      {
        // Put received data in a MIME body part
        final String sReceivedContentType = AS2HttpHelper.getCleanContentType (aMsg.getHeader (CHttpHeader.CONTENT_TYPE));

        final MimeBodyPart aReceivedPart = new MimeBodyPart ();
        aReceivedPart.setDataHandler (new DataHandler (aMsgData));

        // Header must be set AFTER the DataHandler!
        aReceivedPart.setHeader (CHttpHeader.CONTENT_TYPE, sReceivedContentType);
        aMsg.setData (aReceivedPart);
      }
      catch (final Exception ex)
      {
        throw new AS2DispositionException (DispositionType.createError ("unexpected-processing-error"),
                                           AbstractActiveNetModule.DISP_PARSING_MIME_FAILED,
                                           ex);
      }

      // Extract AS2 ID's from header, find the message's partnership and
      // update the message
      try
      {
        final String sAS2From = aMsg.getAS2From ();
        aMsg.partnership ().setSenderAS2ID (sAS2From);

        final String sAS2To = aMsg.getAS2To ();
        aMsg.partnership ().setReceiverAS2ID (sAS2To);

        // Fill all partnership attributes etc.
        aSession.getPartnershipFactory ().updatePartnership (aMsg, false);
      }
      catch (final AS2Exception ex)
      {
        throw new AS2DispositionException (DispositionType.createError ("authentication-failed"),
                                           AbstractActiveNetModule.DISP_PARTNERSHIP_NOT_FOUND,
                                           ex);
      }

      // Per RFC5402 compression is always before encryption but can be before
      // or after signing of message but only in one place
      final ICryptoHelper aCryptoHelper = AS2Helper.getCryptoHelper ();
      boolean bIsDecompressed = false;

      // Decrypt and verify signature of the data, and attach data to the
      // message
      decrypt (aMsg, aResHelper);

      if (aCryptoHelper.isCompressed (aMsg.getContentType ()))
      {
        if (LOGGER.isTraceEnabled ())
          LOGGER.trace ("Decompressing received message before checking signature...");
        decompress (aMsg);
        bIsDecompressed = true;
      }

      verify (aMsg, aResHelper);

      if (aCryptoHelper.isCompressed (aMsg.getContentType ()))
      {
        // Per RFC5402 compression is always before encryption but can be before
        // or after signing of message but only in one place
        if (bIsDecompressed)
        {
          throw new AS2DispositionException (DispositionType.createError ("decompression-failed"),
                                             AbstractActiveNetModule.DISP_DECOMPRESSION_ERROR,
                                             new Exception ("Message has already been decompressed. Per RFC5402 it cannot occur twice."));
        }

        if (LOGGER.isTraceEnabled ())
          if (aMsg.attrs ().containsKey (AS2Message.ATTRIBUTE_RECEIVED_SIGNED))
            LOGGER.trace ("Decompressing received message after verifying signature...");
          else
            LOGGER.trace ("Decompressing received message after decryption...");
        decompress (aMsg);
        bIsDecompressed = true;
      }

      // Store the received message
      try
      {
        aSession.getMessageProcessor ().handle (IProcessorStorageModule.DO_STORE, aMsg, null);
      }
      catch (final AS2NoModuleException ex)
      {
        // No module installed - ignore
      }
      catch (final AS2Exception ex)
      {
        // Issue 90 - use CRLF as separator
        throw new AS2DispositionException (DispositionType.createError ("unexpected-processing-error"),
                                           StringHelper.getConcatenatedOnDemand (AbstractActiveNetModule.DISP_STORAGE_FAILED,
                                                                                 CHttp.EOL,
                                                                                 _getDispositionText (ex)),
                                           ex);
      }

    }
    catch (final AS2DispositionException ex)
    {
      sendMDN (sClientInfo, aResponseHandler, aMsg, ex.getDisposition (), ex.getText (), ESuccess.FAILURE);
      m_aReceiverModule.handleError (aMsg, ex);
    }
    catch (final AS2Exception ex)
    {
      m_aReceiverModule.handleError (aMsg, ex);
    }
    finally
    {
      // close the temporary shared stream if it exists
      final TempSharedFileInputStream sis = aMsg.getTempSharedFileInputStream ();
      if (null != sis)
      {
        try
        {
          sis.closeAll ();
        }
        catch (final IOException e)
        {
          LOGGER.error ("Exception while closing TempSharedFileInputStream", e);
        }
      }
    }
  }

  public void handle (@Nonnull final AbstractActiveNetModule aOwner, @Nonnull final Socket aSocket)
  {
    final String sClientInfo = getClientInfo (aSocket);
    if (LOGGER.isInfoEnabled ())
      LOGGER.info ("Incoming connection " + sClientInfo);

    final AS2Message aMsg = createMessage (aSocket);
    final boolean bQuoteHeaderValues = m_aReceiverModule.isQuoteHeaderValues ();
    final IAS2HttpResponseHandler aResponseHandler = new AS2HttpResponseHandlerSocket (aSocket, bQuoteHeaderValues);

    // Time the transmission
    final StopWatch aSW = StopWatch.createdStarted ();
    DataSource aMsgDataSource = null;
    try
    {
      // Read in the message request, headers, and data
      final IHTTPIncomingDumper aIncomingDumper = getEffectiveHttpIncomingDumper ();
      aMsgDataSource = readAndDecodeHttpRequest (new AS2InputStreamProviderSocket (aSocket),
                                                 aResponseHandler,
                                                 aMsg,
                                                 aIncomingDumper);
    }
    catch (final Exception ex)
    {
      new AS2NetException(aSocket.getInetAddress (), aSocket.getPort (), ex).terminate ();
    }

    aSW.stop ();

    if (aMsgDataSource != null)
      if (aMsgDataSource instanceof ByteArrayDataSource)
      {
        if (LOGGER.isInfoEnabled ())
          LOGGER.info ("received " +
                       AS2IOHelper.getTransferRate (((ByteArrayDataSource) aMsgDataSource).directGetBytes ().length,
                                                    aSW) +
                       " from " +
                       sClientInfo +
                       aMsg.getLoggingText ());

      }
      else
      {
        LOGGER.info ("received message from " +
                     sClientInfo +
                     aMsg.getLoggingText () +
                     " in " +
                     aSW.getMillis () +
                     " ms");

      }
    handleIncomingMessage (sClientInfo, aMsgDataSource, aMsg, aResponseHandler);
  }
}
