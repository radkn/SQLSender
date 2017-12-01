package Main;

import DAO.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
*
*
*
* */
public class Main {

    public static List<Line> newList = new ArrayList<>();
    public static void main(String[] args){
        boolean sendSuccess = false;
        /*long totalTime = System.currentTimeMillis();
        for (int i = 0; i < 25; i++) {
            sendSuccess = sendLines();
        }
        totalTime = System.currentTimeMillis()-totalTime;
        System.out.println("Total time to read and send: " + totalTime);
*/
        sendSuccess = sendLines();
        if(sendSuccess==true){
            try {
                transmittedsToTRUE();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println(sendSuccess);
    }

    public static void transmittedsToTRUE()throws IOException, ClassNotFoundException {
        XMLwriterReader<Parameters> reader = new XMLwriterReader(".idea/parameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO daoL = daoFactory.getDAO(con, Line.class);
            for (Line l: newList) {
                daoL.updateTransmitted(l.getId(), true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**Пример создания новых записей*/
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
            list = daoL.getByTransmittedLimit(false, 100);
            newList.addAll(list);
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

        System.out.println(listForSend.size());
        allObject = listForSend.toJSON();
        return allObject;
    }


    public static String getZones() throws Exception{
        XMLwriterReader<Parameters> reader = new XMLwriterReader(".idea/parameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        //список для хранения полученых линий с базы данных
        List<Zone> list;
        //
        SQLList listForSend = new SQLList();
        //строка для хранения JSON
        String allObject;
        //создание подключения к базе
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, Zone.class);
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
            long time = System.currentTimeMillis();
            messageLines = getLines();
            time = System.currentTimeMillis() - time;
            System.out.println("Read time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonLines = "{" +
                "\"hash\":\"--some hash key--\"," +
                "\"data\":" + messageLines + "}";

        try {
                long time = System.currentTimeMillis();
                sendSuccess = sender.SendData(jsonLines, "http://ppd.cifr.us/api/line/put");
                time = System.currentTimeMillis() - time;
                System.out.println("Send time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendSuccess;
    }

    public static boolean sendZones(){
        boolean sendSuccess = false;
        DataSender sender = new DataSender();
        String messageZones = null;
        try {
            System.out.println("Before reading");
            messageZones = getZones();
            System.out.println("After reading");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonZones = "{" +
                "\"hash\":\"--some hash key--\"," +
                "\"data\":" + messageZones + "}";
        try {

            for (int i = 0; i < 5; i++) {
                System.out.println("Before sending");
                sendSuccess = sender.SendData(jsonZones, "http://ppd.cifr.us/api/zone/put");
                System.out.println("After sending");
            }

        } catch (Exception e) {
            //System.out.println("here!");
            e.printStackTrace();
        }
        return sendSuccess;
    }
}
