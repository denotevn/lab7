package commands;

import exception.DatabaseHandlingException;
import exception.UserAlreadyExists;
import exception.WrongAmountOfElementException;
import interaction.User;
import utility.DatabaseCollectionManager;
import utility.DatabaseUserManager;
import utility.ResponseOutputer;

public class RegisterCommand extends AbstractCommand {
    private DatabaseUserManager databaseUserManager;

    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        super("register", "", "internal team");
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public boolean executed(String argument, Object commandObjectArgument, User user) {
        try {
            if (!argument.isEmpty() || commandObjectArgument != null) throw new WrongAmountOfElementException();
            if (databaseUserManager.insertUser(user)) ResponseOutputer.appendln(" User" +
                    user.getUsername() + " registered.");
            else throw new UserAlreadyExists();
            return true;
        } catch (WrongAmountOfElementException exception) {
            ResponseOutputer.appendln("Usage: ummm ... uh ... this is an internal command ...");
        } catch (UserAlreadyExists userAlreadyExists) {
            ResponseOutputer.appenderror("User" + user.getUsername() + "already exists!");
        } catch (DatabaseHandlingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
