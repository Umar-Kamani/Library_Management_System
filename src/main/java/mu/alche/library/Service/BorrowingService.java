package mu.alche.library.Service;

import mu.alche.library.Database.DAO.BookDAO;
import mu.alche.library.Database.DAO.BorrowingDAO;
import mu.alche.library.Database.DAO.UserDAO;
import mu.alche.library.Models.Book;
import mu.alche.library.Models.Borrowing;
import mu.alche.library.Models.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BorrowingService {

    private final UserDAO userDAO;
    private final BookDAO bookDAO;
    private final BorrowingDAO borrowingDAO;

    public BorrowingService(UserDAO userDAO, BookDAO bookDAO, BorrowingDAO borrowingDAO) {
        this.userDAO = userDAO;
        this.bookDAO = bookDAO;
        this.borrowingDAO = borrowingDAO;
    }

    public Borrowing createBorrowing(int userId, int bookId) throws SQLException {
        User user = userDAO.get(userId);
        if (user == null) throw new IllegalArgumentException("No user found with id " + userId);

        Book book = bookDAO.get(bookId);
        if (book == null) throw new IllegalArgumentException("No book found with id " + bookId);
        if (book.getAvailableCopies() <= 0) throw new IllegalStateException("No copies of book " + bookId + " available");

        long activeCount = countActiveBorrowingsForUser(userId);
        if (activeCount >= user.getMaxBooks())
            throw new IllegalStateException("User " + userId + " has reached their limit of " + user.getMaxBooks());

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(user.getLoanDurationDays());

        Borrowing borrowing = borrowingDAO.create(
                new Borrowing(0, userId, bookId, borrowDate, dueDate, null, "BORROWED")
        );

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookDAO.update(book);

        return borrowing;
    }

    public void returnBook(int borrowingId) throws SQLException {
        Borrowing borrowing = borrowingDAO.get(borrowingId);
        if (borrowing == null) throw new IllegalArgumentException("No borrowing found with id " + borrowingId);
        if (borrowing.getReturnDate() != null) throw new IllegalStateException("Borrowing " + borrowingId + " was already returned");

        borrowing.setReturnDate(LocalDate.now());
        borrowing.setStatus("RETURNED");
        borrowingDAO.update(borrowing);

        Book book = bookDAO.get(borrowing.getBookId());
        if (book != null) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookDAO.update(book);
        }
    }

    public List<Borrowing> getBorrowingsForUser(int userId) throws SQLException {
        return borrowingDAO.findByUser(userId);
    }

    public List<Borrowing> getOverdueBorrowings() throws SQLException {
        return borrowingDAO.findOverdue();
    }

        public List<Borrowing> getAllBorrowings() throws SQLException {
        return borrowingDAO.getAll();
    }

    public void deleteBorrowing(int borrowingId) throws SQLException {
        Borrowing borrowing = borrowingDAO.get(borrowingId);
        if (borrowing != null) {
            borrowingDAO.delete(borrowing);
        }
    }
    
    private long countActiveBorrowingsForUser(int userId) throws SQLException {
        return borrowingDAO.findByUser(userId).stream()
                .filter(b -> b.getReturnDate() == null)
                .count();
    }
}