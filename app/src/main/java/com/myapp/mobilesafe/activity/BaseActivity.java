package com.myapp.mobilesafe.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.myapp.mobilesafe.utils.ActivityUtil;

/**
 * Created by 庹大伟 on 2014/8/18.
 */
public class BaseActivity extends Activity {

    public ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstawnceState) {
        super.onCreate(savedInstawnceState);
        ActivityUtil.add(this);
        mActionBar = getActionBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.remove(this);
    }

    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 通过Action启动Activity
     *
     * @param pAction
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param pAction
     * @param pBundle
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }
}
