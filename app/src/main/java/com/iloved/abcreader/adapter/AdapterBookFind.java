package com.iloved.abcreader.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iloved.abcreader.R;
import com.iloved.abcreader.activity.ReadyReadActivity;
import com.iloved.abcreader.interfaces.OnChapterClickListener;
import com.iloved.abcreader.interfaces.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by WZBC on 2018/5/16.
 */

public class AdapterBookFind extends RecyclerView.Adapter<AdapterBookFind.ViewHolder> {

    private OnItemClickListener onItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View tocView;
        private TextView textview_bookFindClass;
        private ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            tocView = itemView;
            textview_bookFindClass = itemView.findViewById(R.id.bookFind_text);
            imageView = itemView.findViewById(R.id.bookFind_image);
        }




    }

    private ArrayList<String> myCategory;

    public AdapterBookFind(ArrayList<String> Category){
        this.myCategory=Category;
    }

    private Context context;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_find_item,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        holder.textview_bookFindClass.setText(myCategory.get(position));
        holder.itemView.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });

        holder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onItemLongClick(position);
                return false;
            }
        });
        switch (position) {
            case 0:
                Glide.with(context).load(R.drawable.xh).into(holder.imageView);
                break;
            case 1:
                Glide.with(context).load(R.drawable.wx).into(holder.imageView);
                break;
            case 2:
                Glide.with(context).load(R.drawable.yq).into(holder.imageView);
                break;
            case 3:
                Glide.with(context).load(R.drawable.ls).into(holder.imageView);
                break;
            case 4:
                Glide.with(context).load(R.drawable.yx).into(holder.imageView);
                break;
            case 5:
                Glide.with(context).load(R.drawable.kh).into(holder.imageView);
                break;
            case 6:
                Glide.with(context).load(R.drawable.bh).into(holder.imageView);
                break;
            case 7:
                Glide.with(context).load(R.drawable.all).into(holder.imageView);
                break;
            default:
                break;
        }

    }



    public int getItemCount(){
        return myCategory.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }
}
