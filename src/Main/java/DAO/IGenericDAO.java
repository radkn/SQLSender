package DAO;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**универсальный интерфейс объектов базы данных*/
public interface IGenericDAO<T, PK extends Serializable> {

    public T create() throws SQLException;

    public T persist(T object) throws SQLException;

    public T getByPk(int key) throws SQLException;

    public void update(T object) throws SQLException;

    public void delate(T object) throws SQLException;

    /**возвращает все объекты выбраной таблицы*/
    public List<T> getAll() throws SQLException;
}
