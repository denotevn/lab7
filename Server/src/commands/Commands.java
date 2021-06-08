package commands;

import interaction.User;

public interface Commands {
    String getDescription();
    String getName();
    String getUsage();
    boolean executed(String argument, Object commandObjectArgument, User user);
}
