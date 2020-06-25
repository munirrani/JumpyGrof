package jumpygrof;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PointBox extends JLabel {

    public static final int NODE_SIZE = 25;

    public PointBox(String text, int x, int y) {
        setBorder(new LineBorder(Color.YELLOW, 3));
        setBackground(Color.WHITE);
        setBounds(x, y, NODE_SIZE, NODE_SIZE);
        setOpaque(true);
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setText(text);
    }
}
