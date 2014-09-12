package com.myapp.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.mobilesafe.R;

/**
 * 手机防盗界面
 * Created by Administrator on 2014/8/18.
 */
public class BurglarActivity extends BaseActivity {

    private SharedPreferences sp;
    private TextView safePhone;
    private ImageView lockImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = sp.getBoolean("configed", false);
        //判断是否进入设置向导
        if (configed) {
            setContentView(R.layout.activity_burglar);
            safePhone = (TextView) findViewById(R.id.safe_phone);
            lockImg = (ImageView) findViewById(R.id.lock_img);
            String phone = sp.getString("safePhone", "");
            Boolean isOpenProtect = sp.getBoolean("isOpenProtect", false);
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

}
