package server;

import commands.*;
import exception.NotInDeclaredLimitsException;
import exception.WrongFormatCommandException;
import utility.*;

import java.util.logging.*;

public class AppServer {
    private static final int MAX_CLIENTS = 999;

    public static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static String databaseHost;
    private static String databaseUrl = "jdbc:postgresql://localhost:5432/studs";
    private static String databaseUserName = "postgres";
    private static String databasePassword = "123456789";

    private static int port = 6789;
//    private static String databaseHost;
//    private static String databaseURL = "jdbc:postgresql://localhost:5678/studs";
//    private static String databaseUsername = "postgres";
//    private static String databasePassword = "postgres";

    public static void main(String[] args) {
       // if (!initialize(args)) return;
        DatabaseHandler databaseHandler = new DatabaseHandler(databaseUrl, databaseUserName, databasePassword);
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseHandler);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseHandler, databaseUserManager);
        InputChek check = new InputChek();
        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);

        CommandManager commandManager = new CommandManager(
                new AddCommand(collectionManager, databaseCollectionManager),
                new AddIfMinCommand(collectionManager, databaseCollectionManager),
                new ClearCommand(collectionManager, databaseCollectionManager),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new HelpCommand(),
                new InfoCommand(collectionManager),
                new MaxByHealthCommand(collectionManager),
                new PrintAscendingCommand(collectionManager),
                new RemoveByHealthCommand(collectionManager, check, databaseCollectionManager),
                new RemoveByIdCommand(collectionManager, check, databaseCollectionManager),
                new RemoveLowerCommand(collectionManager, databaseCollectionManager),
                new ShowCommand(collectionManager),
                new SortCommand(collectionManager),
                new UpdateIdCommand(collectionManager, check, databaseCollectionManager),
                new ServerExitCommand(),
                new LoginCommand(databaseUserManager),
                new RegisterCommand(databaseUserManager)
        );
        Server server = new Server(port, MAX_CLIENTS, commandManager);
        server.run();
        databaseHandler.closeConnection();
    }

    private static boolean initialize(String[] args) {
        try {
            if (args.length != 3) throw new WrongFormatCommandException();
            port = Integer.parseInt(args[0]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            databaseHost = args[1];
            databasePassword = args[2];
            databaseUrl = "jdbc:postgresql://" + databaseHost + ":5678/studs";
//            databaseURL = "jdbc:postgresql://" + databaseHost + ":5678/studs";
            return true;
        } catch (WrongFormatCommandException exception) {
            String jarName = new java.io.File(AppServer.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Using: 'java -jar " + jarName + " <port> <db_host> <db_password>'");
        } catch (NumberFormatException exception) {
            Outputer.printerror("The port must be represented by a number!");
            AppServer.LOGGER.severe("The port must be represented by a number!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("The port cannot be negative!");
            AppServer.LOGGER.severe("The port cannot be negative!");
        }
        AppServer.LOGGER.severe("Startup port initialization error!");
        return false;
    }
}
