package com.myapp.mobilesafe.ui;

import android.content.Context;
import android.graphics.PixelFormat;
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

    public static View show(final Context context, CharSequence text) {
        view = View.inflate(context, R.layout.mobile_toast, null);
        TextView message = (TextView) view.findViewById(R.id.message);
        message.setText(text);
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        manager.addView(view, params);
        return view;
    }
}
