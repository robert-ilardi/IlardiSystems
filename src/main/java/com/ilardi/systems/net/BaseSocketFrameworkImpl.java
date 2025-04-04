/**
 * Created Jan 18, 2021
 */
package com.ilardi.systems.net;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author rilardi
 *
 */

public class BaseSocketFrameworkImpl implements SocketFrameworkApi {

  protected static final Logger logger = LogUtil.getInstance().getLogger(BaseSocketFrameworkImpl.class);

  protected static final String PROP_SOCKET_MODE = "SocketFramework.SocketMode";
  protected static final String PROP_SOCKET_TYPE = "SocketFramework.SocketType";
  protected static final String PROP_IO_TYPE = "SocketFramework.IoType";

  protected static final String PROP_PORT = "SocketFramework.Port";
  protected static final String PROP_HOST_ADDRESS = "SocketFramework.HostAddress";
  protected static final String PROP_BACKLOG = "SocketFramework.Backlog";

  protected static final String PROP_HANDLER_CLASSNAME = "SocketFramework.SocketHandlerClassname";
  protected static final String PROP_THREADER_CLASSNAME = "SocketFramework.SocketThreaderClassname";
  protected static final String PROP_PROCESSOR_CLASSNAME = "SocketFramework.SocketProcessorClassname";
  protected static final String PROP_CONTROLLER_CLASSNAME = "SocketFramework.SocketControllerClassname";

  protected static final String PROP_SSL_PROTOCOL = "SocketFramework.sslProtocol";
  protected static final String PROP_SSL_ALGORITHM = "SocketFramework.sslAlgorithm";
  protected static final String PROP_SSL_PROVIDER = "SocketFramework.sslProvider";
  protected static final String PROP_SSL_KEYSTORE_TYPE = "SocketFramework.sslKeyStoreType";
  protected static final String PROP_SSL_KEYSTORE_FILE = "SocketFramework.sslKeystoreFile";
  protected static final String PROP_SSL_KEYSTORE_PASSWORD = "SocketFramework.sslKeyStorePassword";

  protected SocketInfo socketInfo;

  protected SocketHandler socketHandler;

  protected SocketFrameworkController controller;

  protected SocketThreader threader;

  protected final Object frameworkLock;

  protected String frameworkPropsFilePath;

  protected Properties frameworkProps;

  public BaseSocketFrameworkImpl() {
    frameworkLock = new Object();
  }

