package DAO;

import java.sql.*;
import java.sql.SQLException;

public interface DAOFactory {

    //возвращает подключение  к базе данных
    public Connection getConnection () throws SQLException;

    //
    public LineDAO getLineDao(Connection connection);


}
