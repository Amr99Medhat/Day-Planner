package com.amr_medhat.dayplanner.listeners;

import java.time.LocalDate;

public interface OnItemClickListener {
    void onItemClick(int position, LocalDate date);
}
