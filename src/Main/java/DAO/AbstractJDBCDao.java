package DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**реализовывает схожие методы получения данных с базы*/
public abstract class AbstractJDBCDao<T, PK extends Serializable> implements IGenericDAO<T,PK>{

    public AbstractJDBCDao(Connection connection){
        this.connection = connection;
    }

    private Connection connection;

    /**
     * Возвращает sql запрос для получения всех записей.
     * <p/>
     * SELECT * FROM [Table]
     */
    public abstract String getSelectQuery();

    /**Разбирает ResultSet и возвращает список объектов соответствующих содержимому ResultSet.*/
    protected abstract List<T> parseResultSet(ResultSet rs);

    /**возвращает все записи выбраной таблицы в виде списка объектов*/
    public List<T> getAll(){
        List<T> list = new ArrayList<T>();
        String sql = getSelectQuery();
        try(PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }
}
