package simulator;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.*;

import static simulator.Simulator.*;

public class SFrame extends JFrame
{
    SPanel spanel;
    GPanel gpanel;
    DPanel dpanel;
    public SFrame(Person people[], int infectionHistory[])
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenSize.getWidth() / 2) - 550;
        int h = (int) (screenSize.getHeight() / 2) - 400;
        this.setLocation(new Point(w, h));
        this.setLayout(null);
        
        spanel = new SPanel(people);
        spanel.setBounds(0, 0, 710, 710);
        this.add(spanel);
        
        gpanel = new GPanel(infectionHistory);
        gpanel.setBounds(715, 355, 365, 355);
        this.add(gpanel);
        
        dpanel = new DPanel();
        dpanel.setBounds(715, 0, 365, 350);
        this.add(dpanel);
        
        this.setTitle("Simulator");
        this.setSize(1100, 750);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
}