package com.example.jeffersonalves.playmovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import Json.Video;

/**
 * Created by JeffersonAlves on 28/09/2015.
 */
public class VideoAdapter extends BaseAdapter {

    private Video[] mDataSet;
    private Context context;

    public VideoAdapter(Context c, Video[] v){
        this.context = c;
        this.mDataSet = v;
    }

    public void setmDataSet(Video[] v){
        mDataSet = v;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataSet.length;
    }

    @Override
    public Video getItem(int position) {
        return mDataSet[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder;

        if(convertView == null){

            view = LayoutInflater.from(context).inflate(R.layout.list_item_video_trailler, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView)view.findViewById(R.id.list_item_video_image_view);
            holder.textView = (TextView)view.findViewById(R.id.list_item_video_text_view);
            view.setTag(holder);

        }else{
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        Video video = mDataSet[position];

        holder.textView.setText( video.name.length() > 29 ? video.name.substring(0,29) : video.name);

        return view;
    }

    private class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }
}
