package com.example.jeffersonalves.playmovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import Json.Viewer;

/**
 * Created by JeffersonAlves on 30/09/2015.
 */
public class ViewerAdapter extends BaseAdapter {

    private Viewer[] mDataSet;
    private Context context;

    public ViewerAdapter(Context c, Viewer[] v){
        this.mDataSet = v;
        this.context = c;
    }

    @Override
    public int getCount() {
        return mDataSet.length;
    }

    @Override
    public Viewer getItem(int position) {
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


            view = LayoutInflater.from(context).inflate(R.layout.list_item_review, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView)view.findViewById(R.id.list_item_review_text_view);
            view.setTag(holder);

        }else{
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        Viewer viewers = mDataSet[position];

        holder.textView.setText( viewers.author + "\n\n" + viewers.content);

        return view;
    }

    private class ViewHolder{
        public TextView textView;
    }
}
