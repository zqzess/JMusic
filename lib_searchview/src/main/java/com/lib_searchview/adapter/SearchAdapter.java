package com.lib_searchview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * author : zqzess
 * github : https://github.com/zqzess
 * date   : 2021/7/20 16:00
 * desc   :
 * version: 1.0
 * project: SearchView
 */
public class SearchAdapter extends BaseAdapter {
    List<String> searchList=new ArrayList<>();
    Activity activity;
    int searchListType;   //0为热搜，1为搜索推荐

    public SearchAdapter(List<String> searchList, Activity activity, int searchListType) {
        this.searchList = searchList;
        this.activity = activity;
        this.searchListType = searchListType;
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
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_search, null);
            holder = new ViewHolder();
            holder.tv_icon = (TextView) convertView.findViewById(R.id.lv_search_tv_icon);
            holder.tv_context = (TextView) convertView.findViewById(R.id.lv_search_tv_text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(searchListType==0)
        {
            holder.tv_icon.setText(position+1+"");
            holder.tv_icon.setBackground(null);
        }else if(searchListType==1)
        {
            holder.tv_icon.setBackground(activity.getDrawable(R.drawable.ic_action_search));
        }
        holder.tv_context.setText(searchList.get(position));

        holder.tv_context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
    private class ViewHolder {
        TextView tv_icon;
        TextView tv_context;
    }
}
