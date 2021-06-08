package utility;

import interaction.Request;
import interaction.Response;
import interaction.ResponseCode;
import interaction.User;

import java.util.concurrent.RecursiveTask;

/**
 * A class for handle request task.
 */
public class HandlerRequestTask extends RecursiveTask<Response> {
    private Request request;
    private CommandManager commandManager;

    public HandlerRequestTask(Request request, CommandManager commandManager) {
        this.request = request;
        this.commandManager = commandManager;
    }

    @Override
    protected Response compute() {
        User hashedUser = new User(
                request.getUser().getUsername(),
                PasswordHasher.hashPassword(request.getUser().getPassword()));
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument(),hashedUser);
        return new Response(responseCode,ResponseOutputer.getAndClear());
    }
    /**
     * Executes a command from a request.
     *
     * @param command               Name of command.
     * @param commandStringArgument String argument for command.
     * @param commandObjectArgument Object argument for command.
     * @return Command execute status.
     */
    private synchronized ResponseCode executeCommand(String command, String commandStringArgument,
                                                     Object commandObjectArgument, User user){
        switch (command){
            case "":
                break;
            case "sort":
                if(commandManager.sort(commandStringArgument,commandObjectArgument,user)){
                    return ResponseCode.ERROR;
                }
                break;
            case "help":
                if (!commandManager.help(commandStringArgument, commandObjectArgument,user)){
                    return ResponseCode.ERROR;
                }
                break;
            case "info":
                if (!commandManager.info(commandStringArgument,commandObjectArgument,user)){
                    return ResponseCode.ERROR;
                }
                break;
            case "show":
                if (!commandManager.show(commandStringArgument,commandObjectArgument,user))
                    return ResponseCode.ERROR;
                break;
            case "add":
                System.out.println("Im here requestTask");
                if (!commandManager.add(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.updateById(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_by_id":
                if (!commandManager.removeById(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "execute_script":
                if (!commandManager.executeScript(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                return ResponseCode.CLIENT_EXIT;
            case "add_if_min":
                if (!commandManager.addIfMin(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_lower":
                if (!commandManager.removeLower(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "server_exit":
                if (!commandManager.serverExit(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                return ResponseCode.SERVER_EXIT;
            case "login":
                if (!commandManager.loginCommand(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "register":
                if (!commandManager.registerCommand(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "max_by_health":
                if (!commandManager.maxByHealth(commandStringArgument,commandObjectArgument,user))
                    return ResponseCode.ERROR;
                break;
            case "remove_all_by_health":
                if (commandManager.removeByHealth(commandStringArgument,commandObjectArgument,user))
                    return ResponseCode.ERROR;
                break;
            case "print_ascending":
                if (!commandManager.printAscending(commandStringArgument,commandObjectArgument,user))
                    return ResponseCode.ERROR;
                break;
            default:
                ResponseOutputer.appendln("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                return ResponseCode.ERROR;
        }
        return ResponseCode.OK;
    }
}
