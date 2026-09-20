package mu.alche.library.Service;

import mu.alche.library.Database.DAO.LocationDAO;
import mu.alche.library.Models.Location;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationService {

    private final LocationDAO locationDAO;

    public LocationService(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }

    // =========================
    // CREATE
    // =========================

    public Location createLocation(Location location) throws SQLException {

        validateLocation(location);

        // A parent location must exist unless this is a root location.
        if (location.getParentLocationId() > 0) {

            Location parent =
                    locationDAO.get(
                            location.getParentLocationId()
                    );

            if (parent == null) {
                throw new IllegalArgumentException(
                        "Parent location does not exist."
                );
            }
        }

        return locationDAO.create(location);
    }


    // =========================
    // GET
    // =========================

    public Location getLocation(int id) throws SQLException {

        Location location =
                locationDAO.get(id);

        if (location == null) {
            throw new IllegalArgumentException(
                    "No location found with id " + id
            );
        }

        return location;
    }


    // =========================
    // GET ALL
    // =========================

    public List<Location> getAllLocations() throws SQLException {
        return locationDAO.getAll();
    }


    // =========================
    // GET CHILDREN
    // =========================

    public List<Location> getChildren(int parentId)
            throws SQLException {

        return locationDAO.findChildren(parentId);
    }


    // =========================
    // UPDATE
    // =========================

    public void updateLocation(Location location)
            throws SQLException {

        if (location == null) {
            throw new IllegalArgumentException(
                    "Location cannot be null"
            );
        }

        Location existingLocation =
                locationDAO.get(location.getId());

        if (existingLocation == null) {
            throw new IllegalArgumentException(
                    "No location found with id "
                            + location.getId()
            );
        }

        validateLocation(location);

        // Prevent a location from being its own parent.
        if (location.getParentLocationId() == location.getId()) {
            throw new IllegalArgumentException(
                    "A location cannot be its own parent."
            );
        }

        // Check that the parent exists.
        if (location.getParentLocationId() > 0) {

            Location parent =
                    locationDAO.get(
                            location.getParentLocationId()
                    );

            if (parent == null) {
                throw new IllegalArgumentException(
                        "Parent location does not exist."
                );
            }
        }

        locationDAO.update(location);
    }


    // =========================
    // DELETE
    // =========================

    public void deleteLocation(int id)
            throws SQLException {

        Location location =
                locationDAO.get(id);

        if (location == null) {
            throw new IllegalArgumentException(
                    "No location found with id " + id
            );
        }

        // A location with children should not be deleted.
        List<Location> children =
                locationDAO.findChildren(id);

        if (!children.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete location because it has "
                            + children.size()
                            + " child location(s)."
            );
        }

        locationDAO.delete(location);
    }


    // =========================
    // VALIDATION
    // =========================

    private void validateLocation(Location location) {

        if (location == null) {
            throw new IllegalArgumentException(
                    "Location cannot be null"
            );
        }

        if (location.getName() == null ||
                location.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Location name cannot be empty"
            );
        }

        if (location.getType() == null ||
                location.getType().isBlank()) {

            throw new IllegalArgumentException(
                    "Location type cannot be empty"
            );
        }

        if (location.getParentLocationId() < 0) {

            throw new IllegalArgumentException(
                    "Parent location ID cannot be negative"
            );
        }
    }
        
    // =========================
    // RECURSIVE TRAVERSAL
    // =========================

    public List<Location> getAllDescendants(int locationId) throws SQLException {
        List<Location> descendants = new ArrayList<>();
        List<Location> children = locationDAO.findChildren(locationId);

        for (Location child : children) {
            descendants.add(child);
            descendants.addAll(getAllDescendants(child.getId())); // recursive call
        }

        return descendants;
    }

    public int countAllDescendants(int locationId) throws SQLException {
        return getAllDescendants(locationId).size();
    }

    public String getFullPath(int locationId) throws SQLException {
        Location location = locationDAO.get(locationId);

        if (location == null) {
            throw new IllegalArgumentException("No location found with id " + locationId);
        }

        if (location.getParentLocationId() == null || location.getParentLocationId() <= 0) {
            return location.getName(); // base case: reached the root
        }

        return getFullPath(location.getParentLocationId()) + " > " + location.getName(); // recursive call
    }
}