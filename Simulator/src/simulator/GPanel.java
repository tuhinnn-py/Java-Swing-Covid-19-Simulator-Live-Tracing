package simulator;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static simulator.Simulator.*;

import javax.swing.*;
import javax.swing.border.Border;
import simulator.Person;

public class GPanel extends JPanel
{
    int points[];
    int idx;

    public GPanel(int infectionHistory[])
    {
        Border border = BorderFactory.createBevelBorder(1);
        this.points = infectionHistory;	
        
        BufferedImage bg = null;
        try
        {
            bg = ImageIO.read(new File("C:\\Users\\hp\\Desktop\\Simulator\\src\\simulator\\graphBG.jpg"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        Image bgimg = bg.getScaledInstance(365, 355, Image.SCALE_SMOOTH);
        
        JLabel label = new JLabel();
        label.setOpaque(false);
        label.setIcon(new ImageIcon(bgimg));
        label.setBounds(0, 0, 365, 355);
        label.setBorder(border);
        this.add(label);     
        
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        this.setBorder(border);
        this.setVisible(true);
    }
    
    public void init_length(int i)
    {
        this.idx = i;
    }
    
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D grp = (Graphics2D)g;
        
        Color color = Color.BLACK;
        grp.setColor(color);
        double width = (1600.0 / SIM_HOURS);
        for(int i = 0; this.idx > 0 && i < this.idx; i++)
        {
            Rectangle2D.Double rect = new Rectangle2D.Double(i * width, 355 - (points[i] * 355 / POPULATION), width, (points[i] * 355 / POPULATION));
            grp.fill(rect);
        }
    }
}