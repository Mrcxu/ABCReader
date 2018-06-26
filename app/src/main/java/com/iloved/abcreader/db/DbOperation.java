package com.iloved.abcreader.db;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.iloved.abcreader.volley.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Iloved on 2018/6/6.
 */

public class DbOperation {
    public static String SERVER_HOST ="106.15.196.105:8080/novelweb_war";

    public void checkIfHave(final String novelName) {
        String url = "http://" + SERVER_HOST + "/page?method=1" + "&" + "novelName=" + novelName;
        StringRequest mRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.trim();
                Log.d("xyf", "correct resp:" + response);
                if (response.equals("false")) {
                    System.out.println("1111");
                    insertNovelName(novelName);
                } else {
                    System.out.println("2222");
                    Log.d("xyf", "correct resp:" + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("xyf", "error:" + error.toString());
            }

        });
        MyApplication.getRequestQueue().add(mRequest);
    }


    public void insertNovelName(String novelName) {
        String url = "http://" + SERVER_HOST + "/page?method=2";
        final JSONObject reqObject = new JSONObject();
        JSONObject userObject = new JSONObject();
        JSONArray userArray = new JSONArray();
        try {
            userArray.put(userObject.put("novelName", novelName).put("now_page", 1).put("over_flag", 0));
            reqObject.put("ReqCode", "1").put("info", userArray);
        } catch (JSONException e) {

        }
        System.out.println(reqObject);
        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.POST, url, reqObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("insert", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("xy", "volley error:" + error.toString());
            }
        });
        mRequest.setTag("myGet");
        MyApplication.getRequestQueue().add(mRequest);
    }

    public void updateFlag(int id) {
        String url = "http://" + SERVER_HOST + "/page?method=3" + "&" + "id=" + id;
        System.out.println(url);
        StringRequest mRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("xyf", "insert correct resp:" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("xyf", "error:" + error.toString());
            }
        });
        MyApplication.getRequestQueue().add(mRequest);
    }

    public void updatePage(int nowpage, int id) {
        String url = "http://" + SERVER_HOST + "/page?method=4" + "&" + "id=" + id + "&" + "page=" + nowpage;
        System.out.println(url);
        StringRequest mRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("xyf", "insert correct resp:" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("xyf", "error:" + error.toString());
            }
        });
        MyApplication.getRequestQueue().add(mRequest);
    }


    public void insertPageData(Map<String, Object> map) {
        String url = "http://" + SERVER_HOST + "/page?method=6";
        final JSONObject reqObject = new JSONObject();
        JSONObject userObject = new JSONObject();
        JSONArray userArray = new JSONArray();
        try {
            userArray.put(userObject.put("txtId", map.get("txtId")).put("pageNum", map.get("pageNum")).put("content", map.get("content")));
            reqObject.put("ReqCode", "1").put("info", userArray);
        } catch (JSONException e) {

        }
        System.out.println(reqObject);
        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.POST, url, reqObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("insertData:", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("xy", "volley error:" + error.toString());
            }
        });
        mRequest.setTag("myGet");
        MyApplication.getRequestQueue().add(mRequest);
    }


    public void deletePageData(int txtId) {
        String url = "http://" + SERVER_HOST + "/page?method=8" + "&" + "txt_id=" + txtId;
        System.out.println(url);
        StringRequest mRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("xyf", "insert correct resp:" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("xyf", "error:" + error.toString());
            }
        });
        MyApplication.getRequestQueue().add(mRequest);
    }
}




