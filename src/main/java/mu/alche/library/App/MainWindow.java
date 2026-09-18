package mu.alche.library.App;

import mu.alche.library.UI.BooksPanel;
import mu.alche.library.UI.BorrowingsPanel;
import mu.alche.library.UI.UIComponents;

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
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        panel = new JPanel(new BorderLayout());

        showHomePanel();
    }

    // =========================
    // HOME PANEL
    // =========================
    public void showHomePanel() {

        panel.removeAll();

        // Header
        JPanel headerPanel = new JPanel();

        // Home buttons
        JPanel homebtnPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        JPanel homebtncontainer = new JPanel();

        // Buttons
        JButton btn1 = UIComponents.createButton("Books", "#192a56", "#ffffff");
        JButton btn2 = UIComponents.createButton("Borrowings", "#192a56", "#ffffff");
        JButton btn3 = UIComponents.createButton("Genres", "#192a56", "#ffffff");
        JButton btn4 = UIComponents.createButton("Locations", "#192a56", "#ffffff");
        JButton btn5 = UIComponents.createButton("Authors", "#192a56", "#ffffff");
        JButton btn6 = UIComponents.createButton("Users", "#192a56", "#ffffff");

        // Add buttons
        homebtnPanel.add(btn1);
        homebtnPanel.add(btn2);
        homebtnPanel.add(btn3);
        homebtnPanel.add(btn4);
        homebtnPanel.add(btn5);
        homebtnPanel.add(btn6);

        homebtnPanel.setPreferredSize(new Dimension(600, 400));

        // Title
        JLabel title = new JLabel("Library Management System");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 50));

        headerPanel.add(title);
        headerPanel.setBackground(Color.decode("#273c75"));

        // Padding
        Border padding = BorderFactory.createEmptyBorder(
                20, 20, 20, 20
        );

        homebtnPanel.setBorder(padding);

        homebtncontainer.add(homebtnPanel);

        // Books button
        btn1.addActionListener(e -> showBooksPanel());
        btn2.addActionListener(e -> showBorrowingsPanel());


        // Add Home to main panel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(homebtncontainer, BorderLayout.CENTER);

        refreshPanel();
    }


    // =========================
    // BOOKS PANEL
    // =========================
    private void showBooksPanel() {

        panel.removeAll();

        panel.add(
                BooksPanel.BooksPanel(this),
                BorderLayout.CENTER
        );

        refreshPanel();
    }

    // =========================
    // BORROWINGS PANEL
    // =========================
    private void showBorrowingsPanel() {

        panel.removeAll();

        panel.add(
                BorrowingsPanel.BorrowingsPanel(this),
                BorderLayout.CENTER
        );

        refreshPanel();
    }


    // =========================
    // REFRESH GUI
    // =========================
    private void refreshPanel() {

        frame.setContentPane(panel);

        panel.revalidate();
        panel.repaint();

        frame.setVisible(true);
    }


    // =========================
    // SHOW WINDOW
    // =========================
    public void show() {
        frame.setVisible(true);
    }
}
