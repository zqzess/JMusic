package com.jmusic.activity;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.voice.SPlayer;
import com.alex.voice.listener.OnDownloadListener;
import com.alex.voice.listener.PlayerListener;
import com.alex.voice.netWork.VoiceDownloadUtil;
import com.alex.voice.player.SMediaPlayer;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.jmusic.R;
import com.lib_common.bean.C;
import com.jmusic.bean.MusicInfo;
import com.jmusic.config.PlayConfig;
import com.jmusic.net.HttpRequest;
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
import com.lib_common.util.SharedPreferencesUtil;
import com.lib_dao.bean.PlayListInfo;
import com.lib_dao.config.PlayListConfig;
import com.lib_dao.db.DaoHelper;
import com.lib_searchview.bean.MusicPageInfo;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

@Route(path = Constance.ACTIVITY_URL_PLAYMUSIC)
public class PlayMusicActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.lyric_view)
    LrcView mLyricView;

    @BindView(R.id.play_prograss)
    ZzHorizontalProgressBar progressBar;

    @BindView(R.id.play_tv_timeend)
    TextView tv_timeend;

    @BindView(R.id.play_tv_time)
    TextView tv_time;

    @BindView(R.id.play_tv_btn_main)
    TextView btn_main;

    @BindView(R.id.play_tv_name)
    TextView tv_name;

    @BindView(R.id.play_tv_author)
    TextView tv_author;

    @BindView(R.id.play_seekbar)
    SeekBar seekBar;

    @BindView(R.id.activity_play_tv_btn_down)
    TextView btn_down;

    @BindView(R.id.activity_play_tv_btn_like)
    TextView btn_like;

    @BindView(R.id.play_tv_btn_playmode)
    TextView btn_playmode;

//    LyricView mLyricView;
    Context context;
    long id;
    MusicInfo music;
    ACache mCache;
    Handler mHandler = new Handler();
    boolean isFavourite=false;
    PlayListInfo playListInfo;
