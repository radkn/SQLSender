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
            System.out.println(testGetAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String testGetAll() throws Exception{
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(DB_URL, DB_USER, DB_PASSWORD);
        //список для хранения полученых линий с базы данных
        List<Line> list;
        //
        SQLList listForSend = new SQLList();
        //строка для хранения JSON
        String allObject;
        //создание подкличения к базе
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, Line.class);
            //получение списка всех записей таблицы
            list = daoL.getAll();
            //создание новой записи в таблице по переданому объекту
            daoL.create(list.get(0));
            Line line = list.get(5);
            line.setId(list.get(list.size()-3).getId());
            //обновление записи в таблице (обновляеться запись ID которой соответствует ID перезаного объекта)
            daoL.update(line);
            //получение записи по ID
            Line l = (Line)daoL.getByPk(440);
            System.out.println(l.toJSON());
            //удаление записи по ID
            daoL.delete(416);
            con.close();
        }

        for(int i = 0; i < list.size(); i++){
            listForSend.add(list.get(i));
        }
        allObject = listForSend.toJSON();

        return allObject;
    }

}
