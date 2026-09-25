package mu.alche.library.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
    private static final String URL  = "jdbc:mysql://localhost:3306/alche_library";
    private static final String USER = "library_manager";
    private static final String PASS = "password1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public void initialiseSchema() {
        String[] schema = {

                // Create database
                "CREATE DATABASE IF NOT EXISTS alche_library",

                // Select database
                "USE alche_library",

                // Genre table
                "CREATE TABLE IF NOT EXISTS genre (" +
                        "genre_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "genre_name VARCHAR(255) NOT NULL" +
                        ")",

                // Location table
                "CREATE TABLE IF NOT EXISTS location (" +
                        "location_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "location_name VARCHAR(255) NOT NULL, " +
                        "location_type VARCHAR(255) NOT NULL, " +
                        "parent_location_id INT NOT NULL, " +
                        "CONSTRAINT location_ibfk_1 FOREIGN KEY(parent_location_id) REFERENCES location(location_id)" +
                        ")", 

                // User table
                "CREATE TABLE IF NOT EXISTS user (" +
                        "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_name VARCHAR(255) NOT NULL, " +
                        "user_phone VARCHAR(255) NOT NULL, " +
                        "user_email VARCHAR(255) NOT NULL, " +
                        "user_role VARCHAR(255) NOT NULL" +
                        ")",

                // Book table
                "CREATE TABLE IF NOT EXISTS book (" +
                        "book_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "book_name VARCHAR(255) NOT NULL UNIQUE, " +
                        "book_isbn VARCHAR(13) NOT NULL, " +
                        "book_genre_id INT NULL, " +
                        "book_location_id INT NULL, " +
                        "total_copies INT NOT NULL, " +
                        "available_copies INT NOT NULL, " +
                        "book_author VARCHAR(255) NOT NULL, " +
                        "FOREIGN KEY (book_genre_id) REFERENCES genre(genre_id), " +
                        "FOREIGN KEY (book_location_id) REFERENCES location(location_id)" +
                        ")",


                // Borrowing table
                "CREATE TABLE IF NOT EXISTS borrowing (" +
                        "borrowing_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "borrowing_user_id INT NOT NULL, " +
                        "borrowing_book_id INT NOT NULL, " +
                        "borrow_date DATE NOT NULL, " +
                        "due_date DATE NOT NULL, " +
                        "return_date DATE NULL, " +
                        "status VARCHAR(255) NOT NULL, " +
                        "FOREIGN KEY (borrowing_user_id) REFERENCES user(user_id), " +
                        "FOREIGN KEY (borrowing_book_id) REFERENCES book(book_id)" +
                        ")",
        };

        try (Statement stmt = getConnection().createStatement()) {

            for (String sql : schema) {
                stmt.execute(sql);
            }

            System.out.println("Schema has been initialised");

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to Initialise Schema for alche_library",
                    e
            );
        }
    }
}
