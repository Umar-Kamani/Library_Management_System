package mu.alche.library.Service;
import mu.alche.library.Database.DAO.BorrowingDAO;
import mu.alche.library.Database.DAO.UserDAO;
import mu.alche.library.Models.Borrowing;
import mu.alche.library.Models.Faculty;
import mu.alche.library.Models.Student;
import mu.alche.library.Models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class UserService {

    private static final int MAX_TEXT_LENGTH = 255;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // optional leading +, then digits, spaces, brackets and dashes
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9 ()-]+$");

    private final UserDAO userDAO;
    private final BorrowingDAO borrowingDAO;

    public UserService(UserDAO userDAO, BorrowingDAO borrowingDAO) {
        this.userDAO = userDAO;
        this.borrowingDAO = borrowingDAO;
    }

    public User createUser(User user) throws SQLException {

        validateAndNormalise(user);

        if (isEmailInUse(user.getEmail(), -1)) {
            throw new IllegalArgumentException(
                    "A user with email " + user.getEmail() + " already exists"
            );
        }

        return userDAO.create(user);
    }
    public User getUser(int id) throws SQLException {

        User user = userDAO.get(id);

        if (user == null) {
            throw new IllegalArgumentException(
                    "No user found with id " + id
            );
        }

        return user;
    }
    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAll();
    }

    public void updateUser(User user) throws SQLException {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        User existingUser = userDAO.get(user.getId());

        if (existingUser == null) {
            throw new IllegalArgumentException(
                    "No user found with id " + user.getId()
            );
        }

        validateAndNormalise(user);

        if (isEmailInUse(user.getEmail(), user.getId())) {
            throw new IllegalArgumentException(
                    "A user with email " + user.getEmail() + " already exists"
            );
        }

                if (!existingUser.getRole().equals(user.getRole())) {

            long activeBorrowings = countActiveBorrowings(user.getId());

            if (activeBorrowings > user.getMaxBooks()) {
                throw new IllegalStateException(
                        "Cannot change user " + user.getId() + " to " + user.getRole() +
                                ": they currently have " + activeBorrowings +
                                " book(s) borrowed but a " + user.getRole() +
                                " can only borrow " + user.getMaxBooks()
                );
            }
        }

        userDAO.update(user);
    }

    public void deleteUser(int id) throws SQLException {

        User user = userDAO.get(id);

        if (user == null) {
            throw new IllegalArgumentException(
                    "No user found with id " + id
            );
        }
        List<Borrowing> borrowings = borrowingDAO.findByUser(id);

        long activeBorrowings = borrowings.stream()
                .filter(b -> b.getReturnDate() == null)
                .count();

        if (activeBorrowings > 0) {
            throw new IllegalStateException(
                    "Cannot delete user " + id + " because they still have " +
                            activeBorrowings + " book(s) borrowed"
            );
        }

        // borrowing.borrowing_user_id has a foreign key to user.user_id,
        // so a user with returned borrowings can't be deleted either.
        if (!borrowings.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete user " + id +
                            " because they have a borrowing history"
            );
        }

        userDAO.delete(user);
    }

    // Checks every field and trims the text values on the user object
    private void validateAndNormalise(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (!(user instanceof Student) && !(user instanceof Faculty)) {
            throw new IllegalArgumentException(
                    "User must be either an ALCHE Student or a Faculty member"
            );
        }

        String name = user.getName();

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }

        name = name.strip();

        if (name.length() > MAX_TEXT_LENGTH) {
            throw new IllegalArgumentException(
                    "User name cannot be longer than " + MAX_TEXT_LENGTH + " characters"
            );
        }
        String phone = user.getPhone();

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("User phone cannot be empty");
        }

        phone = phone.strip();

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException(
                    "Phone number can only contain digits, spaces, dashes, brackets and a leading +"
            );
        }

        long digitCount = phone.chars().filter(Character::isDigit).count();

        if (digitCount < 7 || digitCount > 15) {
            throw new IllegalArgumentException(
                    "Phone number must contain between 7 and 15 digits"
            );
        }
        String email = user.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("User email cannot be empty");
        }

        email = email.strip();

        if (email.length() > MAX_TEXT_LENGTH || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("User email is not a valid email address");
        }

        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);
    }
    private boolean isEmailInUse(String email, int ignoreUserId) throws SQLException {

        for (User existing : userDAO.getAll()) {
            if (existing.getId() != ignoreUserId && email.equalsIgnoreCase(existing.getEmail())) {
                return true;
            }
        }

        return false;
    }

    private long countActiveBorrowings(int userId) throws SQLException {
        return borrowingDAO.findByUser(userId).stream()
                .filter(b -> b.getReturnDate() == null)
                .count();
    }
}

