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

    /**
     * Возвращает sql запрос для вставки новой записи в базу данных.
     * <p/>
     * INSERT INTO [Table] ([column, column, ...]) VALUES (?, ?, ...);
     */
    public abstract String getCreateQuery();

    /**
     * Возвращает sql запрос для обновления записи.
     * <p/>
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     */
    public abstract String getUpdateQuery();

    /**
     * Возвращает sql запрос для удаления записи из базы данных.
     * <p/>
     * DELETE FROM [Table] WHERE id= ?;
     */
    public abstract String getDeleteQuery();

    /**Разбирает ResultSet и возвращает список объектов соответствующих содержимому ResultSet.*/
    protected abstract List<T> parseResultSet(ResultSet rs);

    /**
     * Устанавливает аргументы insert запроса в соответствии со значением полей объекта object.
     */
    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws Exception;

    /**
     * Устанавливает аргументы update запроса в соответствии со значением полей объекта object.
     */
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws Exception;

    @Override
    public T persist(T object) throws SQLException {
        T persistInstance = null;
        /**Добавляем запись*/
        String sql = getCreateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new Exception("On persist modify more then 1 record: " + count);
            }
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**Получаем только что вставленую запись*/
        sql = getSelectQuery() + " WHERE id = LAST_INSERT_ID();";// + lastId;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<T> list = parseResultSet(rs);
            if ((list == null) || (list.size() != 1)) {
                throw new Exception("Exception on findByPK new persist data.");
            }
            persistInstance = list.iterator().next();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return persistInstance;
    }

        /***/
    @Override
    public void update(T object) {
        String sql = getUpdateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            prepareStatementForUpdate(statement, object);
            int count = statement.executeUpdate();
            if(count != 1){
                throw new Exception("On update modify more then 1 record: " + count);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**возвращает все записи выбраной таблицы в виде списка объектов*/
    @Override
    public List<T> getAll(){
        List<T> list = new ArrayList<T>();
        String sql = getSelectQuery();
        try(PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            list = parseResultSet(rs);
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

        @Override
        public T getByPk(int key) throws Exception {
            List<T> list = null;
            String sql = getSelectQuery() + "WHERE id = ?";
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1,key);
                ResultSet rs = statement.executeQuery();
                list = parseResultSet(rs);
                statement.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            if ((list == null) || (list.size() == 0)){
                throw new Exception("Record with PK = " + key + " not found.");
            }
            if (list.size()>1){
                throw new Exception("Received more than one record.");
            }
            return list.iterator().next();
        }

        @Override
        public void delete(int id) {
            String sql = getDeleteQuery();
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setInt(1,id);
                int count = stm.executeUpdate();
                if(count != 1){
                    throw new Exception("On delete modify more then 1 record: " + count);
                }
                stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
