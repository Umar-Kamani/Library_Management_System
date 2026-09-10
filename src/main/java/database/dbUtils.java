package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbUtils {
    private static final String URL  =
            "jdbc:mysql://localhost:3306/school_db"
                    + "?useSSL=false&serverTimezone=UTC"
                    + "&allowPublicKeyRetrieval=true";
    private static final String USER = "school_user";
    private static final String PASS = "SecurePass123!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
