package utility;

import data.*;
import exception.DatabaseHandlingException;
import interaction.MarineRaw;
import interaction.User;
import server.AppServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Stack;
import java.util.logging.Logger;

public class DatabaseCollectionManager {
    private final String SELECT_ALL_MARINES = "SELECT * FROM " + DatabaseHandler.MARINE_TABLE;
    private final String SELECT_MARINE_BY_ID = SELECT_ALL_MARINES + " WHERE " +
            DatabaseHandler.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_MARINE_BY_HEALTH = SELECT_ALL_MARINES + " WHERE "+
            DatabaseHandler.MARINE_TABLE_HEALTH_COLUMN + " = ?";
    private final String SELECT_MARINE_BY_ID_AND_USER_ID = SELECT_MARINE_BY_ID + " AND " +
            DatabaseHandler.MARINE_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_MARINE = "INSERT INTO " +
            DatabaseHandler.MARINE_TABLE + " (" +
            DatabaseHandler.MARINE_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.MARINE_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseHandler.MARINE_TABLE_HEALTH_COLUMN + ", " +
            DatabaseHandler.MARINE_TABLE_CATEGORY_COLUMN + ", " +
            DatabaseHandler.MARINE_TABLE_WEAPON_TYPE_COLUMN + ", " +
            DatabaseHandler.MARINE_TABLE_MELEE_WEAPON_COLUMN + ", " +
            DatabaseHandler.MARINE_TABLE_CHAPTER_ID_COLUMN + ", " +
            DatabaseHandler.MARINE_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?::astartes_category," +
            "?::weapon, ?::melee_weapon, ?, ?)";
    private final String DELETE_MARINE_BY_ID = "DELETE FROM " + DatabaseHandler.MARINE_TABLE +
            " WHERE " + DatabaseHandler.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_MARINE_BY_HEALTH = "DELETE FROM " + DatabaseHandler.MARINE_TABLE +
            " WHERE " + DatabaseHandler.MARINE_TABLE_HEALTH_COLUMN + " = ?";
    private final String UPDATE_MARINE_NAME_BY_ID = "UPDATE " + DatabaseHandler.MARINE_TABLE + " SET " +
            DatabaseHandler.MARINE_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_HEALTH_BY_ID = "UPDATE " + DatabaseHandler.MARINE_TABLE + " SET " +
            DatabaseHandler.MARINE_TABLE_HEALTH_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_CATEGORY_BY_ID = "UPDATE " + DatabaseHandler.MARINE_TABLE + " SET " +
            DatabaseHandler.MARINE_TABLE_CATEGORY_COLUMN + " = ?::astartes_category" + " WHERE " +
            DatabaseHandler.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_WEAPON_TYPE_BY_ID = "UPDATE " + DatabaseHandler.MARINE_TABLE + " SET " +
            DatabaseHandler.MARINE_TABLE_WEAPON_TYPE_COLUMN + " = ?::weapon" + " WHERE " +
            DatabaseHandler.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_MELEE_WEAPON_BY_ID = "UPDATE " + DatabaseHandler.MARINE_TABLE + " SET " +
            DatabaseHandler.MARINE_TABLE_MELEE_WEAPON_COLUMN + " = ?::melee_weapon" + " WHERE " +
            DatabaseHandler.MARINE_TABLE_ID_COLUMN + " = ?";
    // COORDINATES_TABLE
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseHandler.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_MARINE_ID = SELECT_ALL_COORDINATES +
            " WHERE " + DatabaseHandler.COORDINATES_TABLE_SPACE_MARINE_ID_COLUMN + " = ?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseHandler.COORDINATES_TABLE + " (" +
            DatabaseHandler.COORDINATES_TABLE_SPACE_MARINE_ID_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?, ?)";
    private final String UPDATE_COORDINATES_BY_MARINE_ID = "UPDATE " + DatabaseHandler.COORDINATES_TABLE + " SET " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.COORDINATES_TABLE_SPACE_MARINE_ID_COLUMN + " = ?";
    // CHAPTER_TABLE
    private final String SELECT_ALL_CHAPTER = "SELECT * FROM " + DatabaseHandler.CHAPTER_TABLE;
    private final String SELECT_CHAPTER_BY_ID = SELECT_ALL_CHAPTER +
            " WHERE " + DatabaseHandler.CHAPTER_TABLE_ID_COLUMN + " = ?";
//    private final String SELECT_CHAPTER_BY_HEALTH = SELECT_ALL_CHAPTER + " WHERE "+
//            DatabaseHandler.CHAPTER_TABLE_HEALTH_COLUMN + " = ?";
    private final String INSERT_CHAPTER = "INSERT INTO " +
            DatabaseHandler.CHAPTER_TABLE + " (" +
            DatabaseHandler.CHAPTER_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.CHAPTER_TABLE_MARINES_PARENT_LEGION + ") VALUES (?, ?)";
    private final String UPDATE_CHAPTER_BY_ID = "UPDATE " + DatabaseHandler.CHAPTER_TABLE + " SET " +
            DatabaseHandler.CHAPTER_TABLE_NAME_COLUMN + " = ?, " +
            DatabaseHandler.CHAPTER_TABLE_MARINES_PARENT_LEGION + " = ?" + " WHERE " +
            DatabaseHandler.CHAPTER_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_CHAPTER_BY_ID = "DELETE FROM " + DatabaseHandler.CHAPTER_TABLE +
            " WHERE " + DatabaseHandler.CHAPTER_TABLE_ID_COLUMN + " = ?";
    private DatabaseHandler databaseHandler;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseHandler databaseHandler,DatabaseUserManager databaseUserManager){
        this.databaseHandler = databaseHandler;
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Create Marine.
     *
     * @param resultSet Result set parametres of Marine.
     * @return New Marine.
     * @throws SQLException When there's exception inside.
     */
    private SpaceMarine createMarine(ResultSet resultSet) throws SQLException {
        try {

            //resultSet.get() - tra lai du lieu cua chi muc cot duoc chi dinh cau hang hien tai
            int id = resultSet.getInt(DatabaseHandler.MARINE_TABLE_ID_COLUMN);
            String name = resultSet.getString(DatabaseHandler.MARINE_TABLE_NAME_COLUMN);
            java.util.Date creationDate = resultSet.getTimestamp(DatabaseHandler.MARINE_TABLE_CREATION_DATE_COLUMN);
            long health = resultSet.getLong(DatabaseHandler.MARINE_TABLE_HEALTH_COLUMN);
            AstartesCategory category = AstartesCategory.valueOf(resultSet.getString(DatabaseHandler.MARINE_TABLE_CATEGORY_COLUMN));
            Weapon weaponType = Weapon.valueOf(resultSet.getString(DatabaseHandler.MARINE_TABLE_WEAPON_TYPE_COLUMN));
            MeleeWeapon meleeWeapon = MeleeWeapon.valueOf(resultSet.getString(DatabaseHandler.MARINE_TABLE_MELEE_WEAPON_COLUMN));
            Coordinates coordinates = getCoordinatesByMarineId(id);
            Chapter chapter = getChapterById(resultSet.getLong(DatabaseHandler.MARINE_TABLE_CHAPTER_ID_COLUMN));
            User owner = databaseUserManager.getUserById(resultSet.getLong(DatabaseHandler.MARINE_TABLE_USER_ID_COLUMN));
            return new SpaceMarine(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    health,
                    category,
                    weaponType,
                    meleeWeapon,
                    chapter,
                    owner
            );
        } catch (Exception e) {
            System.out.println(e);
        } return null;
    }
    /**
     * @return List of Marines.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public Stack<SpaceMarine> getCollection() throws DatabaseHandlingException{
        Stack<SpaceMarine> marineList = new Stack<>();
        PreparedStatement preparedSelectAllStatement = null;
        try{
            preparedSelectAllStatement = databaseHandler.getPreparedStatement(SELECT_ALL_MARINES,false);
            ResultSet resultSet = preparedSelectAllStatement.executeQuery();
            while(resultSet.next()) {
                marineList.add(createMarine(resultSet));
                //ham createMarine(resultSet da duoc tao o tren (logic la nhu vay))
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        finally {
            databaseHandler.closePreparedStatement(preparedSelectAllStatement);
        }
        return marineList;
    }
    /**
     * @param marineId Id of Marine.
     * @return Chapter id.
     * @throws SQLException When there's exception inside.
     */
    private long getChapterIdByIdMarineId(long marineId) throws SQLException {
        long chapterId ;
        PreparedStatement preparedSelectMarineByIdStatement = null;
        try{
            preparedSelectMarineByIdStatement = databaseHandler.getPreparedStatement(SELECT_MARINE_BY_ID,false);
            preparedSelectMarineByIdStatement.setLong(1, marineId);
            ResultSet resultSet = preparedSelectMarineByIdStatement.executeQuery();
            AppServer.LOGGER.info("Success SELECT_MARINE_BY_ID");
            if (resultSet.next()){
                chapterId = resultSet.getLong(DatabaseHandler.MARINE_TABLE_CHAPTER_ID_COLUMN);
            }else
                throw new SQLException();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("An error occurred while executing the SELECT_MARINE_BY_ID query!");
            throw new SQLException(exception);
        }finally {
            databaseHandler.closePreparedStatement(preparedSelectMarineByIdStatement);
        }
        return chapterId;
    }

    /**
     * @param marineId Id of Marine.
     * @return coordinates.
     * @throws SQLException When there's exception inside.
     */

    private Coordinates getCoordinatesByMarineId(long marineId) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedSelectCoordinateByMarineIdStatement = null;
        try {
            preparedSelectCoordinateByMarineIdStatement =
                    databaseHandler.getPreparedStatement(SELECT_COORDINATES_BY_MARINE_ID,false);
            preparedSelectCoordinateByMarineIdStatement.setLong(1, marineId);
            ResultSet resultSet = preparedSelectCoordinateByMarineIdStatement.executeQuery();
            AppServer.LOGGER.info("Success SELECT_COORDINATE_BY_MARINE_ID");
            if (resultSet.next()){
                coordinates = new Coordinates(
                        (int)resultSet.getLong(DatabaseHandler.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getInt(DatabaseHandler.COORDINATES_TABLE_Y_COLUMN));
            }else
                throw new SQLException();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("An error occurred while executing the SELECT_COORDINATES_BY_MARINE_ID query!");
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectCoordinateByMarineIdStatement);
        }
        return coordinates;
    }

    /**
     * @param chapterId Id of Chapter.
     * @return Chapter.
     * @throws SQLException When there's exception inside.
     */
    private Chapter getChapterById(long chapterId) throws SQLException {
        Chapter chapter;
        PreparedStatement preparedSelectChapterByIdStatement = null;
        try{
            preparedSelectChapterByIdStatement =
                    databaseHandler.getPreparedStatement(SELECT_CHAPTER_BY_ID,false);
            preparedSelectChapterByIdStatement.setLong(1,chapterId);
            ResultSet resultSet = preparedSelectChapterByIdStatement.executeQuery();
            AppServer.LOGGER.info("Success SELECT_CHAPTER_BY_ID.");
            if (resultSet.next())
            {
                chapter = new Chapter(resultSet.getString(DatabaseHandler.CHAPTER_TABLE_NAME_COLUMN),
                        resultSet.getString(DatabaseHandler.CHAPTER_TABLE_MARINES_PARENT_LEGION));
            }else
                throw new SQLException();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("An error occurred while executing the SELECT_CHAPTER_BY_ID query!");
            throw new SQLException(exception);
        }finally {
            databaseHandler.closePreparedStatement(preparedSelectChapterByIdStatement);
        }
        return chapter;
    }

    /**
     * @param marineRaw Marine raw.
     * @param user      User.
     * @return Marine.
     * @throws DatabaseHandlingException When there's exception inside.
     */
