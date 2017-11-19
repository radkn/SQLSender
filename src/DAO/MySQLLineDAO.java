package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLLineDAO implements LineDAO{

    private final Connection connection;

    public MySQLLineDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Line create() {
        return null;
    }

    @Override
    public Line read() {
        return null;
    }

    @Override
    public void update(Line line) {

    }

    @Override
    public void delate(Line line) {

    }

    @Override
    public List<Line> getAll() throws SQLException {
        String sql = "SELECT * FROM Line";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<Line> list = new ArrayList<Line>();
        while(rs.next()){
            Line l = new Line();
            l.setLineTitle(rs.getString(3));
            l.setUid(rs.getString(4));
            l.setDataTime(rs.getString(5));
            l.setStatus(rs.getString(6));
            l.setType(rs.getString(7));

            list.add(l);
        }
        return list;
    }
}
