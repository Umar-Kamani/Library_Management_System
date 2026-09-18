package mu.alche.library.UI;

import mu.alche.library.App.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LocationsPanel {

    public static JPanel LocationPanel(MainWindow mainWindow) {

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

        JButton homeButton = UIComponents.createButton("Home", "#ffffff","#192a56");

        homeButton.addActionListener(e -> {
            mainWindow.showHomePanel();
        });


        // TITLE

        JLabel title = new JLabel("Locations");

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
                new JPanel(new GridLayout(0, 1, 10, 10));

        buttonPanel.setBackground(Color.decode("#273c75"));

        buttonPanel.setBorder(new EmptyBorder(20, 15, 20, 15));


        JButton newButton =UIComponents.createButton("New", "#ffffff","#192a56");
        JButton editButton =UIComponents.createButton("Edit","#ffffff","#192a56");
        JButton deleteButton =UIComponents.createButton ("Delete", "#ffffff","#192a56");


        buttonPanel.add(newButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(homeButton);


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
        // Location TABLE
        // =========================

        String[] columns = {
                "ID",
                "Name",
        };


        Object[][] data = {};


        JTable locationTable =
                new JTable(data, columns);

        locationTable.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        locationTable.setRowHeight(30);


        locationTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );


        locationTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );


        JScrollPane scrollPane =
                new JScrollPane(locationTable);


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
