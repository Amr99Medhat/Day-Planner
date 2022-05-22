package com.amr_medhat.dayplanner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.amr_medhat.dayplanner.R;
import com.amr_medhat.dayplanner.models.Event;
import com.amr_medhat.dayplanner.utilities.Utils;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0, events);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Event event = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.short_event_cell, parent, false);
        }
        TextView eventCellTitle = convertView.findViewById(R.id.tv_eventCellTitle);
        TextView eventCellDate = convertView.findViewById(R.id.tv_eventCellDate);
        TextView eventCellTime = convertView.findViewById(R.id.tv_eventCellTime);

        String eventTitle = event.getTitle();
        String eventDate = Utils.makeDateString(event.getDate().getDay(), event.getDate().getMonth(), event.getDate().getYear());
        String eventTime = (String.format(Locale.getDefault(), "%02d:%02d " + event.getTime().getAm_pm_format(), event.getTime().getHour(), event.getTime().getMinute()));

        eventCellTitle.setText(eventTitle);
        eventCellDate.setText(eventDate);
        eventCellTime.setText(eventTime);

        return convertView;
    }
}
