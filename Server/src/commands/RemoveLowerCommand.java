package commands;

import data.SpaceMarine;
import exception.*;
import interaction.MarineRaw;
import interaction.User;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.MarineAsk;
import utility.ResponseOutputer;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class RemoveLowerCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private MarineAsk MarineAsker;
    DatabaseCollectionManager databaseCollectionManager;
    public RemoveLowerCommand(CollectionManager collectionManager,DatabaseCollectionManager databaseCollectionManager){
        super("remove_lower"," {element}","remove from" +
                " the collection all elements less than the given one");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }
    @Override
    public boolean executed(String argument, Object ObjectArgument, User user) {
        try{
            if (!argument.isEmpty() || ObjectArgument == null) throw new WrongAmountOfElementException();
            if (collectionManager.collectionSize() == 0) throw new MarineIsEmptyCollection();
            MarineRaw marineRaw = (MarineRaw)ObjectArgument;
            SpaceMarine marineToFind = new SpaceMarine(
                    0,
                    marineRaw.getName(),
                    marineRaw.getCoordinates(),
                    java.util.Date.from(Instant.now()),
                    marineRaw.getHealth(),
                    marineRaw.getCategory(),
                    marineRaw.getWeaponType(),
                    marineRaw.getMeleeWeapon(),
                    marineRaw.getChapter(),
                    user
            );
            SpaceMarine marineFromCollection = collectionManager.getByValue(marineToFind);

            if (marineFromCollection == null) throw new MarineNotFoundException();
//            collectionManager.removeAllMarine(marineFromCollection);
            for (SpaceMarine marine : collectionManager.getLower(marineFromCollection)) {
                if (!marine.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkMarineUserId(marine.getId(), user)) throw new ManualDatabaseEditException();
            }
            for (SpaceMarine marine : collectionManager.getLower(marineFromCollection)){
                databaseCollectionManager.deleteMarineById(marine.getId());
                collectionManager.removeFromCollection(marine);
            }
            ResponseOutputer.appendln("Successfully deleted element");
            return true;
        }catch (WrongAmountOfElementException | MarineIsEmptyCollection e){
            e.printStackTrace();
            ResponseOutputer.appendln("Using "+ getName() +" "+ getUsage()+" ");
        } catch (MarineNotFoundException e) {
            e.printStackTrace();
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
        } catch (ManualDatabaseEditException e) {
            e.printStackTrace();
        } catch (DatabaseHandlingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
