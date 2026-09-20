package mu.alche.library.Service;

import mu.alche.library.Database.DAO.BookDAO;
import mu.alche.library.Database.DAO.BorrowingDAO;
import mu.alche.library.Database.DAO.UserDAO;
import mu.alche.library.Models.Book;
import mu.alche.library.Models.Borrowing;
import mu.alche.library.Models.Faculty;
import mu.alche.library.Models.Student;
import mu.alche.library.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BorrowingServiceTest {

    private FakeUserDAO userDAO;
    private FakeBookDAO bookDAO;
    private FakeBorrowingDAO borrowingDAO;
    private BorrowingService service;

    private static final int STUDENT_ID = 1;
    private static final int FACULTY_ID = 2;
    private static final int BOOK_ID = 100;

    @BeforeEach
    void setUp() {
        userDAO = new FakeUserDAO();
        bookDAO = new FakeBookDAO();
        borrowingDAO = new FakeBorrowingDAO();
        service = new BorrowingService(userDAO, bookDAO, borrowingDAO);

        userDAO.add(new Student(STUDENT_ID, "Alice", "0000", "alice@example.com"));
        userDAO.add(new Faculty(FACULTY_ID, "Dr. Smith", "1111", "smith@example.com"));

        bookDAO.add(new Book(BOOK_ID, "Clean Code", "Robert Martin", "1234567890123", 1, 1, 3, 3));
    }

    // ---------- createBorrowing ----------

    @Test
    void createBorrowingDecrementsAvailableCopies() throws SQLException {
        service.createBorrowing(STUDENT_ID, BOOK_ID);
        assertEquals(2, bookDAO.get(BOOK_ID).getAvailableCopies());
    }

    @Test
    void createBorrowingUsesStudentLoanDuration() throws SQLException {
        Borrowing b = service.createBorrowing(STUDENT_ID, BOOK_ID);
        assertEquals(b.getBorrowDate().plusDays(14), b.getDueDate());
    }

    @Test
    void createBorrowingUsesFacultyLoanDuration() throws SQLException {
        Borrowing b = service.createBorrowing(FACULTY_ID, BOOK_ID);
        assertEquals(b.getBorrowDate().plusDays(30), b.getDueDate());
    }

    @Test
    void createBorrowingSetsStatusBorrowed() throws SQLException {
        Borrowing b = service.createBorrowing(STUDENT_ID, BOOK_ID);
        assertEquals("BORROWED", b.getStatus());
        assertNull(b.getReturnDate());
    }

    @Test
    void createBorrowingRejectsUnknownUser() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createBorrowing(999, BOOK_ID));
    }

    @Test
    void createBorrowingRejectsUnknownBook() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createBorrowing(STUDENT_ID, 999));
    }

    @Test
    void createBorrowingRejectsWhenNoCopiesAvailable() throws SQLException {
        Book book = bookDAO.get(BOOK_ID);
        book.setAvailableCopies(0);
        bookDAO.update(book);

        assertThrows(IllegalStateException.class,
                () -> service.createBorrowing(STUDENT_ID, BOOK_ID));
    }

    @Test
    void createBorrowingRejectsWhenUserAtMaxBooks() throws SQLException {
        // Student's max is 3 — borrow three, the fourth should be rejected
        bookDAO.add(new Book(101, "Book B", "Author B", "1111111111111", 1, 1, 1, 1));
        bookDAO.add(new Book(102, "Book C", "Author C", "2222222222222", 1, 1, 1, 1));

        service.createBorrowing(STUDENT_ID, BOOK_ID);
        service.createBorrowing(STUDENT_ID, 101);
        service.createBorrowing(STUDENT_ID, 102);

        bookDAO.add(new Book(103, "Book D", "Author D", "3333333333333", 1, 1, 1, 1));

        assertThrows(IllegalStateException.class,
                () -> service.createBorrowing(STUDENT_ID, 103));
    }

    @Test
    void createBorrowingAllowsAnotherAfterOneIsReturned() throws SQLException {
        bookDAO.add(new Book(101, "Book B", "Author B", "1111111111111", 1, 1, 1, 1));
        bookDAO.add(new Book(102, "Book C", "Author C", "2222222222222", 1, 1, 1, 1));
        bookDAO.add(new Book(103, "Book D", "Author D", "3333333333333", 1, 1, 1, 1));

        Borrowing first = service.createBorrowing(STUDENT_ID, BOOK_ID);
        service.createBorrowing(STUDENT_ID, 101);
        service.createBorrowing(STUDENT_ID, 102);

        service.returnBook(first.getId());

        assertDoesNotThrow(() -> service.createBorrowing(STUDENT_ID, 103));
    }

    // ---------- returnBook ----------

    @Test
    void returnBookIncrementsAvailableCopies() throws SQLException {
        Borrowing b = service.createBorrowing(STUDENT_ID, BOOK_ID);
        service.returnBook(b.getId());
        assertEquals(3, bookDAO.get(BOOK_ID).getAvailableCopies());
    }

    @Test
    void returnBookSetsReturnDateAndStatus() throws SQLException {
        Borrowing b = service.createBorrowing(STUDENT_ID, BOOK_ID);
        service.returnBook(b.getId());

        Borrowing updated = borrowingDAO.get(b.getId());
        assertEquals("RETURNED", updated.getStatus());
        assertEquals(LocalDate.now(), updated.getReturnDate());
    }

    @Test
    void returnBookRejectsUnknownBorrowing() {
        assertThrows(IllegalArgumentException.class, () -> service.returnBook(999));
    }

    @Test
    void returnBookRejectsDoubleReturn() throws SQLException {
        Borrowing b = service.createBorrowing(STUDENT_ID, BOOK_ID);
        service.returnBook(b.getId());

        assertThrows(IllegalStateException.class, () -> service.returnBook(b.getId()));
    }

    // ---------- getAllBorrowings / deleteBorrowing ----------

    @Test
    void getAllBorrowingsReturnsCreatedBorrowing() throws SQLException {
        Borrowing b = service.createBorrowing(STUDENT_ID, BOOK_ID);
        List<Borrowing> all = service.getAllBorrowings();
        assertTrue(all.stream().anyMatch(x -> x.getId() == b.getId()));
    }

    @Test
    void deleteBorrowingRemovesIt() throws SQLException {
        Borrowing b = service.createBorrowing(STUDENT_ID, BOOK_ID);
        service.deleteBorrowing(b.getId());
        assertNull(borrowingDAO.get(b.getId()));
    }

    @Test
    void deleteBorrowingOnUnknownIdDoesNothing() {
        assertDoesNotThrow(() -> service.deleteBorrowing(999));
    }

    // ================= Fakes (test doubles, no Mockito needed) =================

    private static class FakeUserDAO implements UserDAO {
        private final Map<Integer, User> users = new HashMap<>();
        void add(User u) { users.put(u.getId(), u); }

        @Override public User create(User t) { users.put(t.getId(), t); return t; }
        @Override public void update(User t) { users.put(t.getId(), t); }
        @Override public void delete(User t) { users.remove(t.getId()); }
        @Override public User get(int id) { return users.get(id); }
        @Override public List<User> getAll() { return new ArrayList<>(users.values()); }
    }

    private static class FakeBookDAO implements BookDAO {
        private final Map<Integer, Book> books = new HashMap<>();
        void add(Book b) { books.put(b.getId(), b); }

        @Override public Book create(Book t) { books.put(t.getId(), t); return t; }
        @Override public void update(Book t) { books.put(t.getId(), t); }
        @Override public void delete(Book t) { books.remove(t.getId()); }
        @Override public Book get(int id) { return books.get(id); }
        @Override public List<Book> getAll() { return new ArrayList<>(books.values()); }
        @Override public List<Book> findByAuthor(String author) { return List.of(); }
        @Override public List<Book> findByTitle(String title) { return List.of(); }
        @Override public List<Book> findByGenre(String genre) { return List.of(); }
        @Override public List<Book> findByLocation(int locationId) { return List.of(); }
        @Override public List<Book> findByIsbn(String isbn) { return List.of(); }
    }

    private static class FakeBorrowingDAO implements BorrowingDAO {
        private final Map<Integer, Borrowing> borrowings = new HashMap<>();
        private int nextId = 1;

        @Override public Borrowing create(Borrowing t) {
            t.setId(nextId++);
            borrowings.put(t.getId(), t);
            return t;
        }
        @Override public void update(Borrowing t) { borrowings.put(t.getId(), t); }
        @Override public void delete(Borrowing t) { borrowings.remove(t.getId()); }
        @Override public Borrowing get(int id) { return borrowings.get(id); }
        @Override public List<Borrowing> getAll() { return new ArrayList<>(borrowings.values()); }

        @Override public List<Borrowing> findByUser(int userId) {
            List<Borrowing> result = new ArrayList<>();
            for (Borrowing b : borrowings.values()) {
                if (b.getUserId() == userId) result.add(b);
            }
            return result;
        }

        @Override public List<Borrowing> findOverdue() {
            List<Borrowing> result = new ArrayList<>();
            for (Borrowing b : borrowings.values()) {
                if (b.getReturnDate() == null && b.getDueDate().isBefore(LocalDate.now())) result.add(b);
            }
            return result;
        }
    }
}