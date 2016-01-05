package com.baseandroid.assist.tools;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Mark on 2015/11/3.
 */
public class SystemUtil {



    public static boolean isRunBackground(Context context) {
        if (context == null)
            return true;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    LogUtil.i("Task", String.format("Background App:", appProcess.processName));
                    return true;
                } else {
                    LogUtil.i("Task", String.format("Foreground App:", appProcess.processName));
                    return false;
                }
            }
        }
        return false;
    }
}
