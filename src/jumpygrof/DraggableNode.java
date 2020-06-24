package jumpygrof;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DraggableNode extends JLabel {

    public DraggableNode(int x, int y) {
        setBorder(new LineBorder(Color.BLUE, 3));
        setBackground(Color.WHITE);
        setBounds(x, y, 30, 25);
        setOpaque(true);
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
    }
}
