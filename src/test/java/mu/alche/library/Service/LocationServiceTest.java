package mu.alche.library.Service;

import mu.alche.library.Database.DAO.LocationDAO;
import mu.alche.library.Models.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LocationServiceTest {

    private FakeLocationDAO locationDAO;
    private LocationService service;

    @BeforeEach
    void setUp() {
        locationDAO = new FakeLocationDAO();
        service = new LocationService(locationDAO);
    }

    // ---------- createLocation ----------

    @Test
    void createRootLocationSucceeds() throws SQLException {
        Location root = service.createLocation(new Location(0, "Main Library", "Building", 0));
        assertTrue(root.getId() > 0);
    }

    @Test
    void createChildLocationSucceedsWhenParentExists() throws SQLException {
        Location root = service.createLocation(new Location(0, "Main Library", "Building", 0));
        Location child = service.createLocation(new Location(0, "Floor 2", "Floor", root.getId()));
        assertEquals(root.getId(), child.getParentLocationId());
    }

    @Test
    void createLocationRejectsMissingParent() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createLocation(new Location(0, "Ghost Shelf", "Shelf", 999)));
    }

    @Test
    void createLocationRejectsBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createLocation(new Location(0, "  ", "Shelf", 0)));
    }

    @Test
    void createLocationRejectsBlankType() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createLocation(new Location(0, "Shelf A1", "", 0)));
    }

    // ---------- updateLocation ----------

    @Test
    void updateLocationRejectsUnknownId() {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateLocation(new Location(999, "Shelf", "Shelf", 0)));
    }

    @Test
    void updateLocationRejectsSelfAsParent() throws SQLException {
        Location loc = service.createLocation(new Location(0, "Shelf A1", "Shelf", 0));
        loc.setParentLocationId(loc.getId());

        assertThrows(IllegalArgumentException.class, () -> service.updateLocation(loc));
    }

    @Test
    void updateLocationRejectsMissingParent() throws SQLException {
        Location loc = service.createLocation(new Location(0, "Shelf A1", "Shelf", 0));
        loc.setParentLocationId(999);

        assertThrows(IllegalArgumentException.class, () -> service.updateLocation(loc));
    }

    // ---------- deleteLocation ----------

    @Test
    void deleteLocationRejectsWhenChildrenExist() throws SQLException {
        Location root = service.createLocation(new Location(0, "Main Library", "Building", 0));
        service.createLocation(new Location(0, "Floor 2", "Floor", root.getId()));

        assertThrows(IllegalStateException.class, () -> service.deleteLocation(root.getId()));
    }

    @Test
    void deleteLocationSucceedsWhenNoChildren() throws SQLException {
        Location loc = service.createLocation(new Location(0, "Shelf A1", "Shelf", 0));
        service.deleteLocation(loc.getId());
        assertNull(locationDAO.get(loc.getId()));
    }

    // ---------- getChildren (single level) ----------

    @Test
    void getChildrenReturnsOnlyDirectChildren() throws SQLException {
        Location root = service.createLocation(new Location(0, "Main Library", "Building", 0));
        Location floor = service.createLocation(new Location(0, "Floor 2", "Floor", root.getId()));
        service.createLocation(new Location(0, "Shelf A1", "Shelf", floor.getId())); // grandchild

        List<Location> children = service.getChildren(root.getId());
        assertEquals(1, children.size());
        assertEquals(floor.getId(), children.get(0).getId());
    }

    // ---------- getAllDescendants (recursion) ----------

    @Test
    void getAllDescendantsIncludesEveryLevel() throws SQLException {
        Location root = service.createLocation(new Location(0, "Main Library", "Building", 0));
        Location floor = service.createLocation(new Location(0, "Floor 2", "Floor", root.getId()));
        Location section = service.createLocation(new Location(0, "Fiction Section", "Section", floor.getId()));
        Location shelf = service.createLocation(new Location(0, "Shelf A1", "Shelf", section.getId()));

        List<Location> descendants = service.getAllDescendants(root.getId());

        assertEquals(3, descendants.size()); // floor, section, shelf - not root itself
        assertTrue(descendants.stream().anyMatch(l -> l.getId() == floor.getId()));
        assertTrue(descendants.stream().anyMatch(l -> l.getId() == section.getId()));
        assertTrue(descendants.stream().anyMatch(l -> l.getId() == shelf.getId()));
    }

    @Test
    void getAllDescendantsOnLeafReturnsEmpty() throws SQLException {
        Location leaf = service.createLocation(new Location(0, "Shelf A1", "Shelf", 0));
        assertTrue(service.getAllDescendants(leaf.getId()).isEmpty()); // base case
    }

    @Test
    void countAllDescendantsMatchesRecursiveListSize() throws SQLException {
        Location root = service.createLocation(new Location(0, "Main Library", "Building", 0));
        Location floor = service.createLocation(new Location(0, "Floor 2", "Floor", root.getId()));
        service.createLocation(new Location(0, "Fiction Section", "Section", floor.getId()));
        service.createLocation(new Location(0, "Reference Section", "Section", floor.getId()));

        assertEquals(3, service.countAllDescendants(root.getId()));
    }

    // ---------- getFullPath (recursion, upward) ----------

    @Test
    void getFullPathOnRootReturnsJustItsOwnName() throws SQLException {
        Location root = service.createLocation(new Location(0, "Main Library", "Building", 0));
        assertEquals("Main Library", service.getFullPath(root.getId()));
    }

    @Test
    void getFullPathBuildsCompletePathFromRoot() throws SQLException {
        Location root = service.createLocation(new Location(0, "Main Library", "Building", 0));
        Location floor = service.createLocation(new Location(0, "Floor 2", "Floor", root.getId()));
        Location section = service.createLocation(new Location(0, "Fiction Section", "Section", floor.getId()));

        assertEquals("Main Library > Floor 2 > Fiction Section", service.getFullPath(section.getId()));
    }

    @Test
    void getFullPathRejectsUnknownId() {
        assertThrows(IllegalArgumentException.class, () -> service.getFullPath(999));
    }

    // ================= Fake (test double, no Mockito needed) =================

    private static class FakeLocationDAO implements LocationDAO {
        private final Map<Integer, Location> locations = new HashMap<>();
        private int nextId = 1;

        @Override public Location create(Location t) {
            t.setId(nextId++);
            locations.put(t.getId(), t);
            return t;
        }
        @Override public void update(Location t) { locations.put(t.getId(), t); }
        @Override public void delete(Location t) { locations.remove(t.getId()); }
        @Override public Location get(int id) { return locations.get(id); }
        @Override public List<Location> getAll() { return new ArrayList<>(locations.values()); }

        @Override public List<Location> findChildren(int parentId) {
            List<Location> result = new ArrayList<>();
            for (Location l : locations.values()) {
                if (l.getParentLocationId() == parentId && l.getId() != parentId) result.add(l);
            }
            return result;
        }
    }
}