package Main;

import DAO.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class SendToReserveDB{

    private static XMLwriterReader<Parameters> reader;
    private static Parameters param;

    public SendToReserveDB() throws IOException, ClassNotFoundException {

    }

    /**
     * Send all records with transmitted = 1, from  NewVision DB to reserve DB
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void sendAll() throws IOException, ClassNotFoundException {
        setParam();
        //here we get our parameters from .xml file
        try {
            param = reader.ReadFile(Parameters.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            long count = getCountOfRecords(Line.class); //records with transmitted=0
            while (count > 0) {
                count = getCountOfRecords(Line.class);
                if(count >= param.getOnePackOfStrings() )
                    sendLines(param.getOnePackOfStrings());
                else
                    sendLines(count);
                System.out.println("Lines success reserved ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            long count = getCountOfRecords(Zone.class); //records with transmitted=0
            int i = 0;
            while (count > 0) {
                count = getCountOfRecords(Zone.class);
                if (count >= param.getOnePackOfStrings())
                    sendZones(param.getOnePackOfStrings());
                else
                    sendZones(count);
                System.out.println("Zones success reserved");
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set parameters to  reserve database
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void setParam() throws IOException, ClassNotFoundException {
        reader = new XMLwriterReader("parameters/parameters.xml");
        param = reader.ReadFile(Parameters.class);
    }

    /**
     * Get count of records in table which name corresponds to cl
     * @param cl class name of appropriated table
     * @return long count of zones
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static long getCountOfRecords(Class cl) throws IOException, ClassNotFoundException {
        long countZones = 0;
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO dao = daoFactory.getDAO(con, cl);
            countZones = dao.getCountTransmitted(param.getReserveTransmitted());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countZones;
    }

    /**
     * В методе показаны примеры работы с интерфейсом IGenericDAO
     * (интерфейс работы с базо  данных) на примере таблици Line
     * @return Список первых n записей таблицы Line
     * (n указываться как аргумент limit метода daoL.getByTransmittedLimit)
     * @throws Exception
     */
    private static List<Line> getLines(long count) throws Exception{
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        //список для хранения полученых линий с базы данных
        List<Line> list;
        //создание подключения к базе
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, Line.class);
            //получение списка с определенным количеством записей таблици в которых параметр transmitted = false
            list = daoL.getByTransmittedLimit(param.getReserveTransmitted(), count);
            con.close();
        }
        System.out.println("List lines size" + list.size());
        return list;
    }


    /**
     * @return list of records by paramReserveDB.getTransmitted()
     * (n указываться как аргумент limit метода daoL.getByTransmittedLimit)
     * @throws Exception
     */
    private static List<Zone> getZones(long count) throws Exception{
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        //список для хранения полученых линий с базы данных
        List<Zone> list;
        //создание подключения к базе
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoZ = daoFactory.getDAO(con, Zone.class);
            //получение списка с определенным количеством записей таблици в которых параметр transmitted = false
            list = daoZ.getByTransmittedLimit(param.getReserveTransmitted(), count);
            con.close();
        }
        System.out.println("List zones size" + list.size());
        return list;
    }

    /**
     * Send several counts of lines to reserve database
     * @param count count of lines which need to send
     */
    private static void sendLines(long count){
        List<Line> list = new ArrayList();
        try {
            long time = System.currentTimeMillis();
            list.addAll(getLines(count));
            time = System.currentTimeMillis() - time;
            System.out.println("Read time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            long time = System.currentTimeMillis();
            createLines(list);
            time = System.currentTimeMillis() - time;
            System.out.println("Send time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
            deleteLines(list);
    }

    /**
     * Send several counts of zones to reserve database
     * @param count count of zones which need to send
     */
    private static void sendZones(long count){
        List<Zone> list = new ArrayList<>();
        try {
            long time = System.currentTimeMillis();
            list.addAll(getZones(count));
            time = System.currentTimeMillis() - time;
            System.out.println("Read time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            long time = System.currentTimeMillis();
            createZones(list);
            time = System.currentTimeMillis() - time;
            System.out.println("Send time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
            deleteZones(list);
    }


    /**
     * Add records to table Lines of reserve DB
     * @param lines list of lines which need to write to table
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void createLines(List<Line> lines) throws IOException, ClassNotFoundException {
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getReserveDB_URL(), param.getReserveDB_USER(), param.getReserveDB_PASSWORD());
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
     * Add records to table Zones of reserve DB
     * @param zones list of zones which need to write to table
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void createZones(List<Zone> zones) throws IOException, ClassNotFoundException {
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getReserveDB_URL(), param.getReserveDB_USER(), param.getReserveDB_PASSWORD());
        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO daoL = daoFactory.getDAO(con, Zone.class);
            for (Zone l : zones) {
                daoL.create(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Delete all records which are in the list of lines
     * @param lines list of lines
     */
    private static void deleteLines(List<Line> lines){
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO daoL = daoFactory.getDAO(con, Line.class);
            for (Line l : lines) {
                daoL.delete(l.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete all records which are in the list of zones
     * @param zones list of zones
     */
    private static void deleteZones(List<Zone> zones){
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO daoL = daoFactory.getDAO(con, Zone.class);
            for (Zone l : zones) {
                daoL.delete(l.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
