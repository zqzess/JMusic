package com.lib_dao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.lib_dao.bean.PlayListInfo;
import com.lib_dao.config.PlayListConfig;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * author : zqzess
 * github : https://github.com/zqzess
 * date   : 2021/7/26 10:30
 * desc   :
 * version: 1.0
 * project: JMusic
 */
public class DaoHelper {
    //数据库名称
    private static final String DATABASE_NAME = "playlist.db";
    //用来获取Dao的
    private static DaoSession daoSession;
    // 加上 volatile 关键字，禁止指令重排
    private static volatile DaoHelper mDaoHelper;
    private static PlayListInfoDao playListInfoDao;

    public DaoHelper(Context context) {
        MyOpenHelper helper = new MyOpenHelper(context, DATABASE_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        this.playListInfoDao = daoSession.getPlayListInfoDao();
    }
    //单例模式（双检锁+volatile）
    public static DaoHelper getInstance(Context context){
        if (mDaoHelper == null){
            synchronized (DaoHelper.class){
                if (mDaoHelper == null) {
                    mDaoHelper = new DaoHelper(context);
                    return mDaoHelper;
                }
            }
        }
        return mDaoHelper;
    }

    //是否开启Log
    public void setDebugMode(boolean flag) {
        MigrationHelper.DEBUG = true;//如果查看数据库更新的Log，请设置为true
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    //插入
    public static Long insert(PlayListInfo info)
    {
        return playListInfoDao.insert(info);
    }
    public static Long insertOrReplace(PlayListInfo info)
    {
        return playListInfoDao.insertOrReplace(info);
    }

    //删除
    public static void delete(Long id)
    {
        playListInfoDao.queryBuilder().where(PlayListInfoDao.Properties.Id.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities();
    }
    //更新
    public static void update(PlayListInfo playListInfo)
    {
        PlayListInfo old=playListInfoDao.queryBuilder().where(PlayListInfoDao.Properties.Id.eq(playListInfo.getId())).build().unique();
        if(old!=null)
        {
            playListInfoDao.update(playListInfo);
        }
    }
    //查询
    public static List<PlayListInfo> searchAllwithForm(String form)
    {
        List<PlayListInfo> info=playListInfoDao.queryBuilder().where(PlayListInfoDao.Properties.Form.eq(form)).list();
        return info;
    }
    public static PlayListInfo searchwithFavouriteForm(long musicId)
    {
        PlayListInfo info=playListInfoDao.queryBuilder().where(PlayListInfoDao.Properties.Form.eq(PlayListConfig.favouriteList),PlayListInfoDao.Properties.MusicId.eq(musicId)).build().unique();
//        if(info.getId()!=null)
//        {
//            return info.getId();
//        }else
//        {
//            return null;
//        }
        return info;
    }
}
