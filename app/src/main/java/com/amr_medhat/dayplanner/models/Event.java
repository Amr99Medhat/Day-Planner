package com.amr_medhat.dayplanner.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
    private final int id;
    private String title;
    private String description;
    private Date date;
    private Time time;
    private LocalDate currentDate;
    private LocalTime currentTime;
    private final String username;
    public static ArrayList<Event> eventsList = new ArrayList<>();
    public static String EVENT_EDIT_EXTRA = "editEvent";


    public Event(int id, String title, String description, Date date, Time time, LocalDate currentDate, LocalTime currentTime, String username) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.username = username;
    }

    public static Event getEventForID(int passedEventID) {
        for (Event event : eventsList) {
            if (event.getId() == passedEventID) {
                return event;
            }
        }
        return null;
    }

    public static ArrayList<Event> eventsForDate(LocalDate date, String username) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventsList) {
            if (event.getCurrentDate().equals(date)) {
                if (event.getUsername().equals(username)) {
                    events.add(event);
                }
            }
        }
        return events;
    }

    public static ArrayList<Event> eventsForUser(String username) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventsList) {
            if (event.getUsername().equals(username)) {
                events.add(event);
            }
        }
        return events;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public void setCurrentTime(LocalTime currentTime) {
        this.currentTime = currentTime;
    }


    public static ArrayList<Event> getEventsList() {
        return eventsList;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public LocalTime getCurrentTime() {
        return currentTime;
    }

    public String getUsername() {
        return username;
    }
}
