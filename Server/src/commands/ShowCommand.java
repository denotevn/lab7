package commands;

import exception.WrongAmountOfElementException;
import interaction.User;
import utility.CollectionManager;
import utility.ResponseOutputer;
/**
 * Command 'show'. Shows information about all elements of the collection.
 */
public class ShowCommand extends AbstractCommand{
    private CollectionManager collectionManager ;

    public ShowCommand(CollectionManager collectionManager1){
        super("show","","print to standard output all elements of " +
                "the collection in string representation");
        this.collectionManager = collectionManager1;
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean executed(String argument, Object commandObjectArgument, User user) {
        try{
            if (!argument.isEmpty() || commandObjectArgument !=null ) throw new WrongAmountOfElementException();
            ResponseOutputer.appendln(collectionManager.showCollection());
            return true;
        } catch (WrongAmountOfElementException e) {
            System.out.println("catch");
            ResponseOutputer.appenderror("Entered command or data not found. Please check again");
            e.printStackTrace();
        }
        return false;
    }
}
