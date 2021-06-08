package App;

import exception.NotInDeclaredLimitsException;
import exception.WrongFormatCommandException;
import utility.AuthHandler;
import utility.Outputer;
import utility.UserHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ApplicationClient {
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";

    private static final int RECONNECTION_TIMEOUT = 5 * 1000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host = "127.0.0.1";
    //            "se.ifmo.ru";
    //    = "127.0.0.1";
    private static int port = 6789;
    public static void main(String[] args) throws IOException {
        Outputer.println("Welcome to our app !");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        AuthHandler authHandler = new AuthHandler(reader);
        UserHandler userHandler = new UserHandler(reader);

        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler, authHandler);
        client.run();
        reader.close();
        //        Client client = new Client(hostAndPort[0], port, RECONNECTION_TIMEOUT,MAX_RECONNECTION_ATTEMPTS, userHandler);
    }
    private static boolean initializeConnectionAddress(String []hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length != 2) throw new WrongFormatCommandException();
            host = hostAndPortArgs[0];
            port = Integer.parseInt(hostAndPortArgs[1]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongFormatCommandException e) {
            String jarName = new java.io.File(ApplicationClient.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Using: 'java -jar " + jarName + " <host> <port>'");
        } catch (NotInDeclaredLimitsException e) {
            Outputer.printerror("Port must be a number!");
        } catch (NumberFormatException e) {
            Outputer.printerror("Port can't be negative");
        }
        return false;
    }
}

