package com.iloved.abcreader.util;

/**
 * Created by Iloved on 2018/6/6.
 */
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.WindowManager;

import com.iloved.abcreader.db.DataBaseConnection;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class Globals {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static DataBaseConnection dbc;
    //每行显示的字数
    public static int LINE_CHAR_COUNT = 20;
    //字符间隔
    public static int CHAR_SEP = 2;
    //页边距
    public static int PAGE_SEP = 20;
    //每个字的大小
    public static int CHAR_SIZE;
    //行间隔
    public static int LINE_SEP;
    //计算行数
    public static int LINE_COUNT;
    //每一行文本之间的间隔文字
    public static String TXT_SEP_FLAG = "LINE_SEP_FLAG_BU_NENG_CHONG_FU";
    public static void initWindow(Activity activity) {//初始化窗口属性，让状态栏和导航栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            int statusColor = Color.parseColor("#7b7c7c");
            tintManager.setStatusBarTintColor(statusColor);
            tintManager.setStatusBarTintEnabled(true);
        }
    }
    public static void init(Activity a){
        SCREEN_WIDTH = a.getWindowManager().getDefaultDisplay().getWidth();
        SCREEN_HEIGHT = a.getWindowManager().getDefaultDisplay().getHeight();

        dbc = new DataBaseConnection(a);
        dbc.getWritableDatabase();
        CHAR_SIZE =(SCREEN_WIDTH - (PAGE_SEP * 2) - (LINE_CHAR_COUNT - 1)
                * CHAR_SEP)/LINE_CHAR_COUNT;

        LINE_COUNT = SCREEN_HEIGHT * 4 / 5 / (CHAR_SIZE + LINE_SEP);
    }

}