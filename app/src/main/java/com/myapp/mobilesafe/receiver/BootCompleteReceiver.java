package com.myapp.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * 开机启动时检查Sim卡是否改变
 * Created by 庹大伟 on 2014/9/10.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    private TelephonyManager manager;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mSharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String simCode = mSharedPreferences.getString("sim", null);
        String simSerialNumber = manager.getSimSerialNumber() + "aaa";
        String safePhone = mSharedPreferences.getString("safePhone", "");
        if (!simCode.equals(simSerialNumber)) {
            //Sim卡已经改变，发送短信给安全号码
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(safePhone, null, "Sim Changed", null, null);
        }
    }

}
