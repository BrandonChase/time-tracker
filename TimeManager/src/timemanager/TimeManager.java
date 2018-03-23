package timemanager;
import java.util.*;
/**
 * Project 3, software project management
 * @author Andrew
 */
public class TimeManager
{
    ArrayList<StopWatch> watchList; 
    int id;
    //create our list and id counter
    public TimeManager()
    {
        watchList = new ArrayList<>();
        id = 0;
    }
    //ACCESSORS//
    //get time of stopwatch based on ID
    public int getTime(int i)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            return s.getTime();
        }
        return -1;
    }
    //get name of stopwatch based on ID
    public String getName(int i)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            return s.getName();
        }
        return "NULL";
    }
    public int getTop(int i)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            return s.getTop();
        }
        return -1;
    }
    public int getBottom(int i)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            return s.getBottom();
        }
        return -1;
    }
    public int getDirection(int i)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            return s.getDirection();
        }
        return -1;
    }
    public boolean getIsPaused(int i)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            return s.getIsPaused();
        }
        return true;
    }
    //given a name, find the ID of the stopwatch
    public int getID(String n)
    {
        for(StopWatch s: watchList)
        {
            if(s.getName().equals(n))
                return s.getID();
        }
        return -1;
    }
    //MUTATORS//
    //Set new time
    public int setTime(int i, int d)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            s.setDirection(d);
            return 1;
        }
        return -1;
    }
    //set new name
    public int setName(int i, String n)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            s.setName(n);
            return 1;
        }
        return -1;
    }
    //set new top point
    public int setTop(int i, int t)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            s.setTop(t);
            return 1;
        }
        return -1;
    }
    //set new bottom point
    public int setBottom(int i, int b)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            s.setBottom(b);
            return 1;
        }
        return -1;
    }
    //Change direction of a stopwatch
    public int setDirection(int i, int d)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            s.setDirection(d);
            return 1;
        }
        return -1;
    }
    //start/stop a stopwatch
    public int ToggleStopWatch(int i)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            s.switchPaused();
            return 1;
        }
        return -1;
    }
    //ADD/REMOVE/TICK STOPWATCHES//
    //Add a stopwatch to the list
    public int addStopWatch(String n, int d, int b, int t)
    {
        watchList.add(new StopWatch(n, d, b, t, ++id));
        return id;
    }
    //remove a stopwatch based on its ID
    public int removeStopWatch(int i)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            watchList.remove(s);
            return 1;
        }
        return -1;
    }
    public int tickWatch(int i)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            s.tick();
            return s.getTime();
        }
        return -1;
    }
    public int tickAllWatch()
    {
        watchList.forEach((s) -> {s.tick();});
        return 1;
    }
    //SEARCH FUNCTION//
    //get the stopwatch of a specific id
    private StopWatch getStopWatch(int i)
    {
        for(StopWatch s: watchList)
        {
            if(s.getID() == i)
                return s;
        }
        return null;
    }
}
