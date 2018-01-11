package Main;

import DAO.*;
import RSAencryption.RSADataSender;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SendNVToServer<T extends AbstractSendableRecord> {
    private static String parametersAddress = "parameters/parameters.xml";
    private static XMLwriterReader<Parameters> reader = new XMLwriterReader(parametersAddress);
    private static RSADataSender RSASender = new RSADataSender();

    /**
     * Finds number of records with 'transmitted=false' (transmitted is taken from 'parameters')
     * @return number of records
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public long getCountOfRecords(Class c){
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
    private void transmittedsToTRUE(List<T> list, Class c)throws IOException, ClassNotFoundException {
        Parameters param = reader.ReadFile(Parameters.class);
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO daoL = daoFactory.getDAO(con, c);
            for (T l: list) {
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
     * (интерфейс работы с базой данных) на примере таблицы Line
     * @return Список первых n записей таблицы Line
     * (n указываться как аргумент limit метода daoL.getByTransmittedLimit)
     * @throws Exception
     */
    private List<T> getRecords(long count, Class c) throws Exception{
        Parameters param = reader.ReadFile(Parameters.class);
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        //список для хранения полученых линий с базы данных
        List<T> list;
        //создание подключения к базе
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, c);
            //получение списка с определенным количеством записей таблици в которых параметр transmitted = false
            list = daoL.getByTransmittedLimit(param.getTransmitted(), count);
            con.close();
        }
        System.out.println("List lines size: " + list.size());
        return list;
    }

    /**
     * Sends lines to the server
     * @param count - number of lines that should be sent
     * @return
     */
    public boolean sendLines(long count){
        Parameters param = reader.ReadFile(Parameters.class);
        boolean sendSuccess = false;
        DataSender sender = new DataSender();
        String messageLines = null;
        List<T> list = new ArrayList();
        SQLList sqlList = new SQLList();
        try {
            long time = System.currentTimeMillis();
            list.addAll(getRecords(count, Line.class));
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
        System.out.println("Enc Lines: ");
        System.out.println(encryptedLines);
        try {
            long time = System.currentTimeMillis();
            sendSuccess = sender.SendData(jsonLines, param.getServerURL());
            time = System.currentTimeMillis() - time;
            System.out.println("Send time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(sendSuccess==true){
            try {
                transmittedsToTRUE(list, Line.class);
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
    public boolean sendZones(long count){
        Parameters param = reader.ReadFile(Parameters.class);
        boolean sendSuccess = false;
        DataSender sender = new DataSender();
        String messageZones = null;
        List<T> list = new ArrayList<>();
        /**Объект содержащий список с мотодом toJSON*/
        SQLList sqlList = new SQLList();
        try {
            long time = System.currentTimeMillis();
            list.addAll(getRecords(count, Zone.class));
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
        System.out.println("Enc Lines: ");
        System.out.println(encryptedZones);
        try {
            long time = System.currentTimeMillis();
            sendSuccess = sender.SendData(jsonZones, param.getServerURL());
            time = System.currentTimeMillis() - time;
            System.out.println("Send time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(sendSuccess==true){
            try {
                transmittedsToTRUE(list, Zone.class);
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
    public boolean sendHeatMap(long count){
        Parameters param = reader.ReadFile(Parameters.class);
        boolean sendSuccess = false;
        DataSender sender = new DataSender();
        String messageHeatMaps = null;
        List<T> list = new ArrayList<>();
        HeatMapsPars hmList = new HeatMapsPars();
        /**Объект содержащий список с мотодом toJSON*/
        SQLList sqlList = new SQLList();
        try {
            long time = System.currentTimeMillis();
            list.addAll(getRecords(count, HeatMap.class));
            hmList.addAll(list);
            //sqlList.addAll(list);
            messageHeatMaps = hmList.toJSON();
            time = System.currentTimeMillis() - time;
            System.out.println("Read time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("HM string: " + messageHeatMaps);
        String encryptedHeatMaps = RSASender.encryptJSON(messageHeatMaps);
        String jsonHeatMaps = "{" +
                "\"hash\":\"--Julya test--\"," +
                "\"data\":\"" + encryptedHeatMaps + "\"}";
        try {
            long time = System.currentTimeMillis();
            sendSuccess = sender.SendData(jsonHeatMaps, param.getServerURL());
            time = System.currentTimeMillis() - time;
            System.out.println("Send time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(sendSuccess==true){
            try {
                transmittedsToTRUE(list, HeatMap.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sendSuccess;
    }

}
