package commands;

import data.SpaceMarine;
import exception.CollectionIsEmptyException;
import exception.DatabaseHandlingException;
import exception.MarineNotFoundException;
import exception.WrongAmountOfElementException;
import interaction.User;
import utility.*;

import java.util.Stack;

public class RemoveByHealthCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private InputChek inPutCheck;
    private DatabaseCollectionManager databaseCollectionManager;
    public RemoveByHealthCommand(CollectionManager collectionManager, InputChek inPutCheck,
                                 DatabaseCollectionManager databaseCollectionManager){
        super("remove_all_by_health health"," ",
                " health field value is equivalent to the specified one");
        this.inPutCheck = inPutCheck;
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }
    @Override
    public boolean executed(String argument, Object commandObjectArgument, User user) {
        try{
            if (argument.isEmpty() || commandObjectArgument != null) throw new WrongAmountOfElementException();
            if (collectionManager.getCollection().isEmpty()) throw new CollectionIsEmptyException();

            Long health = Long.parseLong(argument);
            Stack<SpaceMarine> marineToRemove = collectionManager.getByHealth(health);

            if (marineToRemove == null) throw new MarineNotFoundException();
            if (inPutCheck.longInValidCheck(argument,(long)0,Long.MAX_VALUE)){
                databaseCollectionManager.deleteMarineByHealth(health);
                collectionManager.removeListFromCollection(marineToRemove);
                ResponseOutputer.appendln("Marine health of "+ health + " has been removed");
                return true;
            }
        } catch (MarineNotFoundException e) {
            ResponseOutputer.appenderror("Marine is not found.");
        } catch (WrongAmountOfElementException e) {
            ResponseOutputer.appenderror("The command entered was incorrect. Click help for more details");
            e.printStackTrace();
        } catch (CollectionIsEmptyException e) {
            e.printStackTrace();
        } catch (DatabaseHandlingException e) {
            ResponseOutputer.appenderror ("An error occurred while accessing the database!");
        }
        return false;
    }
}
