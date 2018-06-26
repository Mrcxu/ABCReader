package com.iloved.abcreader.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iloved.abcreader.R;
import com.iloved.abcreader.activity.ReadyReadActivity;
import com.iloved.abcreader.model.ListItemModel;
import com.iloved.abcreader.adapter.MyListAdapter;
import com.iloved.abcreader.volley.MyApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by WZBC on 2018/5/16.
 */

public class ReadListFragment extends ListFragment{

    private MyListAdapter adapter;
    private View rootView;
    private ListView listView;
    private String[] title;
    private String[] author;
    private String[] introduction;
    private String[] style;
    private String[] content;
    private String[] isCollect;
    private String[] chapterNum;
    private int[] rid = {R.drawable.timg,R.drawable.timg,R.drawable.timg};
    private ViewPager mViewPaper;
    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
    private ViewPagerAdapter vadapter;
    private String[] bookid;
    //记录上一次点的位置
    private int oldPosition = 0;
    //存放图片的id
    private int[] imageIds = new int[]{
            R.drawable.a,
            R.drawable.b,
            R.drawable.a,
            R.drawable.b,
            R.drawable.a
    };
    private String[] titles = new String[]{
            "轮播1",
            "轮播2",
            "轮播3",
            "轮播4",
            "轮播5"
    };
    private ScheduledExecutorService scheduledExecutorService;
    private List<ListItemModel> myListItems;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.pager_book_list, container, false);
        adapter = new MyListAdapter(getActivity());
        myListItems = new ArrayList<ListItemModel>();
        fetchCollect();
//        initData();
        setView();
        adapter.setListItems(myListItems);
        this.setListAdapter(adapter);
        return rootView;
    }

    private void setView(){
        mViewPaper = rootView.findViewById(R.id.vp);
        images = new ArrayList<ImageView>();
        for(int i = 0; i < imageIds.length; i++){
            ImageView imageView = new ImageView(getActivity());
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }

        vadapter = new ViewPagerAdapter();
        mViewPaper.setAdapter(vadapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), ReadyReadActivity.class);
        intent.putExtra("picture", rid[position]);
        intent.putExtra("author",author[position]);
        intent.putExtra("title",title[position]);
        intent.putExtra("style",style[position]);
        intent.putExtra("bookid",bookid[position]);
        intent.putExtra("introduction",introduction[position]);
        intent.putExtra("content",content[position]);
        intent.putExtra("isCollect",isCollect[position]);
        intent.putExtra("chapterNum",chapterNum[position]);
        startActivityForResult(intent,1);
//        startActivity(intent);
        Toast.makeText(getActivity(), "-->>" + title[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myListItems.clear();
        fetchCollect();
    }



    public void initData() {
        for (int i = 0; i < title.length; i++) {
            ListItemModel lm = new ListItemModel();
            lm.setTitle(title[i]);
            lm.setAuthor(author[i]);
            lm.setIntroduction(introduction [i]);
            lm.setStyle(style[i]);
            lm.setRid(rid[i]);
            myListItems.add(lm);
        }
    }

    public class ViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            // TODO Auto-generated method stub
//          super.destroyItem(container, position, object);
//          view.removeView(view.getChildAt(position));
//          view.removeViewAt(position);
            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            // TODO Auto-generated method stub
            view.addView(images.get(position));
            return images.get(position);
        }

    }
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                5,
                5,
                TimeUnit.SECONDS);

    }
    private class ViewPageTask implements Runnable{

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;
            mHandler.sendEmptyMessage(0);
        }
    }
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        }
    };
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }
    public void fetchCollect(){
        String url="http://106.15.196.105:8080/novelweb_war/collect?method=1";
        JsonObjectRequest mRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("wyyyy", "correct resp:" + response.toString());
                        ArrayList<HashMap<String,Object>> list;
                        list=parseJsonObject(response);
                        title=new String[list.size()];
                        author=new String[list.size()];
                        introduction=new String[list.size()];
                        style=new String[list.size()];
                        rid=new int[list.size()];
                        bookid=new String[list.size()];
                        content=new String[list.size()];
                        isCollect=new String[list.size()];
                        chapterNum=new String[list.size()];
                        for(int i=0;i<list.size();i++){
                            Log.d("[][][][][][][]",""+i);
                            author[i]=list.get(i).get("author").toString();
                            System.out.println(list.get(i).get("author").toString());
                            title[i]=list.get(i).get("novelName").toString();
                            introduction[i]=list.get(i).get("introduction").toString();
                            style[i]=list.get(i).get("type").toString();
                            rid[i]=R.drawable.timg;
                            bookid[i]=list.get(i).get("bookid").toString();
                            content[i]=list.get(i).get("content").toString();
                            isCollect[i]="1";
                            chapterNum[i]=list.get(i).get("chapterNum").toString();
                        }
                        initData();

//                        myListItems = new ArrayList<ListItemModel>();
                        adapter = new MyListAdapter(getActivity());
                        adapter.setListItems(myListItems);
//                        this.setListAdapter(adapter);
                        setListAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("wy","volley erroe:"+error.toString());
            }
        });
        mRequest.setTag("myGet");
        MyApplication.getRequestQueue().add(mRequest);
    }

    private ArrayList<HashMap<String,Object>> parseJsonObject(JSONObject jsonObject) {
        ArrayList<HashMap<String,Object>> resultList=new ArrayList<>();
        try{
            int reqCode=jsonObject.getInt("reqCode");
            Log.d("wy","reqCode:"+reqCode);
            if (reqCode==1){

                int totalNum=jsonObject.getInt("totalNum");
                Log.d("wy","totalNum:"+totalNum);
//                JSONArray dataObject=jsonObject.getJSONArray("info");
                if (totalNum>0){
                    System.out.println("666");
                    JSONArray userList=jsonObject.getJSONArray("info");
//                    Log.d("wy","totalNum:"+userList.length());
                    for (int i=0;i<userList.length();i++){
                        JSONObject userObject=(JSONObject) userList.opt(i);
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("bookid",userObject.getInt("bookid"));
                        hashMap.put("novelName",userObject.getString("novelName"));
                        hashMap.put("type",userObject.getString("type"));
                        hashMap.put("content",userObject.getString("content"));
                        hashMap.put("author",userObject.getString("author"));
                        hashMap.put("introduction",userObject.getString("introduction"));
                        hashMap.put("chapterNum",userObject.getString("chapterNum"));
                        resultList.add(hashMap);
                    }

                }
            }
        }catch (Exception e){
            Log.d("wy","prase error:"+e.getMessage());
        }
        return resultList;
    }
}
