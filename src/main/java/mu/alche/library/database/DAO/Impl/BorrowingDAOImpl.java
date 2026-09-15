package mu.alche.library.database.DAO.Impl;

import mu.alche.library.Models.Borrowing;
import mu.alche.library.database.DAO.BorrowingDAO;
import mu.alche.library.database.DBUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowingDAOImpl implements BorrowingDAO {

    @Override
    public Borrowing create(Borrowing b) throws SQLException {
        String sql = "INSERT INTO borrowing (borrowing_user_id, borrowing_book_id, borrow_date, due_date, return_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, b.getUserId());
            ps.setInt(2, b.getBookId());
            ps.setString(3, b.getBorrowDate().toString());
            ps.setString(4, b.getDueDate().toString());
            ps.setString(5, b.getReturnDate() != null ? b.getReturnDate().toString() : ""); // workaround: return_date is NOT NULL in schema
            ps.setString(6, b.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) b.setId(keys.getInt(1));
            }
        }
        return b;
    }

    @Override
    public void update(Borrowing b) throws SQLException {
        String sql = "UPDATE borrowing SET return_date = ?, status = ? WHERE borrowing_id = ?";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, b.getReturnDate() != null ? b.getReturnDate().toString() : "");
            ps.setString(2, b.getStatus());
            ps.setInt(3, b.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Borrowing b) throws SQLException {
        String sql = "DELETE FROM borrowing WHERE borrowing_id = ?";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, b.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public Borrowing get(int id) throws SQLException {
        String sql = "SELECT * FROM borrowing WHERE borrowing_id = ?";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public List<Borrowing> getAll() throws SQLException {
        List<Borrowing> results = new ArrayList<>();
        String sql = "SELECT * FROM borrowing ORDER BY borrow_date DESC";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) results.add(mapRow(rs));
        }
        return results;
    }

    @Override
    public List<Borrowing> findByUser(int userId) throws SQLException {
        List<Borrowing> results = new ArrayList<>();
        String sql = "SELECT * FROM borrowing WHERE borrowing_user_id = ? ORDER BY borrow_date DESC";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        }
        return results;
    }

    @Override
    public List<Borrowing> findOverdue() throws SQLException {
        List<Borrowing> results = new ArrayList<>();
        String sql = "SELECT * FROM borrowing WHERE status = 'BORROWED' AND due_date < ?";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, LocalDate.now().toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        }
        return results;
    }

    private Borrowing mapRow(ResultSet rs) throws SQLException {
        String returnDateStr = rs.getString("return_date");
        return new Borrowing(
                rs.getInt("borrowing_id"),
                rs.getInt("borrowing_user_id"),
                rs.getInt("borrowing_book_id"),
                LocalDate.parse(rs.getString("borrow_date")),
                LocalDate.parse(rs.getString("due_date")),
                (returnDateStr == null || returnDateStr.isEmpty()) ? null : LocalDate.parse(returnDateStr),
                rs.getString("status")
        );
    }
}