//    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysGlobalConfig.SystemBarColor(this,true);
        if(SPlayer.instance().isPlaying()&&id != PlayConfig.playingId)
        {
            SPlayer.instance().stop();
        }
        if(SPlayer.instance().isPlaying())
        {
            btn_main.setBackground(this.getResources().getDrawable(R.drawable.ic_pause));
        }else
        {
            btn_main.setBackground(this.getResources().getDrawable(R.drawable.ic_play));
        }
        if(id!=PlayConfig.playingId)
        {
            PlayConfig.currentPosition=0;
        }
        PlayConfig.playFlag=1;


    }

    @Override
    protected void setContentView() {
        overridePendingTransition(R.anim.in_from_bottom, 0);
        setContentView(R.layout.activity_play_music);
    }

    @Override
    protected void initViews() {
        context=this;
        mCache=ACache.get(context);

        seekBar.setVisibility(View.GONE);

//        executorService = Executors.newCachedThreadPool();

        if(DaoHelper.searchwithFavouriteForm(music.getId())!=null)
        {
            btn_like.setBackground(this.getDrawable(com.lib_searchview.R.drawable.ic_like_select));
            playListInfo=DaoHelper.searchwithFavouriteForm(music.getId());
            isFavourite=true;
        }else
        {
            btn_like.setBackground(this.getDrawable(com.lib_searchview.R.drawable.ic_like));
            isFavourite=false;
        }

        try {
            tv_name.setText(music.getName());
            tv_author.setText(music.getArtist());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        mLyricView.setEmptyContent("加载中");

        //TODO 网络请求歌词
        if(mCache.getAsString("lyrc&"+id)==null)
        {
            NetString.setMusicLyric("/"+id);
            HttpRequest.netGetLyrcGzip(context,NetString.getMusicLyric(),id, ErrCode.NETERRCODE);
            //延迟一秒执行，等待异步网络请求结束
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<Lrc> lrcs = LrcHelper.parseLrcFromFile(new File("/data/data/com.jmusic/files/id&" + id+".lrc"));
                    mLyricView.setLrcData(lrcs);
                    mLyricView.setEmptyContent("网络错误,歌词获取出错或无歌词");
                }
            }, 1500);
        }else
        {
            List<Lrc> lrcs = LrcHelper.parseLrcFromFile(new File("/data/data/com.jmusic/files/id&" + id+".lrc"));
            mLyricView.setLrcData(lrcs);
            mLyricView.setEmptyContent("歌词获取出错或无歌词");
        }


        //TODO 网络请求歌曲播放链接
        NetString.setAudioUrl("/"+id);
        if(mCache.getAsString("audiourl&"+id)==null)
        {
            NetString.setAudioUrl("/"+id);
            HttpRequest.netGetAudioUrlGzip(context,NetString.getAudioUrl(),id,ErrCode.NETERRCODE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try{
                        musicload(mCache.getAsString("audiourl&"+id));
                        Log.d("歌曲链接",mCache.getAsString("audiourl&"+id));
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }, 1500);
        }else
        {
            try{
                musicload(mCache.getAsString("audiourl&"+id));
                Log.d("歌曲链接",mCache.getAsString("audiourl&"+id));
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        //TODO 进度条
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mHandler.removeCallbacks(mRunnable);
                    String timeend=PlayConfig.timeChange(progress);
                    tv_timeend.setText(timeend);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try{
                    mHandler.post(mRunnable);
                    PlayConfig.mediaPlayer.seekTo(seekBar.getProgress());
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
        mLyricView.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
            @Override
            public void onPlay(long time, String content) {
                PlayConfig.mediaPlayer.seekTo((int) time);
            }
        });
        btn_main.setOnClickListener(this);
        btn_down.setOnClickListener(this);
        btn_like.setOnClickListener(this);
        btn_playmode.setOnClickListener(this);
    }

    //TODO 歌曲预加载缓存
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
                                PlayConfig.playNow=music;
                                SharedPreferencesUtil.putLong(context,"id",id,"playinginfo");
                                SharedPreferencesUtil.put(context,"name",music.getName(),"playinginfo");
                                SharedPreferencesUtil.put(context,"author",music.getArtist(),"playinginfo");
                                SharedPreferencesUtil.put(context,"pic",music.getAlbumPic(),"playinginfo");
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
                        try{
                            Log.d("debug-歌曲链接",mCache.getAsString("audiourl&"+id));

                        }catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });

    }


    //TODO 歌曲播放
    void musicPlay(String audioUrl)
    {
        if(PlayConfig.playFlag!=3)
        {
            if(!SPlayer.instance().isPlaying()|| (SPlayer.instance().isPlaying()&&id != PlayConfig.playingId))
            {
                //不在播放
                SPlayer.instance()
                        .useWakeMode(false)//是否使用环形锁,默认不使用
                        .useWifiLock(false)//是否使用wifi锁,默认不使用
                        .setUseCache(true)//是否使用缓存,默认开启
                        .playByUrl(audioUrl, new PlayerListener() {
                            @Override
                            public void LoadSuccess(SMediaPlayer mediaPlayer) {
                                PlayConfig.mediaPlayer=SPlayer.instance().getMediaPlayer();
                                String timeall=PlayConfig.timeAllCount();
                                tv_time.setText(timeall);
                                String timeend=PlayConfig.timeEndCount();
                                tv_timeend.setText(timeend);
                                mediaPlayer.start();
                                mLyricView.resume();
                                if(!PlayConfig.isPlaying&&id == PlayConfig.playingId)
                                {
                                    //还原上次播放进度
                                    PlayConfig.mediaPlayer.seekTo(PlayConfig.currentPosition);
                                    PlayConfig.currentPosition=0;
                                }else if(!PlayConfig.isPlaying&&id!=PlayConfig.playingId)
                                {
                                    PlayConfig.currentPosition=0;
                                }
                                PlayConfig.isPlaying=true;
                                PlayConfig.playFlag=1;
                                PlayConfig.playingId =id;
                                btn_main.setBackground(context.getResources().getDrawable(R.drawable.ic_pause));
                                mHandler.post(mRunnable);
                            }

                            @Override
                            public void Loading(SMediaPlayer mediaPlayer, int i) {

                            }

                            @Override
                            public void onCompletion(SMediaPlayer mediaPlayer) {
                                if(PlayConfig.isSingle)
                                {
                                    mediaPlayer.start();
                                    mLyricView.resume();
                                    mediaPlayer.seekTo(0);
                                }else if(PlayConfig.isSingleList)
                                {

                                }else if(PlayConfig.isRandomList)
                                {

                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(context, ErrCode.PLAYERRCODE, Toast.LENGTH_SHORT).show();
                            }

                        });
            }
            else if (SPlayer.instance().isPlaying()&&id == PlayConfig.playingId){
                String timeall=PlayConfig.timeAllCount();
                PlayConfig.playFlag=1;
                tv_time.setText(timeall);
                mHandler.post(mRunnable);
            }
        }else if(PlayConfig.playFlag==3)
        {
            String timeall=PlayConfig.timeAllCount();
            tv_time.setText(timeall);
            String timeend=PlayConfig.timeEndCount();
            tv_timeend.setText(timeend);
            seekBar.setProgress(PlayConfig.currentPosition);
            if(!PlayConfig.isPlaying&&id == PlayConfig.playingId)
            {
                //还原上次播放进度
                PlayConfig.mediaPlayer.seekTo(PlayConfig.currentPosition);
                PlayConfig.currentPosition=0;
            }
            PlayConfig.playFlag=1;
            mHandler.post(mRunnable);
        }

    }

    //TODO 子线程更新ui
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

//            Log.d("总时长","总时长"+mediaPlayer.getDuration());
            seekBar.setMax(PlayConfig.mediaPlayer.getDuration());
            int currentPosition = PlayConfig.mediaPlayer.getCurrentPosition();
            mLyricView.updateTime(currentPosition);
//            Log.d("当前点","当前播放位置"+currentPosition);
            seekBar.setProgress(currentPosition);
            String timeend=PlayConfig.timeEndCount();
            tv_timeend.setText(timeend);
            mHandler.postDelayed(this, 100);

        }
    };


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    //TODO 接受粘性事件
    @Override
    protected void receiveStickyEvent(MessageEvent event) {
        switch (event.getCode()) {
            case C.EventCode.PLAYINFO:
                music= (MusicInfo) event.getData();
                id=music.getId();
                PlayConfig.playNow=music;
                break;
            case C.EventCode.SEARCHRSULTTOPALY:
                MusicPageInfo info=(MusicPageInfo)event.getData();
                id=info.getId();
                music=new MusicInfo(info.getId(),info.getName(),info.getAlbumId(),info.getAlbum(),info.getAlbumPic(),info.getAlbumPic120(),info.getArtist(),info.getArtistId(),info.getArtistPic(),0,null,info.getIsMv());
                PlayConfig.playNow=music;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.play_tv_btn_playmode:
                if (PlayConfig.isSingle)
                {
                    //单曲
                    btn_playmode.setBackground(this.getDrawable(R.drawable.ic_play_single));
                }else if(PlayConfig.isRandomList)
                {
                    //随机
                    btn_playmode.setBackground(this.getDrawable(R.drawable.ic_play_random));
                }else if(PlayConfig.isSingleList)
                {
                    //列表
                    btn_playmode.setBackground(this.getDrawable(R.drawable.ic__xunhuan));
                }
                break;
            case R.id.play_tv_btn_last:

                break;
            case R.id.play_tv_btn_main:
                if(SPlayer.instance().isPlaying())
                {
                    PlayConfig.isPlaying=false;
                    SPlayer.instance().pause(); //暂停
                    PlayConfig.currentPosition=PlayConfig.mediaPlayer.getCurrentPosition();
                    btn_main.setBackground(this.getResources().getDrawable(R.drawable.ic_play));
                }else
                {
                    if(PlayConfig.mediaPlayer==null)
                    {
                        ACache mCache=ACache.get(context);
                        SPlayer.instance().useWakeMode(false)//是否使用环形锁,默认不使用
                                .useWifiLock(false)//是否使用wifi锁,默认不使用
                                .setUseCache(true)//是否使用缓存,默认开启
                                .playByUrl(mCache.getAsString("audiourl&"+id), new PlayerListener() {
                                    @Override
                                    public void LoadSuccess(SMediaPlayer mediaPlayer) {
                                        PlayConfig.mediaPlayer=SPlayer.instance().getMediaPlayer();
                                        mediaPlayer.start();
                                        PlayConfig.mediaPlayer=SPlayer.instance().getMediaPlayer();
                                        String timeall=PlayConfig.timeAllCount();
                                        tv_time.setText(timeall);
                                        String timeend=PlayConfig.timeEndCount();
                                        PlayConfig.currentPosition=0;
                                        PlayConfig.isPlaying=true;
                                        PlayConfig.playFlag=1;
                                        PlayConfig.playingId =id;
                                        mHandler.post(mRunnable);
                                    }

                                    @Override
                                    public void Loading(SMediaPlayer mediaPlayer, int i) {

                                    }

                                    @Override
                                    public void onCompletion(SMediaPlayer mediaPlayer) {
                                        if(PlayConfig.isSingle)
                                        {
                                            mediaPlayer.start();
                                            mediaPlayer.seekTo(0);
                                        }else if(PlayConfig.isSingleList)
                                        {

                                        }else if(PlayConfig.isRandomList)
                                        {

                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                    }

                                });
                    }else
                    {
                        PlayConfig.isPlaying=true;
                        SPlayer.instance().start(); //继续
                    }

                    mLyricView.resume();
                    btn_main.setBackground(this.getResources().getDrawable(R.drawable.ic_pause));
                }
                break;
            case R.id.play_tv_btn_next:

                break;
            case R.id.play_tv_btn_playlist:

                break;
            case R.id.activity_play_tv_btn_down:
                new Thread() {
                    public void run() {
                        try {
                            Instrumentation inst = new Instrumentation();
                            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                        } catch (Exception e) {

                        }
                    }
                }.start();
                break;
            case R.id.activity_play_tv_btn_like:
                if(!isFavourite)
                {
                    btn_like.setBackground(this.getDrawable(com.lib_searchview.R.drawable.ic_like_select));
                    PlayListInfo info=jclassChange(music, PlayListConfig.favouriteList,mCache.getAsString("audiourl&"+music.getId()));
                    DaoHelper.insert(info);
                    isFavourite=true;
                }else
                {
                    btn_like.setBackground(this.getDrawable(com.lib_searchview.R.drawable.ic_like));
                    DaoHelper.delete(playListInfo.getId());
                    isFavourite=false;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(mRunnable);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public PlayListInfo jclassChange(MusicInfo info,String playListName,String link)
    {
        PlayListInfo listInfo=new PlayListInfo(null,info.getId(),info.getName(),info.getArtist(),info.getArtistId(),info.getAlbum(),info.getAlbumPic(),link,info.getAlbumId(),info.getIsMv(),playListName);
        return listInfo;
    }
}