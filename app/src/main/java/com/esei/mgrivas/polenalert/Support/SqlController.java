package com.esei.mgrivas.polenalert.Support;

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
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_MINUTE = "minute";

    public static final String TABLE_ZONES = "zones";
    public static final String COLUMN_ZONES_ID = "_id";
    public static final String COLUMN_ZONES_NAME = "name";
    public static final String COLUMN_ZONES_LATITUDE = "latitude";
    public static final String COLUMN_ZONES_LONGITUDE = "longitude";


    public static final String TABLE_POLEN = "polen";
    public static final String COLUMN_POLEN_ID = "_id";
    public static final String COLUMN_POLEN_NAME = "name";
    public static final String COLUMN_POLEN_MAX = "max";

    private static final String DATABASE_NAME = "smi.db";
    private static final int DATABASE_VERSION = 8;

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
            + " text not null, " + COLUMN_HOUR
            + " text not null, " + COLUMN_MINUTE
            + " text not null );";

    private static final String DATABASE_CREATE_3 = "create table "
            + TABLE_ZONES + "(" + COLUMN_ZONES_ID
            + " integer primary key autoincrement, " + COLUMN_ZONES_NAME
            + " text not null, " + COLUMN_ZONES_LATITUDE
            + " text not null, " + COLUMN_ZONES_LONGITUDE
            + " text not null );";

    private static final String DATABASE_CREATE_4 = "create table "
            + TABLE_POLEN + "(" + COLUMN_POLEN_ID
            + " text not null primary key, " + COLUMN_POLEN_NAME
            + " text not null, " + COLUMN_POLEN_MAX
            + " integer );";

    public SqlController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE_1);
        database.execSQL(DATABASE_CREATE_2);
        database.execSQL(DATABASE_CREATE_3);
        database.execSQL(DATABASE_CREATE_4);
        insertZones(database);
        insertPolen(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SqlController.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLEN);
        onCreate(db);
    }

    private void insertZones(SQLiteDatabase database) {
        database.execSQL("INSERT INTO " + TABLE_ZONES + " (" + COLUMN_ZONES_NAME + ", " + COLUMN_ZONES_LATITUDE + ", " + COLUMN_ZONES_LONGITUDE + ") VALUES ('Orense', " + 42.335789 + ", " + -7.863881 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_ZONES + " (" + COLUMN_ZONES_NAME + ", " + COLUMN_ZONES_LATITUDE + ", " + COLUMN_ZONES_LONGITUDE + ") VALUES ('A Coruña', " + 43.362344 + ", " + -8.411540 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_ZONES + " (" + COLUMN_ZONES_NAME + ", " + COLUMN_ZONES_LATITUDE + ", " + COLUMN_ZONES_LONGITUDE + ") VALUES ('Lugo', " + 43.009738 + ", " + -7.556758 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_ZONES + " (" + COLUMN_ZONES_NAME + ", " + COLUMN_ZONES_LATITUDE + ", " + COLUMN_ZONES_LONGITUDE + ") VALUES ('Santiago', " + 42.878213 + ", " + -8.544844 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_ZONES + " (" + COLUMN_ZONES_NAME + ", " + COLUMN_ZONES_LATITUDE + ", " + COLUMN_ZONES_LONGITUDE + ") VALUES ('Trives', " + 42.345768 + ", " + -7.257700 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_ZONES + " (" + COLUMN_ZONES_NAME + ", " + COLUMN_ZONES_LATITUDE + ", " + COLUMN_ZONES_LONGITUDE + ") VALUES ('Verin', " + 41.940259 + ", " + -7.434953 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_ZONES + " (" + COLUMN_ZONES_NAME + ", " + COLUMN_ZONES_LATITUDE + ", " + COLUMN_ZONES_LONGITUDE + ") VALUES ('Vigo', " + 42.240599 + ", " + -8.720727 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_ZONES + " (" + COLUMN_ZONES_NAME + ", " + COLUMN_ZONES_LATITUDE + ", " + COLUMN_ZONES_LONGITUDE + ") VALUES ('Viveiro', " + 43.664265 + ", " + -7.594535 + " ) ");
    }

    private void insertPolen(SQLiteDatabase database) {
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Acer', " + "'Acer'" + ", " + 17 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Alnu', " + "'Alnu'" + ", " + 499 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Apia', " + "'Apia'" + ", " + 10 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Arte', " + "'Arte'" + ", " + 3 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Betu', " + "'Betu'" + ", " + 931 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Bras', " + "'Bras'" + ", " + 4 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Cast', " + "'Cast'" + ", " + 362 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Casu', " + "'Casu'" + ", " + 4 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Cedr', " + "'Cedr'" + ", " + 44 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Comp', " + "'Comp'" + ", " + 15 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Cory', " + "'Cory'" + ", " + 20 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Cupr', " + "'Cupr'" + ", " + 405 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Cype', " + "'Cype'" + ", " + 4 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Chen', " + "'Chen'" + ", " + 11 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Eric', " + "'Eric'" + ", " + 107 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Frax', " + "'Frax'" + ", " + 174 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Heli', " + "'Heli'" + ", " + 0 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Junc', " + "'Junc'" + ", " + 3 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Ligu', " + "'Ligu'" + ", " + 24 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Merc', " + "'Merc'" + ", " + 6 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Mora', " + "'Mora'" + ", " + 15 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Myrt', " + "'Myrt'" + ", " + 19 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Olea', " + "'Olea'" + ", " + 102 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Palm', " + "'Palm'" + ", " + 16 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Pinu', " + "'Pinu'" + ", " + 1003 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Plan', " + "'Plan'" + ", " + 42 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Plat', " + "'Plat'" + ", " + 2347 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Poac', " + "'Poac'" + ", " + 535 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Popu', " + "'Popu'" + ", " + 473 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Quer', " + "'Quer'" + ", " + 1250 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Rosa', " + "'Rosa'" + ", " + 10 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Rume', " + "'Rume'" + ", " + 55 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Sali', " + "'Sali'" + ", " + 72 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Samb', " + "'Samb'" + ", " + 16 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Ulmu', " + "'Ulmu'" + ", " + 4 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Urti', " + "'Urti'" + ", " + 117 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Umem', " + "'Umem'" + ", " + 58 + " ) ");
        database.execSQL("INSERT INTO " + TABLE_POLEN + " (" + COLUMN_POLEN_ID + ", " + COLUMN_POLEN_NAME + ", " + COLUMN_POLEN_MAX + ") VALUES ('Indet', " + "'Indet'" + ", " + 27 + " ) ");
    }
}
