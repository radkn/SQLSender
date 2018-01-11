package Main;

import DAO.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to create reserve table in local DB and save slice
 */
public class SaveSliсeToReserveDB<T extends AbstractSendableRecord> {

    private static XMLwriterReader<Parameters> reader = new XMLwriterReader("parameters/parameters.xml");

    /**
     * Send all records with transmitted = 1, from  NewVision DB to reserve DB
     * Create reserve table and write records,
     * or write in existing table
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void sendLinesToReserve(){

        Parameters param = reader.ReadFile(Parameters.class);


        if(param.getNewReserveTable()){
            System.out.println(createTable(Line.class));
        }

        long count = getCountOfRecords(Line.class); //records with transmitted=0
        while (count > 0) {
            if(count >= param.getOnePackOfStrings() )
                sendRecords(param.getOnePackOfStrings(), Line.class);
            else
                sendRecords(count, Line.class);
            System.out.println("Lines success reserved ");
            count = getCountOfRecords(Line.class);
        }
    }

    /**
     * Send all records with transmitted = 1, from  NewVision DB to reserve DB
     * Create reserve table and write records,
     * or write in existing table
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void sendZonesToReserve(){
        Parameters param = reader.ReadFile(Parameters.class);

        if(param.getNewReserveTable()){
            createTable(Zone.class);
        }

        long count = getCountOfRecords(Zone.class); //records with transmitted=0
        while (count > 0) {
            if (count >= param.getOnePackOfStrings())
                sendRecords(param.getOnePackOfStrings(), Zone.class);
            else
                sendRecords(count, Zone.class);
            System.out.println("Zones success reserved");
            count = getCountOfRecords(Zone.class);
        }

    }

    /**
     * Send all records with transmitted = 1, from  NewVision DB to reserve DB
     * Create reserve table and write records,
     * or write in existing table
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void sendHeatMapToReserve(){
        Parameters param = reader.ReadFile(Parameters.class);

        if(param.getNewReserveTable()){
            createTable(HeatMap.class);
        }

        long count = getCountOfRecords(HeatMap.class); //records with transmitted=0
        while (count > 0) {
            if (count >= param.getOnePackOfStrings())
                sendRecords(param.getOnePackOfStrings(), HeatMap.class);
            else
                sendRecords(count, HeatMap.class);
            System.out.println("HeatMaps success reserved");
            count = getCountOfRecords(HeatMap.class);
        }

    }


    /**
     * Get count of records in table which name corresponds to cl
     * @param cl class name of appropriated table
     * @return long count of zones
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static long getCountOfRecords(Class cl){
        Parameters param = reader.ReadFile(Parameters.class);


        long count = 0;
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO dao = daoFactory.getDAO(con, cl);
            count = dao.getCountTransmitted(param.getReserveTransmitted());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * @return List of the first n records of the current table
     * (n it is specified as the argument limit of the method daoL.getByTransmittedLimit)
     * @throws Exception
     */
    private List<T> getRecords(long count, Class cl) throws Exception{
        Parameters param = reader.ReadFile(Parameters.class);

        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        //список для хранения полученых линий с базы данных
        List<T> list;
        //создание подключения к базе
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, cl);
            //получение списка с определенным количеством записей таблици в которых параметр transmitted = false
            list = daoL.getByTransmittedLimit(param.getReserveTransmitted(), count);
            con.close();
        }
        System.out.println("List lines size" + list.size());
        return list;
    }

    /**
     * Send several counts of lines to reserve database
     * @param count count of lines which need to send
     */
    private void sendRecords(long count, Class cl){
        List<T> list = new ArrayList();
        try {
            long time = System.currentTimeMillis();
            list.addAll(getRecords(count, cl));
            time = System.currentTimeMillis() - time;
            System.out.println("Read time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            long time = System.currentTimeMillis();
            createRecords(list, cl);
            time = System.currentTimeMillis() - time;
            System.out.println("Send time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        deleteRecords(list, cl);
    }

    /**
     * Add records to table of reserve DB
     * @param lines list of lines which need to write to table
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void createRecords(List<T> lines, Class cl) throws IOException, ClassNotFoundException {
        Parameters param = reader.ReadFile(Parameters.class);

        IDAOFactory daoFactory = new MySQLDaoFactory(param.getReserveDB_URL(), param.getReserveDB_USER(), param.getReserveDB_PASSWORD());
        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO daoL = daoFactory.getDAO(con, cl);
            daoL.setTableName(cl);
            for (T l : lines) {
                daoL.create(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete all records which are in the list T
     * @param records list of records
     */
    private void deleteRecords(List<T> records, Class cl){
        Parameters param = reader.ReadFile(Parameters.class);

        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try (Connection con = daoFactory.getConnection()) {
            IGenericDAO daoH = daoFactory.getDAO(con, cl);
            for (T l : records) {
                daoH.delete(l.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method create new table for Reserve DB or
     * out error message if table successful created
     * @param cl determine name of table to create
     * @return name of new table or null if table successful created
     */
    private String createTable(Class cl){
        String newTableName = null;
        Parameters param = reader.ReadFile(Parameters.class);
        IDAOFactory idaoFactory = new MySQLDaoFactory(param.getReserveDB_URL(), param.getReserveDB_USER(), param.getReserveDB_PASSWORD());
        try(Connection con = idaoFactory.getConnection()){
            IGenericDAO dao = idaoFactory.getDAO(con, cl);
            newTableName = dao.createNewTable(cl);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(newTableName != null){
            System.out.println("Creating new table with name "+ newTableName + " successful.");
        } else System.err.println("ERROR with creating table! Table successful created");
        return newTableName;
    }
}
