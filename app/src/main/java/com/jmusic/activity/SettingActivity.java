package com.jmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.voice.SPlayer;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.jmusic.MainActivity;
import com.jmusic.R;
import com.jmusic.bean.MusicInfo;
import com.jmusic.config.PlayConfig;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lib_common.base.BaseActivity;
import com.lib_common.bean.C;
import com.lib_common.bean.Constance;
import com.lib_common.bean.MessageEvent;
import com.lib_common.config.SysGlobalConfig;
import com.lib_common.customView.RightAndLeftTextView;
import com.lib_searchview.bean.MusicPageInfo;
import com.lib_update.util.ApkVersionCodeUtils;
import com.lib_update.util.UpdateUtil;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.File;

import butterknife.BindView;
@Route(path = Constance.ACTIVITY_URL_SETTING)
public class SettingActivity extends BaseActivity {

    @BindView(R.id.setting_cache)
    RightAndLeftTextView btn_show_cache;
    @BindView(R.id.setting_update)
    RightAndLeftTextView btn_show_update;
    @BindView(R.id.setting_topbar_back)
    TextView btn_back;

    Context context;
    String versionName;
    KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysGlobalConfig.SystemBarColor(this,true);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initViews() {
        context=this;
        versionName=ApkVersionCodeUtils.getVerName(context);
        String cacheSize = SPlayer.instance().getCacheSize();
        btn_show_cache.setmLeftText("清理缓存");
        btn_show_cache.setmRightText(cacheSize);
        btn_show_update.setmLeftText("当前版本号: v"+versionName);
        btn_show_update.setmRightText("检查更新");
        clickEvent();
    }

    private void clickEvent()
    {
        btn_show_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAlertDialog myAlertDialog = new MyAlertDialog(context).builder()
                        .setTitle("您确认要清理缓存吗？")
                        .setMsg("清理缓存后歌曲需要重新缓存")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SPlayer.instance().clearCache();
                                hud=KProgressHUD.create(context)
                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                        .setCancellable(true)
                                        .setAnimationSpeed(2)
                                        .setDimAmount(0.5f)
                                        .show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        String cacheSize = SPlayer.instance().getCacheSize();
                                        btn_show_cache.setmRightText(cacheSize);
                                        hud.dismiss();
                                        Toast.makeText(context,"清理成功",Toast.LENGTH_SHORT);
                                    }
                                },1000);
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                myAlertDialog.show();

            }
        });

        btn_show_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUtil.getNewVersion(context,versionName);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        try {
                            Instrumentation inst = new Instrumentation();
                            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                        } catch (Exception e) {

                        }
                    }
                }.start();
            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveStickyEvent(MessageEvent event) {
        switch (event.getCode()) {
            case C.EventCode.EXTENDED_DATA_STATUS:
                Intent intent = (Intent)event.getData();
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "JMusic.apk");
//                Uri apkUri = FileProvider.getUriForFile(context,
//                        "com.tdkankan.FileProvider", new File(Environment.getExternalStorageDirectory() + "/TDkankan.apk"));
                    Uri apkUri = FileProvider.getUriForFile(context, "com.jmusic.FileProvider", file);
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    context.startActivity(install);

                } else {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.DIRECTORY_DOWNLOADS, "JMusic.apk")), "application/vnd.android.package-archive");
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(install);
                }
                break;
        }
    }
}