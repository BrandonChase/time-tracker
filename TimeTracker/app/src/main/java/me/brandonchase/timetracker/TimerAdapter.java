package me.brandonchase.timetracker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Brandon on 3/24/2018.
 */

public class TimerAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    TimeManager timeManager;
    ArrayList<Integer> ids;
    Context context;

    public TimerAdapter(Context c,  ArrayList<Integer> ids, TimeManager timeManager) {
        this.timeManager = timeManager;
        this.ids = ids;
        this.mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = c;
    }

    @Override
    public int getCount() {
        return ids.size();
    }

    @Override
    public Object getItem(int i) { return ids.get(i); }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflater.inflate(R.layout.timers_listview_detail, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView timeTextView = (TextView) v.findViewById(R.id.timeTextView);
        ProgressBar timeProgressBar = (ProgressBar) v.findViewById(R.id.timeProgressBar);

        int id = ids.get(i);



        nameTextView.setText(timeManager.getName(id));
        timeTextView.setText(TimeManager.timeToString(timeManager.getTime(id)) + " / " + TimeManager.timeToString(timeManager.getTop(id)));

        //Setup Progress Bar
        timeProgressBar.setMin(0);
        timeProgressBar.setMax(timeManager.getTop(id));
        timeProgressBar.setProgress(timeManager.getTime(id));

        //Set coloring
        if(timeManager.getDirection(id) == 1) { //good habit
            timeProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            timeTextView.setTextColor(Color.GREEN);
        } else {
            timeProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
            timeTextView.setTextColor(Color.RED);
        }

        //Handle good habit that exceeds goal
        if(timeManager.getDirection(id) == 1 && timeManager.getTime(id) >= timeManager.getTop(id)) {
            v.setBackgroundColor(Color.argb(128, 0, 255, 0));
            timeTextView.setTextColor(Color.BLACK);
            nameTextView.setTextColor(Color.BLACK);
        }

        //Handle bad habit that exceeds goal
        if(timeManager.getDirection(id) == 0 && timeManager.getTime(id) <= 0) {
            v.setBackgroundColor(Color.argb(128, 255, 0, 0));
            timeTextView.setTextColor(Color.BLACK);
            nameTextView.setTextColor(Color.BLACK);


        }

        if (timeManager.getIsPaused(id)) {
            timeTextView.setTextColor(Color.GRAY);
        }

        return v;
    }
}
