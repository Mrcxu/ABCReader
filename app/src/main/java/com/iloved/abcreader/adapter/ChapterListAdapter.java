package com.iloved.abcreader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iloved.abcreader.R;
import com.iloved.abcreader.interfaces.OnChapterClickListener;
import com.iloved.abcreader.model.Chapter;

import java.util.List;

/**
 * Created by Administrator on 2018/6/4.
 */

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ViewHolder> {

    private List<Chapter> mChapterList;
    private OnChapterClickListener mOnChapterClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView chapterno;
        TextView chaptername;

        public ViewHolder(View view) {
            super(view);
            chapterno = view.findViewById(R.id.chapter_no);
            chaptername = view.findViewById(R.id.chapter_name);
        }
    }


    public ChapterListAdapter(List<Chapter> mChapterList){
        this.mChapterList=mChapterList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Chapter chapter = mChapterList.get(position);
        holder.chapterno.setText(chapter.getChapterno());
        holder.chaptername.setText(chapter.getChaptername());
        if( mOnChapterClickListener!= null){
            holder. itemView.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnChapterClickListener.onItemClick(position);
                }
            });

            holder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnChapterClickListener.onItemLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mChapterList.size();
    }

    public void setOnItemClickListener(OnChapterClickListener onItemClickListener ){
        this. mOnChapterClickListener=onItemClickListener;
    }
}
