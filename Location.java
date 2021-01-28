package simulator;
import static simulator.Simulator.*;

import java.io.*;
import java.util.*;
import java.lang.*;

public class Location
{
    double x,y;
    public Location()
    {
        Random random = new Random();
        
        this.x = random.nextDouble() * X_SIZE;
        this.y = random.nextDouble() * Y_SIZE;
    }
    
    public Location(double ratio)
    {
        Random random = new Random();
        
        this.x = random.nextDouble() * X_SIZE * ratio;
        this.y = random.nextDouble() * Y_SIZE * ratio;
    }
    
    public Location(double px, double py)
    {
        this.x = px;
        this.y = py;
    }
    
    public double getX()
    {
        return this.x;
    }
    
    public double getY()
    {
        return this.y;
    }
    
    public void setX(double px)
    {
        this.x = px;
    }
    
    public void setY(double py)
    {
        this.y = py;
    }
    
    public double getDistance(Location l)
    {
        double dx = l.getX() - this.x;
        double dy = l.getY() - this.y;
        
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    public boolean atLocation(Location l)
    {
        double distance = this.getDistance(l);
        return distance < CLOSENESS_FACTOR;
    }
    
    public boolean atLocation(Location l, double offset)
    {
        double distance = this.getDistance(l);
        return distance <= offset;
    }
    
    public void moveToward(Location l, double speed)
    {
        double dx = l.getX() - this.x;
        double dy = l.getY() - this.y;
        
        double theta = Math.atan2(dy, dx);
        
        if(this.getDistance(l) < speed)
        {
            Random random = new Random();
            int lowerOffset = -5;
            int upperOffset = 5;
            
            this.setX(l.x + lowerOffset + random.nextInt(upperOffset - lowerOffset + 1));
            this.setY(l.y + lowerOffset + random.nextInt(upperOffset - lowerOffset + 1));
        }
        else
        {
            this.setX(this.x + speed * Math.cos(theta));
            this.setY(this.y + speed * Math.sin(theta));
        }
    }
    
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ")";
    }

}