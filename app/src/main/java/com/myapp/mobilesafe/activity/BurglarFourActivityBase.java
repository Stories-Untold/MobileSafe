package com.myapp.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.myapp.mobilesafe.R;

/**
 * Created by 庹大伟 on 2014/8/18.
 */
public class BurglarFourActivityBase extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burglar_setup_four);
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
}
