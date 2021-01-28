package simulator;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import javax.swing.border.Border;
import static simulator.Simulator.*;

import javax.swing.*;

public class SPanel extends JPanel implements MouseListener
{
    Person points[];
    static Person tracked;
    final double offsetX = 3.033;
    final double offsetY = 2.014;
    static boolean selected[];
    
    public SPanel(Person people[])
    {
        Border border = BorderFactory.createBevelBorder(1);
        this.points = people;	
        this.setLayout(null);
        this.setBackground(Color.WHITE);

        this.setVisible(true);
        this.setBorder(border);
        this.addMouseListener(this);
        SPanel.selected = new boolean[]{false};
    }
    
    public void setTracked(Person person)
    {
        this.tracked = person;
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D grp = (Graphics2D)g;
        grp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for(int i = 0; i < POPULATION; i++)
        {
            Color color = null;
            
            if(!points[i].isAlive())
                color = Color.BLACK;
            if(points[i].state.equals(status[0]))
                color = Color.GREEN;
            if(points[i].state.equals(status[1]))
                color = Color.RED;
            if(points[i].state.equals(status[2]))
                color = Color.BLUE;
            
            if(points[i] == tracked)
            {
                grp.setColor(Color.BLACK);
                Stroke stroke = new BasicStroke(5f);
                
                grp.setStroke(stroke);
                Ellipse2D.Double outline = new Ellipse2D.Double(points[i].location.getX() - 4, points[i].location.getY() - 4, 15, 15);
                grp.draw(outline);
            }
            
            grp.setColor(color);
            Ellipse2D.Double circle = new Ellipse2D.Double(points[i].location.getX(), points[i].location.getY(), 7, 7);
            grp.fill(circle);
            
        }
        grp.setColor(Color.BLACK);
        grp.setFont(new Font("Courier New", Font.BOLD, 10));
        for(int i = 0; i < NUM_POPULAR_PLACES; i++)
            grp.drawString(places[i], (int)(popularPlaces[i].getX()), (int)(popularPlaces[i].getY()));
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    { 
        Thread t1 = new Thread(new Runner(e, selected));
        t1.start();
    }    

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private class Runner implements Runnable
    {
        MouseEvent e;
        boolean pointer[];
        Runner(MouseEvent event, boolean selected[])
        {
            e = event;
            pointer = selected;
        }
        
        @Override
        public void run()
        {
            for(int i = 0; i < POPULATION; i++)
                if(points[i].location.getDistance(new Location(e.getX() - offsetX, e.getY() - offsetY)) <= 3.0)
                {
                   setTracked(points[i]);
                   selected[0] = true;
                   pause = true;
                   break;
                }
        }
    }
}