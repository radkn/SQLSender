package Main;

import DAO.*;

import java.io.IOException;
import java.sql.*;
import java.util.List;
/**
*
*
*
* */
public class Main {
    public static void main(String[] args){
        boolean sendSuccess = sendZones();
        System.out.println(sendSuccess);
    }

    public static void createLines(List<Line> lines) throws IOException, ClassNotFoundException {
        XMLwriterReader<Parameters> reader = new XMLwriterReader(".idea/parameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO daoL = daoFactory.getDAO(con, Line.class);
            for (Line l : lines) {
                daoL.create(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getLines() throws Exception{
        XMLwriterReader<Parameters> reader = new XMLwriterReader(".idea/parameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        //список для хранения полученых линий с базы данных
        List<Line> list;
        //
        SQLList listForSend = new SQLList();
        //строка для хранения JSON
        String allObject;
        //создание подключения к базе
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, Line.class);
            //получение списка всех записей таблицы
            list = daoL.getAll();
            //создание новой записи в таблице по переданому объекту
//            daoL.create(list.get(0));
//            Line line = list.get(5);
//            line.setId(list.get(list.size()-3).getId());
            //обновление записи в таблице (обновляеться запись ID которой соответствует ID перезаного объекта)
//            daoL.update(line);
            //получение записи по ID
//            Line l = (Line)daoL.getByPk(440);
//            System.out.println(l.toJSON());
            //удаление записи по ID
//            daoL.delete(416);
            con.close();
        }

        for(int i = 0; i < list.size(); i++){
            listForSend.add(list.get(i));
        }
        allObject = listForSend.toJSON();

        return allObject;
    }

    public static boolean sendLines(){
        boolean sendSuccess = false;
        DataSender sender = new DataSender();
        String messageLines = null;
        try {
            messageLines = getLines();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonLines = "{" +
                "\"hash\":\"--some hash key--\"," +
                "\"data\":" + messageLines + "}";

        try {
            sendSuccess = sender.SendData(jsonLines, "http://ppd.cifr.us/api/line/put");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendSuccess;
    }

    public static boolean sendZones(){
        boolean sendSuccess = false;
        DataSender sender = new DataSender();
        String messageZones = "";
        String jsonZones = "{" +
                "\"hash\":\"--some hash key--\"," +
                "\"data\":" + messageZones + "}";
        try {
            sendSuccess = sender.SendData(jsonZones, "http://ppd.cifr.us/api/zone/put");
        } catch (Exception e) {
            System.out.println("here!");
            e.printStackTrace();
        }
        return sendSuccess;
    }
}
