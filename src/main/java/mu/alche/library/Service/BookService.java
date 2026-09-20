package mu.alche.library.Service;

import mu.alche.library.Database.DAO.BookDAO;
import mu.alche.library.Models.Book;

import java.sql.SQLException;
import java.util.List;

public class BookService {

    private final BookDAO bookDAO;

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public Book createBook(Book book) throws SQLException {

        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }

        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            throw new IllegalArgumentException("Book ISBN cannot be empty");
        }

        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Book author cannot be empty");
        }

        if (book.getTotalCopies() < 0) {
            throw new IllegalArgumentException("Total copies cannot be negative");
        }

        if (book.getAvailableCopies() < 0) {
            throw new IllegalArgumentException("Available copies cannot be negative");
        }

        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new IllegalArgumentException(
                    "Available copies cannot be greater than total copies"
            );
        }

        return bookDAO.create(book);
    }

    public Book getBook(int id) throws SQLException {

        Book book = bookDAO.get(id);

        if (book == null) {
            throw new IllegalArgumentException(
                    "No book found with id " + id
            );
        }

        return book;
    }

    public List<Book> getAllBooks() throws SQLException {
        return bookDAO.getAll();
    }

    public List<Book> findBooksByTitle(String title) throws SQLException {
        return bookDAO.findByTitle(title);
    }

    public List<Book> findBooksByAuthor(String author) throws SQLException {
        return bookDAO.findByAuthor(author);
    }

    public List<Book> findBooksByIsbn(String isbn) throws SQLException {
        return bookDAO.findByIsbn(isbn);
    }

    public List<Book> findBooksByGenre(String genre) throws SQLException {
        return bookDAO.findByGenre(genre);
    }

    public List<Book> findBooksByLocation(int locationId) throws SQLException {
        return bookDAO.findByLocation(locationId);
    }

    public void updateBook(Book book) throws SQLException {

        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        Book existingBook = bookDAO.get(book.getId());

        if (existingBook == null) {
            throw new IllegalArgumentException(
                    "No book found with id " + book.getId()
            );
        }

        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }

        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            throw new IllegalArgumentException("Book ISBN cannot be empty");
        }

        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Book author cannot be empty");
        }

        if (book.getTotalCopies() < 0) {
            throw new IllegalArgumentException("Total copies cannot be negative");
        }

        if (book.getAvailableCopies() < 0) {
            throw new IllegalArgumentException("Available copies cannot be negative");
        }

        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new IllegalArgumentException(
                    "Available copies cannot be greater than total copies"
            );
        }


        int borrowedCopies = existingBook.getTotalCopies() - existingBook.getAvailableCopies();

        if (book.getTotalCopies() < borrowedCopies) {
            throw new IllegalArgumentException(
                    "Total copies cannot be less than the number of borrowed copies"
            );
        }

        if (book.getAvailableCopies() > book.getTotalCopies() - borrowedCopies) {

            throw new IllegalArgumentException(
                    "Available copies are inconsistent with borrowed copies"
            );
        }

        bookDAO.update(book);
    }

    public void deleteBook(int id) throws SQLException {

        Book book = bookDAO.get(id);

        if (book == null) {
            throw new IllegalArgumentException(
                    "No book found with id " + id
            );
        }

        if (book.getAvailableCopies() < book.getTotalCopies()) {
            throw new IllegalStateException(
                    "Cannot delete book " + id +
                            " because one or more copies are currently borrowed"
            );
        }
        bookDAO.delete(book);
    }
}
