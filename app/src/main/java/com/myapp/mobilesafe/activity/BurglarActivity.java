package com.myapp.mobilesafe.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.mobilesafe.R;
import com.myapp.mobilesafe.receiver.LockScreenReceiver;

/**
 * 手机防盗界面
 * Created by Administrator on 2014/8/18.
 */
public class BurglarActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private SharedPreferences sp;
    private TextView safePhone;
    private ImageView lockImg;
    private CheckBox openAdmin;
    private DevicePolicyManager dpm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        boolean configed = sp.getBoolean("configed", false);
        //判断是否进入设置向导
        if (configed) {
            setContentView(R.layout.activity_burglar);
            safePhone = (TextView) findViewById(R.id.safe_phone);
            lockImg = (ImageView) findViewById(R.id.lock_img);
            openAdmin = (CheckBox) findViewById(R.id.openAdmin);
            String phone = sp.getString("safePhone", "");
            Boolean isOpenProtect = sp.getBoolean("isOpenProtect", false);
            Boolean isOpenAdmin = sp.getBoolean("isOpenAdmin", false);
            openAdmin.setChecked(isOpenAdmin);
            openAdmin(isOpenProtect);
            if (isOpenProtect) {
                lockImg.setImageResource(R.drawable.lock);
            } else {
                lockImg.setImageResource(R.drawable.unlock);
            }
            safePhone.setText(TextUtils.substring(phone, 0, TextUtils.indexOf(phone, "(")));
        } else {
            //进入向导界面
            Intent intent = new Intent(this, BurglarOneActivityBase.class);
            startActivity(intent);
            finish();
        }


    }

    /**
     * 重新进入设置向导
     *
     * @param view
     */
    public void resetBurglar(View view) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("configed");
        editor.commit();
        Intent intent = new Intent(this, BurglarActivity.class);
        startActivity(intent);
        finish();
    }

    private void openAdmin(Boolean isOpenProtect) {
        if (isOpenProtect) {
            openAdmin.setVisibility(View.VISIBLE);
            openAdmin.setOnCheckedChangeListener(this);
        } else {
            openAdmin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences.Editor editor = sp.edit();
        ComponentName mDeviceAdminSample = new ComponentName(this, LockScreenReceiver.class);

        if (b) {
            //开启管理员权限
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "openAdmin");
            startActivity(intent);
            editor.putBoolean("isOpenAdmin", true);
        } else {
            dpm.removeActiveAdmin(mDeviceAdminSample);
            editor.putBoolean("isOpenAdmin", false);
        }
        editor.commit();
    }
}
