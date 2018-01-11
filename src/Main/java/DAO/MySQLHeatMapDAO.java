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
 * and other to work with table HeatMap
 */
public class MySQLHeatMapDAO extends AbstractJDBCDao<HeatMap, Integer> {
    public MySQLHeatMapDAO(Connection connection) {
        super(connection);
    }

    private String tableName = "HeatMap";

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM "+ tableName + "";
    }

    @Override
    public String getConditionOfQuery() {
        return " WHERE transmitted = ? ORDER BY uid LIMIT ?";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO "+ tableName +" (uid, x, y, datetime, type, transmitted)" +
                "VALUE (?,?,?,?,?,?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE "+ tableName +"\n" +
                "SET  uid = ?, x = ?, y = ?, datetime = ?, type = ?, transmitted = ? " +
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
                "uid varchar(255), " +
                "x int(11), " +
                "y int(11), " +
                "datetime timestamp, " +
                "type int(11), " +
                "transmitted tinyint(1))";
    }

    @Override
    protected List<HeatMap> parseResultSet(ResultSet rs) {
        LinkedList<HeatMap> result = new LinkedList<HeatMap>();
        try {
            while (rs.next()){
                HeatMap h = new HeatMap();
                h.setId(rs.getInt(1));
                h.setUid(rs.getString(2));
                h.setX(rs.getInt(3));
                h.setY(rs.getInt(4));
                h.setDatetime(rs.getTimestamp(5));
                h.setType(rs.getInt(6));
                h.setTransmitted(rs.getBoolean(7));
                result.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, HeatMap object) throws Exception {
        statement.setString(1, object.getUid());
        statement.setInt(2, object.getX());
        statement.setInt(3, object.getY());
        statement.setTimestamp(4, object.getDatetime());
        statement.setInt(5, object.getType());
        statement.setBoolean(6, object.getTransmitted());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, HeatMap object) throws Exception {
        statement.setString(1, object.getUid());
        statement.setInt(2, object.getX());
        statement.setInt(3, object.getY());
        statement.setTimestamp(4, object.getDatetime());
        statement.setInt(5, object.getType());
        statement.setBoolean(6, object.getTransmitted());

        statement.setInt(7, object.getId());
    }



    @Override
    public HeatMap create(HeatMap h) throws SQLException {
        return persist(h);
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
