package mu.alche.library.Database.DAO.Impl;

import mu.alche.library.Database.DAO.UserDAO;
import mu.alche.library.Models.Faculty;
import mu.alche.library.Models.Student;
import mu.alche.library.Models.User;
import mu.alche.library.Database.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public User create(User user) throws SQLException {

        String sql = """
                INSERT INTO user (user_name, user_phone, user_email, user_role)
                VALUES (?, ?, ?, ?)""";

        String role;

        if (user instanceof Student) {
            role = "STUDENT";
        } else if (user instanceof Faculty) {
            role = "FACULTY";
        } else {
            throw new IllegalArgumentException("Unknown user type");
        }

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS))
        {

            ps.setString(1, user.getName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getEmail());
            ps.setString(4, role);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }
            }

            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }


    @Override
    public void update(User user) throws SQLException {

        String sql = """
                UPDATE user
                SET user_name=?, user_phone=?, user_email=?, user_role=?
                WHERE user_id=?""";

        String role;

        if (user instanceof Student) {
            role = "STUDENT";
        } else if (user instanceof Faculty) {
            role = "FACULTY";
        } else {
            throw new IllegalArgumentException("Unknown user type");
        }

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {

            ps.setString(1, user.getName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getEmail());
            ps.setString(4, role);
            ps.setInt(5, user.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }


    @Override
    public void delete(User user) throws SQLException {

        String sql = "DELETE FROM user WHERE user_id=?";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {

            ps.setInt(1, user.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }


    @Override
    public User get(int id) throws SQLException {

        String sql = "SELECT * FROM user WHERE user_id = ?";
        User user = null;

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    int userId = rs.getInt("user_id");
                    String name = rs.getString("user_name");
                    String phone = rs.getString("user_phone");
                    String email = rs.getString("user_email");
                    String role = rs.getString("user_role");

                    if (role.equalsIgnoreCase("STUDENT")) {

                        user = new Student(
                                userId,
                                name,
                                phone,
                                email
                        );

                    } else if (role.equalsIgnoreCase("FACULTY")) {

                        user = new Faculty(
                                userId,
                                name,
                                phone,
                                email
                        );
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get user", e);
        }

        return user;
    }


    @Override
    public List<User> getAll() throws SQLException {

        String sql = "SELECT * FROM user";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {

            List<User> users = new ArrayList<>();

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    int userId = rs.getInt("user_id");
                    String name = rs.getString("user_name");
                    String phone = rs.getString("user_phone");
                    String email = rs.getString("user_email");
                    String role = rs.getString("user_role");

                    User user;

                    if (role.equalsIgnoreCase("STUDENT")) {

                        user = new Student(
                                userId,
                                name,
                                phone,
                                email
                        );

                    } else if (role.equalsIgnoreCase("FACULTY")) {

                        user = new Faculty(
                                userId,
                                name,
                                phone,
                                email
                        );

                    } else {
                        continue;
                    }

                    users.add(user);
                }
            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all users", e);
        }
    }
}
