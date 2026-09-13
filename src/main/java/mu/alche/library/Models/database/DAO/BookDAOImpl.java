package mu.alche.library.Models.database.DAO;

import mu.alche.library.Models.Book;
import mu.alche.library.Models.database.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {
    @Override
    public List<Book> findByAuthor(String author) throws SQLException {

        Connection connection = DBUtils.getConnection();

        String sql = "SELECT * FROM book WHERE book_author LIKE ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, "%" + author + "%");


        ResultSet rs = ps.executeQuery();

        List<Book> books = new ArrayList<>();

        while (rs.next()) {

            int id = rs.getInt("book_id");
            String isbn = rs.getString("book_isbn");
            String title = rs.getString("book_name");
            String bookAuthor = rs.getString("book_author");
            int genre = rs.getInt("book_genre_id");
            int location = rs.getInt("book_location_id");
            int totalCopies = rs.getInt("total_copies");
            int availableCopies = rs.getInt("available_copies");

            Book book = new Book(
                    id,
                    title,
                    bookAuthor,
                    isbn,
                    genre,
                    location,
                    totalCopies,
                    availableCopies
            );

            books.add(book);
        }

        return books;
    }


    @Override
    public List<Book> findByTitle(String title) throws SQLException {

        Connection connection = DBUtils.getConnection();

        String sql = "SELECT * FROM book WHERE book_name LIKE ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, "%" + title + "%");


        ResultSet rs = ps.executeQuery();

        List<Book> books = new ArrayList<>();

        while (rs.next()) {

            int id = rs.getInt("book_id");
            String isbn = rs.getString("book_isbn");
            String name = rs.getString("book_name");
            String bookAuthor = rs.getString("book_author");
            int genre = rs.getInt("book_genre_id");
            int location = rs.getInt("book_location_id");
            int totalCopies = rs.getInt("total_copies");
            int availableCopies = rs.getInt("available_copies");

            Book book = new Book(
                    id,
                    name,
                    bookAuthor,
                    isbn,
                    genre,
                    location,
                    totalCopies,
                    availableCopies
            );

            books.add(book);
        }

        return books;
    }

    @Override
    public List<Book> findByGenre(String  genre) throws SQLException {

        Connection connection = DBUtils.getConnection();

        String sql = """
            SELECT b.*
            FROM book b
            JOIN genre g ON b.book_genre_id = g.genre_id
            WHERE g.genre_name LIKE ?
            """;

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, "%" + genre + "%");


        ResultSet rs = ps.executeQuery();

        List<Book> books = new ArrayList<>();

        while (rs.next()) {

            int id = rs.getInt("book_id");
            String isbn = rs.getString("book_isbn");
            String name = rs.getString("book_name");
            String bookAuthor = rs.getString("book_author");
            int bookgenre = rs.getInt("book_genre_id");
            int location = rs.getInt("book_location_id");
            int totalCopies = rs.getInt("total_copies");
            int availableCopies = rs.getInt("available_copies");

            Book book = new Book(
                    id,
                    name,
                    bookAuthor,
                    isbn,
                    bookgenre,
                    location,
                    totalCopies,
                    availableCopies
            );

            books.add(book);
        }

        return books;
    }

    @Override
    public List<Book> findByLocation(int locationId) throws SQLException {

        Connection connection = DBUtils.getConnection();

        String sql = """
            SELECT b.*
            FROM book b
            JOIN location g ON b.book_location_id = g.location_id
            WHERE g.location_name LIKE ?
            """;

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, "%" + locationId + "%");


        ResultSet rs = ps.executeQuery();

        List<Book> books = new ArrayList<>();

        while (rs.next()) {

            int id = rs.getInt("book_id");
            String isbn = rs.getString("book_isbn");
            String name = rs.getString("book_name");
            String bookAuthor = rs.getString("book_author");
            int bookgenre = rs.getInt("book_genre_id");
            int location = rs.getInt("book_location_id");
            int totalCopies = rs.getInt("total_copies");
            int availableCopies = rs.getInt("available_copies");

            Book book = new Book(
                    id,
                    name,
                    bookAuthor,
                    isbn,
                    bookgenre,
                    location,
                    totalCopies,
                    availableCopies
            );

            books.add(book);
        }

        return books;
    }

    @Override
    public List<Book> findByIsbn(String isbn) throws SQLException {

        Connection connection = DBUtils.getConnection();

        String sql = "SELECT * FROM book WHERE book_isbn LIKE ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, "%" + isbn + "%");


        ResultSet rs = ps.executeQuery();

        List<Book> books = new ArrayList<>();

        while (rs.next()) {

            int id = rs.getInt("book_id");
            String bookisbn = rs.getString("book_isbn");
            String name = rs.getString("book_name");
            String bookAuthor = rs.getString("book_author");
            int genre = rs.getInt("book_genre_id");
            int location = rs.getInt("book_location_id");
            int totalCopies = rs.getInt("total_copies");
            int availableCopies = rs.getInt("available_copies");

            Book book = new Book(
                    id,
                    name,
                    bookAuthor,
                    isbn,
                    genre,
                    location,
                    totalCopies,
                    availableCopies
            );

            books.add(book);
        }

        return books;
    }

    @Override
    public Book create(Book book) throws SQLException {

        return null;
    }

    @Override
    public Book update(Book book) throws SQLException {
        return null;
    }

    @Override
    public void delete(Book book) throws SQLException {

    }

    @Override
    public Book get(int id) throws SQLException {

        Connection connection = DBUtils.getConnection();

        String sql = "SELECT * FROM book WHERE book_id = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, "%" + id + "%");


        ResultSet rs = ps.executeQuery();


        Book book = null;
        if (rs.next()) {

            int oid = rs.getInt("book_id");
            String isbn = rs.getString("book_isbn");
            String name = rs.getString("book_name");
            String bookAuthor = rs.getString("book_author");
            int genre = rs.getInt("book_genre_id");
            int location = rs.getInt("book_location_id");
            int totalCopies = rs.getInt("total_copies");
            int availableCopies = rs.getInt("available_copies");

            book = new Book(
                    id,
                    name,
                    bookAuthor,
                    isbn,
                    genre,
                    location,
                    totalCopies,
                    availableCopies
            );
        }
        return book;
    }

    @Override
    public List<Book> getAll() throws SQLException {

        Connection connection = DBUtils.getConnection();

        String sql = "SELECT * FROM book";

        PreparedStatement ps = connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        List<Book> books = new ArrayList<>();

        while (rs.next()) {

            int id = rs.getInt("book_id");
            String isbn = rs.getString("book_isbn");
            String title = rs.getString("book_name");
            String bookAuthor = rs.getString("book_author");
            int genre = rs.getInt("book_genre_id");
            int location = rs.getInt("book_location_id");
            int totalCopies = rs.getInt("total_copies");
            int availableCopies = rs.getInt("available_copies");

            Book book = new Book(
                    id,
                    title,
                    bookAuthor,
                    isbn,
                    genre,
                    location,
                    totalCopies,
                    availableCopies
            );

            books.add(book);
        }

        return books;
    }
}