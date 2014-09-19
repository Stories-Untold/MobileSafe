package com.myapp.mobilesafe.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.mobilesafe.R;
import com.myapp.mobilesafe.db.dao.NumberAddressQueryUtils;

/**
 * Created by 庹大伟 on 2014/9/18.
 */
public class NumberAddressQueryActivity extends BaseActivity {

    private EditText number;
    private String numberStr;
    private TextView addressText;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_address_query);
        mActionBar.setTitle("号码归属地查询");
        number = (EditText) findViewById(R.id.number);
        addressText = (TextView) findViewById(R.id.numberAddress);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String address = charSequence.toString();
                if (address.length() < 7) {
                    addressText.setText("");
                    return;
                } else {
                    address = NumberAddressQueryUtils.queryAddress(address);
                    addressText.setText(address);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * 查询号码归属地
     *
     * @param view
     */
    public void query(View view) {
        numberStr = number.getText().toString().trim();
        if (TextUtils.isEmpty(numberStr)) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            number.startAnimation(shake);
            long[] pattern = {200, 200, 300, 300, 1000, 2000};
            mVibrator.vibrate(pattern, -1);
            Toast.makeText(this, "请输入要查询的电话号码", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String address = NumberAddressQueryUtils.queryAddress(numberStr);
            Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
        }
    }

}
