package com.esei.mgrivas.polenalert.entities;

/**
 * Created by Mark on 20/06/2015.
 */
public class Polen {
    private String id;
    private String name;
    private int max;

    public Polen (String id, String name, int max) {
        this.id = id;
        this.name = name;
        this.max = max;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        String toret = this.name;
        return toret;
    }
}
