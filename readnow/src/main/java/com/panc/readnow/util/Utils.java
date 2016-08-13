package com.panc.readnow.util;

import android.content.res.Resources;
import android.graphics.Color;

import com.panc.readnow.app.App;

import java.util.Random;


/**
 * Created by Pan's on 2016/5/19.
 */
public class Utils {
    //这个是在主线程去更新ui,在没有上下文的环境,
    public static void runOnUIThread(Runnable runnable) {
        App.handler.post(runnable);
    }


    //得到字符串数组信息
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }


    //得到资源管理的类
    public static Resources getResources() {
        return App.context.getResources();
    }

    //在屏幕适配时候使用
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    //得到颜色
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    public static int createRandomTextSize() {
        return new Random().nextInt(12) + 18;
    }

    public static int createRandomColor() {

        int r = new Random().nextInt(255);
        int g = new Random().nextInt(255);
        int b = new Random().nextInt(255);

        return Color.rgb(r, g, b);
    }
}
