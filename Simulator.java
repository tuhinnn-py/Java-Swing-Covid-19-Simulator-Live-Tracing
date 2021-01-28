package simulator;

import java.io.*;
import java.util.*;
import java.lang.*;

public class Simulator 
{
    public static final int X_SIZE = 700;  
    public static final int Y_SIZE = 700;
    
    public static final double CLOSENESS_FACTOR = 0.01;
    public static final int SIM_HOURS = 7000;
    
    public static final int POPULATION = 22000;
    public static final String[] status = {"VULNERABLE", "INFECTED", "IMMUNE", "DEAD"};
    
    public static final int INFECTION_TIME = 24 * 20;
    public static final double INFECTION_PROBABILITY = 0.6;
    
    public static final double NORMAL_FATALITY_RATE = 0.01;
    public static final double SATURATED_FATALITY_RATE = 0.06;
    
    public static final double INFECTION_PROXIMITY = 6.0;
    public static final int INITIAL_INFECTIONS = 2;
    
    public static final int SATURATION_THRESHOLD = POPULATION / 3;
    public static final int NUM_POPULAR_PLACES = 15;
    
    public static double DISTANCING_PROBABILITY = 0.8;
    public static final double NON_SATURATED_HOSPITAL_PROBABILITY = 1.0;  
    
    public static final double SATURATED_HOSPITAL_PROBABILITY = 0.1;
    public static final double NON_INFECTED_HOSPITAL_PROBABILITY = 0.01;
    
    public static final double DISTANCING_HOME_PROBABILITY = 0.997;
    public static final double NOT_DISTANCING_HOME_PROBABILITY = 0.3;
    
    public static boolean saturated = false;
    public static final int topSpeed = 20;
    public static final int OFFSET = 2 * 24;
    
    public static final int maxStay = 5;
    public static int maxInfections = INITIAL_INFECTIONS;
    
    public static int num_vulnerable = POPULATION - INITIAL_INFECTIONS;
    public static int num_infected = INITIAL_INFECTIONS;
    public static int num_immune = 0;
    public static int num_dead = 0;
    
    public static boolean pause = false;
    public static Location popularPlaces[];
    public static String places[];
    
    public static boolean try_event(double probability)
    {
        
        Random random = new Random();
        double bucket = random.nextDouble();
        
        return probability >= bucket;
    }
    
    public static void main(String[] args) 
    {
        Person[] people = new Person[POPULATION];
        
        int[] infectionHistory = new int[SIM_HOURS];
        popularPlaces = new Location[NUM_POPULAR_PLACES];
        
        Arrays.fill(infectionHistory, 0);
        
        for(int i = 0; i < NUM_POPULAR_PLACES; i++)
            popularPlaces[i] = new Location(0.9);
        
        places = new String[]{"Library", "Market", "Museum", "Park", "Gym", "Zoo", "College", "Bank", "Office", "Mall", "Salon", "Stadium", "Airport", "Restaurant", "Hospital"};
        
        for(int i = 0; i < POPULATION; i++)
        {
            people[i] = new Person(new Location(), popularPlaces);
            
            if(i < INITIAL_INFECTIONS)
                people[i].setState(status[1]);
        }
        
        SFrame ui = new SFrame(people, infectionHistory);
        while(!ui.spanel.selected[0]){
            //infinite loop for initial user selection
        }
        
        for(int i = 0; i < SIM_HOURS; i++)
        {
            ui.spanel.repaint();
            ui.gpanel.init_length(i);
            ui.gpanel.repaint();
            ui.dpanel.repaint();
            
            while(pause){
                for(int j = 0; j < POPULATION; j++)
                    people[j].mobilityModel.initialize_distancing_probability();
                ui.spanel.repaint();
                ui.dpanel.repaint();
            }
            
            for(int j = 0; j < POPULATION; j++)
                if(people[j].isAlive())
                {
                    people[j].mobilityModel.move();
                    people[j].progressDisease();
                }
            
            for(int j = 0; j < POPULATION; j++)
                if(people[j].isAlive())
                    for(int k = 0; k < POPULATION; k++)
                        if(j != k && people[k].isAlive())
                            people[j].try_infect(people[k]);
            
            num_vulnerable = 0;
            num_infected = 0;
            num_immune = 0;
            num_dead = 0;
            
            for(int j = 0; j < POPULATION; j++)
            {
                if(!people[j].isAlive())
                    num_dead++;
                if(people[j].state.equals(status[0]))
                    num_vulnerable++;
                if(people[j].state.equals(status[1]))
                    num_infected++;
                if(people[j].state.equals(status[2]))
                    num_immune++;
            }
            
            maxInfections = Math.max(maxInfections, num_infected);
            saturated = (num_infected > SATURATION_THRESHOLD);
            infectionHistory[i] = num_infected;
        }
    } 
}
