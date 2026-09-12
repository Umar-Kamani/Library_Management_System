package service;

import Models.book;
import database.dbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookManager {


    // This adds a new book to our database; it returns true if it worked, false if something went wrong
public boolean addBook(String title, String author, String isbn,
                           int genreId, int locationId, int totalCopies) {

    // This is to insert a new row into the books table, if there is no id,the database auto-generates that on insert availablecopies is  equal to totalcopies since nothing is borrowed yet
    String sql = "INSERT INTO books (title, author, isbn, genreid, locationid, totalcopies, availablecopies) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = dbUtils.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

//? placeholders used to keep this safe from SQL injection
        stmt.setString(1, title);
        stmt.setString(2, author);
        stmt.setString(3, isbn);
        stmt.setInt(4, genreId);
        stmt.setInt(5, locationId);
        stmt.setInt(6, totalCopies);
        stmt.setInt(7, totalCopies); // availablecopies = totalcopies on creation

        int rowsInserted = stmt.executeUpdate();
        return rowsInserted > 0;

    } catch (SQLException e) {
        System.out.println("Failed to add book: " + e.getMessage());
        return false;
    }
}
    }

