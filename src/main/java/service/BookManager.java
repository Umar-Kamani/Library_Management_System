package service;

import Models.book;
import database.dbUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class BookManager {


    // This adds a new book to our database; it returns true if it worked, false if something went wrong
public boolean addBook(String title, String author, String isbn,
                           int genreId, int locationId, int totalCopies) {

    // to insert a new row into the books table, if there is no id,the database auto-generates that on insert availablecopies is  equal to totalcopies since nothing is borrowed yet
    String sql = "INSERT INTO books (title, author, isbn, genreid, locationid, totalcopies, availablecopies) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
                           }

}