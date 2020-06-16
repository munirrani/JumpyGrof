/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author Asus
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.geom.Ellipse2D.Double;

public class kangaroomove extends JPanel  implements ActionListener {
    Timer t = new Timer(5,this);
    double x = 0, y = 0, velX = 2, velY = 2;
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        Ellipse2D circle = new Ellipse2D.Double(x,y,40,40);
        g2.fill(circle);
        t.start();
        
    }
    
    public void actionPerformed(ActionEvent e){
        if( x < 0 || x > 560){
            velX = -velX;
        }
        if( y < 0 || y > 360){
            velY = -velY;
        }
        x += velX;
        y += velY;
        repaint();
    }
    
}
