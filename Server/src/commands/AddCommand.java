package commands;

import exception.DatabaseHandlingException;
import exception.WrongAmountOfElementException;
import interaction.MarineRaw;
import interaction.User;
import server.AppServer;
import utility.*;


import java.sql.SQLException;


public class AddCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;


    /**
     * Command 'add'. Adds a new element to collection.
     */

    public AddCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add ","{element}", "add a new item to the collection.");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager =  databaseCollectionManager;
    }
    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean executed(String stringArgument, Object objectArgument, User user) {
        try{
            if (!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementException();
            MarineRaw marineRaw = (MarineRaw) objectArgument;
//            System.out.println("IM here in addCommand");
//            collectionManager.getListMarine().add(databaseCollectionManager.insertMarine(marineRaw,user));
            collectionManager.addToCollection(databaseCollectionManager.insertMarine(marineRaw,user));

            ResponseOutputer.appendln("Marine added successfully !");
            return true;
        } catch (WrongAmountOfElementException e) {
            ResponseOutputer.appenderror("Using: '" + getName() + "'");
            ResponseOutputer.appenderror("The command entered is incorrect, " +
                    "please press help for more details");
        } catch (DatabaseHandlingException e) {
            ResponseOutputer.appenderror ("An error occurred while accessing the database in add command !");
            AppServer.LOGGER.severe("problem in class AddCommand !");
        }
        return false;
    }
}
