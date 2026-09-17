package mu.alche.library.App;

import javax.swing.*;
import java.awt.*;

public class BooksPanel {

    private static JPanel panel;

    public static JPanel BooksPanel() {

        panel = new JPanel();

        // Window Panels
        JPanel buttonPanel = new JPanel(new GridLayout(0,1,10,10)); //left side button panel
        JPanel headerpanel = new JPanel(); //title top button panel
        JPanel contentPanel = new JPanel(); //table center button panel
        JPanel mainPanel = new JPanel();

        //Coloring panels
        headerpanel.setBackground(Color.decode("#273c75"));
        buttonPanel.setBackground(Color.decode("#273c75"));

        //padding panels


        //Title label for the header panel
        JLabel title = new JLabel("Book System");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(Color.white);
        title.setFont(new Font("Arial", Font.BOLD, 50));




        headerpanel.add(title);


        mainPanel.add(headerpanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.WEST);

        return mainPanel;
    }
}
