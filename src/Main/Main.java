package Main;

import java.sql.*;

public class Main {
    public static String DB_NAME = "jdbc:mysql://localhost:3306/Students";
    public  static String DB_USER = "root";
    public  static String DB_PASSWORD = "root";

    public static void main(String[] args) throws ClassNotFoundException {
        DB_SELECT();
    }

    public static void DB_SELECT(){
        String query = "SELECT * FROM Students";
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DB_NAME, DB_USER, DB_PASSWORD);
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            System.out.println("id" + "    " + "name" + "    " + "mark");
            while(resultSet.next()) {
                int id = resultSet.getInt(1);
                String Name = resultSet.getString(2);
                double Mark = resultSet.getDouble(3);
                System.out.println(id + "    " + Name + "    " + Mark);
            }
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

    }

}
