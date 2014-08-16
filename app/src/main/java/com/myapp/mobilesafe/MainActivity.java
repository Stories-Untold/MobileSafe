package com.myapp.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.mobilesafe.utils.ViewHolder;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private GridView list_home;
    private MyAdapter adapter;
    private static String[] names = {
            "手机防盗", "通讯卫士", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"
    };
    private static int[] icons = {
            R.drawable.exam_private_entry, R.drawable.exam_ad_soft, R.drawable.exam_auto_cloud_scan,
            R.drawable.exam_auto_start, R.drawable.exam_safe_pay, R.drawable.exam_rubbish_clean,
            R.drawable.exam_virus, R.drawable.exam_cloud_safe, R.drawable.exam_leak_scan
    };
    private SharedPreferences sp;
    private Button bt_password_submit;
    private Button bt_password_cancel;
    private AlertDialog.Builder passwordDialog;
    private AlertDialog mAlertDialog;
    private EditText setPwd;
    private EditText confirmPwd;
    private View dialogView;
    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_home = (GridView) findViewById(R.id.list_home);
        list_home.setChoiceMode(ListView.CHOICE_MODE_NONE);
        adapter = new MyAdapter(this);
        list_home.setAdapter(adapter);
        list_home.setOnItemClickListener(this);
        sp = getSharedPreferences("user", MODE_PRIVATE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            //进入设置中心
            case 8:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            //手机防盗
            case 0:
                showLostFindDialog();
                break;
        }
    }

    private void showLostFindDialog() {
        //判断是否设置过密码
        if (isSetupPwd()) {
            //设置过密码
        } else {
            //没有设置过密码，弹出设置密码框
            passwordDialog = new AlertDialog.Builder(this);
            dialogView = View.inflate(this, R.layout.dialog_setup_password, null);
            passwordDialog.setView(dialogView);
            mAlertDialog = passwordDialog.create();
            //修改低版本没填充满的问题
            mAlertDialog.setView(dialogView, 0, 0, 0, 0);
            mAlertDialog.show();
            bt_password_submit = (Button) dialogView.findViewById(R.id.bt_password_submit);
            bt_password_cancel = (Button) dialogView.findViewById(R.id.bt_password_cancel);
            bt_password_submit.setOnClickListener(this);
            bt_password_cancel.setOnClickListener(this);
        }
    }

    private boolean isSetupPwd() {
        return !TextUtils.isEmpty(sp.getString("password", null));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //确定
            case R.id.bt_password_submit:
                setPwd = (EditText) dialogView.findViewById(R.id.et_password);
                confirmPwd = (EditText) dialogView.findViewById(R.id.et_confirm_password);
                password = setPwd.getText().toString().trim();
                confirmPassword = confirmPwd.getText().toString().trim();
                //判断密码是否为空
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                //判断是否一致，保存，消掉对话框，进入手机防盗页面
                if (password.equals(confirmPassword)) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("password", "123");
                    editor.commit();
                } else {
                    Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
                }
                break;
            //取消
            case R.id.bt_password_cancel:
                mAlertDialog.dismiss();
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int i) {
            return names[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = inflater.inflate(R.layout.list_item_home, null);
            }
            TextView textView = ViewHolder.get(view, R.id.home_app);
            textView.setText(names[i]);
            Drawable drawable = getResources().getDrawable(icons[i]);
            //这一步必须做，否则不会显示
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, drawable, null, null);
            return view;
        }
    }

}
