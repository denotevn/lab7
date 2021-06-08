package commands;

import exception.DatabaseHandlingException;
import exception.UserIsNotFoundException;
import exception.WrongAmountOfElementException;
import interaction.User;
import utility.DatabaseUserManager;
import utility.ResponseOutputer;

public class LoginCommand  extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;

    public LoginCommand(DatabaseUserManager databaseUserManager) {
        super("login", "", "internal team.");
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean executed(String argument, Object commandObjectArgument, User user) {
        try{
            if (!argument.isEmpty() || commandObjectArgument != null) throw new WrongAmountOfElementException();
            if (databaseUserManager.checkUserByUsernameAndPassword(user)) ResponseOutputer.appendln("User" +
                    user.getUsername() + "authorized.");
            else throw new UserIsNotFoundException();
            return true;
        } catch (WrongAmountOfElementException exception) {
            ResponseOutputer.appendln ("Usage: ummm ... uh ... this is an internal command ...");
        } catch (UserIsNotFoundException e) {
            ResponseOutputer.appenderror ("Wrong username or password!");
        } catch (DatabaseHandlingException e) {
            ResponseOutputer.appenderror ("An error occurred while accessing the database!");
        }
        return false;
    }
}
