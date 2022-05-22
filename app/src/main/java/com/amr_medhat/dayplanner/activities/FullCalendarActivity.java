package com.amr_medhat.dayplanner.activities;

import static com.amr_medhat.dayplanner.utilities.Utils.daysInMonthArray;
import static com.amr_medhat.dayplanner.utilities.Utils.monthYearFromDate;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.amr_medhat.dayplanner.adapters.CalendarAdapter;
import com.amr_medhat.dayplanner.databinding.ActivityFullCalendarBinding;
import com.amr_medhat.dayplanner.listeners.OnItemClickListener;
import com.amr_medhat.dayplanner.utilities.Utils;
import java.time.LocalDate;
import java.util.ArrayList;

public class FullCalendarActivity extends AppCompatActivity implements OnItemClickListener {
    ActivityFullCalendarBinding activityFullCalendarBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFullCalendarBinding = ActivityFullCalendarBinding.inflate(getLayoutInflater());
        setContentView(activityFullCalendarBinding.getRoot());
        Utils.selectedDate = LocalDate.now();
        setListeners();
        setMonthView();
    }

    private void setListeners() {
        activityFullCalendarBinding.btnPreviousWeek.setOnClickListener(v -> {
            Utils.selectedDate = Utils.selectedDate.minusMonths(1);
            setMonthView();
        });

        activityFullCalendarBinding.btnNextWeek.setOnClickListener(v -> {
            Utils.selectedDate = Utils.selectedDate.plusMonths(1);
            setMonthView();
        });
    }

    private void setMonthView() {
        activityFullCalendarBinding.tvMonthYear.setText(monthYearFromDate(Utils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        activityFullCalendarBinding.calenderRecyclerView.setLayoutManager(layoutManager);
        activityFullCalendarBinding.calenderRecyclerView.setAdapter(calendarAdapter);
    }


    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            Utils.selectedDate = date;
            setMonthView();
        }
    }
}