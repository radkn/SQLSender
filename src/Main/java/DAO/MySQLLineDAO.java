package DAO;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * keep methods that return queries, pars result of query
 * and other to work with table Line
 */
public class MySQLLineDAO extends AbstractJDBCDao<Line, Integer>{

    private String tableName = "Line";

    public MySQLLineDAO(Connection connection) {
        super(connection);

    }

    /**
     * Возвращает sql запрос для получения всех записей.
     * <p/>
     * SELECT * FROM [Table]
     */
    @Override
    public String getSelectQuery() {
        return "SELECT * FROM " + tableName +" ";
    }

    @Override
    public String getConditionOfQuery() {
        return " WHERE transmitted = ? LIMIT ?";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO " + tableName + " (scene_id, lineTitle, uid, datetime, status, type, time_stamp, transmitted)" +
                "VALUE (?,?,?,?,?,?,?,?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE " + tableName + "\n" +
                "SET scene_id = ?, lineTitle = ?, uid = ?, datetime = ?, status = ?, type = ?, time_stamp = ?, transmitted = ? " +
                "WHERE id = ?";
    }

    @Override
    public String getUpdateTransmittedQuery() {
        return "UPDATE "+ tableName +"\n" +
                "SET transmitted = ? " +
                "WHERE id = ? ";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM "+ tableName +" WHERE id = ?;";
    }

    @Override
    public String getCountQuery() {
        return "SELECT COUNT(*) FROM "+ tableName +" WHERE transmitted = ?;";
    }

    @Override
    public String createNewTableQuery() {
        return " (" +
                "id int(11) Primary key NOT NULL AUTO_INCREMENT, " +
                "scene_id varchar(255), " +
                "lineTitle varchar(255), " +
                "uid varchar(255), " +
                "datetime timestamp, " +
                "status int(11), " +
                "type int(11), " +
                "time_stamp timestamp, " +
                "transmitted tinyint(1))";
    }

    /**Разбирает ResultSet и возвращает список объектов соответствующих содержимому ResultSet.*/
    @Override
    protected List<Line> parseResultSet(ResultSet rs) {
        LinkedList<Line> result = new LinkedList<Line>();
        try{
            while (rs.next()){
                Line l = new Line();
                l.setId(rs.getInt(1));
                l.setScene_id(rs.getString(2));
                l.setLineTitle(rs.getString(3));
                l.setUid(rs.getString(4));
                l.setDataTime(rs.getTimestamp(5));
                l.setStatus(rs.getInt(6));
                l.setType(rs.getInt(7));
                l.setTime_stamp(rs.getTimestamp(8));
                l.setTransmitted(rs.getBoolean(9));
                result.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Line object) throws Exception {
        statement.setString(1, object.getScene_id());
        statement.setString(2, object.getLineTitle());
        statement.setString(3, object.getUid());
        statement.setTimestamp(4, object.getDataTime());
        statement.setInt(5, object.getStatus());
        statement.setInt(6, object.getType());
        statement.setTimestamp(7, object.getTime_stamp());
        statement.setBoolean(8, object.getTransmitted());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Line object) throws Exception {

        statement.setString(1, object.getScene_id());
        statement.setString(2, object.getLineTitle());
        statement.setString(3, object.getUid());
        statement.setTimestamp(4, object.getDataTime());
        statement.setInt(5, object.getStatus());
        statement.setInt(6, object.getType());
        statement.setTimestamp(7, object.getTime_stamp());
        statement.setBoolean(8, object.getTransmitted());

        statement.setInt(9, object.getId());
    }

    @Override
    public Line create(Line l) throws SQLException {
        return persist(l);
    }

    @Override
    public String createNewTable(Class cl) {
        tableName = creatingTable(cl);
        return tableName;
    }

    @Override
    public void setTableName(Class cl) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        this.tableName = cl.getSimpleName()+ dateFormat.format(new java.util.Date());
    }
}
