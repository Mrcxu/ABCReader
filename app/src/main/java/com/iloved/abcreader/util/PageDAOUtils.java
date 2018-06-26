package com.iloved.abcreader.util;

/**
 * Created by Iloved on 2018/6/7.
 */

import android.database.Cursor;

import java.util.Map;

public class PageDAOUtils {
//    public static void insertData(Map<String, Object> map) {
//        //String sql = "INSERT INTO page (txt_id,page_num,content) VALUES (?,?,?)";
//        dbOperation.insertPageData(map);
//    }
//
//    public static int getAllCount(int txtId) {
//        //String sql = "SELECT COUNT(*) FROM page WHERE txt_id = ?";
//        String url = "http://" + SERVER_HOST + "/page?method=7";
//        final int[] count = {0};
//        final JSONObject reqObject = new JSONObject();
//        JSONObject userObject = new JSONObject();
//        JSONArray userArray = new JSONArray();
//        try {
//            userArray.put(userObject.put("txt_id", txtId));
//            reqObject.put("ReqCode", "1").put("info", userArray);
//        } catch (JSONException e) {
//        }
//        System.out.println(reqObject);
//        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.POST, url, reqObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("xyf", "correct resp:" + response);
//                try {
//
//                    int reqCode = response.getInt("reqCode");
//                    Log.d("xty", "reqCode: " + reqCode);
//                    if (1 == reqCode) {
//                        JSONArray list = response.getJSONArray("info");
//                        JSONObject userObject = (JSONObject) list.opt(0);
//                        count[0] = userObject.getInt("count");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("xyf", "error:" + error.toString());
//            }
//        });
//        MyApplication.getRequestQueue().add(mRequest);
//        return count[0];
//    }
//
//    public static void deletePageData(int txtId) {
//        //String sql = "DELETE FROM page WHERE txt_id =?";
//        dbOperation.deletePageData(txtId);
//    }
//
//    public static String getPageContent(int txtId, int page_num) {
//        //String sql = "SELECT content FROM page WHERE txt_id = ? AND page_num = ?";
//        final String[] content = {""};
//        String url = "http://" + SERVER_HOST + "/page?method=9";
//        final JSONObject reqObject = new JSONObject();
//        JSONObject userObject = new JSONObject();
//        JSONArray userArray = new JSONArray();
//        try {
//            userArray.put(userObject.put("txt_id", txtId).put("page_num", page_num));
//            reqObject.put("ReqCode", "1").put("info", userArray);
//        } catch (JSONException e) {
//        }
//        System.out.println(reqObject);
//        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.POST, url, reqObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("xyf", "correct resp:" + response);
//                try {
//                    int reqCode = response.getInt("reqCode");
//                    Log.d("xty", "reqCode: " + reqCode);
//                    if (1 == reqCode) {
//                        JSONArray list = response.getJSONArray("info");
//                        JSONObject userObject = (JSONObject) list.opt(0);
//                        content[0] = userObject.getString("content");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("xyf", "error:" + error.toString());
//            }
//        });
//
//        MyApplication.getRequestQueue().add(mRequest);
//        return content[0];
//    }
public static void insertData(Map<String, Object> map) {
    String sql = "INSERT INTO page (txt_id,page_num,content) VALUES (?,?,?)";
    Globals.dbc.getWritableDatabase().execSQL(
            sql,
            new Object[] { map.get("txtId"), map.get("pageNum"),
                    map.get("content") });
}
    public static int getAllCount(int txtId) {
        String sql = "SELECT COUNT(*) FROM page WHERE txt_id = ?";
        Cursor c = Globals.dbc.getReadableDatabase().rawQuery(sql,
                new String[] { String.valueOf(txtId) });
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public static void deletePageData(int txtId) {
        String sql = "DELETE FROM page WHERE txt_id =?";
        Globals.dbc.getWritableDatabase().execSQL(sql, new Object[] { txtId });
    }

    public static String getPageContent(int txtId, int pageNum) {
        String sql = "SELECT content FROM page WHERE txt_id = ? AND page_num = ?";
        Cursor c = Globals.dbc.getReadableDatabase()
                .rawQuery(
                        sql,
                        new String[] { String.valueOf(txtId),
                                String.valueOf(pageNum) });

        c.moveToFirst();
        String content = c.getString(0);

        c.close();
        return content;
    }
}