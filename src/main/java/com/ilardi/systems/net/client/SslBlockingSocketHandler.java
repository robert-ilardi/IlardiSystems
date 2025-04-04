/**
 * Created Jan 8, 2021
 */
package com.ilardi.systems.net.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SocketInfo;

/**
 * @author rilardi
 *
 */

public class SslBlockingSocketHandler extends TcpBlockingSocketClientHandler {

  protected SSLSocketFactory sslSocketFactory = null;

  public SslBlockingSocketHandler() {
    super();
  }

  @Override
  protected Socket createSocket() throws IlardiNetException {
    Socket s;

    try {
      initSslSocketFactory();

      s = sslSocketFactory.createSocket();

      return s;
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attemping to Create SSL Socket! System Message: " + e.getMessage(), e);
    }
  }

  private synchronized void initSslSocketFactory()
      throws NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {
    SocketInfo socketInfo;
    SSLContext ctx;
    KeyManagerFactory kmf;
    KeyStore ks;
    char[] passphraseChars;
    String passphrase;
    FileInputStream fis = null;

    if (sslSocketFactory == null) {
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

        sslSocketFactory = ctx.getSocketFactory();
      } // End try block
      finally {
        try {
          if (fis != null) {
            fis.close();
          }
        }
        catch (Exception e2) {
          e2.printStackTrace();
        }
      }
    }
  }

}
