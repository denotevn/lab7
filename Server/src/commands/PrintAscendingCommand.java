package commands;

import exception.MarineNotFoundException;
import exception.WrongAmountOfElementException;
import interaction.User;
import utility.CollectionManager;
import utility.Outputer;
import utility.ResponseOutputer;

public class PrintAscendingCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    public PrintAscendingCommand(CollectionManager collectionManager){
        super("print_ascending"," ","display the elements of the collection in ascending order");
        this.collectionManager = collectionManager;
    }
    @Override
    public boolean executed(String argument, Object commandObjectArgument, User user) {
        try {
            if (!argument.isEmpty() || commandObjectArgument != null) throw new WrongAmountOfElementException();
            if (collectionManager.collectionSize() == 0) throw new MarineNotFoundException();
            collectionManager.printAscending();
            ResponseOutputer.appendln("Success !");
            return true;
        } catch (WrongAmountOfElementException e) {
            ResponseOutputer.appendln("Using: "+ getName()+" "+getUsage());
        } catch (MarineNotFoundException e) {
            e.printStackTrace();
            ResponseOutputer.appenderror("Collection is null.");
        }
        return false;
    }
}
