package com.myapp.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends Activity {

    private static final String TAG = "myinfo";
    private TextView tv_splash_version;
    private String description;
    private String apkurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_splash_version = (TextView) findViewById(R.id.tp_splash_version);
        tv_splash_version.setText("版本号：" + getVersionName());
        //检查升级
        checkUpdate();
        //加入启动动画
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(500);
        findViewById(R.id.rl_root_splash).startAnimation(animation);
    }

    /**
     * 检查是否有新版本
     */
    private void checkUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils http = new HttpUtils();
                http.send(
                        HttpRequest.HttpMethod.GET,
                        getResources().getString(R.string.serverurl),
                        new RequestCallBack<Object>() {
                            @Override
                            public void onLoading(long total, long current, boolean isUploading) {
                                super.onLoading(total, current, isUploading);
                            }

                            @Override
                            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                                String result = objectResponseInfo.result.toString();
                                //解析JSON
                                try {
                                    JSONObject obj = new JSONObject(result);
                                    //得到服务器版本信息
                                    String version = (String) obj.get("version");
                                    description = (String) obj.get("description");
                                    apkurl = (String) obj.get("apkurl");

                                    if (getVersionName().equals(version)) {
                                        //版本一致，进入主页面
                                        start();
                                    } else {
                                        //有新版本，弹出对话框
                                        AlertDialog builder = new AlertDialog.Builder(SplashActivity.this)
                                                .setTitle("版本更新")
                                                .setIcon(R.drawable.app_icon)
                                                .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Toast.makeText(SplashActivity.this, "确认", Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        start();
                                                    }
                                                })
                                                .create();
                                        builder.show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                Toast.makeText(SplashActivity.this, "获取版本信息失败", Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        }).start();
    }

    private void start() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 得到应用程序的版本名称
     */
    private String getVersionName() {
        //用来管理手机的apk
        PackageManager pm = getPackageManager();
        //得到指定apk的Mainfest文件
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
