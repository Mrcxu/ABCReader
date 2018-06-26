package com.iloved.abcreader.db;

/**
 * Created by Iloved on 2018/6/10.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseConnection extends SQLiteOpenHelper {
    public DataBaseConnection(Context ctx){
        super(ctx,"txt.db",null,1);
    }
    public DataBaseConnection(Context context, String name,
                              CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//初始化文本表
        String sql = "CREATE TABLE txt (" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "novelName  text ," +
                "now_page  integer ," +
                "over_flag  integer " +
                ")";
        db.execSQL(sql);
//初始化页码表
        sql ="CREATE TABLE page(" +
                "id integer primary key AUTOINCREMENT," +
                "txt_id  integer ," +
                "page_num  integer ," +
                "content  text " +
                ")";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}