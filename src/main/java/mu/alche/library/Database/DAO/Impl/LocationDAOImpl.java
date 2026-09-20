package mu.alche.library.Database.DAO.Impl;

import mu.alche.library.Database.DAO.LocationDAO;
import mu.alche.library.Database.DBUtils;
import mu.alche.library.Models.Book;
import mu.alche.library.Models.Location;

import java.sql.*;
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

        String sql = "INSERT INTO location (location_name, parent_location_id, location_type) VALUES (?, ?, ?)";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {

            ps.setString(1, location.getName());
            ps.setInt(2, location.getParentLocationId());
            ps.setString(3, location.getType());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) location.setId(keys.getInt(1));
            }
            return location;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create location", e);
        }
    }

    @Override
    public void update(Location location) throws SQLException {
        String sql = "UPDATE location SET location_name=?, parent_location_id=?, location_type=? WHERE location_id=?";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, location.getName());
            ps.setInt(2, location.getParentLocationId());
            ps.setString(3, location.getType());
            ps.setInt(4, location.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update location", e);
        }

    }

    @Override
    public void delete(Location location) throws SQLException {
        String sql = "DELETE FROM location WHERE location_id=?";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, location.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete location", e);
        }
    }

    @Override
    public Location get(int id) throws SQLException {

        String sql = "SELECT * FROM location WHERE location_id = ?";
        Location location = null;

        try(Connection connection = DBUtils.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    int locationid = rs.getInt("location_id");
                    String locationname = rs.getString("location_name");
                    int parentlocationid = rs.getInt("parent_location_id");
                    String locationtype = rs.getString("location_type");

                    location = new Location(
                            locationid,
                            locationname,
                            locationtype,
                            parentlocationid
                    );
                }
            }
        }
        return location;

    }

    @Override
    public List<Location> getAll() throws SQLException {
        String sql = "SELECT * FROM location";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            List<Location> location;
            try (ResultSet rs = ps.executeQuery()) {

                location = new ArrayList<>();

                while (rs.next()) {

                    int locationid = rs.getInt("location_id");
                    String locationname = rs.getString("location_name");
                    int parentlocationid = rs.getInt("parent_location_id");
                    String locationtype = rs.getString("location_type");

                    Location location1 = new Location(
                            locationid,
                            locationname,
                            locationtype,
                            parentlocationid
                    );

                    location.add(location1);
                }
            }

            return location;
        }
    }

}
