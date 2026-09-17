package mu.alche.library.UI;

import mu.alche.library.App.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BooksPanel {

    public static JPanel BooksPanel(MainWindow mainWindow) {

        // =========================
        // MAIN PANEL
        // =========================

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);


        // =========================
        // HEADER
        // =========================

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.decode("#273c75"));
        headerPanel.setPreferredSize(new Dimension(0, 90));


        // HOME BUTTON

        JButton homeButton = new JButton("Home");

        homeButton.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        homeButton.setFocusPainted(false);

        homeButton.addActionListener(e -> {
            mainWindow.showHomePanel();
        });


        // TITLE

        JLabel title = new JLabel("Books");

        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font("Arial", Font.BOLD, 36)
        );


        headerPanel.add(homeButton, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);


        // =========================
        // BUTTON PANEL
        // =========================

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(3, 1, 10, 10)
                );

        buttonPanel.setBackground(
                Color.decode("#273c75")
        );

        buttonPanel.setBorder(
                new EmptyBorder(
                        20, 15, 20, 15
                )
        );


        JButton newButton = new JButton("New");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");


        newButton.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        editButton.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        deleteButton.setFont(
                new Font("Arial", Font.BOLD, 16)
        );


        newButton.setFocusPainted(false);
        editButton.setFocusPainted(false);
        deleteButton.setFocusPainted(false);


        buttonPanel.add(newButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);


        buttonPanel.setPreferredSize(
                new Dimension(150, 0)
        );


        // =========================
        // CONTENT PANEL
        // =========================

        JPanel contentPanel =
                new JPanel(new BorderLayout());

        contentPanel.setBackground(Color.WHITE);

        contentPanel.setBorder(
                new EmptyBorder(
                        20, 20, 20, 20
                )
        );


        // =========================
        // BOOK TABLE
        // =========================

        String[] columns = {
                "ID",
                "Book Name",
                "ISBN",
                "Genre",
                "Location",
                "Total Copies",
                "Available Copies",
                "Author"
        };


        Object[][] data = {};


        JTable bookTable =
                new JTable(data, columns);

        bookTable.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        bookTable.setRowHeight(30);


        bookTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );


        bookTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );


        JScrollPane scrollPane =
                new JScrollPane(bookTable);


        contentPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        // =========================
        // ADD COMPONENTS
        // =========================

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.WEST
        );

        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );


        // =========================
        // BUTTON ACTIONS
        // =========================

        newButton.addActionListener(e -> {
            System.out.println("New button clicked");
        });


        editButton.addActionListener(e -> {
            System.out.println("Edit button clicked");
        });


        deleteButton.addActionListener(e -> {
            System.out.println("Delete button clicked");
        });


        return mainPanel;
    }
}