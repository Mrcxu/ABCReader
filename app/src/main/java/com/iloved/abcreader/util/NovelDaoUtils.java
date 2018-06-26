package com.iloved.abcreader.util;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iloved.abcreader.volley.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.iloved.abcreader.db.DbOperation.SERVER_HOST;

/**
 * Created by Iloved on 2018/6/7.
 */

public class NovelDaoUtils {
    public InputStream getNovelContent(String novelName) {
        String url = "http://" + SERVER_HOST + "/page?method=10";
        final InputStream[] novelContent = new InputStream[1];
        final JSONObject reqObject = new JSONObject();
        JSONObject userObject = new JSONObject();
        JSONArray userArray = new JSONArray();
        try {
            userArray.put(userObject.put("novelName", novelName));
            reqObject.put("ReqCode", "1").put("info", userArray);
        } catch (JSONException e) {
        }
        System.out.println(reqObject);
        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.POST, url, reqObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("xyf", "correct resp:" + response);
                try {
                    String content = "";
                    int reqCode = response.getInt("reqCode");
                    Log.d("xty", "reqCode: " + reqCode);
                    if (1 == reqCode) {
                        JSONArray list = response.getJSONArray("info");
                        JSONObject userObject = (JSONObject) list.opt(0);
                        content = userObject.getString("content");
                        if (content != null && !content.trim().equals("")) {
                            try {
                                novelContent[0] = new ByteArrayInputStream(content.getBytes());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("xyf", "error:" + error.toString());
            }
        });
        MyApplication.getRequestQueue().add(mRequest);
        return novelContent[0];
    }
}



