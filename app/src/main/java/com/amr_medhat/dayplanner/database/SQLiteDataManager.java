package com.amr_medhat.dayplanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.amr_medhat.dayplanner.models.Event;
import com.amr_medhat.dayplanner.models.Time;
import java.time.LocalDate;
import java.time.LocalTime;


public class SQLiteDataManager extends SQLiteOpenHelper {

    private static SQLiteDataManager sqLiteDataManager;
    private static final String DATABASE_NAME = "EventsDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Events";
    private static final String COUNTER = "Counter";
    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String DATE_DAY_FIELD = "dateDay";
    private static final String DATE_MONTH_FIELD = "dateMonth";
    private static final String DATE_YEAR_FIELD = "dateYear";
    private static final String TIME_HOUR_FIELD = "timeHour";
    private static final String TIME_MINUTE_FIELD = "timeMinute";
    private static final String TIME_FORMAT_FIELD = "timeFormat";
    private static final String CURRENT_DATE_FIELD = "currentDate";
    private static final String CURRENT_TIME_FIELD = "currentTime";
    private static final String USERNAME_FIELD = "username";


    public SQLiteDataManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteDataManager instanceOfDatabase(Context context) {
        if (sqLiteDataManager == null) {
            sqLiteDataManager = new SQLiteDataManager(context);
        }
        return sqLiteDataManager;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql;

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(TITLE_FIELD)
                .append(" TEXT, ")
                .append(DESCRIPTION_FIELD)
                .append(" TEXT, ")
                .append(DATE_DAY_FIELD)
                .append(" INT, ")
                .append(DATE_MONTH_FIELD)
                .append(" INT, ")
                .append(DATE_YEAR_FIELD)
                .append(" INT, ")
                .append(TIME_HOUR_FIELD)
                .append(" INT, ")
                .append(TIME_MINUTE_FIELD)
                .append(" INT, ")
                .append(TIME_FORMAT_FIELD)
                .append(" TEXT, ")
                .append(CURRENT_DATE_FIELD)
                .append(" TEXT, ")
                .append(CURRENT_TIME_FIELD)
                .append(" TEXT, ")
                .append(USERNAME_FIELD)
                .append(" TEXT)");

        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        switch (oldVersion) {
//            case 1:
//                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + NEW_COLUMN + " TEXT");
//            case 2:
//                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + NEW_COLUMN + " TEXT");
//        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addEventToDatabase(Event event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID_FIELD, event.getId());
        values.put(TITLE_FIELD, event.getTitle());
        values.put(DESCRIPTION_FIELD, event.getDescription());
        values.put(DATE_DAY_FIELD, event.getDate().getDay());
        values.put(DATE_MONTH_FIELD, event.getDate().getMonth());
        values.put(DATE_YEAR_FIELD, event.getDate().getYear());
        values.put(TIME_HOUR_FIELD, event.getTime().getHour());
        values.put(TIME_MINUTE_FIELD, event.getTime().getMinute());
        values.put(TIME_FORMAT_FIELD, event.getTime().getAm_pm_format());
        values.put(CURRENT_DATE_FIELD, String.valueOf(event.getCurrentDate()));
        values.put(CURRENT_TIME_FIELD, String.valueOf(event.getCurrentTime()));
        values.put(USERNAME_FIELD, event.getUsername());


        sqLiteDatabase.insert(TABLE_NAME, null, values);
    }

    public void populateEventListArray() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(1);
                    String title = result.getString(2);
                    String description = result.getString(3);
                    int dateDay = result.getInt(4);
                    int dateMonth = result.getInt(5);
                    int dateYear = result.getInt(6);
                    int timeHour = result.getInt(7);
                    int timeMinute = result.getInt(8);
                    String timeFormat = result.getString(9);
                    String currentDate = result.getString(10);
                    String currentTime = result.getString(11);
                    String username = result.getString(12);

                    com.amr_medhat.dayplanner.models.Date date = new com.amr_medhat.dayplanner.models.Date(dateDay, dateMonth, dateYear);
                    Time time = new Time(timeHour, timeMinute, timeFormat);

                    Event event = new Event(id, title, description, date, time, LocalDate.parse(currentDate), LocalTime.parse(currentTime), username);

                    for (Event e : Event.eventsList) {
                        if (e.getId() == event.getId()) {
                            return;
                        }
                    }

                    Event.eventsList.add(event);
                }
            }
        }
    }

    public void updateEventInDB(Event event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, event.getId());
        contentValues.put(TITLE_FIELD, event.getTitle());
        contentValues.put(DESCRIPTION_FIELD, event.getDescription());
        contentValues.put(DATE_DAY_FIELD, event.getDate().getDay());
        contentValues.put(DATE_MONTH_FIELD, event.getDate().getMonth());
        contentValues.put(DATE_YEAR_FIELD, event.getDate().getYear());
        contentValues.put(TIME_HOUR_FIELD, event.getTime().getHour());
        contentValues.put(TIME_MINUTE_FIELD, event.getTime().getMinute());
        contentValues.put(TIME_FORMAT_FIELD, event.getTime().getAm_pm_format());
        contentValues.put(CURRENT_DATE_FIELD, String.valueOf(event.getCurrentDate()));
        contentValues.put(CURRENT_TIME_FIELD, String.valueOf(event.getCurrentTime()));
        contentValues.put(USERNAME_FIELD, event.getUsername());

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =?", new String[]{String.valueOf(event.getId())});
    }

    public Boolean deleteEventInDB(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, ID_FIELD + "=?", new String[]{String.valueOf(id)}) > 0;
    }

}
