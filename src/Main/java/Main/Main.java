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
    public static void main(String[] args){
        boolean sendSuccess;
        XMLwriterReader<Parameters> reader = new XMLwriterReader("parameters/parameters.xml");
        Parameters param = new Parameters();

        try {
            param = reader.ReadFile(Parameters.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        while (param.getNumberOfStringsLines()>0) {
            sendSuccess = sendLines();
            param.setNumberOfStringsLines(param.getNumberOfStringsLines()-param.getOnePackOfStrings());
            System.out.println("Lines success: " + sendSuccess);
            System.out.println("Lines:" + param.getNumberOfStringsLines());
        }

        try {
            param = reader.ReadFile(Parameters.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        while (param.getNumberOfStringsZones()>0) {
            sendSuccess = sendZones();
            param.setNumberOfStringsZones(param.getNumberOfStringsZones()-param.getOnePackOfStrings());
            System.out.println("Zones success: " + sendSuccess);
            System.out.println("Zones:" + param.getNumberOfStringsZones());
        }
    }

    /**
     * Метод меняет поле transmitted указаных в списке записей на true
     * @param list  список объектов Line
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void transmittedsToTRUELine(List<Line> list)throws IOException, ClassNotFoundException {
        XMLwriterReader<Parameters> reader = new XMLwriterReader("parameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO daoL = daoFactory.getDAO(con, Line.class);
            for (Line l: list) {
                daoL.updateTransmitted(l.getId(), true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод меняет поле transmitted указаных в списке записей на true
     * @param list  список объектов Line
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void transmittedsToTRUEZone(List<Zone> list)throws IOException, ClassNotFoundException {
        XMLwriterReader<Parameters> reader = new XMLwriterReader("parameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO daoL = daoFactory.getDAO(con, Zone.class);
            for (Zone l: list) {
                daoL.updateTransmitted(l.getId(), true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Пример добавления записей в базу данных
     * @param lines список линий которые нужно записать в базу данных
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void createLines(List<Line> lines) throws IOException, ClassNotFoundException {
        XMLwriterReader<Parameters> reader = new XMLwriterReader("parameters/parameters.xml");
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


    /**
     * В методе показаны примеры работы с интерфейсом IGenericDAO
     * (интерфейс работы с базо  данных) на примере таблици Line
     * @return Список первых n записей таблицы Line
     * (n указываться как аргумент limit метода daoL.getByTransmittedLimit)
     * @throws Exception
     */
    public static List<Line> getLines() throws Exception{
        XMLwriterReader<Parameters> reader = new XMLwriterReader("parameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        //список для хранения полученых линий с базы данных
        List<Line> list;
        //создание подключения к базе
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, Line.class);
            //получение списка первых 100 записей таблици в которых параметр transmitted = false
            list = daoL.getByTransmittedLimit(param.getTtansmitted(), param.getOnePackOfStrings());

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
        System.out.println(list.size());
        return list;
    }


    /**
     * В методе показаны примеры работы с интерфейсом IGenericDAO
     * (интерфейс работы с базо  данных) на примере таблици Zone
     * @return Список первых n записей таблицы Zone
     * (n указываться как аргумент limit метода daoL.getByTransmittedLimit)
     * @throws Exception
     */
    public static List<Zone> getZones() throws Exception{
        XMLwriterReader<Parameters> reader = new XMLwriterReader("parameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        //список для хранения полученых линий с базы данных
        List<Zone> list;
        //создание подключения к базе
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, Zone.class);
            //получение списка первых 100 записей таблици в которых параметр transmitted = false
            list = daoL.getByTransmittedLimit(param.getTtansmitted(), param.getOnePackOfStrings());


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
        System.out.println(list.size());
        return list;
    }

    public static boolean sendLines(){
        boolean sendSuccess = false;
        DataSender sender = new DataSender();
        String messageLines = null;
        List<Line> list = new ArrayList();
        SQLList sqlList = new SQLList();
        try {
            long time = System.currentTimeMillis();
            list.addAll(getLines());
            sqlList.addAll(list);
            messageLines = sqlList.toJSON();
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
        if(sendSuccess==true){
            try {
                transmittedsToTRUELine(list);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sendSuccess;
    }

    public static boolean sendZones(){
        XMLwriterReader<Parameters> reader = new XMLwriterReader("parameters/parameters.xml");
        Parameters param = new Parameters();
        try {
            param = reader.ReadFile(Parameters.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        boolean sendSuccess = false;
        DataSender sender = new DataSender();
        String messageZones = null;
        List<Zone> list = new ArrayList<>();
        /**Объект содержащий список с мотодом toJSON*/
        SQLList sqlList = new SQLList();
        try {
            long time = System.currentTimeMillis();
            list.addAll(getZones());
            sqlList.addAll(list);
            messageZones = sqlList.toJSON();
            time = System.currentTimeMillis() - time;
            System.out.println("Read time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonZones = "{" +
                "\"hash\":\"--some hash key--\"," +
                "\"data\":" + messageZones + "}";
        try {
                long time = System.currentTimeMillis();
                sendSuccess = sender.SendData(jsonZones, "http://ppd.cifr.us/api/zone/put");
                time = System.currentTimeMillis() - time;
                System.out.println("Send time: " + time);
        } catch (Exception e) {
            //System.out.println("here!");
            e.printStackTrace();
        }
        if(sendSuccess==true){
            try {
                transmittedsToTRUEZone(list);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sendSuccess;
    }
}
