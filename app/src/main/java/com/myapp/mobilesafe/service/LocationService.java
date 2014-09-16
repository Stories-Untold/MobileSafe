package com.myapp.mobilesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.myapp.mobilesafe.R;

import java.io.InputStream;

/**
 * Created by 庹大伟 on 2014/9/15.
 */
public class LocationService extends Service {

    private LocationManager manager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = manager.getBestProvider(criteria, true);
        manager.requestLocationUpdates(provider, 0, 0, mListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.removeUpdates(mListener);
        manager = null;
    }


    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //经度
            String longitude = "j:" + location.getLongitude() + "\n";
            //纬度
            String latitude = "w:" + location.getLatitude() + "\n";
            try {
                //转换坐标
                InputStream ls = getResources().openRawResource(R.raw.axisoffset);
                ModifyOffset modifyOffset = ModifyOffset.getInstance(ls);
                PointDouble pointDouble = modifyOffset.s2c(new PointDouble(location.getLongitude(), location.getLatitude()));
                longitude = "j:" + pointDouble.x + "\n";
                latitude = "w:" + pointDouble.y + "\n";
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("lastLocation", longitude + latitude);
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
