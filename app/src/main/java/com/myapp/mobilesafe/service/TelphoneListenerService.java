package com.myapp.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;

import com.myapp.mobilesafe.db.dao.NumberAddressQueryUtils;
import com.myapp.mobilesafe.ui.MobilesafeToast;

/**
 * Created by 庹大伟 on 2014/9/19.
 */
public class TelphoneListenerService extends Service {

    private TelephonyManager tm;
    private MyPhoneStateListener mListener;
    private OutCallReceiver receiver;
    private View view = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mListener = new MyPhoneStateListener();
        tm.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
        receiver = new OutCallReceiver();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        tm.listen(mListener, PhoneStateListener.LISTEN_NONE);
        mListener = null;
    }

    private class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String outPhone = getResultData();
            view = MobilesafeToast.show(TelphoneListenerService.this, NumberAddressQueryUtils.queryAddress(outPhone));
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    view = MobilesafeToast.show(getApplicationContext(), NumberAddressQueryUtils.queryAddress(incomingNumber));
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    if (view != null) {
                        manager.removeView(view);
                    }
                    break;
            }
        }
    }
}
