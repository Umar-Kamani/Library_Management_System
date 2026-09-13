package mu.alche.library.Models.database.DAO;

import mu.alche.library.Models.Book;
import mu.alche.library.Models.database.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {
    @Override
    public List<Book> findByAuthor(String author) throws SQLException {

        String sql = "SELECT * FROM book WHERE book_author LIKE ?";

        List<Book> books;
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setString(1, "%" + author + "%");

            try (ResultSet rs = ps.executeQuery()) {
                books = new ArrayList<>();

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
            }
        }
        return books;
    }


    @Override
    public List<Book> findByTitle(String title) throws SQLException {

        String sql = "SELECT * FROM book WHERE book_name LIKE ?";
        List<Book> books;

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setString(1, "%" + title + "%");


            try (ResultSet rs = ps.executeQuery()) {

                books = new ArrayList<>();

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
            }
        }

        return books;
    }

    @Override
    public List<Book> findByGenre(String  genre) throws SQLException {

        String sql = """
            
                SELECT b.*
            FROM book b
            JOIN genre g ON b.book_genre_id = g.genre_id
            WHERE g.genre_name LIKE ?
            """;
        List<Book> books;

        try(Connection connection = DBUtils.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setString(1, "%" + genre + "% ");


            try(ResultSet rs = ps.executeQuery()) {

                books = new ArrayList<>();

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
            }
        }

        return books;
    }

    @Override
    public List<Book> findByLocation(int locationId) throws SQLException {

        String sql = """
            SELECT b.*
            FROM book b
            JOIN location g ON b.book_location_id = g.location_id
            WHERE g.location_name LIKE ?
            """;


        ArrayList<Book> books;
        try(Connection connection = DBUtils.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setString(1, "%" + locationId + "%");


            try(ResultSet rs = ps.executeQuery()) {

                books = new ArrayList<>();

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
            }
        }
        return books;
    }

    @Override
    public List<Book> findByIsbn(String isbn) throws SQLException {

        String sql = "SELECT * FROM book WHERE book_isbn LIKE ?";
        List<Book> books;

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setString(1, "%" + isbn + "%");


            try (ResultSet rs = ps.executeQuery()) {

                books = new ArrayList<>();

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
                            bookisbn,
                            genre,
                            location,
                            totalCopies,
                            availableCopies
                    );

                    books.add(book);
                }
            }
        }

        return books;
    }

    @Override
    public Book create(Book book) throws SQLException {

        String sql = """
            
                INSERT INTO book (book_name, book_isbn, book_genre_id, book_location_id, 
                                  total_copies, available_copies, book_author) VALUES (?, ?, ?, ?, ?, ?, ?)""";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setInt(3, book.getGenreId());
            ps.setInt(4, book.getLocationId());
            ps.setInt(5, book.getTotalCopies());
            ps.setInt(6, book.getAvailableCopies());
            ps.setString(7, book.getAuthor());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) book.setId(keys.getInt(1));
            }
            return book;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create book", e);
        }
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

        String sql = "SELECT * FROM book WHERE book_id = ?";
        Book book = null;

        try(Connection connection = DBUtils.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, "%" + id + "%");

            try(ResultSet rs = ps.executeQuery()) {

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
            }
        }
        return book;
    }

    @Override
    public List<Book> getAll() throws SQLException {
        String sql = "SELECT * FROM book";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            List<Book> books;
            try (ResultSet rs = ps.executeQuery()) {

                books = new ArrayList<>();

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
            }

            return books;
        }
    }
}