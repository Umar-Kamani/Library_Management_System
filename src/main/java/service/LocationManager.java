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


}
