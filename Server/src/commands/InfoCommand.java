package commands;

import exception.WrongAmountOfElementException;
import interaction.User;
import utility.CollectionManager;
import utility.Outputer;
import utility.ResponseOutputer;

import java.util.Collections;

public class InfoCommand  extends AbstractCommand{
    private CollectionManager collectionManager;
    public InfoCommand(CollectionManager collectionManager1){
        super("info","","print information about the collection (type, " +
                "date of initialization, number of elements, etc.) to standard output");
        this.collectionManager = collectionManager1;
    }
    @Override
    public boolean executed(String argument, Object objectArgument, User user) {
        try {
            ResponseOutputer.appendln("show info command: ");
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementException();
            ResponseOutputer.appendln("collection Type: Stack");
            ResponseOutputer.appendln("Date now: " + java.time.Clock.systemUTC().instant());
            ResponseOutputer.appendln("Size of collection: "+collectionManager.collectionSize());
            return true;
        } catch (WrongAmountOfElementException e) {
            ResponseOutputer.appendln("Invalid command.");
            ResponseOutputer.appendln("Using"+" "+ getName()+" "+getUsage());
        }
        return false;
    }
}
