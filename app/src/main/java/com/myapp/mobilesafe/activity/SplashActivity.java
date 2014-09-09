package com.myapp.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.myapp.mobilesafe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by 庹大伟 on 2014/8/18.
 */
public class SplashActivity extends Activity {

    private static final String TAG = "myinfo";
    private TextView tv_splash_version;
    private String description;
    private String apkurl;
    private TextView updateInfo;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //加入启动动画
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(500);
        findViewById(R.id.rl_root_splash).startAnimation(animation);
        tv_splash_version = (TextView) findViewById(R.id.tp_splash_version);
        updateInfo = (TextView) findViewById(R.id.tv_update_info);
        tv_splash_version.setText("版本号：" + getVersionName());
        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean update = sp.getBoolean("update", true);
        if (sp.getBoolean("update", true)) {
            //检查升级
            checkUpdate();
        } else {
            //延迟代码，防止不出现Splash页面
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            }, 3000);
        }
    }

    private Handler handler = new Handler();

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
                                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                    @Override
                                                    public void onCancel(DialogInterface dialogInterface) {
                                                        start();
                                                        dialogInterface.dismiss();
                                                    }
                                                })
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
                                                                            tv_splash_version.setVisibility(View.VISIBLE);
                                                                            int process = (int) (current * 100 / total);
                                                                            updateInfo.setText("升级进度：" + process + "%");
                                                                        }

                                                                        @Override
                                                                        public void onSuccess(ResponseInfo<File> fileResponseInfo) {
                                                                            String downloadFilePath = fileResponseInfo.result.getAbsolutePath();
                                                                            File f = new File(downloadFilePath);
                                                                            installAPK(f);
                                                                        }

                                                                        /**
                                                                         * 安装应用
                                                                         * @param f
                                                                         */
                                                                        private void installAPK(File f) {
                                                                            Intent intent = new Intent();
                                                                            intent.setAction("android.intent.action.VIEW");
                                                                            intent.addCategory("android.intent.category.DEFAULT");
                                                                            intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
                                                                            startActivity(intent);
                                                                        }

                                                                        @Override
                                                                        public void onFailure(HttpException e, String s) {
                                                                            e.getStackTrace();
                                                                            File f = new File("/sdcard/mobildsafe.2.0.apk");
                                                                            if (f.exists()) {
                                                                                installAPK(f);
                                                                            } else {
                                                                                Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
                                                                                start();
                                                                            }
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
                                start();
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
