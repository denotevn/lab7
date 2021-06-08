package utility;

import commands.*;
import interaction.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * readLock - tra ve khoa doc
 * writeLock - tra ve khoa ghi
 * */

public class CommandManager {
    private List<Commands> commandsList = new ArrayList<>();
    private Commands addCommand;
    private Commands addIfMinCommand;
    private Commands clearCommand;
    private Commands executeScriptCommand;
    private Commands exitCommand;
    private Commands helpCommand;
    private Commands infoCommand;
    private Commands maxByHealthCommand;
    private Commands printAscendingCommand;
    private Commands removeByHealthCommand;
    private Commands removeByIdCommand;
    private Commands removeLowerCommand;
    private Commands showCommand;
    private Commands sortCommand;
    private Commands updateIdCommand;
    private Commands serverExitCommand;
    private Commands loginCommand;
    private Commands registerCommand;

    private ReadWriteLock collectionLocker = new ReentrantReadWriteLock();

    public CommandManager(Commands addCommand, Commands addIfMinCommand, Commands clearCommand,
                          Commands executeScriptCommand, Commands exitCommand, Commands helpCommand,
                          Commands infoCommand, Commands maxByHealthCommand, Commands printAscendingCommand,
                          Commands removeByHealthCommand, Commands removeByIdCommand, Commands removeLowerCommand,
                          Commands showCommand, Commands sortCommand, Commands updateIdCommand,
                          Commands serverExitCommand,Commands loginCommand,Commands registerCommand) {
        this.addCommand = addCommand;
        this.addIfMinCommand = addIfMinCommand;
        this.clearCommand = clearCommand;
        this.executeScriptCommand = executeScriptCommand;
        this.exitCommand = exitCommand;
        this.helpCommand = helpCommand;
        this.infoCommand = infoCommand;
        this.maxByHealthCommand = maxByHealthCommand;
        this.printAscendingCommand = printAscendingCommand;
        this.removeByHealthCommand = removeByHealthCommand;
        this.removeByIdCommand = removeByIdCommand;
        this.removeLowerCommand = removeLowerCommand;
        this.showCommand = showCommand;
        this.sortCommand = sortCommand;
        this.updateIdCommand = updateIdCommand;
        this.serverExitCommand = serverExitCommand;
        this.loginCommand = loginCommand;
        this.registerCommand = registerCommand;

        commandsList.add(addCommand);
        commandsList.add(addIfMinCommand);
        commandsList.add(clearCommand);
        commandsList.add(executeScriptCommand);
        commandsList.add(exitCommand);
        commandsList.add(helpCommand);
        commandsList.add(infoCommand);
        commandsList.add(maxByHealthCommand);
        commandsList.add(printAscendingCommand);
        commandsList.add(removeByHealthCommand);
        commandsList.add(removeByIdCommand);
        commandsList.add(removeLowerCommand);
        commandsList.add(showCommand);
        commandsList.add(sortCommand);
        commandsList.add(updateIdCommand);
//        commandsList.add(registerCommand);
//        commandsList.add(loginCommand);
    }

    /**
     * Prints info about the all commands.
     * @param stringArg String argument.
     */
    public boolean help(String stringArg, Object objectArg, User user){
        if (helpCommand.executed(stringArg, objectArg, user)) {
            for (Commands command : commandsList) {
                ResponseOutputer.appendtable(command.getName() + " " + command.getUsage(), command.getDescription());
            }
            return true;
        } else return false;
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean show(String stringArg, Object objectArg, User user){
        collectionLocker.readLock().lock();
        try{
            return showCommand.executed(stringArg,objectArg,user);
        }finally {
            collectionLocker.readLock().unlock();
        }
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean info(String stringArg, Object objectArg, User user) {
        collectionLocker.readLock().lock();
        try {
            return infoCommand.executed(stringArg, objectArg, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }
    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean exit(String stringArg, Object objectArg, User user) {
        return exitCommand.executed(stringArg, objectArg, user);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean clear(String stringArg, Object objectArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return clearCommand.executed(stringArg, objectArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }
    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean add(String stringArg,Object objectArg, User user){
        collectionLocker.writeLock().lock();
        try{
            return addCommand.executed(stringArg,objectArg,user);
        }finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean removeById(String stringArg, Object objectArg, User user){
        collectionLocker.writeLock().lock();
        try{
            return removeByIdCommand.executed(stringArg,objectArg,user);
        }finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean updateById(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return updateIdCommand.executed(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean addIfMinCommand(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return addIfMinCommand.executed(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     *
     * @param stringArg Its string argument.
     * @param objectArg Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean removeLower(String stringArg, Object objectArg, User user){
        collectionLocker.writeLock().lock();
        try{
            return removeLowerCommand.executed(stringArg,objectArg,user);
        }finally {
            collectionLocker.writeLock().unlock();
        }
    }

    public boolean serverExit(String stringArgument, Object objectArgument, User user) {
        return serverExitCommand.executed(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     *
     * @param stringArg Its string argument.
     * @param objectArg Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean addIfMin(String stringArg,Object objectArg,User user){
        collectionLocker.writeLock().lock();
        try {
            return addIfMinCommand.executed(stringArg,objectArg,user);
        }finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command
     * @param stringArg its string argument
     * @param objectArg its object argument
     * @param user User object
     * @return Command exit status
     * */
    public boolean executeScript(String stringArg, Object objectArg, User user){
        return executeScriptCommand.executed(stringArg,objectArg,user);
    }

    /**
     * Executes needed command
     * @param stringArg
     * @param object
     * @param user
     * @return Command exit status
     * */
    public boolean maxByHealth(String stringArg, Object object, User user){
        collectionLocker.readLock().lock();
        try{
            return maxByHealthCommand.executed(stringArg,object,user);
        }finally {
            collectionLocker.readLock().unlock();
        }
    }

    public boolean printAscending(String stringArg, Object objectArg, User user){
        collectionLocker.readLock().lock();
        try{
            return printAscendingCommand.executed(stringArg,objectArg,user);
        }finally {
            collectionLocker.readLock().unlock();
        }
    }


    /**
     * @param stringArg
     * @param objectArg
     * @param user
     * @return Command exit status
     * */
    public boolean removeByHealth(String stringArg, Object objectArg, User user){
        collectionLocker.writeLock().lock();
        try{
            return removeByHealthCommand.executed(stringArg,objectArg,user);
        }finally {
            collectionLocker.writeLock().unlock();
        }
    }
    /**
     * @param user
     * @param objectArg
     * @param stringArg
     * @return Command exit status
     * */
    public boolean sort(String stringArg, Object objectArg, User user){
        collectionLocker.writeLock().lock();
        try{
            return sortCommand.executed(stringArg,objectArg,user);
        }finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     *
     * @param stringArg Its string argument.
     * @param objectArg Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean loginCommand(String stringArg, Object objectArg, User user) {
        return loginCommand.executed(stringArg, objectArg, user);
    }

    /**
     * Executes needed command.
     *
     * @param stringArg Its string argument.
     * @param objectArg Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean registerCommand(String stringArg, Object objectArg, User user) {
        return registerCommand.executed(stringArg, objectArg, user);
    }



}
