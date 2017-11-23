package DAO;

import java.sql.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MySQLDaoFactory implements IDAOFactory {

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private Map<Class, DaoCreator> creators;

    // в конструкторе создаеться список возможных классов базы данных
    public MySQLDaoFactory(String DB_URL, String DB_USER, String DB_PASSWORD){
        this.DB_URL = DB_URL;
        this.DB_USER = DB_USER;
        this.DB_PASSWORD = DB_PASSWORD;
        creators = new HashMap<Class, DaoCreator>();
        creators.put(Line.class, new DaoCreator<Connection>() {
            @Override
            public IGenericDAO create(Connection connection) {
                return new MySQLLineDAO(connection);
            }
        });

        creators.put(Zone.class, new DaoCreator<Connection>() {
            @Override
            public IGenericDAO create(Connection connection) {
                return new MySQLZoneDAO(connection);
            }
        });

    }


    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }


    @Override
    public IGenericDAO getDAO(Connection connection, Class dtoClass) throws Exception {
        DaoCreator creator = creators.get(dtoClass);
        if(creator == null){
            throw new Exception("Dao object " + " not found.");
        }
        return creator.create(connection);
    }

}
