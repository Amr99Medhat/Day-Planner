package com.amr_medhat.dayplanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteRegistrationManager extends SQLiteOpenHelper {
    private static SQLiteRegistrationManager sqLiteRegistrationManager;
    private static final String DATABASE_NAME = "RegistrationDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Registrations";
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private static final String VERIFIED_CODE_FIELD = "verifiedCode";

    public SQLiteRegistrationManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteRegistrationManager instanceOfDatabase(Context context) {
        if (sqLiteRegistrationManager == null) {
            sqLiteRegistrationManager = new SQLiteRegistrationManager(context);
        }
        return sqLiteRegistrationManager;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql;

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(USERNAME_FIELD)
                .append(" TEXT PRIMARY KEY, ")
                .append(PASSWORD_FIELD)
                .append(" TEXT, ")
                .append(VERIFIED_CODE_FIELD)
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

    public Boolean addUser(String username, String password, String verifiedCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USERNAME_FIELD, username);
        values.put(PASSWORD_FIELD, password);
        values.put(VERIFIED_CODE_FIELD, verifiedCode);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public Boolean checkUserName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + USERNAME_FIELD + " =?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public Boolean checkUserVerificationCode(String username ,String verificationCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + USERNAME_FIELD + " =?" + " and " + VERIFIED_CODE_FIELD + " =?", new String[]{username, verificationCode});

        return cursor.getCount() > 0;
    }

    public Boolean checkUserNameAndPassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + USERNAME_FIELD + " =?" + " and " + PASSWORD_FIELD + " =?", new String[]{username, password});
        return cursor.getCount() > 0;
    }

    public int updatePassword(String username, String password, String verifiedCode) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME_FIELD, username);
        contentValues.put(PASSWORD_FIELD, password);
        contentValues.put(VERIFIED_CODE_FIELD, verifiedCode);

        return sqLiteDatabase.update(TABLE_NAME, contentValues, USERNAME_FIELD + " =?", new String[]{String.valueOf(username)});
    }

}
