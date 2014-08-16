package com.myapp.mobilesafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.myapp.mobilesafe.utils.ViewHolder;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_home = (GridView) findViewById(R.id.list_home);
        list_home.setChoiceMode(ListView.CHOICE_MODE_NONE);
        adapter = new MyAdapter(this);
        list_home.setAdapter(adapter);
        list_home.setOnItemClickListener(this);
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
