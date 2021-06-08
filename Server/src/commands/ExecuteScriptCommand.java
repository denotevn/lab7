package commands;

import exception.WrongAmountOfElementException;
import interaction.User;
import utility.ResponseOutputer;


/**
 * Command 'execute_script'. Executes scripts from a file. Actually only checks argument and prints messages.
 */
public class ExecuteScriptCommand  extends AbstractCommand{
    public ExecuteScriptCommand(){
        super("execute_script file_name","","read and execute the script from the specified file." +
                " The script contains commands in the same form in which the user enters them interactively.");
    }

    @Override
    public boolean executed(String argument, Object objectArgument, User user) {
        try{
            if (argument.isEmpty() || objectArgument !=null) throw new WrongAmountOfElementException();
            return true;
        } catch (WrongAmountOfElementException e) {
            ResponseOutputer.appendln("Using: "+ getName() +" "+getUsage()+ " " );
        }
        return false;
    }
}
