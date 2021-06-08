package commands;

import data.SpaceMarine;
import exception.DatabaseHandlingException;
import exception.ManualDatabaseEditException;
import exception.PermissionDeniedException;
import exception.WrongAmountOfElementException;
import interaction.User;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.Outputer;
import utility.ResponseOutputer;
/**
 * Command 'clear'. Clears the collection.
 */
public class ClearCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public ClearCommand(CollectionManager collectionManager1,DatabaseCollectionManager databaseCollectionManager){
        super("clear","","clear collection");
        this.collectionManager = collectionManager1;
        this.databaseCollectionManager = databaseCollectionManager;
    }
    @Override
    public boolean executed(String argument, Object commandObjectArgument, User user) {
        try{
            if (!argument.isEmpty() || commandObjectArgument != null) throw new WrongAmountOfElementException();
            for (SpaceMarine marine : collectionManager.getCollection()){
                if (!marine.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkMarineUserId(marine.getId(), user))
                    throw new ManualDatabaseEditException();
            }
            databaseCollectionManager.clearCollection();
            collectionManager.getCollection().clear();
            ResponseOutputer.appendln("Cleared successfully!");
            return true;
        } catch (DatabaseHandlingException e) {
            ResponseOutputer.appenderror ("An error occurred while accessing the database!");
        } catch (WrongAmountOfElementException exception) {
            ResponseOutputer.appendln("Using: '" + getName() + " " + getUsage() + "'");
        } catch (ManualDatabaseEditException e) {
            ResponseOutputer.appenderror("There was a direct change in the basics of the data!");
            ResponseOutputer.appendln("Restart the client for possible errors.");
        } catch (PermissionDeniedException e) {
            ResponseOutputer.appenderror ("Insufficient rights to execute this command!");
            ResponseOutputer.appendln ("Objects owned by other users are read-only.");;
        }
        return false;
    }
}
