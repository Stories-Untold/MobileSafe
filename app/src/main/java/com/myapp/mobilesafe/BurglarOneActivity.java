package com.myapp.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2014/8/18.
 */
public class BurglarOneActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burglar_setup_one);
    }

    /**
     * 下一步点击事件
     *
     * @param view
     */
    public void next(View view) {
        Intent intent = new Intent(this, BurglarTwoActivity.class);
        startActivity(intent);
        finish();
    }
}
