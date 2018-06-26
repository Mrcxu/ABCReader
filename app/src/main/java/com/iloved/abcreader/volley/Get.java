package com.iloved.abcreader.volley;

import android.app.Fragment;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iloved.abcreader.R;
import com.iloved.abcreader.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/6/6.
 */

public class Get {
    MainActivity mainActivity;
    Fragment fragment;
    private ListView listView;

    /**
     * 传Activity
     */
    public Get(MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }
    public Get(Fragment fragment){
        this.fragment=fragment;
    }

    /**
     * 获取收藏
     */
    public void fetchCollect(){
        String url="http://192.168.43.172:8080/collect?method=1";
        JsonObjectRequest mRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("wy", "correct resp:" + response.toString());
                        listView.setAdapter(new SimpleAdapter(mainActivity,
                                parseJsonObject(response), R.layout.listview_adpater,
                                new String[]{"novelName", "author", "style"     /*,"introduction"*/},
                                new int[]{R.id.read_title, R.id.read_author, R.id.read_style  /*,R.id.read_introduction*/}));
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
            int retCode=jsonObject.getInt("retCode");
            Log.d("wy","retCode:"+retCode);
            if (retCode==1){
                JSONObject dataObject=jsonObject.getJSONObject("info");
                int totalNum=dataObject.getInt("totalNum");
                Log.d("wy","totalNum:"+totalNum);
                if (totalNum>0){
                    JSONArray userList=dataObject.getJSONArray("info");
                    Log.d("wy","totalNum:"+userList.length());
                    for (int i=0;i<userList.length();i++){
                        JSONObject userObject=(JSONObject) userList.opt(i);
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("_id",userObject.getInt("_id"));
                        hashMap.put("name",userObject.getString("name"));
                        hashMap.put("password",userObject.getString("password"));
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
