package com.myapp.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myapp.mobilesafe.R;

/**
 * Created by 庹大伟 on 2014/8/14.
 */
public class SettingItemView extends RelativeLayout {

    private CheckBox is_update;
    private TextView tv_title;
    private TextView tv_desc;
    private String desc_off;
    private String desc_on;

    /**
     * 初始化布局文件
     *
     * @param context
     */
    private void initView(Context context) {
        View.inflate(context, R.layout.setting_item, this);
        is_update = (CheckBox) this.findViewById(R.id.cb_is_checkupdate);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_desc = (TextView) this.findViewById(R.id.tv_desc);
    }

    /**
     * 校验组合控件是否选中
     */
    public boolean isChecked() {
        return is_update.isChecked();
    }

    /**
     * 设置组合控件的状态
     */
    public void setChecked(boolean checked) {
        if (checked) {
            setDesc(desc_on);
        } else {
            setDesc(desc_off);
        }
        is_update.setChecked(checked);
    }

    /**
     * 根据状态设置描述设置描述
     */
    public void setDesc(String desc) {
        tv_desc.setText(desc);
    }


    /**
     * 根据状态设置描述设置标题
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public SettingItemView(Context context) {
        super(context);
        initView(context);
    }


    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        String packageName = context.getPackageName();
        String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/" + packageName, "settingTitle");
        desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/" + packageName, "desc_on");
        desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/" + packageName, "desc_off");
        setTitle(title);
        initView(context);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }
}
