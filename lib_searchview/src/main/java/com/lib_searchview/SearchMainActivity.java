package com.lib_searchview;

import android.app.Instrumentation;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.coorchice.library.SuperTextView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lib_common.base.BaseActivity;
import com.lib_common.bean.C;
import com.lib_common.bean.Constance;
import com.lib_common.bean.ErrCode;
import com.lib_common.bean.MessageEvent;
import com.lib_common.bean.NetString;
import com.lib_common.cache.ACache;
import com.lib_common.net.HttpRequestManage;
import com.lib_common.util.HeadersUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lib_dao.db.DaoHelper;
import com.lib_searchview.adapter.SearchAdapter;
import com.lib_searchview.adapter.SearchResultAdapter;
import com.lib_searchview.bean.MusicPageInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author: zqzess
 *
 * @time: 2021/7/22 15:14
 *
 * @github: https://github.com/zqzess
**/

@Route(path = Constance.ACTIVITY_URL_SEARCHMAIN)
public class SearchMainActivity extends BaseActivity {

    @BindView(R2.id.search_edittext)
    ClearEditTextView editText;

    @BindView(R2.id.lv_searchtip)
    ListView searchTip;

    @BindView(R2.id.lv_hotsearch)
    ListView searchHot;

    @BindView(R2.id.lv_searchresoult)
    ListView searchResoult;

    @BindView(R2.id.search_labellinerlayout)
    LinearLayout linearLayoutlabel;

    @BindView(R2.id.search_linearlayout_bar)
    LinearLayout linearLayoutbar;

    @BindView(R2.id.search_hot_linearlayout)
    LinearLayout linearLayouthot;

    @BindView(R2.id.search_label)
    LabelLayoutView labelLayoutView;

    @BindView(R2.id.search_topbar_back)
    TextView btn_back;

    @BindView(R2.id.search_main_topbar_search)
    TextView btn_search;

    List<String> searchHotList=new ArrayList<>();   //热搜
    List<String> searchTipList=new ArrayList<>();   //搜索推荐
    List<MusicPageInfo> searchList=new ArrayList<>();//搜索结果
    String[] labelList=new String[5];   //搜索历史
    SearchAdapter searchAdapter;
    SearchResultAdapter searchResultAdapter;
    ACache mCache;
    Context context;
    LinearLayout all_layout;
    KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchTip.setVisibility(View.GONE);
        searchResoult.setVisibility(View.GONE);
        linearLayoutlabel.setVisibility(View.VISIBLE);
        linearLayouthot.setVisibility(View.VISIBLE);
        initLabel();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_search_main);
        all_layout=findViewById(R.id.search_main);
    }

    @Override
    protected void initViews() {
        editText.setText("");
        context=this;
        mCache=ACache.get(this);
        initData();
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (editText.getCompoundDrawables()[2]!= null){
                    //当抬起时
                    if (event.getAction()== MotionEvent.ACTION_UP){
                        //当点击位置的X坐标大于EditText宽度减去右间距减去清除图标的宽度并且小于EditText宽度减去右间距之间的区域，即清除图标的位置
                        boolean xTouchable= event.getX()> (editText.getWidth()- editText.getPaddingRight()- editText.mClearDrawable.getIntrinsicWidth())
                                &&(event.getX()< (editText.getWidth()- editText.getPaddingRight()));
                        boolean yTouchable= event.getY()> (editText.getHeight()- editText.getPaddingBottom()- editText.mClearDrawable.getIntrinsicHeight())
                                &&(event.getY()< (editText.getHeight()+ editText.mClearDrawable.getIntrinsicHeight())/ 2);
                        //清除文本
                        if (xTouchable&& yTouchable){
                            editText.setText("");
                            labelList=null;
                            labelList=new String[5];
                            labelLayoutView.removeAllViews();
                            initLabel();
                            linearLayoutlabel.setVisibility(View.VISIBLE);
                            linearLayouthot.setVisibility(View.VISIBLE);
//                            if (searchTipList.size()!= 0) {
//                                searchTipList.clear();
//                                searchAdapter.notifyDataSetChanged();
//                                searchTip.setVisibility(View.GONE);
//                            }
                        }
                    }
                }
                return false;
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH){
                    if ("".equals(editText.getText().toString())){
                        Toast.makeText(getApplicationContext(), "请输入搜索内容", Toast.LENGTH_SHORT).show();
                    }else {
                        ((InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                        searchList.clear();
                        searchResoult.setVisibility(View.VISIBLE);
                        linearLayoutlabel.setVisibility(View.GONE);
                        linearLayouthot.setVisibility(View.GONE);
                        save(editText.getText().toString());
                        searchList.clear();
                        searchEvent();
//                        searchTip.setVisibility(View.VISIBLE);
//                        searchAdapter= new SearchAdapter(searchList,SearchMainActivity.this,1);
//                        searchTip.setAdapter(searchAdapter);
                    }
                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.getText().toString().isEmpty())
                {
                    if(mCache.getAsString("search_history")!=null)
                    {
                        labelList=null;
                        labelList=new String[5];
                        labelLayoutView.removeAllViews();
                        initLabel();
                        linearLayoutlabel.setVisibility(View.VISIBLE);
                    }
                    linearLayouthot.setVisibility(View.VISIBLE);
                    searchResoult.setVisibility(View.GONE);
                }else
                {

                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(editText.getText().toString())){
                    Toast.makeText(getApplicationContext(), "请输入搜索内容", Toast.LENGTH_SHORT).show();
                }else {
                    ((InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                        searchList.clear();
                    searchResoult.setVisibility(View.VISIBLE);
                    linearLayoutlabel.setVisibility(View.GONE);
                    linearLayouthot.setVisibility(View.GONE);
                    save(editText.getText().toString());
                    searchList.clear();
                    searchEvent();
//                        searchTip.setVisibility(View.VISIBLE);
//                        searchAdapter= new SearchAdapter(searchList,SearchMainActivity.this,1);
//                        searchTip.setAdapter(searchAdapter);
                }
            }
        });
    }
    private void initLabel() {
        init_create();
        ViewGroup.MarginLayoutParams layoutParams= new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 30, 10, 10);
