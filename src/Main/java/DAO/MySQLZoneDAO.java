package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MySQLZoneDAO extends AbstractJDBCDao<Zone, Integer> {

    public MySQLZoneDAO(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM Zone";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO Zone (scene_id, zoneTitle, uid, datetime_start, datetime_end, datetime_delay, type, time_stamp, transmitted)" +
                "VALUE (?,?,?,?,?,?,?,?,?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE Zone\n" +
                "SET scene_id = ?, zoneTitle = ?, uid = ?, datetime_start = ?, datetime_end = ?, datetime_delay = ?, type = ?, time_stamp = ?, transmitted = ? " +
                "WHERE id = ?";
    }

    @Override
    public String getUpdateTransmittedQuery() {
        return "UPDATE Zone\n" +
                "SET transmitted = ? " +
                "WHERE id = ?";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM Zone WHERE id = ?;";
    }

    @Override
    public String getCountQuery() {
        return "SELECT COUNT(*) FROM Zone WHERE transmitted = ?;";
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
}
