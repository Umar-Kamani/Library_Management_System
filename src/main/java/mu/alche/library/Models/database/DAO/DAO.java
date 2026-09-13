package mu.alche.library.Models.database.DAO;
import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    T create(T t) throws SQLException;
    T update(T t) throws SQLException;
    void delete(T t) throws SQLException;
    T get(int id)  throws SQLException;
    List<T> getAll()  throws SQLException;
}
