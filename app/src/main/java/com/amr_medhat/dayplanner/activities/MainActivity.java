package com.amr_medhat.dayplanner.activities;

import static com.amr_medhat.dayplanner.utilities.Utils.daysInWeekArray;
import static com.amr_medhat.dayplanner.utilities.Utils.monthYearFromDate;
import static com.amr_medhat.dayplanner.utilities.Utils.selectedDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.amr_medhat.dayplanner.adapters.CalendarAdapter;
import com.amr_medhat.dayplanner.adapters.EventAdapter;
import com.amr_medhat.dayplanner.database.PreferenceManager;
import com.amr_medhat.dayplanner.database.SQLiteDataManager;
import com.amr_medhat.dayplanner.databinding.ActivityMainBinding;
import com.amr_medhat.dayplanner.listeners.OnItemClickListener;
import com.amr_medhat.dayplanner.models.Event;
import com.amr_medhat.dayplanner.utilities.Constants;
import com.amr_medhat.dayplanner.utilities.Utils;

import java.time.LocalDate;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    ActivityMainBinding activityMainBinding;
    String username;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        preferenceManager = new PreferenceManager(this);
        username = preferenceManager.getString(Constants.KEY_USERNAME);
        activityMainBinding.tvUsername.setText(username);
        Utils.selectedDate = LocalDate.now();
        loadFromDBToMemory();
        setListeners();
        setWeekView();

    }


    private void loadFromDBToMemory() {
        SQLiteDataManager sqLiteDataManager = SQLiteDataManager.instanceOfDatabase(this);
        sqLiteDataManager.populateEventListArray();
    }


    private void setListeners() {
        activityMainBinding.btnFullCalendar.setOnClickListener(v -> {
            startActivity(new Intent(this, FullCalendarActivity.class));
        });

        activityMainBinding.btnPreviousWeek.setOnClickListener(v -> {
            Utils.selectedDate = Utils.selectedDate.minusWeeks(1);
            setWeekView();
        });

        activityMainBinding.btnNextWeek.setOnClickListener(v -> {
            Utils.selectedDate = Utils.selectedDate.plusWeeks(1);
            setWeekView();
        });

        activityMainBinding.btnSetNewEvent.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EventEditActivity.class);
            intent.putExtra(Constants.KEY_USERNAME, username);
            startActivity(intent);
        });
        activityMainBinding.btnShowAllEvents.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AllEventsActivity.class);
            intent.putExtra(Constants.KEY_USERNAME, username);
            startActivity(intent);
        });
        activityMainBinding.eventsListView.setOnItemClickListener((parent, view, position, id) -> {
            Event selectedEvent = (Event) activityMainBinding.eventsListView.getItemAtPosition(position);
            Intent editEventIntent = new Intent(getApplicationContext(), EventEditActivity.class);
            editEventIntent.putExtra(Event.EVENT_EDIT_EXTRA, selectedEvent.getId());
            startActivity(editEventIntent);
        });
        activityMainBinding.btnLogOut.setOnClickListener(v -> {
            signOut();
        });
    }

    private void setWeekView() {
        activityMainBinding.tvMonthYear.setText(monthYearFromDate(Utils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(Utils.selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        activityMainBinding.calenderRecyclerView.setLayoutManager(layoutManager);
        activityMainBinding.calenderRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            Utils.selectedDate = date;
            setWeekView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        ArrayList<Event> dailyEvents = Event.eventsForDate(selectedDate, username);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        activityMainBinding.eventsListView.setAdapter(eventAdapter);
    }




    private void signOut() {
        preferenceManager.clear();
        startActivity(new Intent(getApplicationContext(), SplashScreenActivity.class));
        finish();
    }

}