package commands;

import exception.MarineIsEmptyCollection;
import exception.WrongAmountOfElementException;
import interaction.User;
import utility.CollectionManager;
import utility.Outputer;
import utility.ResponseOutputer;

public class MaxByHealthCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    public MaxByHealthCommand(CollectionManager collectionManager){
        super("max_by_health","","deduce any object from the collection whose health field value is the maximum");
        this.collectionManager = collectionManager;
    }
    @Override
    public boolean executed(String argument, Object commandObjectArgument, User user) {
        try{
            System.out.println("den day");
            if (!argument.isEmpty()) throw new WrongAmountOfElementException();
            if (collectionManager.collectionSize() == 0)  {
                ResponseOutputer.appenderror("Marine is not found !");
            }
            ResponseOutputer.appendln(collectionManager.maxByHealth());
            ResponseOutputer.appendln("Success use command maxByHealth");
            return true;
        } catch (WrongAmountOfElementException e) {
            ResponseOutputer.appendln("Error");
            ResponseOutputer.appendln("Using: '" + getName() + " " + getUsage() + "'");
        } catch (MarineIsEmptyCollection marineIsEmptyCollection) {
            ResponseOutputer.appenderror("Collection is null");
            marineIsEmptyCollection.printStackTrace();
        }
        return false;
    }
}