//        for (int i= 0; i< labelList.length; i++){
        for (int i= labelList.length-1; i>=0; i--){
            SuperTextView textView= new SuperTextView(this);
            textView.setTag(i);
            textView.setTextSize(13);
            textView.setText(labelList[i]);
            textView.setCorner(40);
            textView.setStrokeWidth(1);
            textView.setStrokeColor(getResources().getColor(R.color.courseTable3));
            textView.setPadding(25,10, 25, 10);
            textView.setTextColor(getResources().getColor(R.color.courseTable4));
            labelLayoutView.addView(textView, layoutParams);
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayoutbar.setFocusable(true);
                    linearLayoutbar.setFocusableInTouchMode(true);
                    linearLayoutbar.requestFocus();
                    editText.getText().clear();
                    editText.setText(labelList[finalI]);
                    ((InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    searchList.clear();
                    linearLayoutlabel.setVisibility(View.GONE);
                    linearLayouthot.setVisibility(View.GONE);
                    searchResoult.setVisibility(View.VISIBLE);
                    searchEvent();
                }
            });
        }
    }

    private void init_create() {
        // 获取搜索记录文件内容
        String history = mCache.getAsString("search_history");
        if(history!=null)
        {
            linearLayoutlabel.setVisibility(View.VISIBLE);
            // 用逗号分割内容返回数组
            String[] history_arr = history.split(",");

            // 保留前50条数据
            if (history_arr.length > 5) {
                // 实现数组之间的复制
                System.arraycopy(history_arr, history_arr.length-5, labelList, 0, 5);
                String tmp="";
                for(int i=0;i<5;i++)
                {
                    tmp=tmp+labelList[i]+",";
                }
                mCache.put("search_history", tmp);
            }else
            {
                labelList=history_arr;
            }
        }else
        {
            linearLayoutlabel.setVisibility(View.GONE);
        }


    }

    public void save(String cache) {
        // 获取搜索框信息
        String old_text = mCache.getAsString("search_history");
        if(old_text==null)
        {
            mCache.put("search_history", cache+",");
        }else
        {
            // 利用StringBuilder.append新增内容，逗号便于读取内容时用逗号拆分开
            StringBuilder builder = new StringBuilder(old_text);
            builder.append(cache + ",");
            // 判断搜索内容是否已经存在于历史文件，已存在则不重复添加
            if (!old_text.contains(cache + ",")) {
                mCache.put("search_history", builder.toString());
            }
        }
    }

    private void initData()
    {
        if(mCache.getAsString("searchhot")!=null)
        {
            try {
                jsonParse(mCache.getAsString("searchhot"));
                searchAdapter=new SearchAdapter(searchHotList,SearchMainActivity.this,0);
                searchHot.setAdapter(searchAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else
        {
            hud=KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            HttpRequestManage.gzipRequest(NetString.searchHot, HeadersUtil.MUSICINFOGZIP, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        mCache.put("searchhot", response, 2 * ACache.TIME_DAY);
                        jsonParse(response);
                        searchAdapter=new SearchAdapter(searchHotList,SearchMainActivity.this,0);
                        searchHot.setAdapter(searchAdapter);
                        hud.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, ErrCode.NETERRCODE,Toast.LENGTH_SHORT);
                }
            });
        }
    }
    private void jsonParse(String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        JSONObject data=jsonObject.getJSONObject("data");
        JSONArray resultList=data.getJSONArray("resultList");
        for (int i = 0; i < resultList.length(); i++) {
            JSONObject object = resultList.getJSONObject(i);
            String key=object.getString("key");
            String describe=object.getString("describe");
            searchHotList.add(key+","+describe);
        }
    }
    //TODO 查找
    private void searchEvent()
    {
        String keyword =editText.getText().toString();
        if(!keyword.isEmpty())
        {
            hud=KProgressHUD.create(SearchMainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
            NetString.setSearchUrl(keyword);
            HttpRequestManage.gzipRequest(NetString.getSearchUrl(), HeadersUtil.MUSICINFOGZIP, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject data=jsonObject.getJSONObject("data");
                        JSONArray musicPage=data.getJSONArray("musicPage");
                        for (int i = 0; i < musicPage.length(); i++) {
                            JSONObject object = musicPage.getJSONObject(i);
                            String album=object.getString("album");
                            long albumId=object.getLong("albumId");
                            String albumPic=object.getString("albumPic");
                            String songName=object.getString("songName");
                            String musicRid=object.getString("musicRid");
                            musicRid=musicRid.replace("MUSIC_","");
                            long musicid=Long.parseLong(musicRid);
                            String artist=object.getString("artist");
                            if(artist.length()>20)
                            {
                                artist="群星";
                            }
                            long artistId=object.getLong("artistId");
                            String albumPic120=object.getString("albumPic120");
                            int isMv=object.getInt("isMv");
//                            int isNew=object.getInt("isNew");
//                            int isOriginal=object.getInt("isOriginal");
                            MusicPageInfo musicPageInfo=new MusicPageInfo(musicid,songName,albumId,album,albumPic,albumPic120,artist,artistId,"",0,null,isMv);
                            searchList.add(musicPageInfo);
                        }
                        searchResultAdapter=new SearchResultAdapter(searchList,SearchMainActivity.this,all_layout);
                        searchResoult.setAdapter(searchResultAdapter);
                        hud.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, ErrCode.NETERRCODE,Toast.LENGTH_SHORT);
                }
            });
        }

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveStickyEvent(MessageEvent event) {
        switch (event.getCode()) {
            case C.EventCode.SEARCHHOTANDTIP:
                String searchtext=(String)event.getData();
                linearLayoutbar.setFocusable(true);
                linearLayoutbar.setFocusableInTouchMode(true);
                linearLayoutbar.requestFocus();
                editText.getText().clear();
                editText.setText(searchtext);
                ((InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                searchList.clear();
                linearLayoutlabel.setVisibility(View.GONE);
                linearLayouthot.setVisibility(View.GONE);
                searchResoult.setVisibility(View.VISIBLE);
                searchEvent();
                break;
        }
    }
}