package com.example.madara.training.utils;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by madara on 2/27/18.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
