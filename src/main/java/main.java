import mu.alche.library.Models.Book;
import mu.alche.library.database.DAO.BookDAO;
import mu.alche.library.database.DAO.Impl.BookDAOImpl;
import mu.alche.library.database.DBUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class main {

    static void main(String[] args) throws SQLException {
        Connection connection= DBUtils.getConnection();

        if(connection!=null){
            System.out.println("Connected to database successfully");
        }else
        {
            System.out.println("Connection to database failed");
        }

        BookDAO bookDAO = new BookDAOImpl();
        Book book = new Book(0,"5 title", "Test Author", "1251253654123", 1, 5, 10, 10);
        Book book1 = bookDAO.create(book);
        System.out.println(book);
    }

}
