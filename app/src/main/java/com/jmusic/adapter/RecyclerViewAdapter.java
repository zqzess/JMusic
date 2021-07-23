package com.jmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jmusic.R;
import com.lib_common.bean.C;
import com.jmusic.bean.MusicInfo;
import com.lib_common.bean.Constance;
import com.lib_common.bean.MessageEvent;
import com.lib_common.util.EventBusUtil;
import com.lib_common.util.ScreenUtil;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder>{

    private ArrayList<MusicInfo> dataList = new ArrayList<>();
//    private final double STANDARD_SCALE = 1.1; //当图片宽高比例大于STANDARD_SCALE时，采用3:4比例，小于时，则采用1:1比例
//    private final float SCALE = 4 * 1.0f / 3;
    Context context;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
//        this.dataList = dataList;
    }

    public void replaceAll(ArrayList<MusicInfo> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addData(int position,ArrayList<MusicInfo> list)
    {
        dataList.addAll(position,list);
        notifyItemInserted(position);
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_pullloadmorerecyclerview, parent, false);

//        return new OneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_pullloadmorerecyclerview, parent, false));
        return new OneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.BaseViewHolder holder, int position) {
        holder.setData(dataList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }
    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
        void setData(MusicInfo data,int position) {
        }
    }

    private class OneViewHolder extends BaseViewHolder {
        private ImageView ivImage;
        private TextView tv_name;
        private TextView tv_author;
        public OneViewHolder(View view) {
            super(view);
            ivImage = (ImageView) view.findViewById(R.id.recyclerview_img);
            tv_name=(TextView) view.findViewById(R.id.recyclerview_tv_name);
            tv_author=(TextView) view.findViewById(R.id.recyclerview_tv_author);

//            int width = ((Activity) ivImage.getContext()).getWindowManager().getDefaultDisplay().getWidth();
//            ViewGroup.LayoutParams params = ivImage.getLayoutParams();
            //设置图片的相对于屏幕的宽高比
//            params.width = width/2;
            //eg1
//            Random random = new Random();
//            int num=random.nextInt(400)%(400-200+1) + 200;
//            params.height =  (int) (200 + num) ;
            //weg2
////            params.height =  (int) (200 + Math.random() * 400) ;
            //eg3
//            for(int i=0;i<dataList.size();i++)
//            {
//                params.height=(i % 2)*100 + 400;
//            }

//            ivImage.setLayoutParams(params);


        }
        @Override
        void setData(MusicInfo data,int position) {
            if (data != null) {


                //计算图片宽高
            ViewGroup.LayoutParams params  = (LinearLayout.LayoutParams) ivImage.getLayoutParams();
                //2列的瀑布流，屏幕宽度减去两列间的间距space所的值再除以2，计算出单列的imageview的宽度，space的值在RecyclerView初始化时传入
            float itemWidth = (ScreenUtil.getScreenWidth(context) - 8*3) / 2;

            params.width = (int) itemWidth;


               if(position%2==0)
               {
                   params.height= 678;
               }else
               {
                   params.height= 453;
               }
//               params.height=(position % 2)*100 + 600;
//                params.height =  (int) (200 + Math.random() * 400) ;
               ivImage.setLayoutParams(params);


                ivImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBusUtil.sendStickyEvent(new MessageEvent(C.EventCode.PLAYINFO,dataList.get(position)));
                        ARouter.getInstance().build(Constance.ACTIVITY_URL_PLAYMUSIC)
                                .withTransition(R.anim.slide_in_bottom,0)
                                .navigation(context);
                    }
                });

                String name = data.getName();
                String author = data.getArtist();
                String piclink = data.getAlbumPic();

                tv_name.setText(name);
                if(author.length()>20)
                {
                    tv_author.setText("群星");
                }else
                {
                    tv_author.setText(author);
                }


                Glide.with(itemView.getContext()).load(piclink).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_launcher).crossFade().into(ivImage);
            }
        }
    }

}
