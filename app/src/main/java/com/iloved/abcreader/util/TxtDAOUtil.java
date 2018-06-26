package com.iloved.abcreader.util;

/**
 * Created by Iloved on 2018/6/6.
 */

import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

public class TxtDAOUtil {
    public static void insertData(String novelName){
//先判断之前是否添加过
        String sql = "SELECT id FROM txt WHERE novelName =?";
        Cursor c = Globals.dbc.getReadableDatabase().rawQuery(sql,
                new String[]{novelName});

        if(!c.moveToFirst()){
//数据库中没有这条数据，就需要添加，默认设置用户打开的页面是第一页, 同时还没有完成分页的操作
            sql = "INSERT INTO txt (novelName,now_page,over_flag) VALUES (?,1,0)";

            Globals.dbc.getWritableDatabase().execSQL(sql,new Object[]{novelName});
        }
        c.close();
    }

    public static void updateOverFlag(int id){
        String sql = "UPDATE txt SET over_flag =1 WHERE id =?";
        Globals.dbc.getWritableDatabase().execSQL(sql, new Object[]{id});
    }

    public static void updateNowPage(int id,int nowPage){
        String sql = "UPDATE txt SET now_page =? WHERE id =?";
        Globals.dbc.getWritableDatabase().execSQL(sql, new Object[]{nowPage,id});
        System.out.println(sql);
    }

    public static Map<String,Object> getTxtDataByNovelName(String novelName){
        Map<String,Object> map = new HashMap<String,Object>();
        String sql = "SELECT id,now_page,over_flag FROM txt WHERE novelName = ?";
        Cursor c = Globals.dbc.getReadableDatabase().rawQuery(sql,
                new String[]{novelName});
        c.moveToFirst();
        map.put("id", c.getInt(0));
        map.put("nowPage", c.getInt(1));
        map.put("overFlag", c.getInt(2));

        return map;
    }
}

//    static DbOperation dbOperation = new DbOperation();
//    private static Map<String, Object> map1;
//
//    public static void insertData(String novelName) {
//        dbOperation.checkIfHave(novelName);
//    }
//
//    public static void updateOverFlag(int id) {
//        //String sql = "UPDATE txt SET over_flag =1 WHERE id =?";
//        dbOperation.updateFlag(id);
//    }
//
//    public static void updateNowPage(int id, int nowPage) {
//        // String sql = "UPDATE txt SET now_page =? WHERE id =?";
//        dbOperation.updatePage(nowPage, id);
//    }

