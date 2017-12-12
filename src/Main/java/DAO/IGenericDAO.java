package DAO;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**универсальный интерфейс объектов базы данных*/
public interface IGenericDAO<T, PK extends Serializable> {

    /** Создает новую запись и соответствующий ей объект */
    T create(T object) throws SQLException;

    /** Создает новую запись, соответствующую объекту object */
    T persist(T object) throws SQLException;

    /** Возвращает объект соответствующий записи с первичным ключом key или null */
    T getByPk(int key) throws Exception;

    /** Сохраняет состояние объекта group в базе данных */
    void update(T object) throws SQLException;

    /**
     * @param tr transmitted = true/false
     * @param limit number of strings we need to get
     * @return List of objects with needed 'transmitted' and with size of 'limit'
     */
    List<T> getByTransmittedLimit(boolean tr, long limit);

    /**
     * Changes 'transmitted' by id
     * @param id - id of the object
     * @param transmitted
     * @throws SQLException
     */
    void updateTransmitted(int id, boolean transmitted) throws SQLException;

    /** Удаляет запись об объекте из базы данных */
    void delete(int id) throws SQLException;

    /**возвращает все объекты выбраной таблицы*/
    List<T> getAll() throws SQLException;

    long getCountTransmitted(boolean transmitted) throws SQLException;

}
