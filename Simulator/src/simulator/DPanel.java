package simulator;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import static simulator.Simulator.*;
import static simulator.SPanel.*;

class DPanel extends JPanel implements ActionListener
{
    JLabel infected;
    JLabel vulnerable;
    JLabel immune;
    JLabel dead;
    
    JLabel sd;
    JSlider sd_probability;
    
    JTextField show_infected;
    JTextField show_vulnerable;
    JTextField show_immune;
    JTextField show_dead;
    
    JTextField live_x;
    JTextField live_y;
    JLabel tracked_live;
    
    JButton playSim;
    JButton pauseSim;
    
    public DPanel()
    {
        vulnerable = new JLabel();
        vulnerable.setText("Number of people vulnerable :");
        vulnerable.setFont(new Font("Courier New", Font.PLAIN, 12));
        vulnerable.setBounds(10, 10, 250, 20);
        this.add(vulnerable);
        
        infected = new JLabel();
        infected.setText("Number of people infected   :");
        infected.setFont(new Font("Courier New", Font.PLAIN, 12));
        infected.setBounds(10, 35, 250, 20);
        this.add(infected);
        
        immune = new JLabel();
        immune.setText("Number of people immune     :");
        immune.setFont(new Font("Courier New", Font.PLAIN, 12));
        immune.setBounds(10, 60, 250, 20);
        this.add(immune);
        
        dead = new JLabel();
        dead.setText("Number of people dead       :");
        dead.setFont(new Font("Courier New", Font.PLAIN, 12));
        dead.setBounds(10, 85, 250, 20);
        this.add(dead);
        
        show_vulnerable = new JTextField();
        show_vulnerable.setCaretColor(Color.WHITE);
        show_vulnerable.setBounds(220, 10, 140, 20);
        //show_vulnerable.setEditable(false);
        this.add(show_vulnerable);
        
        show_infected = new JTextField();
        show_infected.setBounds(220, 35, 140, 20);
        //show_infected.setEditable(false);
        this.add(show_infected);
        
        show_immune = new JTextField();
        show_immune.setBounds(220, 60, 140, 20);
        //show_immune.setEditable(false);
        this.add(show_immune);
        
        show_dead = new JTextField();
        show_dead.setBounds(220, 85, 140, 20);
        //show_dead.setEditable(false);
        this.add(show_dead);
        
        sd = new JLabel();
        sd.setText("S.D. ratio                  :");
        sd.setFont(new Font("Courier New", Font.PLAIN, 12));
        sd.setBounds(10, 200, 250, 20);
        this.add(sd);
        
        sd_probability = new JSlider(0, 100, 50);
        sd_probability.setMinorTickSpacing(10);
        sd_probability.setMajorTickSpacing(20);
        sd_probability.setPaintTicks(true);
        sd_probability.setBounds(220, 200, 140, 30);
        sd_probability.setOpaque(false);
        sd_probability.addChangeListener((ChangeEvent e) -> {
            this.repaint();
        });
        this.add(sd_probability);
        
        playSim = new JButton("Play");
        playSim.setBounds(160, 320, 90, 25);
        playSim.setForeground(Color.WHITE);
        playSim.setBackground(Color.BLACK);
        playSim.setBorder(BorderFactory.createBevelBorder(1));
        playSim.setFocusable(false);
        playSim.setEnabled(false);
        playSim.addActionListener(this);
        this.add(playSim);
        
        pauseSim = new JButton("Pause");
        pauseSim.setBounds(270, 320, 90, 25);
        pauseSim.setForeground(Color.WHITE);
        pauseSim.setBackground(Color.BLACK);
        pauseSim.setBorder(BorderFactory.createBevelBorder(1));
        pauseSim.setFocusable(false);
        pauseSim.addActionListener(this);
        pauseSim.setEnabled(false);
        this.add(pauseSim);
        
        tracked_live = new JLabel();
        tracked_live.setText("Live tracking               :");
        tracked_live.setBounds(10, 250, 250, 20);
        tracked_live.setFont(new Font("Courier New", Font.PLAIN, 12));
        this.add(tracked_live);
        
        live_x = new JTextField();
        //live_x.setEditable(false);
        live_x.setBounds(220, 250, 65, 20);
        this.add(live_x);
        
        live_y = new JTextField();
        //live_x.setEditable(false);
        live_y.setBounds(295, 250, 65, 20);
        this.add(live_y);
        
        this.setBorder(BorderFactory.createBevelBorder(1));
        this.setLayout(null);
        this.setVisible(true);
        this.setBackground(new Color(245, 245, 245));
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        show_vulnerable.setText("" + num_vulnerable);
        show_infected.setText("" + num_infected);
        show_immune.setText("" + num_immune);
        show_dead.setText("" + num_dead);
        
        double slider_value = sd_probability.getValue();
        slider_value /= 100;
        Simulator.DISTANCING_PROBABILITY = slider_value;
        
        g.setFont(new Font("Courier New", Font.PLAIN, 12));
        g.drawString(String.format("(%.3f)", slider_value), 85, 213);
        if(selected[0])
        {
            pauseSim.setEnabled(true);
            playSim.setEnabled(true);
            
            live_x.setText(String.format("%.3f", tracked.location.getX()));
            live_y.setText(String.format("%.3f", tracked.location.getY()));
            
            g.setFont(new Font("Courier New", Font.PLAIN, 12));
            g.drawString("(" + tracked.state + ")", 115, 263);
            
            String loc = (tracked.location.atLocation(tracked.home, 5.0))? "Home": "Travelling...";
            for(int i = 0; i < NUM_POPULAR_PLACES; i++)
                if(tracked.location.atLocation(popularPlaces[i], 20.0))
                {
                    loc = places[i];
                    break;
                }
            
            g.drawString(loc.equals("Travelling...")? loc: "(" + loc + ")", 10, 279);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == pauseSim)
            pause = true;
        else
            pause = false;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}