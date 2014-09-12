package com.myapp.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.myapp.mobilesafe.R;
import com.myapp.mobilesafe.fragment.SelectContactFragment;

/**
 * Created by 庹大伟 on 2014/8/18.
 */
public class BurglarThreeActivityBase extends BaseSetupActivity {

    private EditText safeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burglar_setup_three);
        safeNum = (EditText) findViewById(R.id.safeNum);
        safeNum.setText(sp.getString("safePhone", ""));
    }

    public void showPre() {
        Intent intent = new Intent(this, BurglarTwoActivityBase.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pre_setup_in, R.anim.pre_setup_out);
        finish();
    }

    public void showNext() {
        EditText safeNum = (EditText) findViewById(R.id.safeNum);
        if (TextUtils.isEmpty(safeNum.getText().toString())) {
            Toast.makeText(this, "请选择安全号码", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("safePhone", safeNum.getText().toString().trim());
        editor.commit();

        Intent intent = new Intent(this, BurglarFourActivityBase.class);
        startActivity(intent);
        overridePendingTransition(R.anim.next_setup_in, R.anim.next_setup_out);
        finish();
    }

    public void selectContact(View view) {
        SelectContactFragment selectContactFragment = SelectContactFragment.newInstance("选择联系人");
        selectContactFragment.show(getFragmentManager(), "select_contact");
    }
}
