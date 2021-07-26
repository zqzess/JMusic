package com.lib_searchview.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lib_common.bean.C;
import com.lib_common.bean.Constance;
import com.lib_common.bean.MessageEvent;
import com.lib_common.cache.ACache;
import com.lib_common.customView.ShareBottomPopupDialog;
import com.lib_common.util.EventBusUtil;
import com.lib_dao.bean.PlayListInfo;
import com.lib_dao.config.PlayListConfig;
import com.lib_dao.db.DaoHelper;
import com.lib_searchview.R;
import com.lib_searchview.bean.MusicPageInfo;

import org.raphets.roundimageview.RoundImageView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * author : zqzess
 * github : https://github.com/zqzess
 * date   : 2021/7/22 8:25
 * desc   :
 * version: 1.0
 * project: JMusic
 */
public class SearchResultAdapter extends BaseAdapter {
    List<MusicPageInfo> searchList=new ArrayList<>();
    Activity activity;
    LinearLayout all_layout;
    boolean isFavourite=false;
    PlayListInfo playListInfo;
    ACache mCache;

    public SearchResultAdapter(List<MusicPageInfo> searchList, Activity activity, LinearLayout all_layout) {
        this.searchList = searchList;
        this.activity = activity;
        this.all_layout = all_layout;
    }

    @Override
    public int getCount() {
        return searchList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchList.get(position);
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
            convertView = inflater.inflate(R.layout.listview_searchresult, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.searc_result_tv_name);
            holder.tv_author = (TextView) convertView.findViewById(R.id.searc_result_tv_author);
            holder.btn_more = (TextView) convertView.findViewById(R.id.searc_result_tv_btn_more);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(searchList.get(position).getName());
        holder.tv_author.setText(searchList.get(position).getArtist());
        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(position);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusUtil.sendStickyEvent(new MessageEvent(C.EventCode.SEARCHRSULTTOPALY,searchList.get(position)));
                ARouter.getInstance().build(Constance.ACTIVITY_URL_PLAYMUSIC).withTransition(R.anim.in_from_bottom,0).navigation();
            }
        });
        return convertView;
    }
    private class ViewHolder
    {
        TextView tv_name;
        TextView tv_author;
        TextView btn_more;
    }
    private void showDialog(int position)
    {
        mCache=ACache.get(activity);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.bottom_dialog_search_main, null);
        final ShareBottomPopupDialog shareBottomPopupDialog = new ShareBottomPopupDialog(activity, dialogView);
        shareBottomPopupDialog.showPopup(all_layout);
        RoundImageView iv_icon = (RoundImageView) dialogView.findViewById(R.id.bottom_dialog_icon);
        TextView tv_name = (TextView) dialogView.findViewById(R.id.bottom_dialog_tv_name);
        TextView tv_author = (TextView) dialogView.findViewById(R.id.bottom_dialog_tv_author);
        TextView btn_like = (TextView) dialogView.findViewById(R.id.bottom_dialog_tv_favourite);
        TextView btn_add = (TextView) dialogView.findViewById(R.id.bottom_dialog_tv_btn_add);
        TextView btn_close = (TextView) dialogView.findViewById(R.id.bottom_dialog_tv_btn_close);
        Glide.with(iv_icon.getContext()).load(searchList.get(position).getAlbumPic()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_launcher).crossFade().into(iv_icon);
        tv_name.setText(searchList.get(position).getName());
        tv_author.setText(searchList.get(position).getArtist());
        if(DaoHelper.searchwithFavouriteForm(searchList.get(position).getId())!=null)
        {
            btn_like.setBackground(activity.getDrawable(R.drawable.ic_like_select));
            playListInfo=DaoHelper.searchwithFavouriteForm(searchList.get(position).getId());
            isFavourite=true;
        }else
        {
            btn_like.setBackground(activity.getDrawable(R.drawable.ic_like));
            isFavourite=false;
        }
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFavourite)
                {
                    btn_like.setBackground(activity.getDrawable(R.drawable.ic_like_select));
                    PlayListInfo info=jclassChange(searchList.get(position), PlayListConfig.favouriteList,mCache.getAsString("audiourl&"+searchList.get(position).getId()));
                    DaoHelper.insert(info);
                    isFavourite=true;
                }else
                {
                    btn_like.setBackground(activity.getDrawable(R.drawable.ic_like));
                    DaoHelper.delete(playListInfo.getId());
                    isFavourite=false;
                }

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBottomPopupDialog.dismiss();
            }
        });
    }
    public PlayListInfo jclassChange(MusicPageInfo info,String playListName,String link)
    {
        PlayListInfo listInfo=new PlayListInfo(null,info.getId(),info.getName(),info.getArtist(),info.getArtistId(),info.getAlbum(),info.getAlbumPic(),link,info.getAlbumId(),info.getIsMv(),playListName);
        return listInfo;
    }
}
