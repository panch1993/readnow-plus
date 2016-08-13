package com.panc.readnow.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by Pan's on 2016-8-3.
 */
public class App extends Application {
    public static Context context;
    public static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        handler = new Handler();
    }
}