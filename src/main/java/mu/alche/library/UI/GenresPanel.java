package mu.alche.library.UI;

import mu.alche.library.App.MainWindow;
import mu.alche.library.Database.DAO.GenreDAO;
import mu.alche.library.Database.DAO.Impl.GenreDAOImpl;
import mu.alche.library.Models.Genre;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GenresPanel {

    public static JPanel GenresPanel(MainWindow mainWindow) {

        GenreDAO genreDAO = new GenreDAOImpl();

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

        JButton homeButton = UIComponents.createButton("Home", "#ffffff","#192a56");
        homeButton.addActionListener(e -> mainWindow.showHomePanel());

        JLabel title = new JLabel("Genres");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 36));

        headerPanel.add(homeButton, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);


        // =========================
        // BUTTON PANEL
        // =========================

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        buttonPanel.setBackground(Color.decode("#273c75"));
        buttonPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        JButton newButton = UIComponents.createButton("New", "#ffffff","#192a56");
        JButton editButton = UIComponents.createButton("Edit","#ffffff","#192a56");
        JButton deleteButton = UIComponents.createButton("Delete", "#ffffff","#192a56");

        buttonPanel.add(newButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(homeButton);
        buttonPanel.setPreferredSize(new Dimension(150, 0));


        // =========================
        // CONTENT PANEL
        // =========================

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


        // =========================
        // GENRES TABLE
        // =========================

        String[] columns = {"ID", "Name"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable genreTable = new JTable(tableModel);
        genreTable.setFont(new Font("Arial", Font.PLAIN, 14));
        genreTable.setRowHeight(30);
        genreTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        genreTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(genreTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);


        // =========================
        // ADD COMPONENTS
        // =========================

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);


        // =========================
        // DATA LOADING
        // =========================

        Runnable reload = () -> {
            tableModel.setRowCount(0);
            try {
                List<Genre> genres = genreDAO.getAll();
                for (Genre g : genres) {
                    tableModel.addRow(new Object[]{g.getId(), g.getName()});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Could not load genres: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        };

        reload.run();


        // =========================
        // BUTTON ACTIONS
        // =========================

        newButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(mainPanel, "Genre name:");
            if (name == null) return; // cancelled
            if (name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Genre name cannot be empty.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                genreDAO.create(new Genre(0, name.trim()));
                reload.run();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Could not create genre: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        editButton.addActionListener(e -> {
            int row = genreTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Select a genre to edit.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            String currentName = (String) tableModel.getValueAt(row, 1);

            String newName = JOptionPane.showInputDialog(mainPanel, "Genre name:", currentName);
            if (newName == null) return;
            if (newName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Genre name cannot be empty.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                genreDAO.update(new Genre(id, newName.trim()));
                reload.run();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Could not update genre: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int row = genreTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Select a genre to delete.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            String name = (String) tableModel.getValueAt(row, 1);

            int confirm = JOptionPane.showConfirmDialog(mainPanel,
                    "Delete genre \"" + name + "\"?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                genreDAO.delete(new Genre(id, name));
                reload.run();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Could not delete genre: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return mainPanel;
    }
}