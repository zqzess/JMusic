package com.jmusic.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.voice.SPlayer;
import com.alex.voice.listener.OnDownloadListener;
import com.alex.voice.listener.PlayerListener;
import com.alex.voice.netWork.VoiceDownloadUtil;
import com.alex.voice.player.SMediaPlayer;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jmusic.R;
import com.jmusic.bean.C;
import com.jmusic.net.HttpRequest;
import com.jmusic.util.HeadersUtil;
import com.lauzy.freedom.library.Lrc;
import com.lauzy.freedom.library.LrcHelper;
import com.lauzy.freedom.library.LrcView;
import com.lib_common.base.BaseActivity;
import com.lib_common.bean.Constance;
import com.lib_common.bean.ErrCode;
import com.lib_common.bean.MessageEvent;
import com.lib_common.bean.NetString;
import com.lib_common.cache.ACache;
import com.lib_common.config.SysGlobalConfig;
import com.lib_common.net.HttpRequestManage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import butterknife.BindView;
import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

@Route(path = Constance.ACTIVITY_URL_PLAYMUSIC)
public class PlayMusicActivity extends BaseActivity {

    @BindView(R.id.lyric_view)
    LrcView mLyricView;

    @BindView(R.id.play_prograss)
    ZzHorizontalProgressBar progressBar;

    @BindView(R.id.play_tv_timeend)
    TextView tv_timeend;

    @BindView(R.id.play_seekbar)
    SeekBar seekBar;

//    LyricView mLyricView;
    Context context;
    long id;
    ACache mCache;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysGlobalConfig.SystemBarColor(this,true);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_play_music);
    }

    @Override
    protected void initViews() {
        context=this;
        mCache=ACache.get(context);

        seekBar.setVisibility(View.GONE);


        mLyricView.setEmptyContent("加载中");
        if(mCache.getAsString("lyrc&"+id)==null)
        {
            NetString.setMusicLyric("/"+id);
            HttpRequest.netGetLyrc(context,NetString.getMusicLyric(),id, ErrCode.NETERRCODE);
            //延迟一秒执行，等待异步网络请求结束
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<Lrc> lrcs = LrcHelper.parseLrcFromFile(new File("/data/data/com.jmusic/files/id&" + id+".lrc"));
                    mLyricView.setLrcData(lrcs);
                    mLyricView.setEmptyContent("网络错误,歌词获取出错或无歌词");
                }
            }, 1000);
        }else
        {
            List<Lrc> lrcs = LrcHelper.parseLrcFromFile(new File("/data/data/com.jmusic/files/id&" + id+".lrc"));
            mLyricView.setLrcData(lrcs);
            mLyricView.setEmptyContent("歌词获取出错或无歌词");
        }



        NetString.setAudioUrl("/"+id);
        Log.d("网络请求链接",NetString.getAudioUrl());
        if(mCache.getAsString("audiourl&"+id)==null)
        {
            NetString.setAudioUrl("/"+id);
            HttpRequest.netGetAudioUrl(context,NetString.getAudioUrl(),id,ErrCode.NETERRCODE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try{
                        musicload(mCache.getAsString("audiourl&"+id));
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }, 1000);
        }else
        {
            try{
                musicload(mCache.getAsString("audiourl&"+id));
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mHandler.removeCallbacks(mRunnable);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.post(mRunnable);
                MediaPlayer mediaPlayer=SPlayer.instance().getMediaPlayer();
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    void musicload(String audioUrl)
    {
        //预加载,会下载文件下来
        VoiceDownloadUtil.instance()
                .download(audioUrl, new OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File file) {
                        PlayMusicActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
//                                progressBar.setProgress(0);
//                                progressBar.setProgressColor(Color.parseColor("#ff00ddFF"));
//                                progressBar.setBgColor(Color.parseColor("#DBDBDB"));
                                progressBar.setVisibility(View.GONE);
                                seekBar.setVisibility(View.VISIBLE);
                                seekBar.getThumb().setColorFilter(Color.parseColor("#ff00ddFF"), PorterDuff.Mode.SRC_ATOP);
                                musicPlay(audioUrl);
                            }
                        });
                    }

                    @Override
                    public void onDownloading(int progress) {
                        PlayMusicActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar.setProgressColor(Color.WHITE);
                                progressBar.setProgress(progress);
                            }
                        });

                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
                        Toast.makeText(context,ErrCode.LOADERRCODE,Toast.LENGTH_SHORT).show();
                    }
                });

    }


    void musicPlay(String audioUrl)
    {
        if(!SPlayer.instance().isPlaying())
        {
            SPlayer.instance()
                    .useWakeMode(false)//是否使用环形锁,默认不使用
                    .useWifiLock(false)//是否使用wifi锁,默认不使用
                    .setUseCache(true)//是否使用缓存,默认开启
                    .playByUrl(audioUrl, new PlayerListener() {
                        @Override
                        public void LoadSuccess(SMediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                            mHandler.post(mRunnable);
                        }

                        @Override
                        public void Loading(SMediaPlayer mediaPlayer, int i) {
                        }

                        @Override
                        public void onCompletion(SMediaPlayer mediaPlayer) {
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(context, ErrCode.PLAYERRCODE, Toast.LENGTH_SHORT).show();
                        }

                    });
        }else {
            SPlayer.instance().pause();
        }

    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            MediaPlayer mediaPlayer=SPlayer.instance().getMediaPlayer();
//            Log.d("总时长","总时长"+mediaPlayer.getDuration());
            seekBar.setMax(mediaPlayer.getDuration());
            int currentPosition = mediaPlayer.getCurrentPosition();
            mLyricView.updateTime(currentPosition);
//            Log.d("当前点","当前播放位置"+currentPosition);
            seekBar.setProgress(currentPosition);
            mHandler.postDelayed(this, 100);
        }
    };



    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveStickyEvent(MessageEvent event) {
        switch (event.getCode()) {
            case C.EventCode.PLAYINFO:
                id= (long) event.getData();
                break;
        }
    }
}