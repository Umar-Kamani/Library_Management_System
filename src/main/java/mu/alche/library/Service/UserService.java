package mu.alche.library.Service;

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
    
}
