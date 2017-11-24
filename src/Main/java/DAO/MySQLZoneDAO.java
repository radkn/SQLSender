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


    /**
     * Возвращает sql запрос для получения всех записей.
     * <p/>
     * SELECT * FROM [Table]
     */
    @Override
    public String getSelectQuery() {
        return "SELECT * FROM Zone";
    }

    @Override
    public String getCreateQuery() {
        return null;
    }

    @Override
    public String getupdateQuery() {
        return null;
    }

    @Override
    public String getDeleteQuery() {
        return null;
    }

    /**Разбирает ResultSet и возвращает список объектов соответствующих содержимому ResultSet.*/
    @Override
    protected List<Zone> parseResultSet(ResultSet rs) {
        LinkedList<Zone> result = new LinkedList<Zone>();
        try {
            while (rs.next()){
                Zone z = new Zone();
                z.setId(rs.getString(1));
                z.setScene_id(rs.getString(2));
                z.setZoneTitle(rs.getString(3));
                z.setUid(rs.getString(4));
                z.setDatatime_start(rs.getString(5));
                z.setDatatime_end(rs.getString(6));
                z.setDatatime_delay(rs.getString(7));
                z.setType(rs.getString(8));
                z.setTime_stamp(rs.getString(9));
                z.setTransmitted(rs.getString(10));
                result.add(z);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Zone object) throws Exception {

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Zone object) throws Exception {

    }

    @Override
    public Zone create() throws SQLException {
        return null;
    }

    @Override
    public Zone persist(Zone object) throws SQLException {
        return null;
    }

    @Override
    public Zone getByPk(int key) throws SQLException {
        return null;
    }

    @Override
    public void update(Zone object) throws SQLException {

    }

    @Override
    public void delate(Zone object) throws SQLException {

    }
}
