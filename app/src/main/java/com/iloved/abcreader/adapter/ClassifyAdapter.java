package com.iloved.abcreader.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iloved.abcreader.R;
import com.iloved.abcreader.model.ListModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

/**
 * Created by admin on 2018/6/10.
 */

public class ClassifyAdapter extends BaseAdapter{
    private List<ListModel> listItems;
    private LayoutInflater layoutInflater;

    public ClassifyAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public List<ListModel> getListItems() {
        return listItems;
    }

    public void setListItems(List<ListModel> listItems) {
        this.listItems = listItems;
    }
    @Override
    public int getCount() {return listItems.size();}

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ListItemView listItemView = new ListItemView();
        convertView=layoutInflater.inflate(R.layout.classifylist_adapter,null);
        listItemView.setC_title((TextView)convertView.findViewById(R.id.c_title));
        listItemView.setC_author((TextView)convertView.findViewById(R.id.c_author));
        listItemView.setC_style((TextView)convertView.findViewById(R.id.c_style));
        listItemView.setC_image((ImageView)convertView.findViewById(R.id.c_img));
        listItemView.setI_title((ImageView)convertView.findViewById(R.id.i_title));
        listItemView.setI_style((ImageView)convertView.findViewById(R.id.i_style));
        listItemView.setI_author((ImageView)convertView.findViewById(R.id.i_author));
        listItemView.setC_view(convertView.findViewById(R.id.c_line));
        listItemView.setC_hreat((Button)convertView.findViewById(R.id.c_hreat));
        listItemView.getC_title().setText(listItems.get(position).getTitle());
        listItemView.getC_author().setText(listItems.get(position).getAuthor());
        listItemView.getC_style().setText(listItems.get(position).getStyle());
        listItemView.getC_image().setImageResource(listItems.get(position).getRid());
        System.out.println("adppppppppppppppp"+listItems.get(position).getIsCollect());
        if (listItems.get(position).getIsCollect().equals("1")){
            listItemView.getC_hreat().setBackgroundResource(R.drawable.hreated);
        }
        listItemView.getC_hreat().setOnClickListener(new View.OnClickListener() {
            String a = listItems.get(position).getIsCollect();
            @Override
            public void onClick(View view) {
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                try{
                    if(a.equals("0")){
                        listItemView.getC_hreat().setBackgroundResource(R.drawable.hreated);
                        String url="http://106.15.196.105:8080/novelweb_war/collect?method=2&id="+listItems.get(position).getId();
                //                                String url="http://192.168.43.172:8080/collect?method=3&id="+bookid;
                        Log.d("url=",url);
                        HttpClient httpClient=new DefaultHttpClient();
                        HttpGet httpGet=new HttpGet(url);
                        HttpResponse response = httpClient.execute(httpGet);
                        a="1";
                    }
                    else {
                        listItemView.getC_hreat().setBackgroundResource(R.drawable.hreat);
                        String url="http://106.15.196.105:8080/novelweb_war/collect?method=3&id="+listItems.get(position).getId();
                //                                String url="http://192.168.43.172:8080/collect?method=2&id="+bookid;
                        Log.d("url=",url);
                        HttpClient httpClient=new DefaultHttpClient();
                        HttpGet httpGet=new HttpGet(url);
                        HttpResponse response = httpClient.execute(httpGet);
                        a="0";
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                    }
                });
                thread.start();

            }
        });
        return convertView;
    }

    class ListItemView {
        private ImageView c_image,i_author,i_title,i_style;
        private TextView c_title;
        private TextView c_author;
        private TextView c_style;
        private Button c_hreat;
        private View c_view;
        private int isCollect;

        public int getIsCollect() {
            return isCollect;
        }

        public void setIsCollect(int isCollect) {
            this.isCollect = isCollect;
        }

        public ImageView getC_image() {return c_image;}

        public void setC_image(ImageView c_image) {this.c_image = c_image;}

        public ImageView getI_author() {return i_author;}

        public void setI_author(ImageView i_author) {this.i_author = i_author;}

        public ImageView getI_title() {return i_title;}

        public void setI_title(ImageView i_title) {this.i_title = i_title;}

        public ImageView getI_style() {return i_style;}

        public void setI_style(ImageView i_style) {this.i_style = i_style;}

        public TextView getC_title() {return c_title;}

        public void setC_title(TextView c_title) {this.c_title = c_title;}

        public TextView getC_author() {return c_author;}

        public void setC_author(TextView c_author) {this.c_author = c_author;}

        public TextView getC_style() {return c_style;}

        public void setC_style(TextView c_style) {this.c_style = c_style;}

        public Button getC_hreat() {return c_hreat;}

        public void setC_hreat(Button c_hreat) {this.c_hreat = c_hreat;}

        public View getC_view() {return c_view;}

        public void setC_view(View c_view) {this.c_view = c_view;}
    }
}
