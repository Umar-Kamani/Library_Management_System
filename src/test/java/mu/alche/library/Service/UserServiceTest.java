package mu.alche.library.Service;

import mu.alche.library.Database.DAO.BorrowingDAO;
import mu.alche.library.Database.DAO.UserDAO;
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

class UserServiceTest {

    private FakeUserDAO userDAO;
    private FakeBorrowingDAO borrowingDAO;
    private UserService service;

    @BeforeEach
    void setUp() {
        userDAO = new FakeUserDAO();
        borrowingDAO = new FakeBorrowingDAO();
        service = new UserService(userDAO, borrowingDAO);
    }

    // ---------- createUser ----------

    @Test
    void createUserSucceedsWithValidStudent() throws SQLException {
        User created = service.createUser(new Student(0, "Alice", "0700000000", "alice@example.com"));
        assertTrue(created.getId() > 0);
    }

    @Test
    void createUserRejectsBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createUser(new Student(0, "  ", "0700000000", "alice@example.com")));
    }

    @Test
    void createUserRejectsInvalidPhoneCharacters() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createUser(new Student(0, "Alice", "call-me-maybe", "alice@example.com")));
    }

    @Test
    void createUserRejectsPhoneTooShort() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createUser(new Student(0, "Alice", "12345", "alice@example.com")));
    }

    @Test
    void createUserRejectsInvalidEmailFormat() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createUser(new Student(0, "Alice", "0700000000", "not-an-email")));
    }

    @Test
    void createUserRejectsDuplicateEmail() throws SQLException {
        service.createUser(new Student(0, "Alice", "0700000000", "alice@example.com"));

        assertThrows(IllegalArgumentException.class,
                () -> service.createUser(new Student(0, "Alice Two", "0711111111", "alice@example.com")));
    }

    @Test
    void createUserTrimsWhitespaceFromFields() throws SQLException {
        User created = service.createUser(new Student(0, "  Alice  ", " 0700000000 ", " alice@example.com "));
        assertEquals("Alice", created.getName());
        assertEquals("0700000000", created.getPhone());
        assertEquals("alice@example.com", created.getEmail());
    }

    // ---------- updateUser ----------

    @Test
    void updateUserRejectsUnknownId() {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateUser(new Student(999, "Alice", "0700000000", "alice@example.com")));
    }

    @Test
    void updateUserRejectsRoleChangeThatExceedsNewLimit() throws SQLException {
        User faculty = service.createUser(new Faculty(0, "Dr. Smith", "0700000000", "smith@example.com"));

        // Faculty can hold up to 10; give them 5 active borrowings
        for (int i = 0; i < 5; i++) {
            borrowingDAO.add(new Borrowing(0, faculty.getId(), 100 + i,
                    LocalDate.now(), LocalDate.now().plusDays(30), null, "BORROWED"));
        }

        // Downgrading to Student (max 3) should be rejected: 5 active > 3 allowed
        Student asStudent = new Student(faculty.getId(), "Dr. Smith", "0700000000", "smith@example.com");
        assertThrows(IllegalStateException.class, () -> service.updateUser(asStudent));
    }

    @Test
    void updateUserAllowsRoleChangeWithinNewLimit() throws SQLException {
        User faculty = service.createUser(new Faculty(0, "Dr. Smith", "0700000000", "smith@example.com"));

        borrowingDAO.add(new Borrowing(0, faculty.getId(), 100,
                LocalDate.now(), LocalDate.now().plusDays(30), null, "BORROWED"));

        // Only 1 active borrowing, well within Student's limit of 3
        Student asStudent = new Student(faculty.getId(), "Dr. Smith", "0700000000", "smith@example.com");
        assertDoesNotThrow(() -> service.updateUser(asStudent));
    }

    // ---------- deleteUser ----------

    @Test
    void deleteUserRejectsWhenActiveBorrowingExists() throws SQLException {
        User user = service.createUser(new Student(0, "Alice", "0700000000", "alice@example.com"));
        borrowingDAO.add(new Borrowing(0, user.getId(), 100,
                LocalDate.now(), LocalDate.now().plusDays(14), null, "BORROWED"));

        assertThrows(IllegalStateException.class, () -> service.deleteUser(user.getId()));
    }

    @Test
    void deleteUserRejectsWhenBorrowingHistoryExists() throws SQLException {
        User user = service.createUser(new Student(0, "Alice", "0700000000", "alice@example.com"));
        borrowingDAO.add(new Borrowing(0, user.getId(), 100,
                LocalDate.now().minusDays(20), LocalDate.now().minusDays(6),
                LocalDate.now().minusDays(5), "RETURNED"));

        assertThrows(IllegalStateException.class, () -> service.deleteUser(user.getId()));
    }

    @Test
    void deleteUserSucceedsWithNoBorrowingHistory() throws SQLException {
        User user = service.createUser(new Student(0, "Alice", "0700000000", "alice@example.com"));
        service.deleteUser(user.getId());
        assertNull(userDAO.get(user.getId()));
    }

    // ---------- getAllUsers ----------

    @Test
    void getAllUsersReturnsCreatedUser() throws SQLException {
        User user = service.createUser(new Student(0, "Alice", "0700000000", "alice@example.com"));
        assertTrue(service.getAllUsers().stream().anyMatch(u -> u.getId() == user.getId()));
    }

    // ================= Fakes (test doubles, no Mockito needed) =================

    private static class FakeUserDAO implements UserDAO {
        private final Map<Integer, User> users = new HashMap<>();
        private int nextId = 1;

        @Override public User create(User t) { t.setId(nextId++); users.put(t.getId(), t); return t; }
        @Override public void update(User t) { users.put(t.getId(), t); }
        @Override public void delete(User t) { users.remove(t.getId()); }
        @Override public User get(int id) { return users.get(id); }
        @Override public List<User> getAll() { return new ArrayList<>(users.values()); }
    }

    private static class FakeBorrowingDAO implements BorrowingDAO {
        private final List<Borrowing> borrowings = new ArrayList<>();
        void add(Borrowing b) { borrowings.add(b); }

        @Override public Borrowing create(Borrowing t) { borrowings.add(t); return t; }
        @Override public void update(Borrowing t) {}
        @Override public void delete(Borrowing t) { borrowings.remove(t); }
        @Override public Borrowing get(int id) { return null; }
        @Override public List<Borrowing> getAll() { return borrowings; }

        @Override public List<Borrowing> findByUser(int userId) {
            List<Borrowing> result = new ArrayList<>();
            for (Borrowing b : borrowings) if (b.getUserId() == userId) result.add(b);
            return result;
        }

        @Override public List<Borrowing> findOverdue() { return List.of(); }
    }
}