package utility;


import server.AppServer;

import java.sql.*;
/**
 * A class for handle database.
 */
public class DatabaseHandler {
    // Table name
    public static final String MARINE_TABLE = "space_marine";
    public static final String USER_TABLE = "my_user";
    public static final String COORDINATES_TABLE = "coordinates";
    public static final String CHAPTER_TABLE = "chapter";
    //MARINE_TABLE colum names
    public static final String MARINE_TABLE_ID_COLUMN = "id";
    public static final String MARINE_TABLE_NAME_COLUMN = "name";
    public static final String MARINE_TABLE_CREATION_DATE_COLUMN = "creation_date";
    public static final String MARINE_TABLE_HEALTH_COLUMN = "health";
    public static final String MARINE_TABLE_CATEGORY_COLUMN = "category";
    public static final String MARINE_TABLE_WEAPON_TYPE_COLUMN = "weapon_type";
    public static final String MARINE_TABLE_MELEE_WEAPON_COLUMN = "melee_weapon";
    public static final String MARINE_TABLE_CHAPTER_ID_COLUMN = "chapter_id";
    public static final String MARINE_TABLE_USER_ID_COLUMN = "user_id";
    // USER_TABLE column names
    public static final String USER_TABLE_ID_COLUMN = "id";
    public static final String USER_TABLE_USERNAME_COLUMN = "username";
    public static final String USER_TABLE_PASSWORD_COLUMN = "password";
    // COORDINATES_TABLE column names
    public static final String COORDINATES_TABLE_ID_COLUMN = "id";
    public static final String COORDINATES_TABLE_SPACE_MARINE_ID_COLUMN = "space_marine_id";
    public static final String COORDINATES_TABLE_X_COLUMN = "x";
    public static final String COORDINATES_TABLE_Y_COLUMN = "y";
    // CHAPTER_TABLE column names

    public static final String CHAPTER_TABLE_ID_COLUMN = "id";
    public static final String CHAPTER_TABLE_HEALTH_COLUMN = "health";
    public static final String CHAPTER_TABLE_NAME_COLUMN = "name";
    public static final String CHAPTER_TABLE_MARINES_PARENT_LEGION = "marine_legion";

    private final String JDBC_DRIVER = "org.postgresql.Driver";
    private String url;
    private String user;
    private String password;
    private Connection connection;

    public DatabaseHandler(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;

        connectToDatabase();
    }
    /**
     * A class for connect to database.
     */
    private void connectToDatabase() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, user, password);
            Outputer.println("Database connection established. ");
            AppServer.LOGGER.info("Database connection established.");
        } catch (ClassNotFoundException e) {
            Outputer.printerror("The database management driver was not found!");
            AppServer.LOGGER.severe("The database management driver was not found!");
        } catch (SQLException e) {
            Outputer.printerror("An error occurred while connecting to the database!");
            AppServer.LOGGER.severe("An error occurred while connecting to the database!");
        }
    }
    /**
     * @param sqlStatement SQL statement to be prepared.
     * @param generateKeys Is keys needed to be generated.
     * @return Pprepared statement.
     * @throws SQLException When there's exception inside.
     * */
    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) throws SQLException {
        PreparedStatement preparedStatement;
        try{
            if (connection == null )throw new SQLException();
            int autoGeneratedKeys = generateKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
            /** tao doi tuong preparedStatement trong jdbc
             * try van tham so */
            preparedStatement = connection.prepareStatement(sqlStatement,autoGeneratedKeys);
            return preparedStatement;
            //AppServer.LOGGER.info("SQL query "+ sqlStatement +" is prepared. ");
            //autoGeneratedKeys - gan co cho biet khao co duoc tao tu dong hay khong
            // sqlStatement - co the chua 1 hoac nhieu
        } catch (SQLException exception) {
            if (connection == null) AppServer.LOGGER.severe("Database connection not established!");
            throw new SQLException();
        }
    }

    /**
     * Close prepared statement.
     *
     * @param sqlStatement SQL statement to be closed.
     */
    public void closePreparedStatement(PreparedStatement sqlStatement){
        if (sqlStatement == null ) return;
        try{
            sqlStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Close connection to database.
     */
    public void closeConnection(){
        if (connection == null) return;
        try{
            connection.close();
            Outputer.println ("The connection to the database was broken.");
            AppServer.LOGGER.severe("The connection to the database was broken.");
        } catch (SQLException throwables) {
            Outputer.printerror ("An error occurred while disconnecting the connection to the database!");
            AppServer.LOGGER.severe ("An error occurred while disconnecting the connection to the database!");
        }
    }


    /**
     * Set commit mode of database.
     */
    public void setCommitMode() {
        try {
            if (connection == null) throw new SQLException();
            /**du  chi update vào database khi goi  commit()*/
            // trinh dieu khien se mac nhien bat dau mot giao dich moi sau moi lan cam ket
            /**tat tu dong cam ket*/
            connection.setAutoCommit(false);
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при установлении режима транзакции базы данных!");
        }
    }

    /**
     * Set normal mode of database.
     */
    public void setNormalMode() {
        try {
            if (connection == null) throw new SQLException();
            /**autoCommit mode for the connection*/
            /**tu dong cam ket*/
            //che do tu dong gui, tat ca các cau lenh SQL cua no se duoc chay va duoc cam
            // ket duoi dang cac giao dich rieng le
            connection.setAutoCommit(true);
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при установлении нормального режима базы данных!");
        }
    }

    /**
     * Commit database status.
     */
    public void commit() {
        try {
            if (connection == null) throw new SQLException();
            connection.commit();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при подтверждении нового состояния базы данных!");
        }
    }

    /**
     * Roll back database status.
     */
    public void rollback() {
        try {
            if (connection == null) throw new SQLException();
            /**lay lai du lieu ban dau*/
            connection.rollback();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("An error occurred while resetting the database!");
        }
    }

    /**
     * Set save point of database.lieu
     */

    /**dat diem luu cua csdl bang phuong thúc saeSavepoint(String savePointName) cua giao dien ket noi
     * bat cu khi nao loi xay ra sau diem luu ban hoan toan co the hoan tac cac su kien cho den diem luu bang phuong thuc rollback()
     * */

    public void setSavepoint() {
        try {
            if (connection == null) throw new SQLException();
            connection.setSavepoint();
            // luu tru mot trang thai cua csdl
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("An error occurred while saving the database state!");
        }
    }
}
