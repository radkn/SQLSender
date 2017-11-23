package Main;

import java.sql.*;

public class SQLProvider{
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    private String query = "SELECT * FROM Line";
    private PreparedStatement statement = null;
    private Connection connection = null;
    private ResultSet resultSet = null;

    SQLProvider(String url, String user, String password){
        DB_URL = url;
        DB_USER = user;
        DB_PASSWORD = password;
    }




    public SQLList getLines() throws SQLException {
        query = "SELECT * FROM Line";
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        String lineTitle;
        String uid;
        String dataTime;
        String status;
        String type;
        SQLList lineList = new SQLList();

        while(resultSet.next()) {
            lineTitle = resultSet.getString(3);
            uid = resultSet.getString(4);
            dataTime = resultSet.getString(5);
            dataTime = dataTime.substring(0, dataTime.length()-2);// обрезка двух лишних символов с базы (".0")
            status = resultSet.getString(6);
            type = resultSet.getString(7);
            lineList.add(new SQLLine(lineTitle, uid, dataTime, status, type));
        }
        return lineList;
    }

    public SQLList getZones() throws SQLException {
        query = "SELECT * FROM Zone";
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        String zoneTitle;
        String uid;
        String datatime_start;
        String datatime_end;
        String datatime_seconds;
        String type;
        SQLList lineList = new SQLList();

        while(resultSet.next()) {
            zoneTitle = resultSet.getString(3);
            uid = resultSet.getString(4);
            datatime_start = resultSet.getString(5);
            datatime_start = datatime_start.substring(0, datatime_start.length()-2);
            datatime_end = resultSet.getString(6);
            datatime_end = datatime_end.substring(0, datatime_end.length()-2);// обрезка двух лишних символов с базы (".0")
            datatime_seconds = resultSet.getString(7);
            type = resultSet.getString(8);
            lineList.add(new SQLZone(zoneTitle, uid, datatime_start, datatime_end,datatime_seconds, type));
        }
        return lineList;
    }

    public void open(){
        try {
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            if(statement!=null){
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(connection!=null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
