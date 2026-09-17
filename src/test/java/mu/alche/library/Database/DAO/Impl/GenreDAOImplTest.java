package mu.alche.library.Database.DAO.Impl;

import mu.alche.library.Models.Genre;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreDAOImplTest {

    private final GenreDAOImpl genreDAO = new GenreDAOImpl();
    private Genre testGenre;

    @AfterEach
    void cleanUp() throws SQLException {
        if (testGenre != null && testGenre.getId() != 0) {
            genreDAO.delete(testGenre);
            testGenre = null;
        }
    }

    @Test
    void createAssignsGeneratedId() throws SQLException {
        testGenre = genreDAO.create(new Genre(0, "Test Genre - Create"));
        assertTrue(testGenre.getId() > 0, "create() should assign a generated id");
    }

    @Test
    void getReturnsGenreById() throws SQLException {
        testGenre = genreDAO.create(new Genre(0, "Test Genre - Get"));
        Genre fetched = genreDAO.get(testGenre.getId());
        assertNotNull(fetched);
        assertEquals("Test Genre - Get", fetched.getName());
    }

    @Test
    void getReturnsNullForUnknownId() throws SQLException {
        assertNull(genreDAO.get(-999999));
    }

    @Test
    void getAllIncludesCreatedGenre() throws SQLException {
        testGenre = genreDAO.create(new Genre(0, "Test Genre - GetAll"));
        List<Genre> all = genreDAO.getAll();
        assertTrue(all.stream().anyMatch(g -> g.getId() == testGenre.getId()));
    }

    @Test
    void updateChangesName() throws SQLException {
        testGenre = genreDAO.create(new Genre(0, "Test Genre - Before Update"));
        testGenre.setName("Test Genre - After Update");
        genreDAO.update(testGenre);

        Genre fetched = genreDAO.get(testGenre.getId());
        assertEquals("Test Genre - After Update", fetched.getName());
    }

    @Test
    void deleteRemovesGenre() throws SQLException {
        Genre genre = genreDAO.create(new Genre(0, "Test Genre - Delete"));
        int id = genre.getId();

        genreDAO.delete(genre);
        assertNull(genreDAO.get(id));
        testGenre = null; // already gone, nothing left to clean up
    }

    @Test
    void findByNameLocatesExistingGenre() throws SQLException {
        testGenre = genreDAO.create(new Genre(0, "Test Genre - FindByName"));
        Genre found = genreDAO.findByName("Test Genre - FindByName");
        assertNotNull(found);
        assertEquals(testGenre.getId(), found.getId());
    }

    @Test
    void findByNameReturnsNullWhenNotFound() throws SQLException {
        assertNull(genreDAO.findByName("Definitely Not A Real Genre 12345"));
    }
}