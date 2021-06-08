package server;

import exception.ConnectionErrorException;
import utility.CommandManager;
import utility.ConnectionHandler;
import utility.Outputer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Server {
    private int port ;
    private CommandManager commandManager;
    private boolean isStopped;
    private Semaphore semaphore;
    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    public Server (int port, int maxClients, CommandManager commandManager){
        this.port = port;
        this.commandManager = commandManager;
        this.semaphore =new Semaphore(maxClients);
    }
/** dung de su li du lieu */
    public void run(){
        try {
            while(!isStopped){
                try{
                    acquireConnection();
                    if (isStopped())throw new ConnectionErrorException();
                    forkJoinPool.invoke(new ConnectionHandler(this,port,commandManager));
                } catch (ConnectionErrorException e) {
                    break;
                }
            }
            forkJoinPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            Outputer.println("Server shutdown!");
            AppServer.LOGGER.severe("Server shutdown !");
        } catch (InterruptedException e) {
            Outputer.printerror ("An error occurred while shutting down with already connected clients!");
        }
    }
    /**
     * Acquire connection.
     */
    public void acquireConnection(){
        try{
            // yeu cau lay 1 ticket de su dung
            semaphore.acquire();
            AppServer.LOGGER.info("Connected !");
        } catch (InterruptedException e) {
            Outputer.printerror ("An error occurred while getting permission for a new connection!");
            AppServer.LOGGER.severe("An error occurred while getting permission for a new connection!");
            AppServer.LOGGER.severe("An error occurred while getting permission for a new connection!");
        }
    }
    /**
     * Release connection.
     */
    public void releaseConnection(){
        //tra 1 ticket ve Semaphore
        semaphore.release();
        Logger.getLogger("  ");
    }

    public synchronized  void stop(){
        AppServer.LOGGER.severe("done task in server. ");
        isStopped = true;
        forkJoinPool.shutdown();
        Outputer.println("Finishing work with already connected clients.");
        AppServer.LOGGER.severe("Server shutdown !");
    }
    /**
     * Checked stops of server.
     *
     * @return Status of server stop.
     */
    private synchronized boolean isStopped() {
        return isStopped;
    }
}
