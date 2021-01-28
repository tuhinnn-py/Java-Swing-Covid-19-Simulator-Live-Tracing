package simulator;
import static simulator.Simulator.*;

import java.lang.*;
import java.util.Random;

public class Person
{
    String state;
    Location location;
    int disease_counter;
    PlacesModel mobilityModel;
    Location home;
    int immunity_period;
    
    public Person(Location house, Location popularPlaces[])
    {
        this.state = status[0];
        this.location = new Location(house.getX(), house.getY());
       
        this.home = house;
        Random random = new Random();
        
        this.immunity_period = (30 + random.nextInt(10)) * 24;
        this.disease_counter = (10 + random.nextInt(20)) * 24;
        this.mobilityModel = new PlacesModel(this, popularPlaces, house);
    }
    
    public void setState(String s)
    {
        this.state = s;
    }
    
    public boolean isAlive()
    {
        return !this.state.equals(status[3]);
    }
    
    public boolean infect()
    {
        if(this.state.equals(status[0]))
        {
            this.state = status[1];
            return true;
        }
        
        return false;
    }
    
    public boolean try_infect(Person person)
    {
        if(! this.state.equals(status[1]))
            return false;
        
        if(this.location.getDistance(person.location) > INFECTION_PROXIMITY)
            return false;
        
        if(person.location.atLocation(person.home))
        {
            if(try_event(INFECTION_PROBABILITY) && try_event(1 -DISTANCING_HOME_PROBABILITY))
                return person.infect();
            else
                return false;
        }
        else
        {
            if(try_event(INFECTION_PROBABILITY) && try_event(1 -NOT_DISTANCING_HOME_PROBABILITY))
                return person.infect();
            else
                return false;
        }
    }
    
    public void progressDisease()
    {
        if(this.state.equals(status[1]))
        {
            disease_counter--;
            if(disease_counter <= 0)
            {
                double fatality = NORMAL_FATALITY_RATE;
                if(saturated)
                    fatality = SATURATED_FATALITY_RATE;
                
                if(try_event(fatality))
                    this.setState(status[3]);
                else
                    this.setState(status[2]);
            }
        }
        
        if(this.state.equals(status[2]))
        {
            immunity_period--;
            if(immunity_period <= 0)
                this.setState(status[0]);
        }
    }
    
}