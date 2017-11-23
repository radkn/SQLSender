package DAO;

import java.sql.*;
import java.sql.SQLException;

/** Фабрика объектов для работы с базой данных */
public interface IDAOFactory<Context> {


    public interface DaoCreator<Context>{
        public IGenericDAO create(Context context);
    }

    /**возвращает подключение  к базе данных*/
    public Connection getConnection () throws SQLException;

    /** Возвращает объект для управления персистентным состоянием объекта */
    public IGenericDAO getDAO(Connection connection, Class dtoClass) throws Exception;

}
