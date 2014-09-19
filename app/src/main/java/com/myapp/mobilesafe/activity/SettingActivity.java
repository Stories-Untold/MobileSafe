package com.myapp.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.myapp.mobilesafe.R;
import com.myapp.mobilesafe.service.TelphoneListenerService;
import com.myapp.mobilesafe.ui.SettingItemView;
import com.myapp.mobilesafe.utils.ServiceUtils;

/**
 * 设置中心
 * Created by 庹大伟 on 2014/8/18.
 */
public class SettingActivity extends BaseActivity {

    private SettingItemView mSettingItemView;
    private SettingItemView ShowAddress;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ShowAddress = (SettingItemView) findViewById(R.id.show_address);
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
        if (ServiceUtils.isRunningService(this, "TelphoneListenerService")) {
            ShowAddress.setChecked(true);
        } else {
            ShowAddress.setChecked(false);
        }
    }

    private void init() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        mSettingItemView = (SettingItemView) findViewById(R.id.siv_update);
        ShowAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, TelphoneListenerService.class);
                //判断是否选中
                if (ShowAddress.isChecked()) {
                    ShowAddress.setChecked(false);
                    stopService(intent);
                } else {
                    ShowAddress.setChecked(true);
                    startService(intent);
                }
            }
        });
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
