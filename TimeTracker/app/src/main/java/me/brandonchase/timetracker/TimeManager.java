package me.brandonchase.timetracker;

/**
 * Created by Brandon on 3/24/2018.
 */

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

    //useful relevant methods - Brandon
    //Converts number (in seconds) to String format of "hh:mm:ss"
    public static String timeToString(int n) {
        String result = "";
        if(n < 0) {
            n *= -1;
            result += "-";
        } //if leave it negative string will look like "-2:-2:-29". don't want this, let caller handle negative times (which occur when bad habit timer goes over limit)

        //parse hours
        int hours = n / 3600;
        n %= 3600;
        int minutes = n / 60;
        n %= 60;
        int seconds = n;

        //put items together
        result += String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return result;

    }

    //Gets number (in seconds) from Strings representing number of hours, minutes, and seconds
    public static int stringToTime(String hours, String minutes, String seconds) {
        int result = 0;

        if(!hours.equals("")) { result += 3600 * Integer.parseInt(hours); }
        if(!minutes.equals("")) { result += 60 * Integer.parseInt(minutes); }
        if(!seconds.equals("")) { result += Integer.parseInt(seconds); }

        return result;
    }

    //Converts from String representation of habit type to integer format used in back end. 1=Good, 0=Bad
    public static int stringToHabit(String habitType) {
        if(habitType.equals("Good")) {
            return 1;
        } else {
            return 0;
        }
    }

    //Converts from integer representation of habit type to String format used in front end. 1=Good, 0=Bad
    public static String habitToString(int habitNum) {
        if(habitNum == 1) {
            return "Good";
        } else {
            return "Bad";
        }
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
    public int setTime(int i, int t)
    {
        StopWatch s = getStopWatch(i);
        if(s != null)
        {
            s.setTime(t);
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
    public int toggleStopWatch(int i)
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
        for(StopWatch s : watchList) {
            s.tick();
        }
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