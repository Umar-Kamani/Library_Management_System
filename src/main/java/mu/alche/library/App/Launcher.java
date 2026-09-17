package mu.alche.library.App;


import javax.swing.*;
import java.sql.SQLException;

public class Launcher {

    public static void main(String args[]) throws SQLException, ClassNotFoundException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow main = new MainWindow();
                main.show();
            }
        });
    }
}
