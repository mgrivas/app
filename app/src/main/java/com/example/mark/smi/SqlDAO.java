package com.example.mark.smi;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SqlDAO {

    // Database fields
    private SQLiteDatabase database;
    private SqlController dbHelper;
    private String[] allColumns_tracks = { SqlController.COLUMN_ID,
            SqlController.COLUMN_NAME,
            SqlController.COLUMN_COMMENTS,
            SqlController.COLUMN_DATE};

    private String[] allColumns_points = { SqlController.COLUMN_POINT_ID,
            SqlController.COLUMN_LATITUDE,
            SqlController.COLUMN_LONGITUDE,
            SqlController.COLUMN_TRACK_ID,
            SqlController.COLUMN_TIME };

    private String[] allColumns_polen = { SqlController.COLUMN_POLEN_ID,
            SqlController.COLUMN_POLEN_NAME,
            SqlController.COLUMN_DESC };

    public SqlDAO(Context context) {
        dbHelper = new SqlController(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Track createTrack(Track track) {
        ContentValues values = new ContentValues();
        values.put(SqlController.COLUMN_NAME, track.getName());
        values.put(SqlController.COLUMN_COMMENTS, track.getComment());
        values.put(SqlController.COLUMN_DATE, track.getDate());
        long insertId = database.insert(SqlController.TABLE_TRACKS, null,
                values);

        Cursor cursor = database.query(SqlController.TABLE_TRACKS,
                allColumns_tracks, SqlController.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Track trackFinal = cursorToTrack(cursor);
        cursor.close();

        return trackFinal;
    }

    public void createPoint(Point point) {
        ContentValues values = new ContentValues();
        values.put(SqlController.COLUMN_LATITUDE, point.getLatitude());
        values.put(SqlController.COLUMN_LONGITUDE, point.getLongitude());
        values.put(SqlController.COLUMN_TRACK_ID, point.getTrackId());
        values.put(SqlController.COLUMN_TIME, point.getTime());
        long insertId = database.insert(SqlController.TABLE_POINTS, null, values);


    }

    /*public void createPoint(Point point) {
        ContentValues values = new ContentValues();
        values.put(SqlController.COLUMN_LATITUDE, point.getLatitude());
        values.put(SqlController.COLUMN_LONGITUDE, point.getLongitude());
        long insertId = database.insert(SqlController.TABLE_COMMENTS, null,
                values);
        Cursor cursor = database.query(SqlController.TABLE_COMMENTS,
                allColumns, SqlController.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();
    }

    public void deleteComment(Comment comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(SqlController.TABLE_COMMENTS, SqlController.COLUMN_ID
                + " = " + id, null);
    }*/

    public void deleteTrack(Track track) {
        int id = track.getId();
        database.delete(SqlController.TABLE_TRACKS,SqlController.COLUMN_ID + " = " + id,null);
    }

    public void deletePoint(Point point) {
        int id = point.getId();
        database.delete(SqlController.TABLE_POINTS,SqlController.COLUMN_POINT_ID + " = " + id,null);
    }

    public List<Track> getAllTracks() {
        //Creamos la lista que vamos a devolver
        List<Track> tracks = new ArrayList<Track>();

        //Creamos el cursor sobre la consulta con todos los recorridos
        Cursor cursor = database.query(SqlController.TABLE_TRACKS,
                allColumns_tracks, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Track comment = cursorToTrack(cursor);
            tracks.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tracks;
    }

    public List<Point> getAllPoints() {
        //Creamos la lista que vamos a devolver
        List<Point> points = new ArrayList<Point>();

        //Creamos el cursor sobre la consulta con todos los recorridos
        Cursor cursor = database.query(SqlController.TABLE_POINTS,
                allColumns_points, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Point comment = cursorToPoint(cursor);
            points.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return points;
    }

    public List<Point> getPoints(int id) {
        //Creamos la lista que vamos a devolver
        List<Point> points = new ArrayList<Point>();

        //Creamos el cursor sobre la consulta con todos los puntos por id
        String[] id_track = new String[] {Integer.toString(id)};
        Cursor cursor = database.query(SqlController.TABLE_POINTS,allColumns_points,"track_id=?",id_track,null,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Point comment = cursorToPoint(cursor);
            points.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return points;
    }

    private Point cursorToPoint(Cursor cursor) {
        Point point = new Point(cursor.getDouble(1),cursor.getDouble(2),cursor.getString(4));
        point.setId(cursor.getInt(0));
        point.setTrackId(cursor.getInt(3));
        return point;
    }

    private Track cursorToTrack(Cursor cursor) {
        Track track = new Track(cursor.getString(1));
        track.setId(cursor.getInt(0));
        track.setComment(cursor.getString(2));
        track.setDate(cursor.getString(3));
        return track;
    }

    public int getLastTrack() {
        int id;
        Cursor cursor = database.rawQuery("SELECT * FROM tracks WHERE _id = (SELECT IDENT_CURRENT('tracks'))",null);
        cursor.moveToFirst();
        id = cursor.getInt(0);
        return id;
    }
}
