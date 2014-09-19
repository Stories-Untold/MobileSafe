package com.myapp.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by 庹大伟 on 2014/9/19.
 */
public class ServiceUtils {
    /**
     * 校验服务是否存在
     *
     * @param ServiceName
     * @return
     */
    public static Boolean isRunningService(Context context, String ServiceName) {
        String packageName = context.getPackageName() + ".service." + ServiceName;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : list) {
            String name = info.service.getClassName();
            if (packageName.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
