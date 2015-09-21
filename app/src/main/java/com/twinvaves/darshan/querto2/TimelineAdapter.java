package com.twinvaves.darshan.querto2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by darshan on 18/09/15.
 */


public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.Holder> {

    Context context;
    //    ArrayList<DataClass> list = fetchTimeline.dataList;
    DisplayImageOptions defaultOptions;
    ImageLoaderConfiguration config;


    public TimelineAdapter(Context context) {
        this.context = context;

        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
//                .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
                .displayer(new SimpleBitmapDisplayer()).build();

        config = new ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_view, parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.usernameTv.setText(fetchTimeline.dataList.get(position).UserName);
        holder.questionTv.setText(fetchTimeline.dataList.get(position).Question);
        ImageLoader.getInstance().displayImage(fetchTimeline.dataList.get(position).UserImage, holder.circleimageview, defaultOptions);
    }

    @Override
    public int getItemCount() {
        return fetchTimeline.dataList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView questionTv, usernameTv;
        CircleImageView circleimageview;

        public Holder(View itemView) {
            super(itemView);
            circleimageview = (CircleImageView) itemView.findViewById(R.id.userImage);
            questionTv = (TextView) itemView.findViewById(R.id.question);
            usernameTv = (TextView) itemView.findViewById(R.id.userName);
        }
    }
}
