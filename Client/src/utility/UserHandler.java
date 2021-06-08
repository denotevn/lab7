package utility;

import App.ApplicationClient;
import data.*;
import exception.CommandUSageException;
import exception.IncorrectInputInScriptException;
import exception.ScriptRecursionException;
import interaction.MarineRaw;
import interaction.Request;
import interaction.ResponseCode;
import interaction.User;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Stack;
/**
 * Receives user requests.
 */
public class UserHandler {
    private BufferedReader userReader;
    private final Stack<BufferedReader> readerStack = new Stack<>();
    private final Stack<File> filesScriptStack = new Stack<>();

    public UserHandler(BufferedReader userReader) {
        this.userReader = userReader;
    }


    /**
     * Receives user input.
     *
     * @param serverResponeCode Last server's response code.
     * @return New request to server.
     */
    public Request handler(ResponseCode serverResponeCode, User user)
    {
        String userInput = null;
        String [] userCommand ;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try{
            do{
                try{
                    if (isScriptMode()){
                        userInput = userReader.readLine();
                        if (userInput == null){
                            userReader.close();
                            userReader = readerStack.pop();
                            Outputer.println("Executing file "+ filesScriptStack.pop().getName() + " done");
                        }
                    }

                    if (isScriptMode()) {
                        Outputer.println(App.ApplicationClient.PS1 + userInput);
                        //interactive mode
                    }else {
                        Outputer.println("\nEnter the command:");
                        Outputer.print(ApplicationClient.PS1);
                        userInput = userReader.readLine();
                    }

                    assert userInput != null; // bat xac nhan xem input co null hay la khong
                    userCommand = (userInput.trim()+ " ").split(" ",2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException | IOException exception) {
                    Outputer.println();
                    Outputer.printerror("An error occurred while entering the command!");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    int maxRewriteAttempts = 1;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        Outputer.printerror("Number of input attempts exceeded!");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0],userCommand[1]);
            }while(processingCode == ProcessingCode.ERROR && !isScriptMode() || userCommand[0].isEmpty());
/**DA SUA O DAY ! userCommand[0].isEmpty()*/
            try{
                if (isScriptMode() && ( processingCode == ProcessingCode.ERROR))
                    throw new IncorrectInputInScriptException();
                switch (processingCode){
                    case OBJECT:
                        MarineRaw marineRaw = generateMarineAdd();
                        return new Request(userCommand[0],userCommand[1],marineRaw,user);
                    case UPDATE_OBJECT:
                        MarineRaw updateMarineRaw = generateMarineUpdate();
                        return new Request(userCommand[0],userCommand[1],updateMarineRaw,user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!filesScriptStack.isEmpty() && filesScriptStack.search(scriptFile) != -1){
                            throw new ScriptRecursionException();
                        }
                        readerStack.push(userReader);
                        filesScriptStack.push(scriptFile);
                        userReader = new BufferedReader(new FileReader(scriptFile)); // bat dau doc file
                        Outputer.println("Executing the script "+ scriptFile.getName() + "...");
                        break;
                }
            } catch (ScriptRecursionException e) {
                Outputer.printerror("Scripts can't be called recursively! ");
                throw new IncorrectInputInScriptException();
            } catch (FileNotFoundException e) {
                Outputer.printerror("File not found.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (IncorrectInputInScriptException e){
            try{
                Outputer.printerror("Script execution interrupted.");
                while (!readerStack.isEmpty()) {
                    userReader.close();
                    userReader = readerStack.pop();
                }
                filesScriptStack.clear();
            } catch (IOException ioException) {
                Outputer.printerror("IOException occurred");
                System.exit(0);
            }
            return new Request(user);
        }
        return new Request(userCommand[0],userCommand[1],user);
    }

    private boolean isScriptMode() {
        return !readerStack.isEmpty();
    }


    /**
     * Processes the entered command.
     *
     * @return Status of code.
     */


    public ProcessingCode processCommand(String commandName,String commandArg){
        try {
            switch(commandName){
                case "":
                    Outputer.println("Enter 'help' for help !");
                    return ProcessingCode.ERROR;
                case "help":
                case "show":
                case "info":
                case "exit":
                case "clear":
                case "sort":
                case "remove":
                    if (!commandArg.isEmpty()) throw new CommandUSageException();
                    break;
                case "add":
                case "add_if_min":
                    if (!commandArg.isEmpty()) throw new CommandUSageException("{element}");
                    return ProcessingCode.OBJECT;
                case "remove_lower":
                    if (!commandArg.isEmpty()) throw new CommandUSageException();
                    return ProcessingCode.OBJECT;
                case "remove_by_id":
                    if (commandArg.isEmpty())throw new CommandUSageException("<ID>");
                    break;
                case "remove_all_by_health":
                    if (commandArg.isEmpty())throw new CommandUSageException("<HEALTH>");
                    break;
                case "update":
                    if (commandArg.isEmpty()) throw new CommandUSageException("<ID>");
                    return ProcessingCode.UPDATE_OBJECT;
                case "execute_script":
                    if (commandArg.isEmpty()) throw new CommandUSageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                case "max_by_health":
                    if (!commandArg.isEmpty())throw new CommandUSageException(" ");
                    break;
                case "print_ascending":
                    if (!commandArg.isEmpty()) throw new CommandUSageException();
                    break;
                default:
                    Outputer.println("Command "+ commandName + " "+ "not found.");
                    Outputer.println("Please enter help for more detail");
                    return ProcessingCode.ERROR;
            }
        } catch (CommandUSageException e) {
            if (e.getMessage() == null ) commandName += " " + e.getMessage();
            return ProcessingCode.ERROR;
        }
        return ProcessingCode.OK;
    }

    /**
     * Generates marine to add.
     * @return Marine to add.
     * @throws IOException When something went wrong in script.
     */
    public MarineRaw generateMarineAdd() throws  IOException {
        MarineAsk marineAsk = new MarineAsk(userReader);
        if (isScriptMode()) marineAsk.setFileMode();
        return new MarineRaw(
                marineAsk.nameAsker(),
                marineAsk.coordinatesAsker(),
                marineAsk.healthAsker(),
                marineAsk.categoryAsker(),
                marineAsk.weaponAsker(),
                marineAsk.meleeWeaponAsker(),
                marineAsk.askChapter());
    }

    /**
     * Generates marine to update.
     *
     * @return Marine to update.
     * @throws IncorrectInputInScriptException When something went wrong in script.
     */
    private MarineRaw generateMarineUpdate() throws IncorrectInputInScriptException, IOException {
        MarineAsk marineAsk = new MarineAsk(userReader);
        if (isScriptMode()) marineAsk.setFileMode();
            String name = marineAsk.askQuestions("Want to change the name of a soldier?") ?
                        marineAsk.nameAsker() : null;
            Coordinates coordinates = marineAsk.askQuestions("Want to change the coordinates of a soldier? ") ?
                    marineAsk.coordinatesAsker() : null;
            long health = marineAsk.askQuestions("Want to change the health of a soldier?") ?
                    marineAsk.healthAsker() : -1;
            AstartesCategory category = marineAsk.askQuestions("Want to change the category of a soldier?") ?
                    marineAsk.categoryAsker() : null;
            Weapon weapon = marineAsk.askQuestions("Want to change the weapon of a soldier?") ?
                    marineAsk.weaponAsker() : null;
            MeleeWeapon meleeWeapon = marineAsk.askQuestions("Want to change the meleeWeapon of a soldier?") ?
                    marineAsk.meleeWeaponAsker() : null;
            Chapter chapter = marineAsk.askQuestions("want to change the the Chapter of a Soldier?") ?
                    marineAsk.askChapter() : null;
            return new MarineRaw(name,
                    coordinates,
                    health,
                    category,
                    weapon,
                    meleeWeapon,
                    chapter);
    }
}
