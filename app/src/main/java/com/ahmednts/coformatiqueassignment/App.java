package com.ahmednts.coformatiqueassignment;

import android.app.Application;

/**
 * Created by AhmedNTS on 2016-12-11.
 */
public class App extends Application {
    private static volatile App instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

}
