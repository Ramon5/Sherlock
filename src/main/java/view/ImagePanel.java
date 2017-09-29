/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author root
 */
public class ImagePanel extends JPanel {

    private ImageIcon imageIcon;

    public ImagePanel(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(imageIcon.getImage(), 0, 0, null);
    }

}
