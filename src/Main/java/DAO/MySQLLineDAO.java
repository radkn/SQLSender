package DAO;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class MySQLLineDAO extends AbstractJDBCDao<Line, Integer>{


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
        return "SELECT * FROM Line";
    }

    /**Разбирает ResultSet и возвращает список объектов соответствующих содержимому ResultSet.*/
    @Override
    protected List<Line> parseResultSet(ResultSet rs) {
        LinkedList<Line> result = new LinkedList<Line>();
        try{
            while (rs.next()){
                Line l = new Line();
                l.setId(rs.getString(1));
                l.setScene_id(rs.getString(2));
                l.setLineTitle(rs.getString(3));
                l.setUid(rs.getString(4));
                l.setDataTime(rs.getString(5));
                l.setStatus(rs.getString(6));
                l.setType(rs.getString(7));
                l.setTime_stamp(rs.getString(8));
                l.setTransmitted(rs.getString(9));
                result.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Line create() {
        return null;
    }

    @Override
    public Line persist(Line object) throws SQLException {
        return null;
    }

    @Override
    public Line getByPk(int key) throws SQLException {
        return null;
    }


    @Override
    public void update(Line line) {

    }

    @Override
    public void delate(Line line) {

    }

}
