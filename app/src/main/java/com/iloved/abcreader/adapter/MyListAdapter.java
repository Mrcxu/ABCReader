package com.iloved.abcreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iloved.abcreader.R;
import com.iloved.abcreader.model.ListItemModel;

import java.util.List;

/**
 * Created by WZBC on 2018/5/30.
 */

public class MyListAdapter extends BaseAdapter {
    private List<ListItemModel> listItems;
    private LayoutInflater layoutInflater;

    public MyListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public List<ListItemModel> getListItems() {
        return listItems;
    }

    public void setListItems(List<ListItemModel> listItems) {
        this.listItems = listItems;
    }
    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView mylistItemView= new ListItemView();
        convertView = layoutInflater.inflate(R.layout.listview_adpater,null);
        mylistItemView.setRead_image((ImageView)convertView.findViewById(R.id.img));
        mylistItemView.setRead_title((TextView)convertView.findViewById(R.id.read_title));
        mylistItemView.setRead_author((TextView) convertView.findViewById(R.id.read_author));
        mylistItemView.setRead_style((TextView) convertView.findViewById(R.id.read_style));
        mylistItemView.setRead_time((TextView) convertView.findViewById(R.id.read_introduction));
        mylistItemView.getRead_title().setText(listItems.get(position).getTitle());
        mylistItemView.getRead_author().setText(listItems.get(position).getAuthor());
        mylistItemView.getRead_time().setText(listItems.get(position).getIntroduction());
        mylistItemView.getRead_style().setText(listItems.get(position).getStyle());
        mylistItemView.getRead_image().setImageResource(listItems.get(position).getRid());


        return convertView;
    }
    class ListItemView {
        private ImageView read_image;
        private TextView read_title;
        private TextView read_author;
        private TextView read_introduction;
        private TextView read_style;

        public ImageView getRead_image() {
            return read_image;
        }

        public void setRead_image(ImageView read_image) {
            this.read_image = read_image;
        }

        public TextView getRead_title() {
            return read_title;
        }

        public void setRead_title(TextView read_title) {
            this.read_title = read_title;
        }

        public TextView getRead_author() {
            return read_author;
        }

        public void setRead_author(TextView read_author) {
            this.read_author = read_author;
        }

        public TextView getRead_time() {
            return read_introduction;
        }

        public void setRead_time(TextView read_time) {
            this.read_introduction = read_time;
        }

        public TextView getRead_style() {
            return read_style;
        }

        public void setRead_style(TextView read_style) {
            this.read_style = read_style;
        }
    }
}
