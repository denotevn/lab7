package utility;

import interaction.Request;
import interaction.User;

import java.io.BufferedReader;

public class AuthHandler {
    private final BufferedReader reader;

    public AuthHandler(BufferedReader reader) {
        this.reader = reader;
    }

    public Request handle(){
        AuthAsker authAsker = new AuthAsker(reader);

        String registerCommand = "register";
        String loginCommand = "login";

        String command = authAsker.askQuestion("Have an account already?")?loginCommand : registerCommand;
        User user = new User(authAsker.askUsername(), authAsker.askPassword());
        return new Request(command,"", user);
    }
}