/**problem in here*/
    public SpaceMarine insertMarine(MarineRaw marineRaw,User user) throws DatabaseHandlingException {
        SpaceMarine marine;
        PreparedStatement preparedInsertMarineStatement = null;
        PreparedStatement preparedInsertCoordinatesStatement = null;
        PreparedStatement preparedInsertChapterStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();
            java.util.Date creationDate = java.util.Date.from(Instant.now());

            preparedInsertMarineStatement = databaseHandler.getPreparedStatement(INSERT_MARINE,true);
            preparedInsertCoordinatesStatement = databaseHandler.getPreparedStatement(INSERT_COORDINATES,true);
            preparedInsertChapterStatement = databaseHandler.getPreparedStatement(INSERT_CHAPTER,true);

            preparedInsertChapterStatement.setString(1,marineRaw.getChapter().getName());
            preparedInsertChapterStatement.setString(2,marineRaw.getChapter().getParentLegion());

            if (preparedInsertChapterStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedChapterKeys = preparedInsertChapterStatement.getGeneratedKeys();
            long chapterId;
            if (generatedChapterKeys.next()){
                chapterId = generatedChapterKeys.getInt(1);
            }else
                throw new SQLException();
            AppServer.LOGGER.severe("success INSERT_CHAPTER.");

 //           Date date = new Date();
 //           Timestamp.valueOf(String.valueOf(creationDate))
            preparedInsertMarineStatement.setString(1, marineRaw.getName());
            preparedInsertMarineStatement.setTimestamp(2, new Timestamp(creationDate.getTime()));
            preparedInsertMarineStatement.setLong(3, marineRaw.getHealth());
            preparedInsertMarineStatement.setString(4, marineRaw.getCategory().toString());
            preparedInsertMarineStatement.setString(5, marineRaw.getWeaponType().toString());
            preparedInsertMarineStatement.setString(6, marineRaw.getMeleeWeapon().toString());
            preparedInsertMarineStatement.setLong(7, chapterId);
            preparedInsertMarineStatement.setLong(8, databaseUserManager.getUserIdByUsername(user));
            if (preparedInsertMarineStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedMarineKeys = preparedInsertMarineStatement.getGeneratedKeys();
            long spaceMarineId;
            if (generatedMarineKeys.next()) {
                spaceMarineId = generatedMarineKeys.getInt(1);
            } else throw new SQLException();
            AppServer.LOGGER.info("Success INSERT_MARINE.");

            preparedInsertCoordinatesStatement.setLong(1, spaceMarineId);
            preparedInsertCoordinatesStatement.setInt(2, marineRaw.getCoordinates().getX());
            preparedInsertCoordinatesStatement.setInt(3, marineRaw.getCoordinates().getY());
            preparedInsertCoordinatesStatement.execute();
            AppServer.LOGGER.severe("Success INSERT_COORDINATES.");
            ResponseOutputer.appendln("Success INSERT_COORDINATES.");

            marine = new SpaceMarine(
                    spaceMarineId,
                    marineRaw.getName(),
                    marineRaw.getCoordinates(),
                    creationDate,
                    marineRaw.getHealth(),
                    marineRaw.getCategory(),
                    marineRaw.getWeaponType(),
                    marineRaw.getMeleeWeapon(),
                    marineRaw.getChapter(),
                    user
            );
            databaseHandler.commit();
            return marine;
        }
        catch (SQLException exception) {
            AppServer.LOGGER.severe("An error occurred while executing a group of requests to add a new object!");
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        }
        finally {
            databaseHandler.closePreparedStatement(preparedInsertMarineStatement);
            databaseHandler.closePreparedStatement(preparedInsertCoordinatesStatement);
            databaseHandler.closePreparedStatement(preparedInsertChapterStatement);
            databaseHandler.setNormalMode();
        }
    }

    /**
     * @param marineRaw Marine raw.
     * @param marineId  Id of Marine.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public void updateMarineById(long marineId,MarineRaw marineRaw) throws DatabaseHandlingException{
        PreparedStatement preparedUpdateMarineNameByIdStatement = null;
        PreparedStatement preparedUpdateMarineHealthByIdStatement = null;
        PreparedStatement preparedUpdateMarineCategoryByIdStatement = null;
        PreparedStatement preparedUpdateMarineWeaponTypeByIdStatement = null;
        PreparedStatement preparedUpdateMarineMeleeWeaponByIdStatement = null;
        PreparedStatement preparedUpdateCoordinatesByMarineIdStatement = null;
        PreparedStatement preparedUpdateChapterByIdStatement = null;
        try{
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            preparedUpdateMarineNameByIdStatement = databaseHandler.getPreparedStatement(UPDATE_MARINE_NAME_BY_ID, false);
            preparedUpdateMarineHealthByIdStatement = databaseHandler.getPreparedStatement(UPDATE_MARINE_HEALTH_BY_ID, false);
            preparedUpdateMarineCategoryByIdStatement = databaseHandler.getPreparedStatement(UPDATE_MARINE_CATEGORY_BY_ID, false);
            preparedUpdateMarineWeaponTypeByIdStatement = databaseHandler.getPreparedStatement(UPDATE_MARINE_WEAPON_TYPE_BY_ID, false);
            preparedUpdateMarineMeleeWeaponByIdStatement = databaseHandler.getPreparedStatement(UPDATE_MARINE_MELEE_WEAPON_BY_ID, false);
            preparedUpdateCoordinatesByMarineIdStatement = databaseHandler.getPreparedStatement(UPDATE_COORDINATES_BY_MARINE_ID, false);
            preparedUpdateChapterByIdStatement = databaseHandler.getPreparedStatement(UPDATE_CHAPTER_BY_ID, false);

            if (marineRaw.getName() != null) {
                preparedUpdateMarineNameByIdStatement.setString(1, marineRaw.getName());
                preparedUpdateMarineNameByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineNameByIdStatement.executeUpdate() == 0) throw new SQLException();
                Logger.getLogger("Success UPDATE_MARINE_NAME_BY_ID.");
            }

            if (marineRaw.getCoordinates() != null){
                preparedUpdateCoordinatesByMarineIdStatement.setInt(1,marineRaw.getCoordinates().getX());
                preparedUpdateCoordinatesByMarineIdStatement.setDouble(2,marineRaw.getCoordinates().getY());
                preparedUpdateCoordinatesByMarineIdStatement.setLong(3, marineId);
                System.out.println(preparedUpdateCoordinatesByMarineIdStatement);
                System.out.println(preparedUpdateCoordinatesByMarineIdStatement.executeUpdate());
                if (preparedUpdateCoordinatesByMarineIdStatement.executeUpdate() == 0) throw new SQLException();
                /** can viet them nhat ky  */
            }

            if (marineRaw.getHealth() != -1){
                preparedUpdateMarineHealthByIdStatement.setLong(1,marineRaw.getHealth());
                preparedUpdateMarineHealthByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineHealthByIdStatement.executeUpdate() == 0) throw new SQLException();
                /**Can viet nhat ky o doan nay*/

            }
            if (marineRaw.getCategory() != null){
                preparedUpdateMarineCategoryByIdStatement.setString(1, marineRaw.getCategory().toString());
                preparedUpdateMarineCategoryByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineCategoryByIdStatement.executeUpdate() == 0) throw new SQLException();
                /**Can viet nhat ky o doan nay*/
            }
            if (marineRaw.getWeaponType() != null) {
                preparedUpdateMarineWeaponTypeByIdStatement.setString(1, marineRaw.getWeaponType().toString());
                preparedUpdateMarineWeaponTypeByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineWeaponTypeByIdStatement.executeUpdate() == 0) throw new SQLException();
                /**Can viet nhat ky o doan nay*/
            }
            if (marineRaw.getMeleeWeapon() != null) {
                preparedUpdateMarineMeleeWeaponByIdStatement.setString(1, marineRaw.getMeleeWeapon().toString());
                preparedUpdateMarineMeleeWeaponByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineMeleeWeaponByIdStatement.executeUpdate() == 0) throw new SQLException();
                /**Can viet nhat ky o doan nay*/
            }
            if (marineRaw.getChapter() != null) {
                preparedUpdateChapterByIdStatement.setString(1, marineRaw.getChapter().getName());
                preparedUpdateChapterByIdStatement.setString(2, marineRaw.getChapter().getParentLegion());
                preparedUpdateChapterByIdStatement.setLong(3, getChapterIdByIdMarineId(marineId));
                if (preparedUpdateChapterByIdStatement.executeUpdate() == 0) throw new SQLException();
                /**Can viet nhat ky o doan nay*/
            }

            databaseHandler.commit();
        } catch (SQLException exception) {
            /**viet ghi nhat ky vao day*/
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        }finally {
            databaseHandler.closePreparedStatement(preparedUpdateMarineNameByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateMarineHealthByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateMarineCategoryByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateMarineWeaponTypeByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateMarineMeleeWeaponByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateCoordinatesByMarineIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateChapterByIdStatement);
            databaseHandler.setNormalMode();
        }

    }

    public void deleteMarineById(long marineId) throws DatabaseHandlingException {
        PreparedStatement preparedDeleteChapterByIdStatement = null;
        try{
            preparedDeleteChapterByIdStatement = databaseHandler.getPreparedStatement(DELETE_MARINE_BY_ID,false);
            preparedDeleteChapterByIdStatement.setLong(1, marineId);

            preparedDeleteChapterByIdStatement.execute();

            if (preparedDeleteChapterByIdStatement.executeUpdate() == 0) Outputer.println(3);

            AppServer.LOGGER.severe("Success DELETE_CHAPTER_BY_ID.");
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("An error occurred while executing the DELETE_CHAPTER_BY_ID request!");
            throw new DatabaseHandlingException();
        }finally {
            databaseHandler.closePreparedStatement(preparedDeleteChapterByIdStatement);
        }
    }

    private long getChapterIdByHealthMarineHealth(Long healthMarine) throws SQLException{
        long chapterHealth;
        PreparedStatement preparedDeleteChapterByHealthStatement = null;
        try{
            preparedDeleteChapterByHealthStatement = databaseHandler.getPreparedStatement(SELECT_MARINE_BY_HEALTH,false);
            preparedDeleteChapterByHealthStatement.setLong(1,healthMarine);
            ResultSet resultSet = preparedDeleteChapterByHealthStatement.executeQuery();
            AppServer.LOGGER.info("Success SELECT_MARINE_BY_HEALTH.");
            if (resultSet.next()){
                chapterHealth = resultSet.getLong(DatabaseHandler.MARINE_TABLE_HEALTH_COLUMN);
            }else throw new SQLException();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("An error occurred while executing the SELECT_MARINE_BY_HEALTH query!");
            throw new SQLException();
        }finally {
            databaseHandler.closePreparedStatement(preparedDeleteChapterByHealthStatement);
        }
        return chapterHealth;
    }

    public void deleteMarineByHealth(long marineHealth) throws DatabaseHandlingException {
        PreparedStatement preparedDeleteChapterByIdStatement = null;
        try{
           preparedDeleteChapterByIdStatement = databaseHandler.getPreparedStatement(DELETE_MARINE_BY_HEALTH,false);
           preparedDeleteChapterByIdStatement.setLong(1,getChapterIdByHealthMarineHealth(marineHealth));
           if (preparedDeleteChapterByIdStatement.executeUpdate() == 0) Outputer.println(3); /** tai sao lai la print 3*/
            AppServer.LOGGER.info("Success DELETE_MARINE_BY_HEALTH");
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("An error occurred while executing the DELETE_MARINE_BY_HEALTH query!");
            throw new DatabaseHandlingException();
        }finally {
            databaseHandler.closePreparedStatement(preparedDeleteChapterByIdStatement);
        }
    }

    /**
     * Checks Marine user id.
     *
     * @param marineId Id of Marine.
     * @param user Owner of marine.
     * @throws DatabaseHandlingException When there's exception inside.
     * @return Is everything ok.
     */
    public boolean checkMarineUserId(long marineId, User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectMarineByIdAndUserIdStatement = null;
        try {
            preparedSelectMarineByIdAndUserIdStatement = databaseHandler.getPreparedStatement(SELECT_MARINE_BY_ID_AND_USER_ID, false);
            preparedSelectMarineByIdAndUserIdStatement.setLong(1, marineId);
            preparedSelectMarineByIdAndUserIdStatement.setLong(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedSelectMarineByIdAndUserIdStatement.executeQuery();
            AppServer.LOGGER.info("Success SELECT_MARINE_BY_ID_AND_USER_ID.");
            System.out.println(resultSet.next());
            return !resultSet.next();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("An error occurred while executing the SELECT_MARINE_BY_ID_AND_USER_ID! query!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectMarineByIdAndUserIdStatement);
        }
    }


    /**
     * Clear the collection.
     *
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public void clearCollection() throws DatabaseHandlingException {
        Stack<SpaceMarine> marineList = getCollection();
        for (SpaceMarine marine : marineList) {
            deleteMarineById(marine.getId());
        }
    }
}
