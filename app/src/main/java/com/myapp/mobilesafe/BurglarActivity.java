package com.myapp.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * 手机防盗界面
 * Created by Administrator on 2014/8/18.
 */
public class BurglarActivity extends Activity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = sp.getBoolean("configed", false);
        //判断是否进入设置向导
        if (configed) {
            setContentView(R.layout.activity_burglar);
        } else {
            //进入向导界面
            Intent intent = new Intent(this, BurglarOneActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
