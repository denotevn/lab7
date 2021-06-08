package commands;

import exception.WrongAmountOfElementException;
import interaction.User;
import utility.CollectionManager;
import utility.Outputer;
import utility.ResponseOutputer;

public class HelpCommand extends AbstractCommand {
    public HelpCommand(){
        super("help", "","display help for available commands");
    }
    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean executed(String argument, Object objectArgument, User user) {
        try{
            if (!argument.isEmpty()) throw new WrongAmountOfElementException();
            return true;
        } catch (WrongAmountOfElementException e) {
            e.printStackTrace();
            ResponseOutputer.appendln("Using: "+getName()+" "+getUsage()+" ");
        }
        return false;
    }
}
