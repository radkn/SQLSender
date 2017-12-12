package DAO;

import java.sql.*;
import java.sql.SQLException;

/** Фабрика объектов для работы с базой данных */
public interface IDAOFactory<Context> {


    interface DaoCreator<Context>{
        IGenericDAO create(Context context);
    }

    /**возвращает подключение  к базе данных*/
    Connection getConnection () throws SQLException;

    /** Возвращает объект который содержит коннект с текущим состоянием базы в данный конкретный момент */
    IGenericDAO getDAO(Connection connection, Class dtoClass) throws Exception;

}
