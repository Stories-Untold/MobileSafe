package com.myapp.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.myapp.mobilesafe.ui.SettingItemView;

/**
 * 设置中心
 */
public class SettingActivity extends Activity {

    private SettingItemView mSettingItemView;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        configUpdate();
    }

    private void configUpdate() {
        if (sp.getBoolean("update", true)) {
            mSettingItemView.setChecked(true);
        } else {
            mSettingItemView.setChecked(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        configUpdate();
    }

    private void init() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        mSettingItemView = (SettingItemView) findViewById(R.id.siv_update);
        mSettingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                //判断是否选中
                if (mSettingItemView.isChecked()) {
                    //关闭自动升级
                    mSettingItemView.setChecked(false);
                    editor.putBoolean("update", false);
                } else {
                    //打开自动升级
                    mSettingItemView.setChecked(true);
                    editor.putBoolean("update", true);
                }
                editor.commit();
            }
        });
    }

}
