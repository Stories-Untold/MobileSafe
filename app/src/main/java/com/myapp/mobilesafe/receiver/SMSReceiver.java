package com.myapp.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.myapp.mobilesafe.R;
import com.myapp.mobilesafe.service.LocationService;

/**
 * Created by 庹大伟 on 2014/9/13.
 */
public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收短信
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object b : objs) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) b);
            //发送者
            String sender = message.getOriginatingAddress();
            //内容
            String body = message.getMessageBody();
            if ("#*location*#".equals(body)) {
                //新建服务获取地理位置
                Intent i = new Intent(context, LocationService.class);
                context.startService(i);
                SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                String lastLocation = sp.getString("lastLocation", "");
                SmsManager manager = SmsManager.getDefault();
                if (TextUtils.isEmpty(lastLocation)) {
                    manager.sendTextMessage(sender, "", "getting location。。。", null, null);
                } else {
                    manager.sendTextMessage(sender, "", lastLocation, null, null);
                }
                //把短信广播终止
                abortBroadcast();
            } else if ("#*alarm*#".equals(body)) {
                //播放报警音乐
                MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                //设置是否循环播放
                player.setLooping(false);
                //设置声音大小
                player.setVolume(1.0f, 1.0f);
                player.start();
                abortBroadcast();
            } else if ("#*wipedata*#".equals(body)) {
                //清除数据
                abortBroadcast();
            } else if ("#*lockscreen*#".equals(body)) {
                //远程锁屏
                Log.i("smg", "lock");
                abortBroadcast();
            }
        }
    }

    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            location.getLongitude();
            location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

}