  @Override
  public void init() throws IlardiNetException {
    synchronized (frameworkLock) {
      String tmp;

      try {
        logger.info("Initializing Socket Framework");

        socketInfo = createSocketInfo();

        tmp = (new StringBuilder()).append("Socket Framework Configuration: ").append(socketInfo).toString();
        logger.info(tmp);

        tmp = (new StringBuilder()).append("Initializing Socket Framework in ").append(socketInfo.getSocketMode()).append(" Mode...").toString();
        logger.info(tmp);

        socketHandler = createSocketHandler();

        if (socketInfo.getSocketControllerClassname() != null && socketInfo.getSocketControllerClassname().trim().length() > 0) {
          controller = createSocketFrameworkController();
          controller.init();
        }

        threader = createSocketThreader();
        threader.init();

        socketHandler.init();
      } // End try block
      catch (Exception e) {
        throw new IlardiNetException("An error occurred while attempting to Initialize Base Socket Framework. System Message: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public void startSocketHandling() throws IlardiNetException {
    synchronized (frameworkLock) {
      if (isHandlingSockets()) {
        System.err.println("Socket Framework is already Running!");
      }
      else {
        socketHandler.startSocketHandling();
      }
    }
  }

  @Override
  public void stopHandlingSockets() throws IlardiNetException {
    synchronized (frameworkLock) {
      if (!isHandlingSockets()) {
        System.err.println("Server is NOT Running!");
      }
      else {
        socketHandler.stopSocketHandling();
      }
    }
  }

  @Override
  public void destroy() throws IlardiNetException {
    synchronized (frameworkLock) {
      logger.info("Destroying Base Socket Framework");

      try {
        if (isHandlingSockets()) {
          stopHandlingSockets();
        }
      }
      catch (Exception e) {
        logger.error(e);
      }

      try {
        if (controller != null) {
          controller.destroy();
          controller = null;
        }
      }
      catch (Exception e) {
        logger.error(e);
      }

      try {
        if (threader != null) {
          threader.destroy();
          threader = null;
        }
      }
      catch (Exception e) {
        logger.error(e);
      }

      try {
        if (socketHandler != null) {
          socketHandler.destroy();
          socketHandler = null;
        }
      }
      catch (Exception e) {
        logger.error(e);
      }

      frameworkPropsFilePath = null;
      frameworkProps = null;
      socketInfo = null;
    } // End synchronized block
  }

  @Override
  public SocketInfo getSocketInfo() {
    return socketInfo;
  }

  @Override
  public void loadSocketFrameworkProperties(String frameworkPropsFilePath) throws IlardiNetException {
    FileInputStream fis = null;

    try {
      this.frameworkPropsFilePath = frameworkPropsFilePath;

      if (frameworkPropsFilePath != null) {
        logger.info((new StringBuilder()).append("Using Configuration File: ").append(this.frameworkPropsFilePath).toString());
      }

      fis = new FileInputStream(this.frameworkPropsFilePath);

      loadSocketFrameworkProperties(fis);
    }
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attempting to Load Socket Framework Properties from File Path = " + this.frameworkPropsFilePath + ". System Message: " + e.getMessage(), e);
    }
    finally {
      if (fis != null) {
        try {
          fis.close();
        }
        catch (Exception e) {
          logger.error(e);
        }
      }
    }
  }

  @Override
  public void loadSocketFrameworkProperties(InputStream ins) throws IlardiNetException {
    Properties props;

    try {
      props = new Properties();

      props.load(ins);

      setSocketFrameworkProperties(props);
    }
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attempting to Load Socket Framework Properties from Input Stream. System Message: " + e.getMessage(), e);
    }
  }

  @Override
  public void setSocketFrameworkProperties(Properties frameworkProps) {
    this.frameworkProps = frameworkProps;
  }

  @Override
  public SocketInfo createSocketInfo() throws IlardiNetException {
    SocketInfo socketInfo;
    String tmp;
    int num;
    SocketMode sockMode;
    SocketType sockType;
    IoType ioType;

    socketInfo = new SocketInfo();
    socketInfo.setConfig(frameworkProps);

    tmp = frameworkProps.getProperty(PROP_SOCKET_MODE);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        sockMode = SocketMode.valueOf(tmp);
        socketInfo.setSocketMode(sockMode);
      }
      else {
        throw new IlardiNetException("Socket Mode NOT Set! Please Check Socket Framework Properties.");
      }
    }
    else {
      throw new IlardiNetException("Socket Type Mode Set! Please Check Socket Framework Properties.");
    }

