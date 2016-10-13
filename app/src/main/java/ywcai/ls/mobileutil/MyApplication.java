package ywcai.ls.mobileutil;

import android.app.Application;

import java.util.HashMap;

/**
 * Created by zmy_11 on 2016/8/11.
 */
public class MyApplication extends Application {


    private static MyApplication instance;
    public static MyApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
