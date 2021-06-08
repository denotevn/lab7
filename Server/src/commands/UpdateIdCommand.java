package commands;

import data.*;
import exception.*;
import interaction.MarineRaw;
import interaction.Response;
import interaction.User;
import server.AppServer;
import utility.*;

import javax.xml.crypto.Data;
import java.io.IOException;

public class UpdateIdCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private InputChek inPutCheck;
    private DatabaseCollectionManager databaseCollectionManager;
    public UpdateIdCommand(CollectionManager collectionManager,
                           InputChek inPutCheck,DatabaseCollectionManager databaseCollectionManager){
        super("update id: ", "ID {element}","update the value " +
                "of the collection element whose id is equal to the given");
        this.collectionManager = collectionManager;
        this.inPutCheck = inPutCheck;
        this.databaseCollectionManager = databaseCollectionManager;
    }
    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean executed(String argument, Object ObjectArgument, User user) {
        try{
            if (argument.isEmpty() || ObjectArgument == null) throw new WrongAmountOfElementException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            if (inPutCheck.longInValidCheck(argument,(long)0,Long.MAX_VALUE)){
                int id = Integer.parseInt(argument);
                if (id<0) throw new NumberFormatException();
                SpaceMarine marine = collectionManager.getMarineById(id);

                if (marine == null) throw new MarineNotFoundException();
                if (!marine.getOwner().equals(user)) throw new PermissionDeniedException();
                /**loi o dong duoi nay*/
                if (!databaseCollectionManager.checkMarineUserId(marine.getId(), user)) throw new ManualDatabaseEditException();
                MarineRaw marineRaw = (MarineRaw) ObjectArgument;
                /**loi o duoi nay*/
                databaseCollectionManager.updateMarineById(id, marineRaw);

                String name = marineRaw.getName() == null ? marine.getName() : marineRaw.getName();
                Coordinates coordinates = marineRaw.getCoordinates() == null ? marine.getCoordinates() : marineRaw.getCoordinates();
                java.util.Date creationDate = marine.getCreationDate();
                long health = marineRaw.getHealth() == -1 ? marine.getHealth() : marineRaw.getHealth();
                AstartesCategory category = marineRaw.getCategory() == null ? marine.getCategory() : marineRaw.getCategory();
                Weapon weaponType = marineRaw.getWeaponType() == null ? marine.getWeaponType() : marineRaw.getWeaponType();
                MeleeWeapon meleeWeapon = marineRaw.getMeleeWeapon() == null ? marine.getMeleeWeapon() : marineRaw.getMeleeWeapon();
                Chapter chapter = marineRaw.getChapter() == null ? marine.getChapter() : marineRaw.getChapter();

                collectionManager.removeFromCollection(marine);
                collectionManager.add(new SpaceMarine(
                        id,
                        name,
                        coordinates,
                        creationDate,
                        health,
                        category,
                        weaponType,
                        meleeWeapon,
                        chapter,
                        user
                ));

                ResponseOutputer.appendln("Success update marine ! ");
                return true;
            }
        } catch (WrongAmountOfElementException e) {
            ResponseOutputer.appendln("Using: "+ getName() + getUsage() + " ");
        } catch (CollectionIsEmptyException e) {
            ResponseOutputer.appendln("Collection is null.");
        }catch (MarineNotFoundException e) {
            ResponseOutputer.appendln("Marine is not found.");
        }catch(NumberFormatException exception){
            ResponseOutputer.appenderror("The index id needs to be greater than 0 !");
        } catch (PermissionDeniedException e) {
            ResponseOutputer.appenderror ("Insufficient rights to execute this command!");
            ResponseOutputer.appendln ("Objects owned by other users are read-only.");
        } catch (ManualDatabaseEditException e) {
            e.printStackTrace();
        } catch (DatabaseHandlingException e) {
            ResponseOutputer.appenderror ("An error occurred while accessing the database!");
        }
        return false;
    }
}
