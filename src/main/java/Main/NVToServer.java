package Main;

import DAO.*;

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
    public static long getCountOfLines() throws IOException, ClassNotFoundException {
        long countLines = 0;
        Parameters param = reader.ReadFile(Parameters.class);
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());

        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO daoL = daoFactory.getDAO(con, Line.class);
            countLines = daoL.getCountTransmitted(false);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countLines;
    }

    public static long getCountOfZones() throws IOException, ClassNotFoundException {
        long countZones = 0;
        Parameters param = reader.ReadFile(Parameters.class);
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());

        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO daoZ = daoFactory.getDAO(con, Zone.class);
            countZones = daoZ.getCountTransmitted(false);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countZones;
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
     * @param list  список объектов Line
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
     * В методе показаны примеры работы с интерфейсом IGenericDAO
     * (интерфейс работы с базо  данных) на примере таблици Line
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
            list = daoL.getByTransmittedLimit(param.getTtansmitted(), count);
            con.close();
        }
        System.out.println("List lines size" + list.size());
        return list;
    }


    /**
     * В методе показаны примеры работы с интерфейсом IGenericDAO
     * (интерфейс работы с базой  данных) на примере таблици Zone
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
            list = daoZ.getByTransmittedLimit(param.getTtansmitted(), count);
            con.close();
        }
        System.out.println("List zones size" + list.size());
        return list;
    }

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
        String jsonLines = "{" +
                "\"hash\":\"--Julya test--\"," +
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
        String jsonZones = "{" +
                "\"hash\":\"--Julya test--\"," +
                "\"data\":" + messageZones + "}";
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
}
