
package utility;

import App.Client;
import server.AppServer;
import server.Server;
import interaction.Request;
import interaction.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.*;

public class ConnectionHandler extends RecursiveAction {
    private Server server;
    private  int port;
    private DatagramChannel channel;
    private CommandManager commandManager;
    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private final ExecutorService sendCachedThreadPool = Executors.newCachedThreadPool();

    public ConnectionHandler(Server server, int port, CommandManager commandManager) {
        this.server = server;
        this.port = port;
        this.commandManager = commandManager;
    }

    @Override
    protected void compute() {
        boolean stopFlag = false;
        Con con = new Con();
        try {
            AppServer.LOGGER.info("Server starting on port " + port);

            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(port));
            channel.configureBlocking(false); //  thiet lap che do khong chan

            Selector selector = Selector.open();
            SelectionKey cliKey = channel.register(selector, SelectionKey.OP_READ);
            cliKey.attach(con);

            while (channel.isOpen()) {
                try {
                    if (selector.selectNow() != 0) {
                        Iterator<SelectionKey> iter = selector.selectedKeys().iterator();

                        while (iter.hasNext()) {
                            try {
                                SelectionKey key = iter.next();
                                iter.remove();

                                if (!key.isValid()) {
                                    continue;
                                }

                                if (key.isReadable()) {
                                    read(key);
                                    key.interestOps(SelectionKey.OP_WRITE);
                                }

                                if (key.isWritable()) {
                                    write(key);
                                    key.interestOps(SelectionKey.OP_READ);
                                }

                            } catch (IOException e) {
                                System.err.println("glitch, continuing... " + (e.getMessage() != null ? e.getMessage() : ""));
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("glitch, continuing... " + (e.getMessage() != null ? e.getMessage() : ""));
                }
            }
            selector.close();
        } catch (ClassNotFoundException exception) {
            Outputer.printerror("An error occurred while reading the received data!");
            AppServer.LOGGER.severe("An error occurred while reading the received data!");
        } catch (InvalidClassException | NotSerializableException exception) {
            Outputer.printerror("An error occurred while sending data to the client!");
            AppServer.LOGGER.severe("An error occurred while sending data to the client!");
        } catch (IOException exception) {
            if (con.request == null) {
                Outputer.printerror("Unexpected disconnection from the client!");
                AppServer.LOGGER.severe("Unexpected disconnection from the client!");
            } else {
                Outputer.println("The client has been successfully disconnected from the server!");
                AppServer.LOGGER.info("The client has been successfully disconnected from the server!");
            }
        } finally {
            try {
              //  sendCachedThreadPool.shutdown();
                channel.close();
                Outputer.println("Client disconnected from server");
                AppServer.LOGGER.info("Client disconnected from server");
            } catch (IOException exception) {
                Outputer.printerror("An error occurred while trying to end the connection with the client!");
                AppServer.LOGGER.severe("An error occurred while trying to end the connection with the client!");
            }
            if (stopFlag) server.stop();
            server.releaseConnection();
        }
    }

//read(DatagramChannel channel, Con con)
    /**su dung forkJoinpool de doc yeu cau tu client*/
    private void read(SelectionKey key) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        DatagramChannel channel = (DatagramChannel)key.channel();
        Con con = (Con)key.attachment();

        ByteBuffer buf = ByteBuffer.allocate(2048);
        con.sa = channel.receive(buf);
        AppServer.LOGGER.info("Receiving a request...");

        byte[] arr = buf.array();
        ByteArrayInputStream bais = new ByteArrayInputStream(arr);
        ObjectInputStream ois = new ObjectInputStream(bais);

        con.request = (Request) ois.readObject();
        Future<Response> responseFuture = forkJoinPool.submit(new HandlerRequestTask(con.request, commandManager));
        con.response = responseFuture.get();

        AppServer.LOGGER.info("Processing request " + con.request.getCommandName() + "...");
        ois.close();
    }

    //write(DatagramChannel channel, Con con, SocketAddress addr)
    /**tao thread moi de gui ket qua den nguoi dung*/
    private void write(SelectionKey key) {
        Runnable thread = ()->{
            try{
                DatagramChannel channel = (DatagramChannel) key.channel();
                Con con = (Con) key.attachment();

                ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
                oos.writeObject(con.response);
                oos.flush();

                byte[] arr = baos.toByteArray();
                ByteBuffer buf = ByteBuffer.wrap(arr);
                channel.send(buf, con.sa);
                AppServer.LOGGER.info("Sending a response...");

                oos.close();
            }catch (IOException e) {
                Outputer.printerror ("An error occurred while sending data to the client!");
                AppServer.LOGGER.severe("An error occurred while sending data to the client!");
            }
        };
        Thread thread1 = new Thread(thread);
        thread1.start();

//            sendCachedThreadPool.submit(() -> {
//            try{
//                System.out.println("hi from write");
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
//                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
//                oos.writeObject(con.response);
//                oos.flush();
//
//                byte[] arr = baos.toByteArray();
//                ByteBuffer buf = ByteBuffer.wrap(arr);
//
//                channel.send(buf, con.sa);
//
//                System.out.println("sent already");
//
//                oos.close();
//            } catch (IOException e) {
//                Outputer.printerror ("An error occurred while sending data to the client!");
//                AppServer.LOGGER.severe("An error occurred while sending data to the client!");
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//        });
//        thread.start();
    }

    static class Con {
        Request request;
        Response response;
        SocketAddress sa;
    }


}
