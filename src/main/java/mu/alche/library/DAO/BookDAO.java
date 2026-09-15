package mu.alche.library.DAO;
import mu.alche.library.Models.Book;

import java.sql.SQLException;
import java.util.List;


public interface BookDAO extends DAO<Book> {

    List<Book> findByAuthor(String author) throws SQLException;
    List<Book> findByTitle(String title)  throws SQLException;
    List<Book> findByGenre(String genre)  throws SQLException;
    List<Book> findByLocation(int locationId)   throws SQLException;
    List<Book> findByIsbn(String isbn) throws SQLException;

}
