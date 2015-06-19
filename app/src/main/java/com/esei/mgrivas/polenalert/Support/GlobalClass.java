package com.esei.mgrivas.polenalert.Support;

import android.app.Application;
import android.content.Context;

public class GlobalClass extends Application{

    private String email;
    private static GlobalClass instance;

    public String getEmail() {

        return email;
    }

    public static GlobalClass getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    public void setEmail(String aEmail) {

        email = aEmail;
    }

}
