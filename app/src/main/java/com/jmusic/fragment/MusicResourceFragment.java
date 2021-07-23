package com.jmusic.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jmusic.R;
import com.jmusic.util.FragmentUtils;
import com.jmusic.util.SearchBar;
import com.lib_common.bean.Constance;

@Route(path = Constance.FRAGMENT_URL_PLAY)
public class MusicResourceFragment extends Fragment {

    Activity activity;
    SearchBar searchBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musicsource, container, false);
        activity=this.getActivity();
        searchBar=(SearchBar)view.findViewById(R.id.music_search_bar);
        initView();
        return view;
    }
    void initView()
    {
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(Constance.ACTIVITY_URL_SEARCHMAIN).navigation();
            }
        });
    }
}
