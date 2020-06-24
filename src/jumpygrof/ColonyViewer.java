package jumpygrof;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class ColonyViewer extends JFrame {
    private String title;
    private JPanel draggableNodesPanel;
    private List<DraggableNode> draggableNodes = new ArrayList<>();

    public ColonyViewer(String title) {
        this.title = title;
        initDraggableNodesList();
        initDraggableNodesPanel();
        initFrame();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseMotionHandler);
    }

    private void initDraggableNodesList() {
        DraggableNode mds1 = new DraggableNode(253, 210);
        mds1.setText("C1");
        DraggableNode mds2 = new DraggableNode(408, 341);
        mds2.setText("C2");
        DraggableNode mds3 = new DraggableNode(157, 213);
        mds3.setText("C3");
        DraggableNode mds4 = new DraggableNode(231, 192);
        mds4.setText("C4");
        DraggableNode mds5 = new DraggableNode(421, 241);
        mds5.setText("C5");
        DraggableNode mds6 = new DraggableNode(139, 268);
        mds6.setText("C6");
        
        draggableNodes = new ArrayList<>();
        draggableNodes.add(mds1);
        draggableNodes.add(mds2);
        draggableNodes.add(mds3);
        draggableNodes.add(mds4);
        draggableNodes.add(mds5);
        draggableNodes.add(mds6);
    }

    private void initDraggableNodesPanel() {
        draggableNodesPanel = new JPanel();
        draggableNodesPanel.setMinimumSize(new Dimension(300, 150));
        draggableNodesPanel.setLayout(null);

        for(DraggableNode mds : draggableNodes) {
            draggableNodesPanel.add(mds);
            initMouseListenerForMDS(mds);
            repaint();
        }
    }

    private void initFrame() {
        setTitle("Jumpygrof " + title);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                setVisible(false);
            }
        });
        setLayout(new GridLayout(1,1));
        add(draggableNodesPanel);
        setSize(700,500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private int screenX = 0;
    private int screenY = 0;
    private int myX = 0;
    private int myY = 0;

    private void initMouseListenerForMDS(DraggableNode mds) {
        mds.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();
                myX = mds.getX();
                myY = mds.getY();
            }
        });

        mds.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getXOnScreen() - screenX;
                int deltaY = e.getYOnScreen() - screenY;
                mds.setLocation(myX + deltaX, myY + deltaY);
            }
        });
    }

    private int xbegin;
    private int ybegin;
    private int xend;
    private int yend;
    private int width;
    private int height;
    private int x;
    private int y;
    private boolean isNewRect = false;
    private List<DraggableNode> selectedMDSList = new ArrayList<>();
    private MouseListener mouseHandler = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            for(DraggableNode mds : selectedMDSList) {
                mds.setBorder(new LineBorder(Color.BLUE, 3));
            }
            selectedMDSList.clear();


            xbegin = e.getX();
            ybegin = e.getY();
            isNewRect = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            xend = e.getX();
            yend = e.getY();
            isNewRect = true;
            repaint();

            Rectangle rectangle = new Rectangle(x,y,width,height);
            for(DraggableNode mds : draggableNodes) {
                if(rectangle.intersects(new Rectangle(mds.getX(), mds.getY(), mds.getWidth(), mds.getHeight()))) {
                    selectedMDSList.add(mds);
                }
            }
            for(DraggableNode mds : selectedMDSList) {
                mds.setBorder(new LineBorder(Color.CYAN, 3));
            }

        }
    };

    private MouseMotionListener mouseMotionHandler = new MouseAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            xend = e.getX();
            yend = e.getY();
            isNewRect = false;

            repaint();

        }
    };

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if((xbegin-xend)<0) {
            width = Math.abs(xbegin-xend);
            x = xbegin;
        } else {
            width = Math.abs(xend-xbegin);
            x = xend;
        }

        if((ybegin-yend)<0) {
            height = Math.abs(ybegin-yend);
            y = ybegin;
        } else {
            height = Math.abs(yend-ybegin);
            y = yend;
        }

        Graphics2D g2D = (Graphics2D) g;
        AlphaComposite transparency = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2D.setComposite(transparency);
        g2D.setColor(new Color(0x0073b5e9));
        if(!isNewRect) {
            g2D.fill(new Rectangle2D.Float(x,y,width,height));
        }
    }

    public static class Main {
        public static void main(String[] argv){ new ColonyViewer("Simulation"); }
    }
}
