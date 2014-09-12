package com.myapp.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.myapp.mobilesafe.R;
import com.myapp.mobilesafe.ui.SettingItemView;

/**
 * Created by 庹大伟 on 2014/8/18.
 */
public class BurglarTwoActivityBase extends BaseSetupActivity {

    private SettingItemView bindSim;
    //可以读取手机sim卡的信息
    private TelephonyManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burglar_setup_two);
        bindSim = (SettingItemView) findViewById(R.id.bind_sim);
        String simCode = sp.getString("sim", null);
        if (!TextUtils.isEmpty(simCode)) {
            bindSim.setChecked(true);
            bindSim.setDesc("已绑定sim卡");
        }
        //保存SIM卡的序列号
        manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        bindSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                if (bindSim.isChecked()) {
                    editor.remove("sim");
                    bindSim.setChecked(false);
                    bindSim.setDesc("sim卡没有绑定");
                } else {
                    //得到SIM卡的序列号
                    String SimSerialNumber = manager.getSimSerialNumber();
                    editor.putString("sim", SimSerialNumber);
                    bindSim.setChecked(true);
                    bindSim.setDesc("已绑定sim卡");
                }
                editor.commit();
            }
        });
    }

    public void showPre() {
        Intent intent = new Intent(this, BurglarOneActivityBase.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pre_setup_in, R.anim.pre_setup_out);
        finish();
    }

    public void showNext() {
        String simCode = sp.getString("sim", null);
        if (TextUtils.isEmpty(simCode)) {
            Toast.makeText(this, "没有绑定Sim卡", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, BurglarThreeActivityBase.class);
        startActivity(intent);
        overridePendingTransition(R.anim.next_setup_in, R.anim.next_setup_out);
        finish();
    }

}
