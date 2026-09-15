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
        JPanel gridPanel = new JPanel(new GridLayout(0,4,10,10)); //The grid of button on the homepage


        //
        JButton btn1 = new JButton("Books");
        JButton btn2 = new JButton("Borrowings");
        JButton btn3 = new JButton("Genres");
        JButton btn4 = new JButton("Locations");
        JButton btn5 = new JButton("Add Book");
        JButton btn6 = new JButton("Add Borrowing");
        JButton btn7 = new JButton("Add Genre");
        JButton btn8 = new JButton("Add Location");

        gridPanel.setBackground(Color.WHITE);
        gridPanel.add(btn1);
        gridPanel.add(btn2);
        gridPanel.add(btn3);
        gridPanel.add(btn4);
        gridPanel.add(btn5);
        gridPanel.add(btn6);
        gridPanel.add(btn7);
        gridPanel.add(btn8);

        headerPanel.setBackground(Color.LIGHT_GRAY);


        Border padding = BorderFactory.createEmptyBorder(20,20,20,20);
        gridPanel.setBorder(padding);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(gridPanel, BorderLayout.CENTER);

    }

    private JButton createButton() {

        JButton btn = new JButton("Book");

        return btn;
    }

    public void show() {
        frame.setVisible(true);
    };
}
