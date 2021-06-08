package utility;

import data.SpaceMarine;
import exception.DatabaseHandlingException;
import exception.MarineIsEmptyCollection;
import server.AppServer;

import java.util.*;
public class CollectionManager {
    private Stack<SpaceMarine> listMarine;
    private DatabaseCollectionManager databaseCollectionManager;

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;
        loadCollection();
    }

    public void loadCollection() {
        try {
            listMarine = databaseCollectionManager.getCollection();
            Outputer.println("The collection is loaded.");
            AppServer.LOGGER.severe("The collection is loaded.");
        } catch (DatabaseHandlingException e) {
            listMarine = new Stack<>();
            Outputer.printerror("The collection is not loaded! ");
            AppServer.LOGGER.severe("The collection is not loaded!");
        }
    }

    public Stack<SpaceMarine> getCollection() {
        return listMarine;
    }

    /**
     * Add all objects to the list
     *
     * @param marineStack
     */

    public Stack<SpaceMarine> addToCollection(Stack<SpaceMarine> marineStack) {
        marineStack.addAll(listMarine);
        return marineStack;
    }
    /** delete an element from the collection */
    /**
     * outputs the collection element number
     */
    public int collectionSize() {
        return listMarine.size();
    }
//    public Long generateNextId() {
//        if (listMarine.isEmpty()) return 1L;
//        return listMarine.lastElement().getId() + 1;
//    }

    /**
     * Get by Id
     *
     * @param id
     */
    public SpaceMarine getMarineById(long id) {
        SpaceMarine marine = null;
        for (SpaceMarine spaceMarine: listMarine){
            if (spaceMarine.getId() == id){
                marine = spaceMarine;
                break;
            }
        }
        return marine;
    }
        /**Add a Space Marine object
         * @param spaceMarine*/
        public void add (SpaceMarine spaceMarine){
            listMarine.add(spaceMarine);
        }

        public SpaceMarine getFirstMarine () {
            Collections.sort(listMarine, Collections.reverseOrder());
            return listMarine.stream().findFirst().orElse(null);
        }
        /**
         * @return Collection content or corresponding string if collection is empty.
         */
        public String showCollection () {
            if (listMarine.isEmpty()) return "The collection is empty!";
            return listMarine.stream().reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
        }
        /**delete listMarine.*/
        public void clear () {
            listMarine.clear();
        }

        /**deduce any object from the collection whose health field value is the maximum
         * @return String spaceMarine*/
        public String maxByHealth () throws MarineIsEmptyCollection {
            if (listMarine.isEmpty()) throw new MarineIsEmptyCollection();
            SpaceMarine spaceMarine = getFirstMarine();
            for (SpaceMarine spaceMarine1 : listMarine) {
                if (spaceMarine1.getHealth() > spaceMarine.getHealth()) {
                    spaceMarine = spaceMarine1;
                }
            }
            return spaceMarine.toString();
        }

        /** display the elements of the collection in ascending order */
        public void printAscending () {
            ArrayList<SpaceMarine> marines = new ArrayList<>();
            ResponseOutputer.appendln("Result of command print_ascending: ");
            for (SpaceMarine s : listMarine) {
                marines.add(s);
            }
            Collections.sort(marines, new Comparator<SpaceMarine>() {
                @Override
                public int compare(SpaceMarine o1, SpaceMarine o2) {
                    return (int) o1.getId() - (int) o2.getId();
                }
            });
            System.out.println("List of elements in ascending order of id: ");
            for (SpaceMarine marine : marines) {
                ResponseOutputer.appendln(marine);
            }
        }
        /**@param health of the marine.
         * @return A marine by health or null if marine is not found.
         */
        public Stack<SpaceMarine> getByHealth (long health){
            Stack<SpaceMarine> spaceMarines = new Stack<>();
            for (SpaceMarine marine : listMarine) {
                if (marine.getHealth() == health) {
                    spaceMarines.add(marine);
                }
            }
            return spaceMarines;
        }
        /**
         * Removes a new marine to collection.
         * @param marine A marine to remove.
         */
        public void removeFromCollection (SpaceMarine marine){
            listMarine.remove(marine);
        }
        public void removeListFromCollection (Stack < SpaceMarine > marineStack) {
            listMarine.removeAll(marineStack);
        }
        public void removeAllMarine (SpaceMarine arrMarine){
            listMarine.remove(arrMarine);
        }
        /**
         * @param marineToFind
         * @return outputs items that are less than Marin To Find
         */
        public SpaceMarine getByValue (SpaceMarine marineToFind){
            return listMarine.stream().filter(marine -> marine.equals(marineToFind)).findFirst().orElse(null);
        }

        /**
         * Remove marines greater than the selected one.
         *
         * @param marineToCompare A marine to compare with.
         * @return Greater marines list.
         */
        public Stack<SpaceMarine> getLower (SpaceMarine marineToCompare){
            return listMarine.stream().filter(marine -> marine.compareTo(marineToCompare) < 0).collect(
                    Stack::new,
                    Stack::add,
                    Stack::addAll
            );
        }

        /** sort the collection in natural order */
        public void sort () {
            Collections.sort(listMarine);
        }
        /**Method to save the file object*/

        public void addToCollection (SpaceMarine marine){
            listMarine.add(marine);
        }
    }
