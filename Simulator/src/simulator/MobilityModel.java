package simulator;
public interface MobilityModel
{
    public abstract void move();
    public abstract void pick_new_waypoint();
}