package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * keep methods that return queries, pars result of query
 * and other to work with table Zone
 */
public class MySQLZoneDAO extends AbstractJDBCDao<Zone, Integer> {

    public MySQLZoneDAO(Connection connection) {
        super(connection);
    }

    private String tableName = "Zone";

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM "+ tableName + "";
    }

    @Override
    public String getConditionOfQuery() {
        return " WHERE transmitted = ? LIMIT ?";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO "+ tableName +" (scene_id, zoneTitle, uid, datetime_start, datetime_end, datetime_delay, type, time_stamp, transmitted)" +
                "VALUE (?,?,?,?,?,?,?,?,?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE "+ tableName +"\n" +
                "SET scene_id = ?, zoneTitle = ?, uid = ?, datetime_start = ?, datetime_end = ?, datetime_delay = ?, type = ?, time_stamp = ?, transmitted = ? " +
                "WHERE id = ?";
    }

    @Override
    public String getUpdateTransmittedQuery() {
        return "UPDATE "+ tableName +"\n" +
                "SET transmitted = ? " +
                "WHERE id = ?";
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
                "zoneTitle varchar(255), " +
                "uid varchar(255), " +
                "datetime_start timestamp, " +
                "datetime_end timestamp, " +
                "datetime_delay int(11), " +
                "type int(11), " +
                "time_stamp timestamp, " +
                "transmitted tinyint(1))";
    }

    /**Разбирает ResultSet и возвращает список объектов соответствующих содержимому ResultSet.*/
    @Override
    protected List<Zone> parseResultSet(ResultSet rs) {
        LinkedList<Zone> result = new LinkedList<Zone>();
        try {
            while (rs.next()){
                Zone z = new Zone();
                z.setId(rs.getInt(1));
                z.setScene_id(rs.getString(2));
                z.setZoneTitle(rs.getString(3));
                z.setUid(rs.getString(4));
                z.setDatatime_start(rs.getTimestamp(5));
                z.setDatatime_end(rs.getTimestamp(6));
                z.setDatatime_delay(rs.getInt(7));
                z.setType(rs.getInt(8));
                z.setTime_stamp(rs.getTimestamp(9));
                z.setTransmitted(rs.getBoolean(10));
                result.add(z);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Zone object) throws Exception {
        statement.setString(1, object.getScene_id());
        statement.setString(2, object.getZoneTitle());
        statement.setString(3, object.getUid());
        statement.setTimestamp(4, object.getDatatime_start());
        statement.setTimestamp(5, object.getDatatime_end());
        statement.setInt(6, object.getDatatime_delay());
        statement.setInt(7, object.getType());
        statement.setTimestamp(8, object.getTime_stamp());
        statement.setBoolean(9, object.getTransmitted());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Zone object) throws Exception {
        statement.setString(1, object.getScene_id());
        statement.setString(2, object.getZoneTitle());
        statement.setString(3, object.getUid());
        statement.setTimestamp(4, object.getDatatime_start());
        statement.setTimestamp(5, object.getDatatime_end());
        statement.setInt(6, object.getDatatime_delay());
        statement.setInt(7, object.getType());
        statement.setTimestamp(8, object.getTime_stamp());
        statement.setBoolean(9, object.getTransmitted());

        statement.setInt(10, object.getId());
    }

    @Override
    public Zone create(Zone z) throws SQLException {
        return persist(z);
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
