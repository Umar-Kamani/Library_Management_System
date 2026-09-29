package mu.alche.library.Database.DAO.Impl;

import mu.alche.library.Models.Borrowing;
import mu.alche.library.Database.DBUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SqlResolve") // tables exist in the live alche_library DB; IDE has no introspected schema
class BorrowingDAOImplTest {

    private final BorrowingDAOImpl borrowingDAO = new BorrowingDAOImpl();

    private static int testUserId;
    private static int testBookId;

    private Borrowing testBorrowing;

    @BeforeAll
    static void setUpUserAndBook() throws SQLException {
        try (Connection conn = DBUtils.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO user (user_name, user_phone, user_email, user_role) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "Test Borrower");
                ps.setString(2, "0000000000");
                ps.setString(3, "test.borrower@example.com");
                ps.setString(4, "STUDENT");
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) testUserId = keys.getInt(1);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO book (book_name, book_isbn, total_copies, available_copies, book_author) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "Test Book For Borrowing");
                ps.setString(2, "0000000000000");
                ps.setInt(3, 3);
                ps.setInt(4, 3);
                ps.setString(5, "Test Author");
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) testBookId = keys.getInt(1);
                }
            }
        }
    }

    @AfterAll
    static void tearDownUserAndBook() throws SQLException {
        try (Connection conn = DBUtils.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM book WHERE book_id = ?")) {
                ps.setInt(1, testBookId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM user WHERE user_id = ?")) {
                ps.setInt(1, testUserId);
                ps.executeUpdate();
            }
        }
    }

    @AfterEach
    void cleanUpBorrowing() throws SQLException {
        if (testBorrowing != null && testBorrowing.getId() != 0) {
            borrowingDAO.delete(testBorrowing);
            testBorrowing = null;
        }
    }

    private Borrowing newTestBorrowing(LocalDate dueDate) {
        return new Borrowing(0, testUserId, testBookId, LocalDate.now(), dueDate, null, "BORROWED");
    }

    @Test
    void createAssignsGeneratedId() throws SQLException {
        testBorrowing = borrowingDAO.create(newTestBorrowing(LocalDate.now().plusDays(14)));
        assertTrue(testBorrowing.getId() > 0);
    }

    @Test
    void getReturnsCreatedBorrowing() throws SQLException {
        testBorrowing = borrowingDAO.create(newTestBorrowing(LocalDate.now().plusDays(14)));
        Borrowing fetched = borrowingDAO.get(testBorrowing.getId());
        assertNotNull(fetched);
        assertEquals(testUserId, fetched.getUserId());
        assertEquals(testBookId, fetched.getBookId());
        assertEquals("BORROWED", fetched.getStatus());
        assertNull(fetched.getReturnDate());
    }

    @Test
    void updateSetsReturnDateAndStatus() throws SQLException {
        testBorrowing = borrowingDAO.create(newTestBorrowing(LocalDate.now().plusDays(14)));

        testBorrowing.setReturnDate(LocalDate.now());
        testBorrowing.setStatus("RETURNED");
        borrowingDAO.update(testBorrowing);

        Borrowing fetched = borrowingDAO.get(testBorrowing.getId());
        assertEquals("RETURNED", fetched.getStatus());
        assertEquals(LocalDate.now(), fetched.getReturnDate());
    }

    @Test
    void findByUserIncludesCreatedBorrowing() throws SQLException {
        testBorrowing = borrowingDAO.create(newTestBorrowing(LocalDate.now().plusDays(14)));
        List<Borrowing> results = borrowingDAO.findByUser(testUserId);
        assertTrue(results.stream().anyMatch(b -> b.getId() == testBorrowing.getId()));
    }

    @Test
    void findOverdueIncludesPastDueBorrowedBook() throws SQLException {
        testBorrowing = borrowingDAO.create(newTestBorrowing(LocalDate.now().minusDays(1)));
        List<Borrowing> overdue = borrowingDAO.findOverdue();
        assertTrue(overdue.stream().anyMatch(b -> b.getId() == testBorrowing.getId()));
    }

    @Test
    void findOverdueExcludesFutureDueDate() throws SQLException {
        testBorrowing = borrowingDAO.create(newTestBorrowing(LocalDate.now().plusDays(14)));
        List<Borrowing> overdue = borrowingDAO.findOverdue();
        assertFalse(overdue.stream().anyMatch(b -> b.getId() == testBorrowing.getId()));
    }

    @Test
    void deleteRemovesBorrowing() throws SQLException {
        Borrowing b = borrowingDAO.create(newTestBorrowing(LocalDate.now().plusDays(14)));
        int id = b.getId();

        borrowingDAO.delete(b);
        assertNull(borrowingDAO.get(id));
        testBorrowing = null;
    }
}