package com.esei.mgrivas.polenalert.entities;

import com.esei.mgrivas.polenalert.R;
import com.esei.mgrivas.polenalert.Support.GlobalClass;

import java.io.Serializable;

/**
 * Created by Mark on 22/04/2015.
 */
public class Track implements Serializable {
    private int id;
    private String comment;
    private String name;
    private String date;

    public Track (String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        String toret = GlobalClass.getContext().getString(R.string.track) + ": " + this.name + " " + GlobalClass.getContext().getString(R.string.created) + " " + this.date;
        return toret;
    }
}
