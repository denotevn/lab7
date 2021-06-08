package commands;

import exception.WrongAmountOfElementException;
import interaction.User;
import utility.ResponseOutputer;
/**
 * Command 'exit'. Checks for wrong arguments then do nothing.
 */
public class ExitCommand  extends AbstractCommand{
    public ExitCommand() {
        super("exit", "", "shutdown client");
    }
    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean executed(String argument, Object commandObjectArgument, User user) {
        try{
            if(!argument.isEmpty() || commandObjectArgument == null) throw new WrongAmountOfElementException();
            return true;
        }catch(WrongAmountOfElementException exception){
            ResponseOutputer.appendln("Using: "+ getName() + " "+ getUsage()+" ");
        }
        return false;
    }
}
