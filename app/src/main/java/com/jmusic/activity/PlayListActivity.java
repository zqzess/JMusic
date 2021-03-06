package com.jmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.a520wcf.yllistview.YLListView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.jmusic.R;
import com.jmusic.adapter.PlayListAdapter;
import com.lib_common.base.BaseActivity;
import com.lib_common.bean.C;
import com.lib_common.bean.Constance;
import com.lib_common.bean.MessageEvent;
import com.lib_common.util.EventBusUtil;
import com.lib_dao.bean.PlayListInfo;
import com.lib_dao.config.PlayListConfig;
import com.lib_dao.db.DaoHelper;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

@Route(path = Constance.ACTIVITY_URL_PLAYLIST)
public class PlayListActivity extends BaseActivity {

    @BindView(R.id.activity_playlist_listView)
    ListView listView;
    @BindView(R.id.activity_playlist_topbar_name)
    TextView tv_name;
    @BindView(R.id.activity_playlist_topbar_back)
    TextView btn_back;

    List<PlayListInfo> list;
    PlayListAdapter adapter;
    String playListName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_play_list);
    }

    @Override
    protected void initViews() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void onStickyEventBusCome(MessageEvent event) {
        switch (event.getCode())
        {
            case C.EventCode.PLAYLISTNAME:
                playListName=(String)event.getData();
                list= DaoHelper.searchAllwithForm(playListName);
                if(playListName.equals("favourite"))
                {
                    tv_name.setText("????????????");
                }else if(playListName.equals("history"))
                {
                    tv_name.setText("????????????");
                    Collections.reverse(list);//??????
                }else
                {
                    tv_name.setText(playListName);
                }
                adapter=new PlayListAdapter(list,this);
                listView.setAdapter(adapter);

        }
    }
}