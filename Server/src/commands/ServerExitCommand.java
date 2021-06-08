package commands;

import exception.WrongAmountOfElementException;
import interaction.User;
import utility.ResponseOutputer;

public class ServerExitCommand extends AbstractCommand{
    public ServerExitCommand() {
        super("server_exit", "", "shutdown the server");
    }

    @Override
    public boolean executed(String argument, Object commandObjectArgument, User user) {
        try {
            if (!argument.isEmpty() || commandObjectArgument != null) throw new WrongAmountOfElementException();
            ResponseOutputer.appendln("The server has been successfully completed!");
            return true;
        } catch (WrongAmountOfElementException exception) {
            ResponseOutputer.appendln("Using: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
