package commands;

import data.SpaceMarine;
import exception.DatabaseHandlingException;
import exception.WrongAmountOfElementException;
import interaction.MarineRaw;
import interaction.User;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.ResponseOutputer;


import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.time.Instant;
/**
 * Command 'add_if_min'. Adds a new element to collection if it's less than the minimal one.
 */
public class AddIfMinCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddIfMinCommand(CollectionManager collectionManager1, DatabaseCollectionManager databaseCollectionManager){
        super("add_if_min","{element}","add a new item to a collection if " + "its value is less than the smallest item in this collection");
        this.collectionManager = collectionManager1;
        this.databaseCollectionManager = databaseCollectionManager;
    }
    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean executed(String stringArgument,Object objectArgument,User user) {
        try{
            if (!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementException();
            MarineRaw marineRaw = (MarineRaw) objectArgument;
            SpaceMarine marineToAdd = databaseCollectionManager.insertMarine(marineRaw, user);
            if (collectionManager.collectionSize() == 0 || marineToAdd.compareTo(collectionManager.getFirstMarine()) < 0) {
                collectionManager.add(marineToAdd);
                ResponseOutputer.appendln("Soldier added successfully!");
                return true;
            }else{
                ResponseOutputer.appenderror("The initialization value is greater than the smallest " +
                        "value in the collection.");
            }
        } catch (WrongAmountOfElementException e) {
            e.printStackTrace();
            ResponseOutputer.appendln("Using: '" + getName() + " " + getUsage() + "'");
        }catch (ClassCastException e){
            ResponseOutputer.appenderror("The object submitted by the client is invalid");
        } catch (DatabaseHandlingException e) {
            ResponseOutputer.appenderror ("An error occurred while accessing the database!");
        }
        return false;
    }
}
//            SpaceMarine marineToAdd = new SpaceMarine(
//                    collectionManager.generateNextId(),
//                    marineRaw.getName(),
//                    marineRaw.getCoordinates(),
//                    java.util.Date.from(Instant.now()),
//                    marineRaw.getHealth(),
//                    marineRaw.getCategory(),
//                    marineRaw.getWeaponType(),
//                    marineRaw.getMeleeWeapon(),
//                    marineRaw.getChapter()
//            );