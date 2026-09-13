package service;

import Models.Location;
import database.dbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LocationManager {

    public boolean addLocation(String name) {

        // this helps us to insert a new row into the locations table
        String sql = "INSERT INTO locations (name) VALUES (?)";

        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Failed to add location: " + e.getMessage());
            return false;
        }
    }


}
