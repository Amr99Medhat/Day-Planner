package com.amr_medhat.dayplanner.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.amr_medhat.dayplanner.adapters.AllEventsAdapter;
import com.amr_medhat.dayplanner.databinding.ActivityAllEventsBinding;
import com.amr_medhat.dayplanner.models.Event;
import com.amr_medhat.dayplanner.utilities.Constants;
import java.util.ArrayList;

public class AllEventsActivity extends AppCompatActivity {
    ActivityAllEventsBinding activityAllEventsBinding;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAllEventsBinding = ActivityAllEventsBinding.inflate(getLayoutInflater());
        setContentView(activityAllEventsBinding.getRoot());
        username = getIntent().getStringExtra(Constants.KEY_USERNAME);
        setListeners();

    }

    void setListeners() {
        activityAllEventsBinding.allEventsListView.setOnItemClickListener((parent, view, position, id) -> {
            Event selectedEvent = (Event) activityAllEventsBinding.allEventsListView.getItemAtPosition(position);
            Intent editEventIntent = new Intent(getApplicationContext(), EventEditActivity.class);
            editEventIntent.putExtra(Event.EVENT_EDIT_EXTRA, selectedEvent.getId());
            startActivity(editEventIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        ArrayList<Event> allEvents = Event.eventsForUser(username);
        AllEventsAdapter eventAdapter = new AllEventsAdapter(getApplicationContext(), allEvents);
        activityAllEventsBinding.allEventsListView.setAdapter(eventAdapter);
    }
}