    tmp = frameworkProps.getProperty(PROP_SOCKET_TYPE);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        sockType = SocketType.valueOf(tmp);
        socketInfo.setSocketType(sockType);
      }
      else {
        throw new IlardiNetException("Socket Type NOT Set! Please Check Socket Framework Properties.");
      }
    }
    else {
      throw new IlardiNetException("Socket Type NOT Set! Please Check Socket Framework Properties.");
    }

    tmp = frameworkProps.getProperty(PROP_IO_TYPE);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        ioType = IoType.valueOf(tmp);
        socketInfo.setIoType(ioType);
      }
      else {
        throw new IlardiNetException("IO Type NOT Set! Please Check Socket Framework Properties.");
      }
    }
    else {
      throw new IlardiNetException("IO Type NOT Set! Please Check Socket Framework Properties.");
    }

    tmp = frameworkProps.getProperty(PROP_PORT);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        num = Integer.parseInt(tmp);
        socketInfo.setPort(num);
      }
      else {
        throw new IlardiNetException("Port NOT Set! Please Check Socket Framework Properties.");
      }
    }
    else {
      throw new IlardiNetException("Port NOT Set! Please Check Socket Framework Properties.");
    }

    tmp = frameworkProps.getProperty(PROP_HOST_ADDRESS);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        socketInfo.setHostAddress(tmp);
      }
      else {
        socketInfo.setHostAddress(null);
      }
    }
    else {
      socketInfo.setHostAddress(null);
    }

    tmp = frameworkProps.getProperty(PROP_BACKLOG);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        num = Integer.parseInt(tmp);
        socketInfo.setBacklog(num);
      }
      else {
        socketInfo.setBacklog(0);
      }
    }
    else {
      socketInfo.setBacklog(0);
    }

    tmp = frameworkProps.getProperty(PROP_HANDLER_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        socketInfo.setSocketHandlerClassname(tmp);
      }
      else {
        throw new IlardiNetException("Socket Handler Classname NOT Set! Please Check Socket Framework Properties.");
      }
    }
    else {
      throw new IlardiNetException("Socket Handler Classname NOT Set! Please Check Socket Framework Properties.");
    }

    tmp = frameworkProps.getProperty(PROP_THREADER_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        socketInfo.setSocketThreaderClassname(tmp);
      }
      else {
        throw new IlardiNetException("Socket Threader Classname NOT Set! Please Check Socket Framework Properties.");
      }
    }
    else {
      throw new IlardiNetException("Socket Threader Classname NOT Set! Please Check Socket Framework Properties.");
    }

    tmp = frameworkProps.getProperty(PROP_PROCESSOR_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        socketInfo.setSocketProcessorClassname(tmp);
      }
      else {
        throw new IlardiNetException("Socket Processor Classname NOT Set! Please Check Socket Framework Properties.");
      }
    }
    else {
      throw new IlardiNetException("Socket Processor Classname NOT Set! Please Check Socket Framework Properties.");
    }

    tmp = frameworkProps.getProperty(PROP_CONTROLLER_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        socketInfo.setSocketControllerClassname(tmp);
      }
      else {
        socketInfo.setSocketControllerClassname(null);
      }
    }
    else {
      socketInfo.setSocketControllerClassname(null);
    }

    tmp = frameworkProps.getProperty(PROP_SSL_PROTOCOL);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        socketInfo.setSslProtocol(tmp);
      }
      else {
        socketInfo.setSslProtocol(null);
      }
    }
    else {
      socketInfo.setSslProtocol(null);
    }

    tmp = frameworkProps.getProperty(PROP_SSL_ALGORITHM);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        socketInfo.setSslAlgorithm(tmp);
      }
      else {
        socketInfo.setSslAlgorithm(null);
      }
    }
    else {
      socketInfo.setSslAlgorithm(null);
    }

    tmp = frameworkProps.getProperty(PROP_SSL_PROVIDER);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        socketInfo.setSslProvider(tmp);
      }
      else {
        socketInfo.setSslProvider(null);
      }
    }
    else {
      socketInfo.setSslProvider(null);
    }

    tmp = frameworkProps.getProperty(PROP_SSL_KEYSTORE_TYPE);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        socketInfo.setSslKeyStoreType(tmp);
      }
      else {
        socketInfo.setSslKeyStoreType(null);
      }
    }
    else {
      socketInfo.setSslKeyStoreType(null);
    }

    tmp = frameworkProps.getProperty(PROP_SSL_KEYSTORE_FILE);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        socketInfo.setSslKeystoreFile(tmp);
      }
      else {
        socketInfo.setSslKeystoreFile(null);
      }
    }
    else {
      socketInfo.setSslKeystoreFile(null);
    }

    tmp = frameworkProps.getProperty(PROP_SSL_KEYSTORE_PASSWORD);
    if (tmp != null) {
      tmp = tmp.trim();

      socketInfo.setSslKeyStorePassword(tmp);
    }
    else {
      socketInfo.setSslKeyStorePassword(null);
    }

    return socketInfo;
  }

  @Override
  @SuppressWarnings("unchecked")
  public SocketFrameworkController createSocketFrameworkController() throws IlardiNetException {
    SocketFrameworkController sfc;
    String sfccn;
    Class<SocketFrameworkController> sfcc;

    try {
      sfccn = socketInfo.getSocketControllerClassname();

      sfcc = (Class<SocketFrameworkController>) Class.forName(sfccn);

      sfc = sfcc.newInstance();

      return sfc;
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attempting to Create Socket Framework Controller. System Message: " + e.getMessage(), e);
    }
  }

  @Override
  public SessionContext createSessionContext(Object connectionObj, SocketSession clientSession, SocketReader clientReader, SocketWriter clientWriter) throws IlardiNetException {
    synchronized (frameworkLock) {
      String sessionId = generateSessionId(connectionObj);

      SessionContext sessionCtx = new SessionContext(sessionId, socketInfo, clientSession, clientReader, clientWriter);

      sessionCtx.init();

      return sessionCtx;
    }
  }

  @Override
  public SocketSession createSocketSession(Object connectionObj) throws IlardiNetException {
    SocketSession session;

    session = new SocketSession();

    return session;
  }

  @Override
  public String generateSessionId(Object connectionObj) throws IlardiNetException {
    String sessionId;
    UUID uuid;

    uuid = UUID.randomUUID();
    sessionId = uuid.toString();

    return sessionId;
  }

  @Override
  public void closeSession(SessionContext sessionCtx) throws IlardiNetException {
    synchronized (frameworkLock) {
      String sessionId = sessionCtx.getSessionId();

      String tmp = (new StringBuilder()).append("Closing Session ").append(sessionId).toString();
      logger.info(tmp);

      try {
        onDisconnect(sessionCtx);
      }
      catch (Exception e) {
        logger.error(e);
      }

      sessionCtx.close();
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public SocketProcessor createSocketProcessor() throws IlardiNetException {
    SocketProcessor sp;
    String spcn;
    Class<SocketProcessor> spc;

    try {
      spcn = socketInfo.getSocketProcessorClassname();

      spc = (Class<SocketProcessor>) Class.forName(spcn);

      sp = spc.newInstance();
      sp.setSocketThreader(threader);

      return sp;
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attempting to Create Socket Processor. System Message: " + e.getMessage(), e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public SocketThreader createSocketThreader() throws IlardiNetException {
    SocketThreader sst;
    String sstcn;
    Class<SocketThreader> sstc;

    try {
      sstcn = socketInfo.getSocketThreaderClassname();

      sstc = (Class<SocketThreader>) Class.forName(sstcn);

      sst = sstc.newInstance();
      sst.setSocketFramework(this);

      return sst;
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attempting to Create Socket Threader. System Message: " + e.getMessage(), e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public SocketHandler createSocketHandler() throws IlardiNetException {
    SocketHandler sh;
    String shcn;
    Class<SocketHandler> shc;

    try {
      shcn = socketInfo.getSocketHandlerClassname();

      shc = (Class<SocketHandler>) Class.forName(shcn);

      sh = shc.newInstance();
      sh.setSocketFramework(this);

      return sh;
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attempting to Create Socket Handler. System Message: " + e.getMessage(), e);
    }
  }

  @Override
  public boolean isHandlingSockets() {
    return socketHandler != null && socketHandler.isHandlingSockets();
  }

  @Override
  public void waitWhileIsHandlingSockets() throws IlardiNetException {
    socketHandler.waitWhileIsHandlingSockets();
  }

  @Override
  public void onConnect(SessionContext clientContext) throws IlardiNetException {
    final LocalDateTime ldt;
    String tmp = (new StringBuilder()).append("Socket Framework Executing onConnect for Session = ").append(clientContext.getSessionId()).toString();
    logger.info(tmp);

    threader.onConnect(clientContext);

    if (controller != null) {
      controller.onConnect(clientContext);
    }
  }

  @Override
  public void onDisconnect(SessionContext clientContext) throws IlardiNetException {
    String tmp = (new StringBuilder()).append("Socket Framework Executing onDisconnect for Session = ").append(clientContext.getSessionId()).toString();
    logger.info(tmp);

    if (threader != null) {
      threader.onDisconnect(clientContext);
    }

    if (controller != null) {
      controller.onDisconnect(clientContext);
    }
  }

  @Override
  public void raiseSocketDataSignal(SessionContext clientContext) throws IlardiNetException {
    final LocalDateTime ldt;
    String tmp = (new StringBuilder()).append("Socket Framework Executing onData for Session = ").append(clientContext.getSessionId()).toString();
    logger.trace(tmp);

    if (controller != null) {
      controller.onData(clientContext);
    }
  }

  @Override
  public void raiseSocketDisconnectSignal(SessionContext clientContext) throws IlardiNetException {
    String tmp, sessionId;

    if (clientContext == null) {
      return;
    }

    sessionId = clientContext.getSessionId();

    tmp = (new StringBuilder()).append("Socket Framework received raiseSocketDisconnectSignal for Session = ").append(sessionId).toString();
    logger.info(tmp);

    closeSession(clientContext);
  }

}
