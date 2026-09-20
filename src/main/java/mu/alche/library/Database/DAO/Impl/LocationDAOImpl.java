package mu.alche.library.Database.DAO.Impl;

import mu.alche.library.Database.DAO.LocationDAO;
import mu.alche.library.Database.DBUtils;
import mu.alche.library.Models.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationDAOImpl implements LocationDAO {
    @Override
    public List<Location> findChildren(int parentId) throws SQLException {

        String sql = "SELECT * FROM location WHERE parent_location_id = ?";

        List<Location> locations;

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, parentId);

            try (ResultSet rs = ps.executeQuery()) {

                locations = new ArrayList<>();

                while (rs.next()) {

                    int id = rs.getInt("location_id");
                    String name = rs.getString("location_name");
                    String type = rs.getString("location_type");
                    int parentLocationId = rs.getInt("parent_location_id");

                    Location location = new Location(
                            id,
                            name,
                            type,
                            parentLocationId
                    );

                    locations.add(location);
                }
            }
        }

        return locations;
    }

    @Override
    public Location create(Location location) throws SQLException {
        return null;
    }

    @Override
    public void update(Location location) throws SQLException {

    }

    @Override
    public void delete(Location location) throws SQLException {

    }

    @Override
    public Location get(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Location> getAll() throws SQLException {
        return List.of();
    }


}
