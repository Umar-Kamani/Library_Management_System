package mu.alche.library.Impl;

import mu.alche.library.Models.Genre;
import mu.alche.library.DAO.GenreDAO;
import mu.alche.library.Database.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDAOImpl implements GenreDAO {

    @Override
    public Genre create(Genre genre) throws SQLException {
        String sql = "INSERT INTO genre (genre_name) VALUES (?)";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, genre.getName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) genre.setId(keys.getInt(1));
            }
        }
        return genre;
    }

    @Override
    public void update(Genre genre) throws SQLException {
        String sql = "UPDATE genre SET genre_name = ? WHERE genre_id = ?";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, genre.getName());
            ps.setInt(2, genre.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Genre genre) throws SQLException {
        String sql = "DELETE FROM genre WHERE genre_id = ?";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, genre.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public Genre get(int id) throws SQLException {
        String sql = "SELECT genre_id, genre_name FROM genre WHERE genre_id = ?";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
            }
        }
        return null;
    }

    @Override
    public List<Genre> getAll() throws SQLException {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT genre_id, genre_name FROM genre ORDER BY genre_name";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) genres.add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
        }
        return genres;
    }

    @Override
    public Genre findByName(String name) throws SQLException {
        String sql = "SELECT genre_id, genre_name FROM genre WHERE genre_name = ?";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
            }
        }
        return null;
    }
}