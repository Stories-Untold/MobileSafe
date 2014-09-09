package com.myapp.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 庹大伟 on 2014/9/9.
 */
public abstract class BaseSetupActivity extends Activity {

    //手势识别类
    private GestureDetector mGestureDetector;

    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
                    return true;
                }
                if (Math.abs(velocityX) < 200) {
                    return true;
                }
                if ((e1.getRawX() - e2.getRawX() > 200)) {
                    showNext();
                    return true;
                }
                if ((e2.getRawX() - e1.getRawX() > 200)) {
                    showPre();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 下一步点击事件
     *
     * @param view
     */
    public void next(View view) {
        showNext();
    }

    /**
     * 上一步点击事件
     *
     * @param view
     */
    public void pre(View view) {
        showPre();
    }

    public abstract void showNext();

    public abstract void showPre();

}
