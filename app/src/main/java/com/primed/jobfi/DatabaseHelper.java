package com.primed.jobfi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 2;

    // User table and column names
    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_EXPERIENCE = "experience";
    public static final String COLUMN_SALARY = "salary";
    public static final String COLUMN_YEAR_OF_GRADUATION = "year_of_graduation";
    public static final String COLUMN_TOKEN = "token";

    // Field of Study table and column names
    public static final String TABLE_FIELD_OF_STUDY = "field_of_study";
    public static final String COLUMN_MAJOR_ID = "major_id";
    public static final String COLUMN_MAJOR = "major";

    // SQL command to create the user table
    private static final String TABLE_USER_CREATE =
	"CREATE TABLE " + TABLE_USER + " (" +
	COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	COLUMN_NAME + " TEXT, " +
	COLUMN_EMAIL + " TEXT, " +
	COLUMN_EXPERIENCE + " TEXT, " +
	COLUMN_SALARY + " TEXT, " +
	COLUMN_YEAR_OF_GRADUATION + " TEXT, " +
	COLUMN_TOKEN + " TEXT);";

    // SQL command to create the field of study table
    private static final String TABLE_FIELD_OF_STUDY_CREATE =
	"CREATE TABLE " + TABLE_FIELD_OF_STUDY + " (" +
	COLUMN_MAJOR_ID + " INTEGER, " +
	COLUMN_MAJOR + " TEXT, " +
	"PRIMARY KEY (" + COLUMN_MAJOR_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USER_CREATE);
        db.execSQL(TABLE_FIELD_OF_STUDY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD_OF_STUDY);
        onCreate(db);
    }

    // Method to truncate a table
    public void truncateTable(String tableName) {
        SQLiteDatabase db = getWritableDatabase();

        // Delete all rows from the table
        db.execSQL("DELETE FROM " + tableName);

        // Reset auto-increment counter (if the table has an auto-increment column)
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + tableName + "'");
		
        db.close();
    }
}
