/**
 * Created Jan 8, 2021
 */
package com.ilardi.systems.net.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SocketInfo;

/**
 * @author rilardi
 *
 */

public class SslBlockingSocketServerHandler extends TcpBlockingSocketServerHandler {

  protected static final Logger logger = LogUtil.getInstance().getLogger(SslBlockingSocketServerHandler.class);

  protected SSLServerSocketFactory sslServerSocketFactory = null;

  public SslBlockingSocketServerHandler() {
    super();
  }

  @Override
  protected ServerSocket createServerSocket() throws IlardiNetException {
    ServerSocket ss;

    try {
      logger.info("SslBlockingSocketServerHandler Executing createServerSocket");

      initSslServerSocketFactory();

      ss = sslServerSocketFactory.createServerSocket();

      return ss;
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attemping to Create SSL Server Socket! System Message: " + e.getMessage(), e);
    }
  }

  private synchronized void initSslServerSocketFactory()
      throws NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {
    SocketInfo socketInfo;
    SSLContext ctx;
    KeyManagerFactory kmf;
    KeyStore ks;
    char[] passphraseChars;
    String passphrase;
    FileInputStream fis = null;

    logger.info("SslBlockingSocketServerHandler Executing initSslServerSocketFactory");

    if (sslServerSocketFactory == null) {
      try {
        socketInfo = socketFramework.getSocketInfo();

        ctx = SSLContext.getInstance(socketInfo.getSslProtocol());

        if (socketInfo.getSslProvider() != null) {
          kmf = KeyManagerFactory.getInstance(socketInfo.getSslAlgorithm(), socketInfo.getSslProvider());
        }
        else {
          kmf = KeyManagerFactory.getInstance(socketInfo.getSslAlgorithm());
        }

        ks = KeyStore.getInstance(socketInfo.getSslKeyStoreType());

        passphrase = socketInfo.getSslKeyStorePassword();

        if (passphrase != null) {
          passphraseChars = passphrase.toCharArray();
        }
        else {
          passphraseChars = null;
        }

        fis = new FileInputStream(socketInfo.getSslKeystoreFile());

        ks.load(fis, passphraseChars);
        kmf.init(ks, passphraseChars);

        ctx.init(kmf.getKeyManagers(), null, null);

        sslServerSocketFactory = ctx.getServerSocketFactory();
      } // End try block
      finally {
        try {
          if (fis != null) {
            fis.close();
          }
        }
        catch (Exception e2) {
          logger.error(e2);
        }
      }
    }
  }

}
