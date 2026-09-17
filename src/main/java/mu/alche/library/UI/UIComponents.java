package mu.alche.library.UI;

import javax.swing.*;
import java.awt.*;

public class UIComponents {

    static JButton createButton(String btn_title, String btn_color) {

        JButton btn = new JButton(btn_title);
        btn.setFocusPainted(false);
        btn.setBackground(Color.decode(btn_color));
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setForeground(Color.white);
        return btn;
    }

}
