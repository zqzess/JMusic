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
import com.lib_common.bean.Constance;

@Route(path = Constance.FRAGMENT_URL_ME)
public class MineFragment extends Fragment {
    TextView btn_more;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        btn_more=(TextView)view.findViewById(R.id.fragment_mine_tv_btn_more);
        context=this.getActivity();
        initView(view);
        return view;
    }
    void initView(View view)
    {
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(Constance.ACTIVITY_URL_SETTING).navigation();
            }
        });
    }
}
