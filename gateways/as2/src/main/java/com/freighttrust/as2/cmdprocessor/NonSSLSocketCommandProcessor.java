package com.freighttrust.as2.cmdprocessor;

import com.helger.as2.cmd.CommandResult;
import com.helger.as2.cmd.ICommand;
import com.helger.as2.cmdprocessor.AbstractCommandProcessor;
import com.helger.as2.cmdprocessor.SocketCommandParser;
import com.helger.as2.cmdprocessor.SocketCommandProcessor;
import com.helger.as2.cmdprocessor.StreamCommandProcessor;
import com.helger.as2.util.CommandTokenizer;
import com.helger.as2lib.CAS2Info;
import com.helger.as2lib.exception.AS2Exception;
import com.helger.as2lib.exception.WrappedAS2Exception;
import com.helger.as2lib.session.IAS2Session;
import com.helger.commons.collection.attr.IStringMap;
import com.helger.commons.collection.attr.StringMap;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.stream.NonBlockingBufferedReader;
import com.helger.commons.io.stream.NonBlockingBufferedWriter;
import com.helger.commons.string.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class ONLY to be used for testing!
 *
 * It disables SSL and removes the necessity of specifying user and password (both will be ignored).
 * Also, it removes timeouts.
 * **/
public class NonSSLSocketCommandProcessor extends AbstractCommandProcessor {
  public static final String ATTR_PORTID = "portid";
  public static final String ATTR_USERID = "userid";
  public static final String ATTR_PASSWORD = "password";

  private static final Logger LOGGER = LoggerFactory.getLogger(SocketCommandProcessor.class);

  private NonBlockingBufferedReader m_aReader;
  private NonBlockingBufferedWriter m_aWriter;
  private ServerSocket m_aSSLServerSocket;

  private String m_sUserID;
  private String m_sPassword;
  private SocketCommandParser m_aParser;

  @Override
  public void initDynamicComponent(@Nonnull IAS2Session aSession, @Nullable IStringMap aParams) throws AS2Exception {
    final StringMap aParameters = aParams == null ? new StringMap() : new StringMap(aParams);
    final String sPort = aParameters.getAsString(ATTR_PORTID);
    try {
      final int nPort = Integer.parseInt(sPort);

      final ServerSocketFactory aSSLServerSocketFactory = ServerSocketFactory.getDefault();
      m_aSSLServerSocket = aSSLServerSocketFactory.createServerSocket(nPort);
    } catch (final IOException e) {
      throw new AS2Exception(e);
    } catch (final NumberFormatException e) {
      throw new AS2Exception("Error converting portid parameter '" + sPort + "': " + e);
    }

    m_sUserID = aParameters.getAsString(ATTR_USERID);
    if (StringHelper.hasNoText(m_sUserID))
      throw new AS2Exception("missing 'userid' parameter");

    m_sPassword = aParameters.getAsString(ATTR_PASSWORD);
    if (StringHelper.hasNoText(m_sPassword))
      throw new AS2Exception("missing 'password' parameter");

    try {
      m_aParser = new SocketCommandParser();
    } catch (final Exception e) {
      throw new AS2Exception(e);
    }
  }

  @Override
  public void processCommand() throws AS2Exception {
    try (final Socket socket = m_aSSLServerSocket.accept()) {
//      socket.setSoTimeout(2000);
      m_aReader = new NonBlockingBufferedReader(new InputStreamReader(socket.getInputStream()));
      m_aWriter = new NonBlockingBufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

      final String line = m_aReader.readLine();

      m_aParser.parse(line);

//      if (!m_aParser.getUserid().equals(m_sUserID)) {
//        m_aWriter.write("Bad userid/password");
//        throw new AS2Exception("Bad userid");
//      }
//
//      if (!m_aParser.getPassword().equals(m_sPassword)) {
//        m_aWriter.write("Bad userid/password");
//        throw new AS2Exception("Bad password");
//      }

      final String str = m_aParser.getCommandText();
      if (str != null && str.length() > 0) {
        final CommandTokenizer cmdTkn = new CommandTokenizer(str);

        if (cmdTkn.hasMoreTokens()) {
          final String sCommandName = cmdTkn.nextToken().toLowerCase();

          if (sCommandName.equals(StreamCommandProcessor.EXIT_COMMAND)) {
            terminate();
          } else {
            final ICommonsList<String> params = new CommonsArrayList<>();

            while (cmdTkn.hasMoreTokens()) {
              params.add(cmdTkn.nextToken());
            }

            final ICommand aCommand = getCommand(sCommandName);
            if (aCommand != null) {
              final CommandResult aResult = aCommand.execute(params.toArray());

              if (aResult.getType().isSuccess()) {
                m_aWriter.write(aResult.getAsXMLString());
              } else {
                m_aWriter.write("\r\n" + StreamCommandProcessor.COMMAND_ERROR + "\r\n");
                m_aWriter.write(aResult.getResultAsString());
              }
            } else {
              m_aWriter.write(StreamCommandProcessor.COMMAND_NOT_FOUND + "> " + sCommandName + "\r\n");
              m_aWriter.write("List of commands:" + "\r\n");
              for (final String sCurCmd : getAllCommands().keySet())
                m_aWriter.write(sCurCmd + "\r\n");
            }
          }
        }

      }
      m_aWriter.flush();
    } catch (final IOException ex) {
      throw WrappedAS2Exception.wrap(ex);
    }
  }

  @Override
  public void run() {
    try {
      while (true)
        processCommand();
    } catch (final AS2Exception ex) {
      LOGGER.error("Error running command processor " + CAS2Info.NAME_VERSION, ex);
    }
  }
}
