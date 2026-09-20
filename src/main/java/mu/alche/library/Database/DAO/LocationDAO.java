package mu.alche.library.Database.DAO;
import mu.alche.library.Models.Location;

import java.sql.SQLException;
import java.util.List;

public interface LocationDAO extends DAO<Location> {
    List<Location> findChildren(int parentId) throws SQLException;
}
