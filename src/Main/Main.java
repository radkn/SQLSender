package Main;

import java.sql.*;

public class Main {
    private static String DB_NAME = "jdbc:mysql://localhost:3306/NewVision";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "root";

    public static void main(String[] args){
        System.out.println(DB_SELECT_ToJSON());
    }

    public static String DB_SELECT_ToJSON(){
        String query = "SELECT * FROM Line";
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        String allObject = "[\n";
        String lineTitle;
        String uid;
        String dataTime;
        String status;
        String type;

        try {
            connection = DriverManager.getConnection(DB_NAME, DB_USER, DB_PASSWORD);
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {

                lineTitle = resultSet.getString(3);
                uid = resultSet.getString(4);
                dataTime = resultSet.getString(5);
                dataTime = dataTime.substring(0, dataTime.length()-2);// обрезка двух лишних символов с базы (".0")
                status = resultSet.getString(6);
                type = resultSet.getString(7);

                allObject+=  "\t{\n\t\t\"lineTitle\":\"" + lineTitle
                        + "\",\n\t\t\"uid\":\"" + uid
                        + "\",\n\t\t\"dataTime\":\"" + dataTime
                        + "\",\n\t\t\"status\":\"" + status
                        + "\",\n\t\t\"type\":\"" + type
                        + "\"\n\t},\n";
            }
            allObject = allObject.substring(0,allObject.length()-2);
            allObject+="\n]";
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
            try {
                if(connection!=null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return allObject;
    }

}
