package com.iloved.abcreader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iloved.abcreader.R;
import com.iloved.abcreader.adapter.MyListAdapter;
import com.iloved.abcreader.model.ListItemModel;
import com.iloved.abcreader.volley.MyApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import static com.iloved.abcreader.util.Globals.initWindow;

/**
 * Created by Administrator on 2018/6/15.
 */

public class TypeActivity extends AppCompatActivity {
    private MyListAdapter adapter;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.classify_activity);
        initWindow(this);
        Bundle bundle = this.getIntent().getExtras();
        type=bundle.getString("type");
        findId();
        adapter = new MyListAdapter(this);
        myListItems = new ArrayList<ListItemModel>();
        fetchCollect();
        setView();
        adapter.setListItems(myListItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TypeActivity.this, ReadyReadActivity.class);
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
                Toast.makeText(TypeActivity.this, "-->>" + title[position], Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findId(){
        listView=(ListView)findViewById(R.id.type_list);
    }
    private void setView(){

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
                            introduction[i]=list.get(i).get("content").toString();
                            style[i]=list.get(i).get("type").toString();
                            rid[i]=R.drawable.timg;
                            bookid[i]=list.get(i).get("bookid").toString();
                            content[i]=list.get(i).get("content").toString();
                            isCollect[i]=list.get(i).get("isCollect").toString();
                            chapterNum[i]=list.get(i).get("chapterNum").toString();
                        }
                        initData();
                        adapter = new MyListAdapter(TypeActivity.this);
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
}
