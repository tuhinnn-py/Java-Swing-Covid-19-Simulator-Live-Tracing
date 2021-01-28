package simulator;
import java.util.Random;
import static simulator.Simulator.*;

public class PlacesModel implements MobilityModel
{
    Person person;
    Location wp;
    Location home;
    
    int speed, stay;
    double home_probability;
    Location pp[];
    
    public PlacesModel(Person p, Location popularPlaces[], Location house)
    {
        person = p;
        home =  house;
        pp = popularPlaces;
        speed = -1;
        
        if(try_event(DISTANCING_PROBABILITY))
            home_probability = DISTANCING_HOME_PROBABILITY;
        else
            home_probability = NOT_DISTANCING_HOME_PROBABILITY;
        
    }
    
    public void initialize_distancing_probability()
    {
        if(try_event(DISTANCING_PROBABILITY))
            home_probability = DISTANCING_HOME_PROBABILITY;
        else
            home_probability = NOT_DISTANCING_HOME_PROBABILITY;
    }
    
    public void move()
    {        
        if(speed < 0) 
            pick_new_waypoint();
        
        else if(person.location.atLocation(wp))
        {
            stay--;
            if(stay <= 0)
                pick_new_waypoint();
        }
        else
            person.location.moveToward(wp, speed);
    }
    
    public void pick_new_waypoint()
    {
        Random random = new Random();
        speed = random.nextInt(topSpeed);
        stay = random.nextInt(maxStay);
        
        if(try_event(home_probability))
            wp = home;
        else 
        {
            if(person.state.equals(status[1]))
            {
                if(saturated)
                {
                    if(try_event(SATURATED_HOSPITAL_PROBABILITY))
                        wp = pp[NUM_POPULAR_PLACES - 1];
                    else
                        wp = pp[random.nextInt(NUM_POPULAR_PLACES - 1)];
                }
                else
                {
                    if(try_event(NON_SATURATED_HOSPITAL_PROBABILITY))
                        wp = pp[NUM_POPULAR_PLACES - 1];
                    else
                        wp = pp[random.nextInt(NUM_POPULAR_PLACES - 1)];
                }
            }
            
            else
            {
                if(try_event(NON_INFECTED_HOSPITAL_PROBABILITY))
                    wp = pp[NUM_POPULAR_PLACES - 1];
                else
                    wp = pp[random.nextInt(NUM_POPULAR_PLACES - 1)];
            }
        }
    }
    
}