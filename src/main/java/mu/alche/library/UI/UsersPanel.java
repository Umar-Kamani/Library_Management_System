package mu.alche.library.UI;

import mu.alche.library.App.MainWindow;
import mu.alche.library.Database.DAO.BorrowingDAO;
import mu.alche.library.Database.DAO.Impl.BorrowingDAOImpl;
import mu.alche.library.Database.DAO.Impl.UserDAOImpl;
import mu.alche.library.Database.DAO.UserDAO;
import mu.alche.library.Models.Faculty;
import mu.alche.library.Models.Student;
import mu.alche.library.Models.User;
import mu.alche.library.Service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UsersPanel {

    public static JPanel UsersPanel(MainWindow mainWindow) {

        UserDAO userDAO = new UserDAOImpl();
        BorrowingDAO borrowingDAO = new BorrowingDAOImpl();
        UserService userService = new UserService(userDAO, borrowingDAO);

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

        JButton homeButton = UIComponents.createButton("Home", "#ffffff", "#192a56");
        homeButton.addActionListener(e -> mainWindow.showHomePanel());

        JLabel title = new JLabel("Users");
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

        JButton newButton = UIComponents.createButton("New", "#ffffff", "#192a56");
        JButton editButton = UIComponents.createButton("Edit", "#ffffff", "#192a56");
        JButton deleteButton = UIComponents.createButton("Delete", "#ffffff", "#192a56");

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
        // USER TABLE
        // =========================

        String[] columns = {"ID", "Name", "Phone", "Email", "Role"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable userTable = new JTable(tableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userTable.setRowHeight(30);
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(userTable);
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
                List<User> users = userService.getAllUsers();
                for (User u : users) {
                    tableModel.addRow(new Object[]{
                            u.getId(), u.getName(), u.getPhone(), u.getEmail(), u.getRole()
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Could not load users: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        };

        reload.run();


        // =========================
        // BUTTON ACTIONS
        // =========================

        newButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(mainPanel, "Name:");
            if (name == null) return;
            String phone = JOptionPane.showInputDialog(mainPanel, "Phone:");
            if (phone == null) return;
            String email = JOptionPane.showInputDialog(mainPanel, "Email:");
            if (email == null) return;

            int roleChoice = JOptionPane.showOptionDialog(mainPanel, "Role:", "Select Role",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new String[]{"Student", "Faculty"}, "Student");
            if (roleChoice == JOptionPane.CLOSED_OPTION) return;

            User user = (roleChoice == 1)
                    ? new Faculty(0, name, phone, email)
                    : new Student(0, name, phone, email);

            try {
                userService.createUser(user);
                reload.run();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainPanel, ex.getMessage(),
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Database error: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        editButton.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Select a user to edit.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) tableModel.getValueAt(row, 0);
            String currentName = (String) tableModel.getValueAt(row, 1);
            String currentPhone = (String) tableModel.getValueAt(row, 2);
            String currentEmail = (String) tableModel.getValueAt(row, 3);
            String currentRole = (String) tableModel.getValueAt(row, 4);

            String name = JOptionPane.showInputDialog(mainPanel, "Name:", currentName);
            if (name == null) return;
            String phone = JOptionPane.showInputDialog(mainPanel, "Phone:", currentPhone);
            if (phone == null) return;
            String email = JOptionPane.showInputDialog(mainPanel, "Email:", currentEmail);
            if (email == null) return;

            int defaultRoleIndex = currentRole.equalsIgnoreCase("Faculty") ? 1 : 0;
            int roleChoice = JOptionPane.showOptionDialog(mainPanel, "Role:", "Select Role",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new String[]{"Student", "Faculty"},
                    defaultRoleIndex == 1 ? "Faculty" : "Student");
            if (roleChoice == JOptionPane.CLOSED_OPTION) return;

            User user = (roleChoice == 1)
                    ? new Faculty(id, name, phone, email)
                    : new Student(id, name, phone, email);

            try {
                userService.updateUser(user);
                reload.run();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(mainPanel, ex.getMessage(),
                        "Cannot Update User", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Database error: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Select a user to delete.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) tableModel.getValueAt(row, 0);
            String name = (String) tableModel.getValueAt(row, 1);

            int confirm = JOptionPane.showConfirmDialog(mainPanel,
                    "Delete user \"" + name + "\"?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                userService.deleteUser(id);
                reload.run();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(mainPanel, ex.getMessage(),
                        "Cannot Delete User", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Database error: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return mainPanel;
    }
}