package mu.alche.library.Models.database.DAO;

import mu.alche.library.Models.Genre;

import java.sql.SQLException;

public interface GenreDAO extends DAO<Genre> {
    Genre findByName(String name) throws SQLException;
}