package com.moutamid.homecarepro.utilis;

import android.app.Application;

import com.fxn.stash.Stash;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stash.init(this);
        AndroidThreeTen.init(this);
    }
}
