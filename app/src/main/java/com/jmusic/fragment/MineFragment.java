package com.jmusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jmusic.R;
import com.jmusic.util.SearchBar;
import com.lib_common.bean.C;
import com.lib_common.bean.Constance;
import com.lib_common.bean.MessageEvent;
import com.lib_common.customView.VericalIconTextView;
import com.lib_common.util.EventBusUtil;
import com.lib_dao.config.PlayListConfig;
import com.lib_dao.db.DaoHelper;

@Route(path = Constance.FRAGMENT_URL_ME)
public class MineFragment extends Fragment {
    TextView btn_more;
    SearchBar btn_search;
    Context context;
    VericalIconTextView btn_like;
    VericalIconTextView btn_history;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        btn_more=(TextView)view.findViewById(R.id.fragment_mine_tv_btn_more);
        btn_like=(VericalIconTextView)view.findViewById(R.id.fragment_mine_tv_btn_like);
        btn_search=(SearchBar)view.findViewById(R.id.fragment_mine_search_bar);
        btn_history=(VericalIconTextView)view.findViewById(R.id.fragment_mine_tv_btn_history);
        context=this.getActivity();
        initView(view);
        return view;
    }
    void initView(View view)
    {
        int favoutiteSize=DaoHelper.searchAllwithForm(PlayListConfig.favouriteList).size();
        int historySize=DaoHelper.searchAllwithForm(PlayListConfig.historyList).size();
        btn_like.setViewText2(favoutiteSize+"");
        btn_history.setViewText2(historySize+"");
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(Constance.ACTIVITY_URL_SETTING).navigation();
            }
        });
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusUtil.sendStickyEvent(new MessageEvent(C.EventCode.PLAYLISTNAME, PlayListConfig.favouriteList));
                ARouter.getInstance().build(Constance.ACTIVITY_URL_PLAYLIST).navigation();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(Constance.ACTIVITY_URL_SEARCHMAIN).navigation();
            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusUtil.sendStickyEvent(new MessageEvent(C.EventCode.PLAYLISTNAME, PlayListConfig.historyList));
                ARouter.getInstance().build(Constance.ACTIVITY_URL_PLAYLIST).navigation();
            }
        });
    }
}
