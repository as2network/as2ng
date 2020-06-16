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
 * <p>
 * It disables SSL and removes the necessity of specifying user and password (both will be ignored).
 * Also, it removes timeouts.
 **/
public class NonSSLSocketCommandProcessor extends AbstractCommandProcessor {
  public static final String ATTR_PORTID = "portid";

  private static final Logger LOGGER = LoggerFactory.getLogger(SocketCommandProcessor.class);

  private ServerSocket serverSocket;

  private SocketCommandParser socketCommandParser;

  @Override
  public void initDynamicComponent(@Nonnull IAS2Session session, @Nullable IStringMap params) throws AS2Exception {
    final StringMap parameters = params == null ? new StringMap() : new StringMap(params);
    final String port = parameters.getAsString(ATTR_PORTID);
    try {
      final int nPort = Integer.parseInt(port);

      final ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
      serverSocket = serverSocketFactory.createServerSocket(nPort);
    } catch (final IOException e) {
      throw new AS2Exception(e);
    } catch (final NumberFormatException e) {
      throw new AS2Exception("Error converting portid parameter '" + port + "': " + e);
    }

    try {
      socketCommandParser = new SocketCommandParser();
    } catch (final Exception e) {
      throw new AS2Exception(e);
    }
  }

  @Override
  public void processCommand() throws AS2Exception {
    try (final Socket socket = serverSocket.accept()) {
      NonBlockingBufferedReader nonBlockingBufferedReader = new NonBlockingBufferedReader(new InputStreamReader(socket.getInputStream()));
      NonBlockingBufferedWriter nonBlockingBufferedWriter = new NonBlockingBufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

      final String line = nonBlockingBufferedReader.readLine();

      socketCommandParser.parse(line);

      final String str = socketCommandParser.getCommandText();
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
                nonBlockingBufferedWriter.write(aResult.getAsXMLString());
              } else {
                nonBlockingBufferedWriter.write("\r\n" + StreamCommandProcessor.COMMAND_ERROR + "\r\n");
                nonBlockingBufferedWriter.write(aResult.getResultAsString());
              }
            } else {
              nonBlockingBufferedWriter.write(StreamCommandProcessor.COMMAND_NOT_FOUND + "> " + sCommandName + "\r\n");
              nonBlockingBufferedWriter.write("List of commands:" + "\r\n");
              for (final String sCurCmd : getAllCommands().keySet())
                nonBlockingBufferedWriter.write(sCurCmd + "\r\n");
            }
          }
        }

      }
      nonBlockingBufferedWriter.flush();
    } catch (final IOException ex) {
      throw WrappedAS2Exception.wrap(ex);
    }
  }

  @Override
  public void run() {
    try {
      while (true) processCommand();
    } catch (final AS2Exception ex) {
      LOGGER.error("Error running command processor " + CAS2Info.NAME_VERSION, ex);
    }
  }
}
