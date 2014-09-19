package com.myapp.mobilesafe.activity;

import android.os.Bundle;
import android.view.View;

import com.myapp.mobilesafe.R;

/**
 * Created by 庹大伟 on 2014/9/17.
 */
public class AtoolsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstawnceState) {
        super.onCreate(savedInstawnceState);
        setContentView(R.layout.activity_atools);
        mActionBar.setTitle("高级工具");
    }

    //号码归属地查询
    public void numberQuery(View view) {
        openActivity(NumberAddressQueryActivity.class);
    }
}
