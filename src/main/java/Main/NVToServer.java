package Main;

import DAO.*;
import RSAencryption.RSADataSender;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by July on 11.12.2017.
 */
public final class NVToServer {
    private static String parametersAddress = "parameters/parameters.xml";
    private static XMLwriterReader<Parameters> reader = new XMLwriterReader(parametersAddress);
    private static RSADataSender RSASender = new RSADataSender();

    /**
     * Finds number of records with 'transmitted=false' (transmitted is taken from 'parameters')
     * @return number of records
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static long getCountOfRecords(Class c){
        long countOfRecords = 0;
        Parameters param = reader.ReadFile(Parameters.class);
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());

        try(Connection con = daoFactory.getConnection()){
            IGenericDAO daoC = daoFactory.getDAO(con, c);
            countOfRecords = daoC.getCountTransmitted(param.getTransmitted());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return countOfRecords;
    }

    /**
     * Метод меняет поле transmitted указаных в списке записей на true
     * @param list  список объектов Line
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void transmittedsToTRUELine(List<Line> list)throws IOException, ClassNotFoundException {
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
     * @param list  список объектов Zone
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void transmittedsToTRUEZone(List<Zone> list)throws IOException, ClassNotFoundException {
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
     * Метод меняет поле transmitted указаных в списке записей на true
     * @param list  список объектов HeatMap
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void transmittedToTrueHeatMap(List<HeatMap> list){
        Parameters param = reader.ReadFile(Parameters.class);
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try(Connection con = daoFactory.getConnection()) {
            IGenericDAO daoH = daoFactory.getDAO(con, HeatMap.class);
            for(HeatMap hm: list){
                daoH.updateTransmitted(hm.getId(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * В методе показаны примеры работы с интерфейсом IGenericDAO
     * (интерфейс работы с базой данных) на примере таблицы Line
     * @return Список первых n записей таблицы Line
     * (n указываться как аргумент limit метода daoL.getByTransmittedLimit)
     * @throws Exception
     */
    private static List<Line> getLines(long count) throws Exception{
        Parameters param = reader.ReadFile(Parameters.class);
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        //список для хранения полученых линий с базы данных
        List<Line> list;
        //создание подключения к базе
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, Line.class);
            //получение списка с определенным количеством записей таблици в которых параметр transmitted = false
            list = daoL.getByTransmittedLimit(param.getTransmitted(), count);
            con.close();
        }
        System.out.println("List lines size: " + list.size());
        return list;
    }


    /**
     * В методе показаны примеры работы с интерфейсом IGenericDAO
     * (интерфейс работы с базой  данных) на примере таблицы Zone
     * @return Список первых n записей таблицы Zone
     * (n указываться как аргумент limit метода daoL.getByTransmittedLimit)
     * @throws Exception
     */
    private static List<Zone> getZones(long count) throws Exception{
        Parameters param = reader.ReadFile(Parameters.class);
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        //список для хранения полученых линий с базы данных
        List<Zone> list;
        //создание подключения к базе
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoZ = daoFactory.getDAO(con, Zone.class);
            //получение списка с определенным количеством записей таблици в которых параметр transmitted = false
            list = daoZ.getByTransmittedLimit(param.getTransmitted(), count);
            con.close();
        }
        System.out.println("List zones size: " + list.size());
        return list;
    }

    /**
     * В методе показаны примеры работы с интерфейсом IGenericDAO
     * (интерфейс работы с базой  данных) на примере таблицы HeatMap
     * @return Список первых n записей таблицы HeatMap
     * (n указываться как аргумент limit метода daoL.getByTransmittedLimit)
     * @throws Exception
     */
    private static List<HeatMap> getHeatMap(long count){
        Parameters param = reader.ReadFile(Parameters.class);
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        //список для хранения полученых линий с базы данных
        List<HeatMap> list = null;
        try(Connection con = daoFactory.getConnection()){
            IGenericDAO daoH = daoFactory.getDAO(con, HeatMap.class);
            list = daoH.getByTransmittedLimit(param.getTransmitted(), count);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("List heat map size: " + list.size());
        return list;
    }
    /**
     * Sends lines to the server
     * @param count - number of lines that should be sent
     * @return
     */
    public static boolean sendLines(long count){
        boolean sendSuccess = false;
        DataSender sender = new DataSender();
        String messageLines = null;
        List<Line> list = new ArrayList();
        SQLList sqlList = new SQLList();
        try {
            long time = System.currentTimeMillis();
            list.addAll(getLines(count));
            sqlList.addAll(list);
            messageLines = sqlList.toJSON();
            time = System.currentTimeMillis() - time;
            System.out.println("Read time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String encryptedLines = RSASender.encryptJSON(messageLines);

        String jsonLines = "{" +
                "\"hash\":\"--Julya test--\"," +
                "\"data\":\"" + encryptedLines + "\"}";
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

    /**
     * Sends zones to the server
     * @param count - number of zones that should be sent
     * @return
     */
    public static boolean sendZones(long count){
        boolean sendSuccess = false;
        DataSender sender = new DataSender();
        String messageZones = null;
        List<Zone> list = new ArrayList<>();
        /**Объект содержащий список с мотодом toJSON*/
        SQLList sqlList = new SQLList();
        try {
            long time = System.currentTimeMillis();
            list.addAll(getZones(count));
            sqlList.addAll(list);
            messageZones = sqlList.toJSON();
            time = System.currentTimeMillis() - time;
            System.out.println("Read time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String encryptedZones = RSASender.encryptJSON(messageZones);
        String jsonZones = "{" +
                "\"hash\":\"--Julya test--\"," +
                "\"data\":\"" + encryptedZones + "\"}";
        try {
            long time = System.currentTimeMillis();
            sendSuccess = sender.SendData(jsonZones, "http://ppd.cifr.us/api/zone/put");
            time = System.currentTimeMillis() - time;
            System.out.println("Send time: " + time);
        } catch (Exception e) {
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

    /**
     * Sends heat maps to the server
     * @param count - number of heat maps that should be sent
     * @return
     */
    public static boolean sendHeatMap(long count){
        boolean sendSuccess = false;
        DataSender sender = new DataSender();
        String messageHeatMaps = null;
        List<HeatMap> list = new ArrayList<>();
        HeatMapsPars hmList = new HeatMapsPars();
        /**Объект содержащий список с мотодом toJSON*/
        SQLList sqlList = new SQLList();
        try {
            long time = System.currentTimeMillis();
            list.addAll(getHeatMap(count));
            hmList.addAll(list);
            sqlList.addAll(list);
            messageHeatMaps = hmList.toJSON();
            time = System.currentTimeMillis() - time;
            System.out.println("Read time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String encryptedHeatMaps = RSASender.encryptJSON(messageHeatMaps);
        String jsonHeatMaps = "{" +
                "\"hash\":\"--Julya test--\"," +
                "\"data\":\"" + encryptedHeatMaps + "\"}";
        try {
            long time = System.currentTimeMillis();
            sendSuccess = sender.SendData(jsonHeatMaps, "http://ppd.cifr.us/api/point/put");
            time = System.currentTimeMillis() - time;
            System.out.println("Send time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(sendSuccess==true){
            transmittedToTrueHeatMap(list);
        }
        return sendSuccess;
    }
}
