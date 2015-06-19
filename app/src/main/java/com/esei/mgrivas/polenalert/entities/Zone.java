package com.esei.mgrivas.polenalert.entities;

/**
 * Created by Mark on 15/06/2015.
 */
public class Zone {
    private int id;
    private String name;
    private double latitude;
    private double longitude;

    public Zone (String name, double latitude, double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
    }

    public int getId() {
            return id;
        }

    public void setId(int id) {
            this.id = id;
        }

    public String getName() {
            return name;
        }

    public void setName(String name) {
            this.name = name;
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
            String toret = this.name;
            return toret;
    }
}
