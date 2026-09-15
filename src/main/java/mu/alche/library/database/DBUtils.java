package mu.alche.library.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
    private static final String URL  = "jdbc:mysql://localhost:3306/alche_library";
    private static final String USER = "library_manager";
    private static final String PASS = "password123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public void initialiseSchema() {
        String[] schema = {
                "CREATE TABLE IF NOT EXISTS alche_library",
                "USE alche_library",

                "CREATE USER 'library_manager'@'localhost' IDENTIFIED BY 'password1234';",
                "GRANT ALL PRIVILEGES ON alche_library.* TO 'library_manager'@'localhost';",

                "CREATE table if not exists genre(genre_id int primary key auto_increment," +
                        "genre_name varchar(255) not null);",

                "CREATE TABLE IF NOT EXISTS location (location_id INT PRIMARY KEY AUTO_INCREMENT, " +
                        "location_name VARCHAR(255) NOT NULL);",

                "create table if not exists User(user_id int primary key auto_increment," +
                        "user_name varchar(255) not null," +
                        "user_phone varchar(255) not null," +
                        "user_email varchar(255) not null," +
                        "user_role varchar(255) not null);",

                "create table if not exists Book(" +
                        "book_id int primary key auto_increment," +
                        "book_name varchar(20) not null unique," +
                        "book_isbn decimal(10,2) not null," +
                        "book_genre_id int," +
                        "foreign key (book_genre_id) references genre(genre_id)," +
                        "book_location_id int," +
                        "foreign key (book_location_id) references location(location_id)," +
                        "total_copies int not null," +
                        "available_copies int not null);",

                "create table if not exists borrowing(" +
                        "borrowing_id int primary key auto_increment," +
                        "borrowing_user_id int not null," +
                        "foreign key (borrowing_user_id) references User(user_id)," +
                        "borrowing_book_id int not null," +
                        "foreign key(borrowing_book_id) references Book(book_id)," +
                        "borrow_date varchar(255) not null," +
                        "due_date varchar(255) not null," +
                        "return_date varchar(255) not null," +
                        "status varchar(255) not null);"
        };

        try(Statement stmt = getConnection().createStatement()) {
            for (String sql : schema) stmt.execute(sql);
            System.out.println("Schema has been initialised");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to Initialise Schema for alche_library", e);
        }
    }
}
