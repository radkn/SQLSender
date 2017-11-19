package DAO;

import java.sql.*;
import java.sql.SQLException;

public class MySQLDaoFactory implements DAOFactory {

    private static String DB_URL = "jdbc:mysql://localhost:3306/NewVision";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "root";

    //имя драйвера???



    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = (Connection) DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        return (Connection) DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }



    @Override
    public LineDAO getLineDao(Connection connection) {

        return new MySQLLineDAO(connection);
    }
}
