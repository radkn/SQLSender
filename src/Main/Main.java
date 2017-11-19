package Main;

import DAO.DAOFactory;
import DAO.Line;
import DAO.LineDAO;
import DAO.MySQLDaoFactory;

import java.sql.*;
import java.util.List;

public class Main {
    private static String DB_NAME = "jdbc:mysql://localhost:3306/NewVision";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "root";

    public static void main(String[] args){
        System.out.println(DB_SELECT_All_ToJSON());
        try {
            testGetAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void testGetAll() throws Exception{
        DAOFactory daoFactory = new MySQLDaoFactory();
        List<Line> list;
        try(Connection con = daoFactory.getConnection()){
            LineDAO daoL = daoFactory.getLineDao(con);
            list = daoL.getAll();
        }

        System.out.println(list.get(1).getLineTitle());
    }

}
