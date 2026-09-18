package mu.alche.library.UI;

import mu.alche.library.App.MainWindow;
import mu.alche.library.Database.DAO.*;
import mu.alche.library.Database.DAO.Impl.*;
import mu.alche.library.Models.Borrowing;
import mu.alche.library.Models.User;
import mu.alche.library.Service.BorrowingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BorrowingsPanel {

    public static JPanel BorrowingsPanel(MainWindow mainWindow) {

        UserDAO userDAO = new UserDAOImpl();
        BookDAO bookDAO = new BookDAOImpl();
        BorrowingDAO borrowingDAO = new BorrowingDAOImpl();
        BorrowingService borrowingService = new BorrowingService(userDAO, bookDAO, borrowingDAO);

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

        JLabel title = new JLabel("Borrowings");
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
        JButton returnButton = UIComponents.createButton("Return","#ffffff","#192a56");
        JButton deleteButton = UIComponents.createButton("Delete", "#ffffff","#192a56");

        buttonPanel.add(newButton);
        buttonPanel.add(returnButton);
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
        // BORROWINGS TABLE
        // =========================

        String[] columns = {
                "ID", "Borrower", "Book ID", "Borrowed Date", "Due Date", "Return Date", "Status"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable borrowTable = new JTable(tableModel);
        borrowTable.setFont(new Font("Arial", Font.PLAIN, 14));
        borrowTable.setRowHeight(30);
        borrowTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        borrowTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(borrowTable);
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
                List<Borrowing> borrowings = borrowingService.getAllBorrowings();
                for (Borrowing b : borrowings) {
                    String borrowerName;
                    try {
                        User u = userDAO.get(b.getUserId());
                        borrowerName = (u != null) ? u.getName() : "Unknown (id " + b.getUserId() + ")";
                    } catch (SQLException ex) {
                        borrowerName = "id " + b.getUserId();
                    }

                    tableModel.addRow(new Object[]{
                            b.getId(),
                            borrowerName,
                            b.getBookId(),
                            b.getBorrowDate(),
                            b.getDueDate(),
                            b.getReturnDate() == null ? "-" : b.getReturnDate(),
                            b.getStatus()
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Could not load borrowings: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        };

        reload.run();


        // =========================
        // BUTTON ACTIONS
        // =========================

        newButton.addActionListener(e -> {
            String userIdStr = JOptionPane.showInputDialog(mainPanel, "User ID:");
            if (userIdStr == null) return;
            String bookIdStr = JOptionPane.showInputDialog(mainPanel, "Book ID:");
            if (bookIdStr == null) return;

            int userId, bookId;
            try {
                userId = Integer.parseInt(userIdStr.trim());
                bookId = Integer.parseInt(bookIdStr.trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel, "User ID and Book ID must be numbers.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                borrowingService.createBorrowing(userId, bookId);
                reload.run();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(mainPanel, ex.getMessage(),
                        "Cannot Borrow Book", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Database error: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        returnButton.addActionListener(e -> {
            int row = borrowTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Select a borrowing to return.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);

            try {
                borrowingService.returnBook(id);
                reload.run();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(mainPanel, ex.getMessage(),
                        "Cannot Return Book", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Database error: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int row = borrowTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Select a borrowing to delete.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(mainPanel,
                    "Delete this borrowing record?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                borrowingService.deleteBorrowing(id);
                reload.run();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Could not delete borrowing: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return mainPanel;
    }
}