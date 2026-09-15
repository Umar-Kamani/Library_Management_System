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

        JPanel headerPanel = new JPanel(); //The header element of the page
        JPanel homebtnPanel = new JPanel(new GridLayout(0,3,20,20)); //The grid of button on the homepage
        JPanel homebtncontainer = new JPanel();

        //
        JButton btn1 = createButton("Books");
        JButton btn2 = createButton("Borrowings");
        JButton btn3 = createButton("Genres");
        JButton btn4 = createButton("Locations");
        JButton btn5 = createButton("Authors");
        JButton btn6 = createButton("Users");

        homebtnPanel.add(btn1);
        homebtnPanel.add(btn2);
        homebtnPanel.add(btn3);
        homebtnPanel.add(btn4);
        homebtnPanel.add(btn5);
        homebtnPanel.add(btn6);
        homebtnPanel.setPreferredSize(new Dimension(600,400));

        JLabel title = new JLabel("Library Management System");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(Color.white);
        title.setFont(new Font("Arial", Font.BOLD, 50));



        headerPanel.setBackground(Color.decode("#273c75"));
        headerPanel.add(title);


        Border padding = BorderFactory.createEmptyBorder(20,20,20,20);
        homebtnPanel.setBorder(padding);

        homebtncontainer.add(homebtnPanel);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(homebtncontainer, BorderLayout.CENTER);

    }

    private JButton createButton(String btn_title) {

        JButton btn = new JButton(btn_title);
        btn.setFocusPainted(false);
        btn.setBackground(Color.decode("#192a56"));
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setForeground(Color.white);
        return btn;
    }

    public void show() {
        frame.setVisible(true);
    };
}
