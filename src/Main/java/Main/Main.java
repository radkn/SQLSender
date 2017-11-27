package Main;

import DAO.*;

import java.sql.*;
import java.util.List;
/**
*
*
*
* */
public class Main {
    private static String DB_URL = "jdbc:mysql://localhost:3306/NewVision";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "root";

    public static void main(String[] args){
        try {
            System.out.println(testGetAll(Line.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String testGetAll(Class cl) throws Exception{
        IDAOFactory daoFactory = new MySQLDaoFactory(DB_URL, DB_USER, DB_PASSWORD);
        List<ISendable> list;
        SQLList listForSend = new SQLList();
        String allObject;
        try(Connection con = daoFactory.getConnection()){
            IGenericDAO daoL = daoFactory.getDAO(con, cl);
            list = daoL.getAll();
            //list.get(1).setTransmitted(true);
            //daoL.update(list.get(1));
            for(int i = 0; i < list.size(); i++){
                listForSend.add(list.get(i));
            }
            allObject = listForSend.toJSON();
        }
        return allObject;
    }

}
