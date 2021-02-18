package com.ub_hackers.mockinterview.tools;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    public static App instance;
    public App(){
        instance=this;
    }
    public static Context getContext(){return instance;}
}
