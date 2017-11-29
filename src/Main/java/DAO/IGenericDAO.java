package DAO;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**универсальный интерфейс объектов базы данных*/
public interface IGenericDAO<T, PK extends Serializable> {

    /** Создает новую запись и соответствующий ей объект */
    public T create(T object) throws SQLException;

    /** Создает новую запись, соответствующую объекту object */
    public T persist(T object) throws SQLException;

    /** Возвращает объект соответствующий записи с первичным ключом key или null */
    public T getByPk(int key) throws Exception;

    /** Сохраняет состояние объекта group в базе данных */
    public void update(T object) throws SQLException;

    /** Удаляет запись об объекте из базы данных */
    public void delete(int id) throws SQLException;

    /**возвращает все объекты выбраной таблицы*/
    public List<T> getAll() throws SQLException;
}
