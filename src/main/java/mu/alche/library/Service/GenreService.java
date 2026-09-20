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

    public Genre getGenre(int id) throws SQLException {

        Genre genre = genreDAO.get(id);

        if (genre == null) {
            throw new IllegalArgumentException(
                    "No genre found with id " + id
            );
        }

        return genre;
    }

    public List<Genre> getAllGenres() throws SQLException {
        return genreDAO.getAll();
    }

    // Returns null when no genre has that name
    public Genre findGenreByName(String name) throws SQLException {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Genre name cannot be empty");
        }

        return genreDAO.findByName(name.strip());

    }

    public void updateGenre(Genre genre) throws SQLException {

        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }

        Genre existingGenre = genreDAO.get(genre.getId());

        if (existingGenre == null) {
            throw new IllegalArgumentException(
                    "No genre found with id " + genre.getId()
            );
        }

        String name = validateName(genre.getName());

        // Only look for duplicates when the name really changes.
        // Changing only the capitalisation ("fiction" -> "Fiction") is allowed.
        if (!name.equalsIgnoreCase(existingGenre.getName())) {

            Genre sameName = genreDAO.findByName(name);

            if (sameName != null && sameName.getId() != genre.getId()) {
                throw new IllegalArgumentException(
                        "A genre named \"" + name + "\" already exists"
                );
            }
        }

        genre.setName(name);


        genreDAO.update(genre);
    }

    public void deleteGenre(int id) throws SQLException {

        Genre genre = genreDAO.get(id);

        if (genre == null) {
            throw new IllegalArgumentException(
                    "No genre found with id " + id
            );
        }

        try {
            genreDAO.delete(genre);
        } catch (SQLIntegrityConstraintViolationException e) {
            // book.book_genre_id has a foreign key to genre.genre_id,
            // so MySQL refuses to delete a genre that books still use.
            throw new IllegalStateException(
                    "Cannot delete genre \"" + genre.getName() +
                            "\" because one or more books are assigned to it",
                    e
            );
        }
    }

    // Checks the name and returns it trimmed
    private String validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Genre name cannot be empty");

        }

        String trimmed = name.strip();

        if (trimmed.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    "Genre name cannot be longer than " + MAX_NAME_LENGTH + " characters"
            );
        }

        return trimmed;
    }
}