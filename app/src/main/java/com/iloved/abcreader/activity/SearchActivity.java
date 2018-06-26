package com.iloved.abcreader.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

import static com.iloved.abcreader.db.DbOperation.SERVER_HOST;
import static com.iloved.abcreader.util.Globals.initWindow;

/**
 * Created by Carson_Ho on 17/8/11.
 */

public class SearchActivity extends ListActivity {

    private SearchView searchView;
    private SystemBarTintManager tintManager;
    private String[] title;
    private String[] author;
    private String[] introduction;
    private String[] style;
    private String[] content;
    private String[] isCollect;
    private String[] bookid;
    private String[] chapterNum;
    private int[] rid = {R.drawable.timg, R.drawable.timg, R.drawable.timg};
    private MyListAdapter adapter;
    private List<ListItemModel> myListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initWindow(this);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.search_view);

        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                if (!string.equals("")){
                adapter = new MyListAdapter(SearchActivity.this);
                myListItems = new ArrayList<ListItemModel>();
                fetchCollect(string);
                adapter.setListItems(myListItems);
                setListAdapter(adapter);
            }else {
                    Toast.makeText(SearchActivity.this,"请输入查询关键字!",Toast.LENGTH_SHORT).show();
                }}
        });

        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, ReadyReadActivity.class);
        intent.putExtra("picture", rid[position]);
        intent.putExtra("author", author[position]);
        intent.putExtra("title", title[position]);
        intent.putExtra("style", style[position]);
        intent.putExtra("bookid",bookid[position]);
        intent.putExtra("introduction",introduction[position]);
        intent.putExtra("content",content[position]);
        intent.putExtra("isCollect",isCollect[position]);
        intent.putExtra("chapterNum",chapterNum[position]);
//        startActivity(intent);
        startActivityForResult(intent,1);
        Toast.makeText(this, "-->>" + title[position], Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        myListItems.clear();
//        fetchCollect(String search);
    }

    public void initData() {
        for (int i = 0; i < title.length; i++) {
            ListItemModel lm = new ListItemModel();
            lm.setTitle(title[i]);
            lm.setAuthor(author[i]);
            lm.setIntroduction(introduction[i]);
            lm.setStyle(style[i]);
            lm.setRid(rid[i]);
            myListItems.add(lm);
        }
    }


    public void fetchCollect(String search) {
        String url = "http://" + SERVER_HOST + "/novel?method=3&search=" + search;
        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("wyyyy", "correct resp:" + response.toString());
                        ArrayList<HashMap<String, Object>> list;
                        list = parseJsonObject(response);
                        if (list.size() > 0) {
                            title = new String[list.size()];
                            author = new String[list.size()];
                            introduction = new String[list.size()];
                            style = new String[list.size()];
                            rid = new int[list.size()];
                            bookid=new String[list.size()];
                            content=new String[list.size()];
                            isCollect=new String[list.size()];
                            chapterNum=new String[list.size()];
                            for (int i = 0; i < list.size(); i++) {
                                Log.d("[][][][][][][]", "" + i);
                                author[i] = list.get(i).get("author").toString();
                                System.out.println(list.get(i).get("author").toString());
                                title[i] = list.get(i).get("novelName").toString();
                                introduction[i] = list.get(i).get("content").toString();
                                style[i] = list.get(i).get("type").toString();
                                rid[i] = R.drawable.timg;
                                bookid[i]=list.get(i).get("_id").toString();
                                content[i]=list.get(i).get("content").toString();
                                isCollect[i]=list.get(i).get("isCollect").toString();
                                chapterNum[i]=list.get(i).get("chapterNum").toString();
                            }
                            initData();
                            adapter = new MyListAdapter(SearchActivity.this);
                            adapter.setListItems(myListItems);
                            setListAdapter(adapter);
                        } else {
                            Toast.makeText(SearchActivity.this, "无相关图书！", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("wy", "volley erroe:" + error.toString());
            }
        });
        mRequest.setTag("myGet");
        MyApplication.getRequestQueue().add(mRequest);
    }

    private ArrayList<HashMap<String, Object>> parseJsonObject(JSONObject jsonObject) {
        ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
        try {
            int reqCode = jsonObject.getInt("reqCode");
            Log.d("wy", "reqCode:" + reqCode);
            if (reqCode == 1) {
                System.out.println("666");
                JSONArray userList = jsonObject.getJSONArray("info");
                for (int i = 0; i < userList.length(); i++) {
                    JSONObject userObject = (JSONObject) userList.opt(i);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("_id", userObject.getInt("_id"));
                    hashMap.put("novelName", userObject.getString("novelName"));
                    hashMap.put("type", userObject.getString("type"));
                    hashMap.put("content", userObject.getString("content"));
                    hashMap.put("author", userObject.getString("author"));
                    hashMap.put("introduction",userObject.getString("introduction"));
                    hashMap.put("isCollect",userObject.getString("isCollect"));
                    hashMap.put("chapterNum",userObject.getString("chapterNum"));
                    resultList.add(hashMap);
                }
            }
        } catch (Exception e) {
            Log.d("wy", "prase error:" + e.getMessage());
        }
        return resultList;
    }
}
