package com.amr_medhat.dayplanner.viewHolders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.amr_medhat.dayplanner.R;
import com.amr_medhat.dayplanner.listeners.OnItemClickListener;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ArrayList<LocalDate> days;
    public final View parentView;
    public final TextView dayOfMonth;
    private final OnItemClickListener onItemClickListener;

    public CalendarViewHolder(@NonNull View itemView, ArrayList<LocalDate> days, OnItemClickListener onItemClickListener) {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemClickListener = onItemClickListener;
        itemView.setOnClickListener(this);
        this.days = days;
    }

    @Override
    public void onClick(View v) {
        onItemClickListener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
    }
}
