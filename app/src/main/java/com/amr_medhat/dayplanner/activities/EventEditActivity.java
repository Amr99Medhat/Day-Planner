package com.amr_medhat.dayplanner.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import com.amr_medhat.dayplanner.database.SQLiteDataManager;
import com.amr_medhat.dayplanner.databinding.ActivityEventEditBinding;
import com.amr_medhat.dayplanner.models.Date;
import com.amr_medhat.dayplanner.models.Event;
import com.amr_medhat.dayplanner.models.Time;
import com.amr_medhat.dayplanner.utilities.Constants;
import com.amr_medhat.dayplanner.utilities.Utils;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class EventEditActivity extends AppCompatActivity {
    ActivityEventEditBinding activityEventEditBinding;
    String username;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    int day, month, year;
    LocalTime localTime;
    int hour, minute;
    String am_pm_format;
    private Event selectedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEventEditBinding = ActivityEventEditBinding.inflate(getLayoutInflater());
        setContentView(activityEventEditBinding.getRoot());
        username = getIntent().getStringExtra(Constants.KEY_USERNAME);
        localTime = LocalTime.now();
        setListeners();
        initDatePiker();
        initTimePiker();
        setDateAndTime();
        checkForEditEvent();
    }

    @SuppressLint("SetTextI18n")
    private void checkForEditEvent() {
        Intent previousIntent = getIntent();

        int passedEventID = previousIntent.getIntExtra(Event.EVENT_EDIT_EXTRA, -1);
        selectedEvent = Event.getEventForID(passedEventID);

        if (selectedEvent != null) {
            activityEventEditBinding.tvHeader.setText("Edit Event");
            activityEventEditBinding.edEventTitle.setText(selectedEvent.getTitle());
            activityEventEditBinding.edEventDescription.setText(selectedEvent.getDescription());

        } else {
            activityEventEditBinding.btnDeleteEvent.setVisibility(View.INVISIBLE);
        }

    }

    @SuppressLint("SetTextI18n")
    private void setDateAndTime() {
        activityEventEditBinding.tvEventCurrentDate.setText("Event Selection Date: " + Utils.formattedDate(Utils.selectedDate));
        activityEventEditBinding.tvEventCurrentTime.setText("Event Selection time: " + Utils.formattedTime(localTime));
    }

    private void setListeners() {


        activityEventEditBinding.btnEventSelectDate.setOnClickListener(v -> {
            datePickerDialog.show();
        });
        activityEventEditBinding.btnEventSelectTime.setOnClickListener(v -> {
            timePickerDialog.show();
        });

        activityEventEditBinding.btnSaveEvent.setOnClickListener(v -> {
            if (isValidInputs()) {
                SQLiteDataManager sqLiteDataManager = SQLiteDataManager.instanceOfDatabase(this);
                String eventTitle = Objects.requireNonNull(activityEventEditBinding.edEventTitle.getText()).toString();
                String eventDescription = Objects.requireNonNull(activityEventEditBinding.edEventDescription.getText()).toString();
                Date date = new Date(day, month, year);
                Time time = new Time(hour, minute, am_pm_format);
                if (selectedEvent == null) {
                    int id = Event.getEventsList().size();
                    Event newEvent = new Event(id, eventTitle, eventDescription, date, time, Utils.selectedDate, localTime, username);
                    sqLiteDataManager.addEventToDatabase(newEvent);
                    Event.eventsList.add(newEvent);
                } else {
                    selectedEvent.setTitle(eventTitle);
                    selectedEvent.setDescription(eventDescription);
                    selectedEvent.setDate(date);
                    selectedEvent.setTime(time);
                    selectedEvent.setCurrentDate(Utils.selectedDate);
                    selectedEvent.setCurrentTime(localTime);

                    sqLiteDataManager.updateEventInDB(selectedEvent);

                }
                finish();
            }

        });
        activityEventEditBinding.btnDeleteEvent.setOnClickListener(v -> {
            SQLiteDataManager sqLiteDataManager = SQLiteDataManager.instanceOfDatabase(this);
            Boolean res = sqLiteDataManager.deleteEventInDB(selectedEvent.getId());
            if (res != null) {
                Event.eventsList.remove(selectedEvent.getId());
                finish();
            }

        });
    }

    private void initDatePiker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int currentYear, int currentMonth, int currentDay) {
                currentMonth = currentMonth + 1;
                String date = Utils.makeDateString(currentDay, currentMonth, currentYear);
                activityEventEditBinding.btnEventSelectDate.setText(date);
                day = currentDay;
                month = currentMonth;
                year = currentYear;
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }


    private void initTimePiker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            //get time 12 hour view.
            //get if time is am or pm.
            if (hour == 0) {
                hour += 12;
                am_pm_format = "AM";
            } else if (hour == 12) {
                am_pm_format = "PM";
            } else if (hour > 12) {
                hour -= 12;
                am_pm_format = "PM";
            } else {
                am_pm_format = "AM";
            }
            activityEventEditBinding.btnEventSelectTime.setText(String.format(Locale.getDefault(), "%02d:%02d " + am_pm_format, hour, minute));
        };

        timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
        timePickerDialog.setTitle("Select Time");
    }

    private boolean isValidInputs() {
        if (Objects.requireNonNull(activityEventEditBinding.edEventTitle.getText()).toString().equals("")) {
            activityEventEditBinding.edEventTitle.setError("Enter title");
            activityEventEditBinding.edEventTitle.requestFocus();
            return false;
        }
        if (Objects.requireNonNull(activityEventEditBinding.edEventDescription.getText()).toString().equals("")) {
            activityEventEditBinding.edEventDescription.setError("Enter description");
            activityEventEditBinding.edEventDescription.requestFocus();
            return false;
        }
        if (Objects.requireNonNull(activityEventEditBinding.btnEventSelectDate.getText()).toString().equals("")) {
            activityEventEditBinding.btnEventSelectDate.setError("Select date");
            activityEventEditBinding.btnEventSelectDate.requestFocus();
            return false;
        }
        if (Objects.requireNonNull(activityEventEditBinding.btnEventSelectTime.getText()).toString().equals("")) {
            activityEventEditBinding.btnEventSelectTime.setError("Select date");
            activityEventEditBinding.btnEventSelectTime.requestFocus();
            return false;
        }
        return true;
    }
}