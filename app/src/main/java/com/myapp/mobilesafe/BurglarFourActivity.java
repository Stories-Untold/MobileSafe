package com.myapp.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

/**
 * Created by 庹大伟 on 2014/8/18.
 */
public class BurglarFourActivity extends Activity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burglar_setup_four);

        sp = getSharedPreferences("config", MODE_PRIVATE);
    }

    /**
     * 上一步点击事件
     *
     * @param view
     */
    public void pre(View view) {
        Intent intent = new Intent(this, BurglarThreeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Finish点击事件
     *
     * @param view
     */
    public void finish(View view) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("configed", true);
        editor.commit();
        Intent intent = new Intent(this, BurglarActivity.class);
        startActivity(intent);
        finish();
    }
}
