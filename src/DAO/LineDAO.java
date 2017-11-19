package DAO;

import java.sql.SQLException;
import java.util.List;

public interface LineDAO {

    public Line create();

    public Line read();

    public void update(Line line);

    public void delate(Line line);

    public List<Line> getAll() throws SQLException;
}
