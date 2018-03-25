package me.brandonchase.timetracker;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //Variables
    private TimeManager timeManager;
    private ArrayList<Integer> ids;
    int selectedTimerIndex;
    private Handler handler;
    private int delay;

    //Variables for GUI elements
    //Switches
    private Switch deleteSwitch;
    private Switch editSwitch;
    //Buttons
    private Button resetBtn;
    private Button actionBtn;
    //Text Boxes
    private EditText nameEditText;
    private EditText hourEditText;
    private EditText minuteEditText;
    private EditText secondEditText;
    //Drop Down Box
    private Spinner habitSpinner;
    //Timer List View
    private ListView timersListView;

    //Setup ListView
    private TimerAdapter timerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Implementation Variables
        timeManager = new TimeManager();
        ids = new ArrayList<>();
        resetSelectedTimerIndex();

        //-----Intialize all GUI items-----
        //Switches
        deleteSwitch = findViewById(R.id.deleteSwitch);
        editSwitch = findViewById(R.id.editSwitch);
        //Buttons
        resetBtn = findViewById(R.id.resetBtn);
        actionBtn = findViewById(R.id.actionBtn);
        //Text Boxes
        nameEditText = findViewById(R.id.nameEditText);
        hourEditText = findViewById(R.id.hourEditText);
        minuteEditText = findViewById(R.id.minuteEditText);
        secondEditText = findViewById(R.id.secondEditText);
        //Drop Down Box
        habitSpinner = findViewById(R.id.habitSpinner);
        //Timer List View
        timersListView = findViewById(R.id.timersListView);

        //Setup ListView
        final TimerAdapter timerAdapter = new TimerAdapter(this, ids, timeManager);
        timersListView.setAdapter(timerAdapter);

        //-----Logic for handling events-----
        //---Button Presses---
        //Reset Timers Button
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int id : ids) {
                    //reset to starting value
                    int direction = timeManager.getDirection(id);
                    if(direction == 0) { //count down, bad
                        timeManager.setTime(id, timeManager.getTop(id));
                    } else { //count up, good
                        timeManager.setTime(id, 0);
                    }

                    //reset to paused state
                    if(!timeManager.getIsPaused(id)) { //toggle timer if it not paused to make it paused
                        timeManager.toggleStopWatch(id);
                    }
                }
            }
        });

        //Action Button
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editSwitch.isChecked()) { //EDIT MODE
                    if(selectedTimerIndexIsValid()) {
                        //get info for timer
                        String name = nameEditText.getText().toString();
                        if(name.length() <= 16) {
                            int goal = TimeManager.stringToTime(hourEditText.getText().toString(), minuteEditText.getText().toString(), secondEditText.getText().toString());
                            int habitType = TimeManager.stringToHabit(habitSpinner.getSelectedItem().toString());

                            //manually override all values even if not changed (quicker than tracking which changed and only updating that one)
                            int id = ids.get(selectedTimerIndex);

                            int time = timeManager.getTime(id);

                            int elapsedTime;
                            if(timeManager.getDirection(id) == 0) { //bad timer counts down
                                elapsedTime = timeManager.getTop(id) - time;
                            } else {
                                elapsedTime = time;
                            }

                            if(habitType == 0) {
                                time = goal - elapsedTime;
                            } else {
                                time = elapsedTime;
                            }

                            timeManager.setName(id, name);
                            timeManager.setTop(id, goal);
                            timeManager.setDirection(id, habitType);
                            timeManager.setTime(id, time);
                        } else { //Name too long
                            Toast error = Toast.makeText(getApplicationContext(), "Name must be no more than 16 characters!", Toast.LENGTH_SHORT);
                            error.show();
                        }
                    }

                } else if(deleteSwitch.isChecked()) { //DELETE MODE
                    if(selectedTimerIndexIsValid()) {
                        int id = ids.get(selectedTimerIndex);
                        timeManager.removeStopWatch(id);
                        ids.remove(selectedTimerIndex);
                        resetInputs();
                        resetSelectedTimerIndex();
                    }
                } else { //ADD MODE

                    if (hourEditText.getText().toString().equals("") && minuteEditText.getText().toString().equals("") && secondEditText.getText().toString().equals("")) {
                        Toast error = Toast.makeText(getApplicationContext(), "Must enter some time goal for the timer!", Toast.LENGTH_SHORT);
                        error.show();
                    } else{ //at least one time value box has a value
                        try {
                            //Get info to create timer
                            String name = nameEditText.getText().toString();

                            if(name.length() <= 16) {
                                int goal = TimeManager.stringToTime(hourEditText.getText().toString(), minuteEditText.getText().toString(), secondEditText.getText().toString());
                                int habitType = TimeManager.stringToHabit(habitSpinner.getSelectedItem().toString());

                                ids.add(timeManager.addStopWatch(name, habitType, 0, goal));
                                resetInputs();
                            } else { //Name too long
                                Toast error = Toast.makeText(getApplicationContext(), "Name must be no more than 16 characters!", Toast.LENGTH_SHORT);
                                error.show();
                            }

                        } catch (Exception e) {
                            Toast error = Toast.makeText(getApplicationContext(), "ERROR OCCURRED WHILE CREATING TIMER", Toast.LENGTH_SHORT);
                            error.show();
                        }
                    }
                }
            }
        });

        //---Switch Presses---
        //Delete Timer Switch
        deleteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    //disable edit switch if it is on
                    editSwitch.setChecked(false);
                    //change action button to delete button
                    setActionDelete();
                    //disable timer attribute edit texts since not changing any fields, just viewing them to see which timer will be deleted
                    resetInputs();
                    disableInputs();

                    resetSelectedTimerIndex();
                } else {
                    //reset action button to add button
                    setActionAdd();
                    //enable timer attribute edit texts since not changing any fields, just viewing them to see which timer will be delete
                    enableInputs();

                    resetSelectedTimerIndex();
                }
            }
        });

        //Edit Timer Switch
        editSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    //disable delete switch if it is on
                    deleteSwitch.setChecked(false);
                    //change action button to edit button
                    setActionEdit();
                    resetInputs();

                    resetSelectedTimerIndex();
                } else {
                    setActionAdd();
                    resetInputs();

                    resetSelectedTimerIndex();
                }
            }
        });

        //Timer in List Clicked
        timersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(editSwitch.isChecked() || deleteSwitch.isChecked()) { //EDIT MODE
                    selectedTimerIndex = i;
                    fillInputs();
                } else { //NORMAL MODE: Just pause/resume timer
                    timeManager.toggleStopWatch(ids.get(i)); //pass id of timer, which is stored in list whose indexes match the list displayed to user
                }
            }
        });

        //-----Logic Performed Every Second-----
        handler = new Handler();
        delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Update timers
                timeManager.tickAllWatch();

                //Check for any completed timers
                for (int id : ids) {
                    if (timeManager.getDirection(id) == 1 && timeManager.getTime(id) == timeManager.getTop(id)) {
                        showAlert(id, "'s goal has been met. KEEP ON ROCKING!");
                        timersListView.smoothScrollToPosition(ids.indexOf(id));
                    }

                    if (timeManager.getDirection(id) == 0 && timeManager.getTime(id) == 0) {
                        showAlert(id, "'s goal has been met. STOP NOW FOR YOUR OWN GOOD!");
                        timersListView.smoothScrollToPosition(ids.indexOf(id));
                    }
                }

                //Refresh list of timers
                ((BaseAdapter) timersListView.getAdapter()).notifyDataSetChanged();

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    //
    //
    //
    //-----Helper functions that reduce GUI code reuse for common operations-----
    //
    //
    //

    private void enableInputs() {
        nameEditText.setEnabled(true);
        hourEditText.setEnabled(true);
        minuteEditText.setEnabled(true);
        secondEditText.setEnabled(true);
        habitSpinner.setEnabled(true);
    }

    private void disableInputs() {
        nameEditText.setEnabled(false);
        hourEditText.setEnabled(false);
        minuteEditText.setEnabled(false);
        secondEditText.setEnabled(false);
        habitSpinner.setEnabled(false);
    }

    //-----Helper functions that reduce GUI code reuse for common operations-----
    private void setActionAdd() {
        actionBtn.setText("ADD");
        actionBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_green_dark)));
    }

    private void setActionDelete() {
        actionBtn.setText("DELETE");
        actionBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
    }

    private void setActionEdit() {
        actionBtn.setText("EDIT");
        actionBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_blue_dark)));
    }

    private void resetInputs() {
        nameEditText.getText().clear();
        hourEditText.getText().clear();
        minuteEditText.getText().clear();
        secondEditText.getText().clear();
        habitSpinner.setSelection(0);
    }

    private void resetSelectedTimerIndex() { selectedTimerIndex = -1; }
    private boolean selectedTimerIndexIsValid() { return (selectedTimerIndex != -1); }

    private void fillInputs() {
        if(selectedTimerIndexIsValid()) {
            int id = ids.get(selectedTimerIndex);
            nameEditText.setText(timeManager.getName(id));
            String time = TimeManager.timeToString(timeManager.getTop(id)); //top is goal time
            String[] items = time.split(":"); //time is in form "hh:mm:ss"
            hourEditText.setText(items[0]);
            minuteEditText.setText(items[1]);
            secondEditText.setText(items[2]);
            habitSpinner.setSelection(1 - timeManager.getDirection(id)); //work around since index 0 is Good and 1 is Bad but direction 0 is down (Bad) and 1 is up (Good). (1 - val) handles swapping the values; 1 becomes 0, 0 becomes 1
        }
    }

    private void showAlert(int id, String message) {
        Toast alert = Toast.makeText(this, timeManager.getName(id) + message, Toast.LENGTH_LONG);
        alert.show();
    }
}
