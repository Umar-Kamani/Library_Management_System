package mu.alche.library.UI;

import mu.alche.library.App.MainWindow;
import mu.alche.library.Database.DAO.BookDAO;
import mu.alche.library.Database.DAO.Impl.BookDAOImpl;
import mu.alche.library.Models.Book;
import mu.alche.library.Service.BookService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BooksPanel {

    public static JPanel BooksPanel(MainWindow mainWindow) {

        BookDAO bookDAO = new BookDAOImpl();
        BookService bookService = new BookService(bookDAO);

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

        JButton homeButton =
                UIComponents.createButton("Home", "#ffffff", "#192a56");

        homeButton.addActionListener(e ->
                mainWindow.showHomePanel()
        );

        JLabel title = new JLabel("Books");

        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font("Arial", Font.BOLD, 36)
        );

        headerPanel.add(
                title,
                BorderLayout.CENTER
        );


        // =========================
        // BUTTON PANEL
        // =========================

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(0, 1, 10, 10)
                );

        buttonPanel.setBackground(
                Color.decode("#273c75")
        );

        buttonPanel.setBorder(
                new EmptyBorder(
                        20, 15, 20, 15
                )
        );

        JButton newButton =
                UIComponents.createButton(
                        "New",
                        "#ffffff",
                        "#192a56"
                );

        JButton editButton =
                UIComponents.createButton(
                        "Edit",
                        "#ffffff",
                        "#192a56"
                );

        JButton deleteButton =
                UIComponents.createButton(
                        "Delete",
                        "#ffffff",
                        "#192a56"
                );

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
        // SEARCH
        // =========================

        JPanel searchPanel =
                new JPanel(new BorderLayout(10, 10));

        searchPanel.setBackground(Color.WHITE);

        JComboBox<String> searchType =
                new JComboBox<>(
                        new String[]{
                                "Title",
                                "Author",
                                "ISBN"
                        }
                );

        JTextField searchField =
                new JTextField();

        JButton searchButton =
                UIComponents.createButton(
                        "Search",
                        "#ffffff",
                        "#192a56"
                );

        JButton clearButton =
                UIComponents.createButton(
                        "Clear",
                        "#ffffff",
                        "#192a56"
                );

        JPanel searchInputPanel =
                new JPanel(new BorderLayout(10, 10));

        searchInputPanel.setBackground(Color.WHITE);

        searchInputPanel.add(
                searchType,
                BorderLayout.WEST
        );

        searchInputPanel.add(
                searchField,
                BorderLayout.CENTER
        );

        JPanel searchButtonPanel =
                new JPanel(
                        new GridLayout(1, 2, 10, 0)
                );

        searchButtonPanel.setBackground(Color.WHITE);

        searchButtonPanel.add(searchButton);
        searchButtonPanel.add(clearButton);

        searchPanel.add(
                searchInputPanel,
                BorderLayout.CENTER
        );

        searchPanel.add(
                searchButtonPanel,
                BorderLayout.EAST
        );

        searchPanel.setBorder(
                new EmptyBorder(
                        0, 0, 15, 0
                )
        );


        // =========================
        // BOOK TABLE
        // =========================

        String[] columns = {
                "ID",
                "Book Name",
                "Author",
                "ISBN",
                "Genre ID",
                "Location ID",
                "Total Copies",
                "Available Copies"
        };

        DefaultTableModel tableModel =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        JTable bookTable =
                new JTable(tableModel);

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


        // =========================
        // ADD SEARCH + TABLE
        // =========================

        contentPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

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
        // DATA LOADING
        // =========================

        Runnable loadBooks = () -> {

            tableModel.setRowCount(0);

            try {

                List<Book> books =
                        bookService.getAllBooks();

                for (Book book : books) {

                    tableModel.addRow(
                            new Object[]{
                                    book.getId(),
                                    book.getTitle(),
                                    book.getAuthor(),
                                    book.getIsbn(),
                                    book.getGenreId(),
                                    book.getLocationId(),
                                    book.getTotalCopies(),
                                    book.getAvailableCopies()
                            }
                    );
                }

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Could not load books: "
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };


        // =========================
        // SEARCH DATA
        // =========================

        Runnable searchBooks = () -> {

            String searchText =
                    searchField.getText().trim();

            if (searchText.isEmpty()) {

                loadBooks.run();

                return;
            }

            try {

                List<Book> books;

                String selectedType =
                        searchType.getSelectedItem().toString();


                if (selectedType.equals("Title")) {

                    books =
                            bookService.findBooksByTitle(
                                    searchText
                            );

                } else if (selectedType.equals("Author")) {

                    books =
                            bookService.findBooksByAuthor(
                                    searchText
                            );

                } else {

                    books =
                            bookService.findBooksByIsbn(
                                    searchText
                            );
                }


                tableModel.setRowCount(0);


                for (Book book : books) {

                    tableModel.addRow(
                            new Object[]{
                                    book.getId(),
                                    book.getTitle(),
                                    book.getAuthor(),
                                    book.getIsbn(),
                                    book.getGenreId(),
                                    book.getLocationId(),
                                    book.getTotalCopies(),
                                    book.getAvailableCopies()
                            }
                    );
                }


                if (books.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            mainPanel,
                            "No books found.",
                            "Search",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }


            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Search failed: "
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };


        // Load books when panel opens
        loadBooks.run();


        // =========================
        // SEARCH BUTTON
        // =========================

        searchButton.addActionListener(e ->
                searchBooks.run()
        );


        // =========================
        // ENTER TO SEARCH
        // =========================

        searchField.addActionListener(e ->
                searchBooks.run()
        );


        // =========================
        // CLEAR SEARCH
        // =========================

        clearButton.addActionListener(e -> {

            searchField.setText("");

            searchType.setSelectedIndex(0);

            loadBooks.run();
        });


        // =========================
        // NEW BUTTON
        // =========================

        newButton.addActionListener(e -> {

            JTextField titleField =
                    new JTextField();

            JTextField authorField =
                    new JTextField();

            JTextField isbnField =
                    new JTextField();

            JTextField genreField =
                    new JTextField();

            JTextField locationField =
                    new JTextField();

            JTextField totalCopiesField =
                    new JTextField();

            JTextField availableCopiesField =
                    new JTextField();


            JPanel formPanel =
                    new JPanel(
                            new GridLayout(
                                    0,
                                    2,
                                    10,
                                    10
                            )
                    );


            formPanel.add(
                    new JLabel("Book Name:")
            );
            formPanel.add(titleField);


            formPanel.add(
                    new JLabel("Author:")
            );
            formPanel.add(authorField);


            formPanel.add(
                    new JLabel("ISBN:")
            );
            formPanel.add(isbnField);


            formPanel.add(
                    new JLabel("Genre ID:")
            );
            formPanel.add(genreField);


            formPanel.add(
                    new JLabel("Location ID:")
            );
            formPanel.add(locationField);


            formPanel.add(
                    new JLabel("Total Copies:")
            );
            formPanel.add(totalCopiesField);


            formPanel.add(
                    new JLabel("Available Copies:")
            );
            formPanel.add(availableCopiesField);


            int result =
                    JOptionPane.showConfirmDialog(
                            mainPanel,
                            formPanel,
                            "New Book",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );


            if (result != JOptionPane.OK_OPTION) {
                return;
            }


            try {

                String titleText =
                        titleField.getText().trim();

                String authorText =
                        authorField.getText().trim();

                String isbnText =
                        isbnField.getText().trim();

                int genreId =
                        Integer.parseInt(
                                genreField.getText().trim()
                        );

                int locationId =
                        Integer.parseInt(
                                locationField.getText().trim()
                        );

                int totalCopies =
                        Integer.parseInt(
                                totalCopiesField
                                        .getText()
                                        .trim()
                        );

                int availableCopies =
                        Integer.parseInt(
                                availableCopiesField
                                        .getText()
                                        .trim()
                        );


                Book book =
                        new Book(
                                0,
                                titleText,
                                authorText,
                                isbnText,
                                genreId,
                                locationId,
                                totalCopies,
                                availableCopies
                        );


                bookService.createBook(book);


                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Book created successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                loadBooks.run();


            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Genre ID, Location ID, Total Copies, "
                                + "and Available Copies must be numbers.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );


            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        ex.getMessage(),
                        "Invalid Book",
                        JOptionPane.WARNING_MESSAGE
                );


            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Database error: "
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        // =========================
        // EDIT BUTTON
        // =========================

        editButton.addActionListener(e -> {

            int row =
                    bookTable.getSelectedRow();


            if (row == -1) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Select a book to edit.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            int id =
                    (int) tableModel.getValueAt(
                            row,
                            0
                    );


            try {

                Book book =
                        bookService.getBook(id);


                JTextField titleField =
                        new JTextField(
                                book.getTitle()
                        );

                JTextField authorField =
                        new JTextField(
                                book.getAuthor()
                        );

                JTextField isbnField =
                        new JTextField(
                                book.getIsbn()
                        );

                JTextField genreField =
                        new JTextField(
                                String.valueOf(
                                        book.getGenreId()
                                )
                        );

                JTextField locationField =
                        new JTextField(
                                String.valueOf(
                                        book.getLocationId()
                                )
                        );

                JTextField totalCopiesField =
                        new JTextField(
                                String.valueOf(
                                        book.getTotalCopies()
                                )
                        );

                JTextField availableCopiesField =
                        new JTextField(
                                String.valueOf(
                                        book.getAvailableCopies()
                                )
                        );


                JPanel formPanel =
                        new JPanel(
                                new GridLayout(
                                        0,
                                        2,
                                        10,
                                        10
                                )
                        );


                formPanel.add(
                        new JLabel("Book Name:")
                );
                formPanel.add(titleField);


                formPanel.add(
                        new JLabel("Author:")
                );
                formPanel.add(authorField);


                formPanel.add(
                        new JLabel("ISBN:")
                );
                formPanel.add(isbnField);


                formPanel.add(
                        new JLabel("Genre ID:")
                );
                formPanel.add(genreField);


                formPanel.add(
                        new JLabel("Location ID:")
                );
                formPanel.add(locationField);


                formPanel.add(
                        new JLabel("Total Copies:")
                );
                formPanel.add(totalCopiesField);


                formPanel.add(
                        new JLabel("Available Copies:")
                );
                formPanel.add(availableCopiesField);


                int result =
                        JOptionPane.showConfirmDialog(
                                mainPanel,
                                formPanel,
                                "Edit Book",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE
                        );


                if (result != JOptionPane.OK_OPTION) {
                    return;
                }


                book.setTitle(
                        titleField.getText().trim()
                );

                book.setAuthor(
                        authorField.getText().trim()
                );

                book.setIsbn(
                        isbnField.getText().trim()
                );

                book.setGenreId(
                        Integer.parseInt(
                                genreField.getText().trim()
                        )
                );

                book.setLocationId(
                        Integer.parseInt(
                                locationField.getText().trim()
                        )
                );

                book.setTotalCopies(
                        Integer.parseInt(
                                totalCopiesField
                                        .getText()
                                        .trim()
                        )
                );

                book.setAvailableCopies(
                        Integer.parseInt(
                                availableCopiesField
                                        .getText()
                                        .trim()
                        )
                );


                bookService.updateBook(book);


                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Book updated successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                loadBooks.run();


            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Genre ID, Location ID, Total Copies, "
                                + "and Available Copies must be numbers.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );


            } catch (
                    IllegalArgumentException |
                    IllegalStateException ex
            ) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        ex.getMessage(),
                        "Cannot Update Book",
                        JOptionPane.WARNING_MESSAGE
                );


            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Database error: "
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        // =========================
        // DELETE BUTTON
        // =========================

        deleteButton.addActionListener(e -> {

            int row =
                    bookTable.getSelectedRow();


            if (row == -1) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Select a book to delete.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            int id =
                    (int) tableModel.getValueAt(
                            row,
                            0
                    );


            String bookName =
                    tableModel.getValueAt(
                            row,
                            1
                    ).toString();


            int confirm =
                    JOptionPane.showConfirmDialog(
                            mainPanel,
                            "Delete \"" + bookName + "\"?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );


            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }


            try {

                bookService.deleteBook(id);


                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Book deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                loadBooks.run();


            } catch (
                    IllegalArgumentException |
                    IllegalStateException ex
            ) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        ex.getMessage(),
                        "Cannot Delete Book",
                        JOptionPane.WARNING_MESSAGE
                );


            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Database error: "
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        return mainPanel;
    }
}
