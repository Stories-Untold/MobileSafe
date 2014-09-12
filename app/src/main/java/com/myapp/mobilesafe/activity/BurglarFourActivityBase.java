package com.myapp.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.myapp.mobilesafe.R;

/**
 * Created by 庹大伟 on 2014/8/18.
 */
public class BurglarFourActivityBase extends BaseSetupActivity implements CompoundButton.OnCheckedChangeListener {

    private SharedPreferences sp;
    private CheckBox isOpenProtect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burglar_setup_four);
        isOpenProtect = (CheckBox) findViewById(R.id.is_open_protect);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        isOpenProtect.setOnCheckedChangeListener(this);
        Boolean openProtect = sp.getBoolean("isOpenProtect", false);
        if (openProtect) {
            isOpenProtect.setChecked(true);
            isOpenProtect.setText("您已经开启防盗保护");
        }
    }

    public void showPre() {
        Intent intent = new Intent(this, BurglarThreeActivityBase.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pre_setup_in, R.anim.pre_setup_out);
        finish();
    }

    public void showNext() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("configed", true);
        editor.commit();
        Intent intent = new Intent(this, BurglarActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.next_setup_in, R.anim.next_setup_out);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isOpenProtect", b);
        if (b) {
            isOpenProtect.setText("您已经开启防盗保护");
        } else {
            isOpenProtect.setText("您没有开启防盗保护");
        }
        editor.commit();
    }
}
