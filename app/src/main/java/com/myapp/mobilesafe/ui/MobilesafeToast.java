package com.myapp.mobilesafe.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.myapp.mobilesafe.R;

/**
 * 自定义Toast
 * Created by 庹大伟 on 2014/9/19.
 */
public class MobilesafeToast {

    private static WindowManager manager;
    private static View view;
    private static WindowManager.LayoutParams params;
    private static SharedPreferences sp;

    public static View show(Context context, CharSequence text) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        int style = sp.getInt("address_style", R.drawable.call_locate_orange);
        view = View.inflate(context, R.layout.mobile_toast, null);
        TextView message = (TextView) view.findViewById(R.id.message);
        view.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //起始x,y
                        startX = (int) motionEvent.getRawX();
                        startY = (int) motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //偏移量
                        int newx = (int) motionEvent.getRawX();
                        int newy = (int) motionEvent.getRawY();
                        int dx = newx - startX;
                        int dy = newy - startY;
                        params.x += dx;
                        params.y += dy;
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > (manager.getDefaultDisplay().getWidth() - view.getWidth())) {
                            params.x = manager.getDefaultDisplay().getWidth() - view.getWidth();
                        }
                        if (params.y > (manager.getDefaultDisplay().getHeight() - view.getHeight())) {
                            params.y = manager.getDefaultDisplay().getHeight() - view.getHeight();
                        }
                        manager.updateViewLayout(view, params);
                        startX = (int) motionEvent.getRawX();
                        startY = (int) motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //手指离开屏幕的位置
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("addressToastX", params.x);
                        editor.putInt("addressToastY", params.y);
                        editor.commit();
                        break;
                }
                return true;
            }
        });
        view.setBackgroundResource(style);
        message.setText(text);
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP + Gravity.LEFT;
        params.x = sp.getInt("addressToastX", 40);
        params.y = sp.getInt("addressToastY", 100);
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        // android系统里面具有电话优先级的一种窗体类型，记得添加权限。
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        manager.addView(view, params);
        return view;
    }
}
