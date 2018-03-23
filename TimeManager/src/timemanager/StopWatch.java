package timemanager;

/*
Written by Andrew Conroy
StopWatch.java
*/

class StopWatch
{
    public String name;
    public int direction;//0 decrements, 1 increments
    public int bottom;
    public int top;
    public boolean isPaused;
    public int id;
    public int time;
    
    public StopWatch(String n, int d, int b, int t, int i)
    {
        name = n; direction = d; id = i;
        bottom = b; top = t; isPaused = false;
        
        if(direction == 0)
            time = top;
        else
            time = 0;
    }
    //start or stop the stopwatch
    public void switchPaused()
    {
        isPaused = !isPaused;
    }
    //increment the timer
    public void tick()
    {
      if(isPaused == false)
      {
        if(direction == 1)
            time++;
        else if(direction == 0)
            time--;
      }
    }
    
    //full suite of accessors
    public int getDirection()
    {return direction;}
    public boolean getIsPaused()
    {return isPaused;}
    public int getID()
    {return id;}
    public String getName()
    {return name;}
    public int getBottom()
    {return bottom;}
    public int getTop()
    {return top;}
    public int getTime()
    {return time;}
    
    //full suite of mutators, please do not use setIsPaused, use switchPaused() instead
    public void setDirection(int d)
    {direction = d;}
    public void setIsPaused(boolean p)
    {isPaused = p;}
    public void setID(int i)
    {id = i;}
    public void setName(String n)
    {name = n;}
    public void setBottom(int b)
    {bottom = b;}
    public void setTop(int to)
    {top = to;}
    public void setTime(int ti)
    {time = ti;}
}