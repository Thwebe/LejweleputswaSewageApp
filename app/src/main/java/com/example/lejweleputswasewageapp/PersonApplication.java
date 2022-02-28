package com.example.lejweleputswasewageapp;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class  PersonApplication extends Application {
    public static final String APPLICATION_ID = "CBA5A8CD-D8A2-863F-FFAA-1CCADAF77500";
    public static final String API_KEY = "2764B627-AC9A-428D-932F-15713772303F";
    public static final String SERVER_URL = "https://api.backendless.com";

public static BackendlessUser user;
public static List<Location> userLocations;
public static List<Person> personList;

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }

}
