package com.myapp.mobilesafe.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 庹大伟 on 2014/9/9.
 */
public class ActivityUtil {

    private static List<Activity> activityMan = new ArrayList<Activity>();

    public static void add(Activity activity) {
        activityMan.add(activity);
    }

    public static void remove(Activity activity) {
        activityMan.remove(activity);
    }

    public static void finishAllActivity() {
        for (Activity activity : activityMan) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
