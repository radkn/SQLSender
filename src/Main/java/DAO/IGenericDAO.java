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

    /**
     * Finds number of records by 'transmitted'
     * @param transmitted to tell which data we want to get (with transmitted = true/false)
     * @return count of records by transmitted
     */
    long getCountTransmitted(boolean transmitted) throws SQLException;

    /**need to point on class of creating table*/
    String createNewTable(Class cl);

    /**
     * Create new table
     * @param cl determine type of table to create
     * @return new table name
     */
    String creatingTable(Class cl);

    /**
     * Set name of table with which we wood work
     * @param cl name of this table
     */
    void setTableName(INewVisionDB cl);

}
