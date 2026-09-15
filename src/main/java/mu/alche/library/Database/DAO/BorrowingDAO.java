package mu.alche.library.Database.DAO;

import mu.alche.library.Models.Borrowing;

import java.sql.SQLException;
import java.util.List;

public interface BorrowingDAO extends DAO<Borrowing> {
    List<Borrowing> findByUser(int userId) throws SQLException;
    List<Borrowing> findOverdue() throws SQLException;
}
