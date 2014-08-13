package com.myapp.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

import java.io.File;

public class SplashActivity extends Activity {

    private static final String TAG = "myinfo";
    private TextView tv_splash_version;
    private String description;
    private String apkurl;
    private TextView updateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_splash_version = (TextView) findViewById(R.id.tp_splash_version);
        updateInfo = (TextView) findViewById(R.id.tv_update_info);
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                                        builder.setTitle("更新提示")
                                                .setIcon(R.drawable.app_icon)
                                                .setMessage(description)
                                                .setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                                            Log.i(TAG, apkurl);
                                                            HttpUtils http = new HttpUtils();
                                                            http.download(
                                                                    apkurl,
                                                                    "/sdcard/mobildsafe.2.0.apk",
                                                                    true,
                                                                    true,
                                                                    new RequestCallBack<File>() {
                                                                        @Override
                                                                        public void onLoading(long total, long current, boolean isUploading) {
                                                                            super.onLoading(total, current, isUploading);
                                                                            int process = (int) (current * 100 / total);
                                                                            updateInfo.setText("升级进度：" + process + "%");
                                                                        }

                                                                        @Override
                                                                        public void onSuccess(ResponseInfo<File> fileResponseInfo) {
                                                                            installAPK(fileResponseInfo);
                                                                        }

                                                                        private void installAPK(ResponseInfo<File> fileResponseInfo) {

                                                                        }

                                                                        @Override
                                                                        public void onFailure(HttpException e, String s) {
                                                                            e.getStackTrace();
                                                                            Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                            );
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "请插入SdCard", Toast.LENGTH_LONG).show();
                                                            return;
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
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
                                Toast.makeText(getApplicationContext(), "获取版本信息失败", Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        }).start();
    }

    private void start() {
        Intent intent = new Intent(this, MainActivity.class);
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
