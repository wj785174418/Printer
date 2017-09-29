package com.example.mrx.printer.util;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;

/**
 * Created by Administrator on 2017/3/23.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Utils.init(this);
    }

    /**
    * 获取全局的Context
    * */
    public static Context getContext() {
        return context;
    }
}
