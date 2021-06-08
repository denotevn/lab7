package commands;

import data.SpaceMarine;
import exception.*;
import interaction.User;
import server.AppServer;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.InputChek;
import utility.ResponseOutputer;

public class RemoveByIdCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private final  InputChek inPutCheck;
    private DatabaseCollectionManager databaseCollectionManager;
    public RemoveByIdCommand(CollectionManager collectionManager1, InputChek inPutCheck,
                             DatabaseCollectionManager databaseCollectionManager) {
        super("remove_by_id id", "", "remove an item from the collection by its id");
        this.collectionManager = collectionManager1;
        this.inPutCheck = inPutCheck;
        this.databaseCollectionManager = databaseCollectionManager;
    }
    @Override
    public boolean executed(String argument, Object commandObjectArgument, User user) {
        try{
            if (argument.isEmpty() || commandObjectArgument !=null) throw new WrongFormatCommandException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            long id = Long.parseLong(argument);
            SpaceMarine marineToRemove = collectionManager.getMarineById(id);

            if (marineToRemove == null) throw new MarineNotFoundException();
            if (!marineToRemove.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkMarineUserId(id,user)) throw new ManualDatabaseEditException();

            databaseCollectionManager.deleteMarineById(marineToRemove.getId());
            collectionManager.removeFromCollection(marineToRemove);

            ResponseOutputer.appendln("Successfully deleted !");
            return true;
        } catch (WrongFormatCommandException e) {
            ResponseOutputer.appenderror("The inserting ID is not in valid range! Please insert Id greater than 0!");
        } catch (ManualDatabaseEditException e) {
            AppServer.LOGGER.severe("An error occurred in ManualDatabaseEditException in Command RemoveById.");
        } catch (DatabaseHandlingException e) {
            e.printStackTrace();
        } catch (CollectionIsEmptyException e) {
            e.printStackTrace();
        } catch (MarineNotFoundException e) {
            e.printStackTrace();
        } catch (PermissionDeniedException e) {
            ResponseOutputer.appenderror("Not permission!");
        }
        return false;
    }
}
