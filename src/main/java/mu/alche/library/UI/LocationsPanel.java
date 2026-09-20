package mu.alche.library.UI;

import mu.alche.library.App.MainWindow;
import mu.alche.library.Database.DAO.LocationDAO;
import mu.alche.library.Database.DAO.Impl.LocationDAOImpl;
import mu.alche.library.Models.Location;
import mu.alche.library.Service.LocationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationsPanel {

    public static JPanel LocationsPanel(MainWindow mainWindow) {

        LocationDAO locationDAO =
                new LocationDAOImpl();

        LocationService locationService =
                new LocationService(locationDAO);


        // =========================
        // MAIN PANEL
        // =========================

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(Color.WHITE);


        // =========================
        // HEADER
        // =========================

        JPanel headerPanel =
                new JPanel(new BorderLayout());

        headerPanel.setBackground(
                Color.decode("#273c75")
        );

        headerPanel.setPreferredSize(
                new Dimension(0, 90)
        );


        JButton homeButton =
                UIComponents.createButton(
                        "Home",
                        "#ffffff",
                        "#192a56"
                );

        homeButton.addActionListener(
                e -> mainWindow.showHomePanel()
        );


        JLabel title =
                new JLabel("Locations");

        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        36
                )
        );



        headerPanel.add(title, BorderLayout.CENTER);


        // =========================
        // BUTTON PANEL
        // =========================

        JPanel buttonPanel = new JPanel(new GridLayout(0,1,10,10));

        buttonPanel.setBackground(
                Color.decode("#273c75")
        );

        buttonPanel.setBorder(
                new EmptyBorder(
                        20,
                        15,
                        20,
                        15
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
                        20,
                        20,
                        20,
                        20
                )
        );


        // =========================
        // LOCATION TABLE
        // =========================

        String[] columns = {
                "ID",
                "Location Name",
                "Type",
                "Parent Location"
        };


        DefaultTableModel tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };


        JTable locationTable =
                new JTable(tableModel);


        locationTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        locationTable.setRowHeight(30);

        locationTable.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
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
        // DATA LOADING
        // =========================

        Runnable reload = () -> {

            tableModel.setRowCount(0);

            try {

                List<Location> locations =
                        locationService
                                .getAllLocations();


                // Store locations by ID so we can
                // display the parent's name.
                Map<Integer, String> locationNames =
                        new HashMap<>();


                for (Location location : locations) {

                    locationNames.put(
                            location.getId(),
                            location.getName()
                    );
                }


                for (Location location : locations) {

                    int parentId =
                            location
                                    .getParentLocationId();


                    String parentName;


                    if (parentId <= 0) {

                        parentName = "-";

                    } else {

                        parentName =
                                locationNames.get(
                                        parentId
                                );

                        if (parentName == null) {

                            parentName =
                                    "Unknown (ID "
                                            + parentId
                                            + ")";
                        }
                    }


                    tableModel.addRow(
                            new Object[]{
                                    location.getId(),
                                    location.getName(),
                                    location.getType(),
                                    parentName
                            }
                    );
                }


            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Could not load locations: "
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };


        reload.run();


        // =========================
        // NEW BUTTON
        // =========================

        newButton.addActionListener(e -> {

            JTextField nameField =
                    new JTextField();

            JTextField typeField =
                    new JTextField();

            JTextField parentField =
                    new JTextField("0");


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
                    new JLabel("Location Name:")
            );

            formPanel.add(nameField);


            formPanel.add(
                    new JLabel("Type:")
            );

            formPanel.add(typeField);


            formPanel.add(
                    new JLabel("Parent Location ID:")
            );

            formPanel.add(parentField);


            int result =
                    JOptionPane.showConfirmDialog(
                            mainPanel,
                            formPanel,
                            "New Location",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );


            if (result != JOptionPane.OK_OPTION) {
                return;
            }


            try {

                String name =
                        nameField
                                .getText()
                                .trim();

                String type =
                        typeField
                                .getText()
                                .trim();

                int parentId =
                        Integer.parseInt(
                                parentField
                                        .getText()
                                        .trim()
                        );


                Location location =
                        new Location(
                                0,
                                name,
                                type,
                                parentId
                        );


                locationService.createLocation(
                        location
                );


                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Location created successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                reload.run();


            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Parent Location ID must be a number.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );


            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        ex.getMessage(),
                        "Invalid Location",
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
                    locationTable.getSelectedRow();


            if (row == -1) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Select a location to edit.",
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

                Location location =
                        locationService
                                .getLocation(id);


                JTextField nameField =
                        new JTextField(
                                location.getName()
                        );

                JTextField typeField =
                        new JTextField(
                                location.getType()
                        );

                JTextField parentField =
                        new JTextField(
                                String.valueOf(
                                        location
                                                .getParentLocationId()
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
                        new JLabel("Location Name:")
                );

                formPanel.add(nameField);


                formPanel.add(
                        new JLabel("Type:")
                );

                formPanel.add(typeField);


                formPanel.add(
                        new JLabel("Parent Location ID:")
                );

                formPanel.add(parentField);


                int result =
                        JOptionPane.showConfirmDialog(
                                mainPanel,
                                formPanel,
                                "Edit Location",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE
                        );


                if (result != JOptionPane.OK_OPTION) {
                    return;
                }


                location.setName(
                        nameField
                                .getText()
                                .trim()
                );

                location.setType(
                        typeField
                                .getText()
                                .trim()
                );

                location.setParentLocationId(
                        Integer.parseInt(
                                parentField
                                        .getText()
                                        .trim()
                        )
                );


                locationService.updateLocation(
                        location
                );


                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Location updated successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                reload.run();


            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Parent Location ID must be a number.",
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
                        "Cannot Update Location",
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
                    locationTable.getSelectedRow();


            if (row == -1) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Select a location to delete.",
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


            String locationName =
                    tableModel
                            .getValueAt(
                                    row,
                                    1
                            )
                            .toString();


            int confirm =
                    JOptionPane.showConfirmDialog(
                            mainPanel,
                            "Delete \""
                                    + locationName
                                    + "\"?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );


            if (confirm !=
                    JOptionPane.YES_OPTION) {

                return;
            }


            try {

                locationService.deleteLocation(
                        id
                );


                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Location deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                reload.run();


            } catch (
                    IllegalArgumentException |
                    IllegalStateException ex
            ) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        ex.getMessage(),
                        "Cannot Delete Location",
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