package com.jmusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jmusic.R;
import com.jmusic.bean.MusicInfo;
import com.lib_common.bean.C;
import com.lib_common.bean.Constance;
import com.lib_common.bean.MessageEvent;
import com.lib_common.util.EventBusUtil;
import com.lib_dao.bean.PlayListInfo;
import com.lib_searchview.bean.MusicPageInfo;

import java.util.List;

/**
 * author : zqzess
 * github : https://github.com/zqzess
 * date   : 2021/7/26 16:18
 * desc   :
 * version: 1.0
 * project: JMusic
 */
public class PlayListAdapter extends BaseAdapter {
    List<PlayListInfo> list;
    Activity activity;

    public PlayListAdapter(List<PlayListInfo> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater=activity.getLayoutInflater();
        if (convertView==null)
        {
            convertView = inflater.inflate(R.layout.listview_playlist, null);
            holder = new PlayListAdapter.ViewHolder();
            holder.tv_count = (TextView) convertView.findViewById(R.id.lv_playlist_tv_count);
            holder.tv_name = (TextView) convertView.findViewById(R.id.lv_playlist_tv_name);
            holder.tv_author = (TextView) convertView.findViewById(R.id.lv_playlist_tv_author);
            holder.btn_more = (TextView) convertView.findViewById(R.id.lv_playlist_tv_more);
            convertView.setTag(holder);
        }else {
            holder = (PlayListAdapter.ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(list.get(position).getSongName());
        holder.tv_author.setText(list.get(position).getArtist());
        holder.tv_count.setText(position+1+"");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicInfo info=jclassChange(list.get(position));
                EventBusUtil.sendStickyEvent(new MessageEvent(C.EventCode.PLAYINFO,info));
                ARouter.getInstance().build(Constance.ACTIVITY_URL_PLAYMUSIC).navigation();
            }
        });
        return convertView;
    }
    private class ViewHolder
    {
        TextView tv_count;
        TextView tv_name;
        TextView tv_author;
        TextView btn_more;
    }
    public MusicInfo jclassChange(PlayListInfo info)
    {
        MusicInfo listInfo=new MusicInfo(info.getMusicId(),info.getSongName(),info.getAlbumId(),info.getAlbum(),info.getAlbumPic(),"",info.getArtist(),info.getArtistId(),"",0,"",info.getIsMv());
        return listInfo;
    }
}
