package com.example.mark.smi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//This class is responsible for creating the database
public class SqlController extends SQLiteOpenHelper {
    public static final String TABLE_TRACKS = "tracks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COMMENTS = "comments";
    public static final String COLUMN_DATE = "date";

    public static final String TABLE_POINTS = "points";
    public static final String COLUMN_POINT_ID = "_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_TRACK_ID = "track_id";
    public static final String COLUMN_TIME = "time";

    public static final String TABLE_POLEN = "polen";
    public static final String COLUMN_POLEN_ID = "_id";
    public static final String COLUMN_POLEN_NAME = "name";
    public static final String COLUMN_DESC = "description";

    private static final String DATABASE_NAME = "smi.db";
    private static final int DATABASE_VERSION = 3;

    // Database creation sql statement
    private static final String DATABASE_CREATE_1 = "create table "
            + TABLE_TRACKS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_COMMENTS
            + ", " + COLUMN_DATE + " text not null );";

    private static final String DATABASE_CREATE_2 = "create table "
            + TABLE_POINTS + "(" + COLUMN_POINT_ID
            + " integer primary key autoincrement, " + COLUMN_LATITUDE
            + " text not null, " + COLUMN_LONGITUDE
            + " text not null, " + COLUMN_TRACK_ID
            + " text not null, " + COLUMN_TIME
            + " text not null );";

    private static final String DATABASE_CREATE_3 = "create table "
            + TABLE_POLEN + "(" + COLUMN_POLEN_ID
            + " integer primary key autoincrement, " + COLUMN_POLEN_NAME
            + " text not null, " + COLUMN_COMMENTS + " );";

    public SqlController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE_1);
        database.execSQL(DATABASE_CREATE_2);
        database.execSQL(DATABASE_CREATE_3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SqlController.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLEN);
        onCreate(db);
    }
}
