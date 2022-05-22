package com.amr_medhat.dayplanner.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.amr_medhat.dayplanner.R;
import com.amr_medhat.dayplanner.listeners.OnItemClickListener;
import com.amr_medhat.dayplanner.utilities.Utils;
import com.amr_medhat.dayplanner.viewHolders.CalendarViewHolder;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final ArrayList<LocalDate> days;
    private final OnItemClickListener onItemClickListener;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemClickListener onItemClickListener) {
        this.days = days;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (days.size() > 15) { //month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        } else { // week view
            layoutParams.height = (int) parent.getHeight();
        }
        return new CalendarViewHolder(view, days, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
        if (date.equals(Utils.selectedDate)) {
            holder.dayOfMonth.setTextColor(Color.BLACK);
            holder.parentView.setBackgroundColor(Color.parseColor("#f1f1f3"));
        }

        if (!date.getMonth().equals(Utils.selectedDate.getMonth())) {
            holder.dayOfMonth.setTextColor(Color.DKGRAY);
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}
