/**
 * Created Jan 9, 2021
 */
package com.ilardi.systems.net;

/**
 * @author rilardi
 *
 */

public class BlockingThreadSocketProcessorContainer {

  private SocketThreader threader;

  private final Object containerLock;

  private SessionContext clientContext;

  private SocketProcessor processor;

  private Thread containerThread;

  private boolean runContainer = false;
  private boolean containerThreadRunning = false;

  public BlockingThreadSocketProcessorContainer() {
    containerLock = new Object();
  }

  public void setSocketThreader(SocketThreader threader) {
    this.threader = threader;
  }

  public void setClientContext(SessionContext clientContext) {
    this.clientContext = clientContext;
  }

  public void setSocketProcessor(SocketProcessor processor) {
    this.processor = processor;
  }

  public void init() throws IlardiNetException {
    try {
      System.out.println("BlockingThreadSocketProcessorContainer Executing Init");

      synchronized (containerLock) {
        runContainer = true;

        containerThread = new Thread(containerRunner);
        containerThread.start();

        while (!containerThreadRunning) {
          containerLock.wait();
        }
      }
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attemping to Initialize Blocking Thread Socket Processor Container! System Message: " + e.getMessage(), e);
    }
  }

  public void shutdown() throws IlardiNetException {
    synchronized (containerLock) {
      System.out.println("BlockingThreadSocketProcessorContainer Executing Shutdown");

      runContainer = false;

      if (containerThreadRunning) {
        clientContext.interruptReader();

        try {
          while (containerThreadRunning) {
            containerLock.wait();
          }
        } // End try block
        catch (Exception e) {
          e.printStackTrace();
        }
      }

      threader = null;
      clientContext = null;
      processor = null;
      containerThread = null;
    }
  }

  private Runnable containerRunner = new Runnable() {
    @Override
    public void run() {
      byte[] data;
      boolean socketClosed = false;

      System.out.println("BlockingThreadSocketProcessorContainer Entering Container Runner Process Loop");

      try {
        synchronized (containerLock) {
          containerThreadRunning = true;
          containerLock.notifyAll();
        }

        while (runContainer) {
          data = clientContext.read();

          if (data != null) {
            synchronized (containerLock) {
              if (runContainer) {
                clientContext.enqueueData(data);

                processor.process(clientContext);

                threader.raiseSocketDataSignal(clientContext);
              }
            }
          }
          else {
            // NULL Data Return - Socket Closed by Client
            socketClosed = true;
            break;
          }
        } // End while (runContainer)
      } // End try block
      catch (Exception e) {
        e.printStackTrace();
      }
      finally {
        synchronized (containerLock) {
          containerThreadRunning = false;
          containerLock.notifyAll();
        }

        try {
          if (socketClosed) {
            // Socket Closed by Client
            System.out.println("Socket Closed by Peer...");
            threader.raiseSocketDisconnectSignal(clientContext);
          }
        } // End try block
        catch (Exception e) {
          e.printStackTrace();
        }
      } // End finally block

      System.out.println("BlockingThreadSocketProcessorContainer Exiting Container Runner Process Loop");
    } // End run method
  };

}
