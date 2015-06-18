package com.esei.mgrivas.polenalert;

import android.os.Parcel;
import android.os.Parcelable;

public class Point implements Parcelable {
    private int id;
    private double latitude;
    private double longitude;
    private int hour;
    private int minute;
    private int track_id;

    public Point(double lat, double longitude, int h, int m) {
        this.latitude = lat;
        this.longitude = longitude;
        this.id = 0;
        this.track_id = 0;
        this.hour = h;
        this.minute = m;
    }

    public Point(Parcel in) {
        id = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        track_id = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() { return hour;}

    public void setHour(int h) {this.hour = h;}

    public int getMinute() { return minute;}

    public void setMinute(int m) {this.minute = m;}

    public int getTrackId() { return track_id;}

    public void setTrackId(int id) {
        this.track_id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        String toret = "A hora: " + this.hour + ":" + this.minute + " - Punto: latitud " + this.latitude + " y longitud " + this.longitude;
        return toret;
    }

    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(track_id);
        dest.writeInt(hour);
        dest.writeInt(minute);
    }

    public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>()
    {
        public Point createFromParcel(Parcel in)
        {
            return new Point(in);
        }
        public Point[] newArray(int size)
        {
            return new Point[size];
        }
    };
}
