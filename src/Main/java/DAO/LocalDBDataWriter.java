

import dataClasses.Data;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by July on 27.10.2017.
 * Connects to DataBase NewVision via OpenServer.
 * Writes data to the tables in this DataBase.
 */
public class LocalDBDataWriter{

    private static String DB_NAME = "jdbc:mysql://localhost:3306/NewVision";
    private static String DB_USER = "mysql";
    private static String DB_PASSWORD = "mysql";

    PreparedStatement statement = null;
    Connection connection = null;

    /**
     * Opens connection to database
     * @param db_name - address and name of database
     * @param db_user - user
     * @param db_password - password
     */
    public void openConnection(String db_name, String db_user, String db_password){
        this.DB_NAME = db_name;
        this.DB_USER = db_user;
        this.DB_PASSWORD = db_password;
        try {
            connection = DriverManager.getConnection(DB_NAME, DB_USER, DB_PASSWORD);
            System.out.println("Database connected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Inserts data of line intersection into LinesDataTable
     * @param scene_id - id of current scene
     * @param lineTitle - name of line (location)
     * @param uid - uid
     * @param data - object of "Data" class. It contains time of intersection and in/out information.
     * @param type - type of line (now it is constantly '1')
     */
    public void writeLinesDataToDB(String scene_id, String lineTitle, String uid, Data data, int type) throws Exception {
        String query = "INSERT INTO Line (scene_id,lineTitle,uid,datetime,status,type,time_stamp,transmitted) VALUES (?,?,?,?,?,?,?,?)";
        if(!(tableExist("Line"))) throw new Exception("Table 'Line' doesn`t exist!");
        Date date = data.getDate(); //time of intersection
        long millis = date.getTime(); //this time in millis
        Timestamp datetime = new Timestamp(millis);//this time in "Timestamp" format (for sql)
        long current = System.currentTimeMillis();
        Timestamp currentDate = new Timestamp(current);
        boolean transmitted = false;
        int status; //1 - entered, 0 - exited

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1,scene_id);
            statement.setString(2,lineTitle);
            statement.setString(3, uid);
            statement.setTimestamp(4, datetime);

            if(data.getIn()==1){
                status = 1;
                statement.setInt(5, status);
            }
            else{
                status = 0;
                statement.setInt(5, status);
            }

            statement.setInt(6, type);
            statement.setTimestamp(7, currentDate);
            statement.setBoolean(8, transmitted);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(statement!=null){
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inserts data of zone intersection into ZonesDataTable
     * @param scene_id - id of scene
     * @param zoneTitle - name of zone
     * @param uid - uid
     * @param start - time when object entered the zone
     * @param end - time when object left the zone
     * @param spent - time the object spent in zone
     * @param type - type of zone (now constantly '1')
     */
    public void writeZonesDataToDB(String scene_id, String zoneTitle, String uid, long start, long end, long spent, int type) throws Exception {
        String query = "INSERT INTO Zone (scene_id,zoneTitle,uid,datetime_start,datetime_end,datetime_delay,type,time_stamp,transmitted) VALUES (?,?,?,?,?,?,?,?,?)";
        if(!(tableExist("Zone"))) throw new Exception("Table 'Zone' doesn`t exist!");
        Timestamp datetime_start = new Timestamp(start); //time the object entered
        Timestamp datetime_end = new Timestamp(end); //time the object exited
        int time_spent = (int)(spent/1000);
        if(spent%1000!=0){
            time_spent += 1;
        }
        long current = System.currentTimeMillis();
        Timestamp currentDate = new Timestamp(current);
        boolean transmitted = false;
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1,scene_id);
            statement.setString(2,zoneTitle);
            statement.setString(3, uid);
            statement.setTimestamp(4, datetime_start);
            statement.setTimestamp(5, datetime_end);
            statement.setInt(6, time_spent);
            statement.setInt(7, type);
            statement.setTimestamp(8, currentDate);
            statement.setBoolean(9, transmitted);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(statement!=null){
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Closes connection to database
     */
    public void closeConnection(){
        try {
            connection.close();
            System.out.println("Connection to database was closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Checks the existence of a certain table in database
     * @param tableName - name of table
     * @return
     */
    private boolean tableExist(String tableName){
        boolean tExists = false;
        try {
            try (ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null)) {
                while (rs.next()) {
                    String tName = rs.getString("TABLE_NAME");
                    if (tName != null && tName.equals(tableName)) {
                        tExists = true;
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tExists;
    }

    public void createLinesTable(){
        String query = "CREATE TABLE Line " +
                "(id INT NOT NULL AUTO_INCREMENT, " +
                "scene_id VARCHAR(255), " +
                "lineTitle VARCHAR(255), " +
                "uid VARCHAR(255), " +
                "datetime TIMESTAMP NULL, " +
                "status INT(11), " +
                "type INT(11), " +
                "time_stamp TIMESTAMP, " +
                "transmitted TINYINT(1) NOT NULL, " +
                "PRIMARY KEY(id))";
        try {
            statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createZonesTable(){
        String query = "CREATE TABLE Zone " +
                "(id INT NOT NULL AUTO_INCREMENT, " +
                "scene_id VARCHAR(255), " +
                "zoneTitle VARCHAR(255), " +
                "uid VARCHAR(255), " +
                "datetime_start TIMESTAMP NULL, " +
                "datetime_end TIMESTAMP NULL, " +
                "datetime_delay INT(11), " +
                "type INT(11), " +
                "time_stamp TIMESTAMP, " +
                "transmitted TINYINT(1) NOT NULL, " +
                "PRIMARY KEY(id))";
        try {
            statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

