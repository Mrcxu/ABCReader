package com.iloved.abcreader.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iloved.abcreader.R;
import com.iloved.abcreader.adapter.ClassifyAdapter;
import com.iloved.abcreader.model.ListModel;
import com.iloved.abcreader.volley.MyApplication;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import static com.iloved.abcreader.util.Globals.initWindow;

/**
 * Created by admin on 2018/6/10.
 */

public class ClassifyActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private ClassifyAdapter adapter;
//    private ListView listView;
//    private List<ListModel> mlistModels;
//
//    private String[] title = {"自定义1","自定义2"};
//    private String[] author = {"自定义1","自定义2"};
//    private String[] style = {"自定义1","自定义2"};
//    private int[] img = {R.drawable.timg,R.drawable.timg};
    private ListView listView;
    private String[] title;
    private String[] author;
    private String[] introduction;
    private String[] style;
    private String[] content;
    private String[] isCollect;
    private String[] chapterNum;
    private int[] rid = {R.drawable.timg,R.drawable.timg,R.drawable.timg};
    private List<View> dots;
    private String[] bookid;
    private String type;
    private TextView textView;
    //记录上一次点的位置
    SystemBarTintManager tintManager;
    private ScheduledExecutorService scheduledExecutorService;
    private List<ListModel> myListItems;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.classify_activity);
        initWindow(this);
        Bundle bundle = this.getIntent().getExtras();
        type=bundle.getString("type");
        textView=findViewById(R.id.spin_one);
        textView.setText(type);
        myListItems = new ArrayList<ListModel>();
        fetchCollect();
        adapter = new ClassifyAdapter(this);
        adapter.setListItems(myListItems);

        listView = findViewById(R.id.classify_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("dianji","sssss");
                Intent intent = new Intent(ClassifyActivity.this, ReadyReadActivity.class);
                intent.putExtra("picture", rid[position]);
                intent.putExtra("author",author[position]);
                intent.putExtra("title",title[position]);
                intent.putExtra("style",style[position]);
                intent.putExtra("bookid",bookid[position]);
                intent.putExtra("introduction",introduction[position]);
                intent.putExtra("content",content[position]);
                intent.putExtra("isCollect",isCollect[position]);
                intent.putExtra("chapterNum",chapterNum[position]);
                startActivity(intent);
            }
        });
    }
    public void initData() {
        for (int i = 0; i < title.length; i++) {
            ListModel lm = new ListModel();
            lm.setTitle(title[i]);
            lm.setAuthor(author[i]);
            lm.setIntroduction(introduction [i]);
            lm.setStyle(style[i]);
            lm.setRid(rid[i]);
            lm.setIsCollect(isCollect[i]);
            lm.setId(bookid[i]);
            myListItems.add(lm);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

    public void fetchCollect(){
        String url="http://106.15.196.105:8080/novelweb_war/novel?method=2&type='"+type+"'";
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
                            isCollect[i]=list.get(i).get("isCollect").toString();
                            chapterNum[i]=list.get(i).get("chapterNum").toString();
                        }
                        initData();
                        adapter = new ClassifyAdapter(ClassifyActivity.this);
                        adapter.setListItems(myListItems);
                        listView.setAdapter(adapter);

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
                        hashMap.put("bookid",userObject.getInt("_id"));
                        hashMap.put("novelName",userObject.getString("novelName"));
                        hashMap.put("type",userObject.getString("type"));
                        hashMap.put("content",userObject.getString("content"));
                        hashMap.put("author",userObject.getString("author"));
                        hashMap.put("introduction",userObject.getString("introduction"));
                        hashMap.put("isCollect",userObject.getString("isCollect"));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

