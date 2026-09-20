package mu.alche.library.Service;

import mu.alche.library.Database.DAO.GenreDAO;
import mu.alche.library.Models.Genre;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class GenreService {
    private static final int MAX_NAME_LENGTH = 255;

    private final GenreDAO genreDAO;

    public GenreService(GenreDAO genreDAO) {
        this.genreDAO = genreDAO;
    }

    public Genre createGenre(Genre genre) throws SQLException {

        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be empty");
        }

        String name = validateName(genre.getName());

        if (genreDAO.findByName(name) != null) {
            throw new IllegalArgumentException(
                    "A genre named \"" + name + "\" already exists"
            );

        }

        genre.setName(name);

        return genreDAO.create(genre);
    }

