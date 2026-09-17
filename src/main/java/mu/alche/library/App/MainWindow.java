package mu.alche.library.App;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class MainWindow {

    private final JFrame frame;
    private final JPanel panel;

    public MainWindow() {
        frame = new JFrame();
        frame.setTitle("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        panel = new JPanel();

        //Different panels
        JPanel headerPanel = new JPanel(); //The header element of the page
        JPanel homebtnPanel = new JPanel(new GridLayout(0,3,20,20)); //The grid of button on the homepage
        JPanel homebtncontainer = new JPanel(); //Main Container

        //button components that lead to the separate menus
        JButton btn1 = UIComponents.createButton("Books", "#192a56");
        JButton btn2 = UIComponents.createButton("Borrowings", "#192a56");
        JButton btn3 = UIComponents.createButton("Genres", "#192a56");
        JButton btn4 = UIComponents.createButton("Locations", "#192a56");
        JButton btn5 = UIComponents.createButton("Authors", "#192a56");
        JButton btn6 = UIComponents.createButton("Users", "#192a56");

        homebtnPanel.add(btn1);
        homebtnPanel.add(btn2);
        homebtnPanel.add(btn3);
        homebtnPanel.add(btn4);
        homebtnPanel.add(btn5);
        homebtnPanel.add(btn6);
        homebtnPanel.setPreferredSize(new Dimension(600,400));


        //Title label for the header panel
        JLabel title = new JLabel("Library Management System");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(Color.white);
        title.setFont(new Font("Arial", Font.BOLD, 50));
        headerPanel.add(title);


        //Background Colour of header panel
        headerPanel.setBackground(Color.decode("#273c75"));


        //Setting padding for homebtnpanel
        Border padding = BorderFactory.createEmptyBorder(20,20,20,20);
        homebtnPanel.setBorder(padding);

        homebtncontainer.add(homebtnPanel);

        //Adding elements to the frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(homebtncontainer, BorderLayout.CENTER);
        frame.add(BooksPanel.BooksPanel(), BorderLayout.CENTER);
    }

    public void show() {
        frame.setVisible(true);
    };
}
