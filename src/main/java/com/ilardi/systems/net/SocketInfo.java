/**
 * Created Jan 8, 2021
 */
package com.ilardi.systems.net;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * @author rilardi
 *
 */

public class SocketInfo implements Serializable {

  private SocketMode socketMode;

  private SocketType socketType;

  private IoType ioType;

  private String hostAddress;

  private int port;

  private int backlog = 0;

  private String socketHandlerClassname;

  private String socketThreaderClassname;

  private String socketProcessorClassname;

  private String socketControllerClassname;

  private String sslProtocol;
  private String sslAlgorithm;
  private String sslProvider;
  private String sslKeyStoreType;
  private String sslKeystoreFile;
  private String sslKeyStorePassword;

  private Properties config;

  public SocketInfo() {}

  public SocketType getSocketType() {
    return socketType;
  }

  public void setSocketType(SocketType socketType) {
    this.socketType = socketType;
  }

  public String getHostAddress() {
    return hostAddress;
  }

  public void setHostAddress(String hostAddress) {
    this.hostAddress = hostAddress;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public IoType getIoType() {
    return ioType;
  }

  public void setIoType(IoType ioType) {
    this.ioType = ioType;
  }

  public String getSocketProcessorClassname() {
    return socketProcessorClassname;
  }

  public void setSocketProcessorClassname(String socketProcessorClassname) {
    this.socketProcessorClassname = socketProcessorClassname;
  }

  public String getSocketHandlerClassname() {
    return socketHandlerClassname;
  }

  public void setSocketHandlerClassname(String socketHandlerClassname) {
    this.socketHandlerClassname = socketHandlerClassname;
  }

  public int getBacklog() {
    return backlog;
  }

  public void setBacklog(int backlog) {
    this.backlog = backlog;
  }

  public InetAddress getHostAddressAsInetAddress() throws UnknownHostException {
    InetAddress inAddr;

    inAddr = InetAddress.getByName(hostAddress);

    return inAddr;
  }

  public String getSslProtocol() {
    return sslProtocol;
  }

  public void setSslProtocol(String sslProtocol) {
    this.sslProtocol = sslProtocol;
  }

  public String getSslAlgorithm() {
    return sslAlgorithm;
  }

  public void setSslAlgorithm(String sslAlgorithm) {
    this.sslAlgorithm = sslAlgorithm;
  }

  public String getSslProvider() {
    return sslProvider;
  }

  public void setSslProvider(String sslProvider) {
    this.sslProvider = sslProvider;
  }

  public String getSslKeyStoreType() {
    return sslKeyStoreType;
  }

  public void setSslKeyStoreType(String sslKeyStoreType) {
    this.sslKeyStoreType = sslKeyStoreType;
  }

  public String getSslKeystoreFile() {
    return sslKeystoreFile;
  }

  public void setSslKeystoreFile(String sslKeystoreFile) {
    this.sslKeystoreFile = sslKeystoreFile;
  }

  public String getSslKeyStorePassword() {
    return sslKeyStorePassword;
  }

  public void setSslKeyStorePassword(String sslKeyStorePassword) {
    this.sslKeyStorePassword = sslKeyStorePassword;
  }

  public Properties getConfig() {
    return config;
  }

  public void setConfig(Properties config) {
    this.config = config;
  }

  public String getSocketControllerClassname() {
    return socketControllerClassname;
  }

  public void setSocketControllerClassname(String controllerClassname) {
    this.socketControllerClassname = controllerClassname;
  }

  public String getSocketThreaderClassname() {
    return socketThreaderClassname;
  }

  public void setSocketThreaderClassname(String socketThreaderClassname) {
    this.socketThreaderClassname = socketThreaderClassname;
  }

  public SocketMode getSocketMode() {
    return socketMode;
  }

  public void setSocketMode(SocketMode socketMode) {
    this.socketMode = socketMode;
  }

  @Override
  public String toString() {
    return "SocketInfo [socketMode=" + socketMode + ", socketType=" + socketType + ", ioType=" + ioType + ", hostAddress=" + hostAddress + ", port=" + port + ", backlog=" + backlog
        + ", socketHandlerClassname=" + socketHandlerClassname + ", socketThreaderClassname=" + socketThreaderClassname + ", socketProcessorClassname=" + socketProcessorClassname
        + ", socketControllerClassname=" + socketControllerClassname + ", sslProtocol=" + sslProtocol + ", sslAlgorithm=" + sslAlgorithm + ", sslProvider=" + sslProvider + ", sslKeyStoreType="
        + sslKeyStoreType + ", sslKeystoreFile=" + sslKeystoreFile + "]";
  }

}
