package Main;

import java.sql.*;

public class Main {
    private static String DB_NAME = "jdbc:mysql://localhost:3306/NewVision";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "root";

    public static void main(String[] args){
        System.out.println(DB_SELECT_All_ToJSON());
    }

    public static String DB_SELECT_All_ToJSON(){
        String allObject = null;
        SQLProvider db = new SQLProvider(DB_NAME, DB_USER,DB_PASSWORD);

        try {
            SQLList listForSend;
            db.open();
            listForSend = db.getZones();
            db.close();
            allObject=listForSend.toJSON();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allObject;
    }

}
