package utility;

import App.ApplicationClient;
import exception.MustBeNotEmptyException;
import exception.NotInDeclaredLimitsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;

public class AuthAsker {
    private final BufferedReader reader;

    public AuthAsker(BufferedReader reader) {
        this.reader = reader;
    }
    /**
     * Asks user an username.
     *
     * @return username.
     */
    public String askUsername(){
        String username;
        while (true){
            try{
                Outputer.println("Enter username: ");
                Outputer.print(ApplicationClient.PS2);
                username = reader.readLine().trim();
                if (username.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("This username does not exist!");
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Username can not be empty!");
            } catch (IllegalStateException | IOException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return username;
    }
    /**
     * Asks user a password.
     *
     * @return password.
     */
    public String askPassword(){
        String password;
        while (true){
            try{
                Outputer.println("Enter password: ");
                Outputer.print(ApplicationClient.PS2);
                password = reader.readLine().trim();
                if (password.equals("")) throw new MustBeNotEmptyException();
                break;
            }catch (NoSuchElementException exception) {
                Outputer.printerror("This password does not exist!");
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Password can not be empty!");
            } catch (IllegalStateException | IOException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return password;
    }

    /**
     * Asks a user a question.
     *
     * @param question A question.
     * @return Answer (true/false).
     */
    public boolean askQuestion(String question) {
        String finalQuestion = question + " (yes/no):";
        String answer;
        while (true) {
            try {
                Outputer.println(finalQuestion);
                Outputer.print(ApplicationClient.PS2);
                answer = reader.readLine().trim();
                if (!answer.equals("yes") && !answer.equals("no")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("The answer is not recognized!");
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("The answer must be represented by '+' or '-' signs!");
            } catch (IllegalStateException | IOException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return answer.equals("yes");
    }

}
