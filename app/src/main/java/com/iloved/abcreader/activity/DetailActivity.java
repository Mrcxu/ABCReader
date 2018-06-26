package com.iloved.abcreader.activity;

/**
 * Created by Iloved on 2018/6/7.
 */

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iloved.abcreader.R;
import com.iloved.abcreader.util.Globals;
import com.iloved.abcreader.util.PageDAOUtils;
import com.iloved.abcreader.util.TxtDAOUtil;
import com.iloved.abcreader.view.MyView;
import com.iloved.abcreader.volley.MyApplication;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static com.iloved.abcreader.db.DbOperation.SERVER_HOST;
import static com.iloved.abcreader.util.Globals.initWindow;
import static com.iloved.abcreader.util.TxtDAOUtil.insertData;


public class DetailActivity extends Activity {
    Thread t;
    private Handler handler;
    private Map<String, Object> txtMap;
    private Map<String, Object> pageMap = new HashMap<String, Object>();
    private InputStream inputStream = null;
    private int lineCount;
    private int pageNum;
    private StringBuilder builder;
    private String novelName;
    private TextView txtPage;
    private boolean showedFlag = false;
    private int nowPage = 1;
    private String chapterID;
    private int ifChapter;
    private MyView txtContent;

    public int getNowPage() {
        return nowPage;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = this.getIntent().getExtras();
        chapterID = bundle.getString("chapterID");
        ifChapter = bundle.getInt("ifChapter", 0);
        initWindow(this);
        Globals.init(this);
        txtPage = findViewById(R.id.txt_page);
        txtContent = findViewById(R.id.txt_content);
        novelName = getIntent().getStringExtra("novelName");

        System.out.println("TxtMap:+++++" + txtMap);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    txtPage.setText("已经处理好了：" + (pageNum - 1) + "页的数据");
                } else {
                    txtPage.setText("当前：1/" + (pageNum - 1));
                }
            }
        };
        if (ifChapter == 1) {
            novelName=novelName+chapterID;
            insertData(novelName);
            txtMap = TxtDAOUtil.getTxtDataByNovelName(novelName);
            BoolChapter(chapterID);
        } else if (ifChapter == 0) {
            insertData(novelName);
            txtMap = TxtDAOUtil.getTxtDataByNovelName(novelName);
            BoolPage();
        } else {
            System.out.println("ifChapter::ERROR！！！！！！！！！！！！：：：：" + ifChapter);
        }


    }



    public void BoolChapter(String chapter) {
        String realNovelName= novelName.replace(chapterID,"");
        String url = "http://" + SERVER_HOST + "/page?method=11&novelName="+realNovelName+"&chapter="+chapterID;
        System.out.println("url:" + url);
        final JSONObject reqObject = new JSONObject();
        JSONObject userObject = new JSONObject();
        JSONArray userArray = new JSONArray();
        try {
            userArray.put(userObject.put("novelName", realNovelName).put("chapter", chapter));
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
                                inputStream = new ByteArrayInputStream(content.getBytes());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    if ((Integer) (txtMap.get("overFlag")) == 0) {
                            t = new Thread() {
                            @Override
                            public void run() {
                        try {
                            PageDAOUtils.deletePageData((Integer) txtMap.get("id"));
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(inputStream));
                            System.out.println("reader!!!!!!!!!!!!!!!!!!:" + reader);
                            String line = null;
                            builder = new StringBuilder();
                            lineCount = 0;
                            pageNum = 1;
                            while ((line = reader.readLine()) != null) {
                                while (line.length() > 20) {
                                    String tempStr = line.substring(0, 20);
                                    System.out.println("content:!!!!!!!!!!!!!!!!!!" + tempStr);
                                    addPageLineData(tempStr);
                                    line = line.substring(20);
                                }
                                addPageLineData(line);
                            }
                            pageMap.put("txtId", txtMap.get("id"));
                            pageMap.put("pageNum", pageNum++);
                            pageMap.put("content", builder.toString());
                            PageDAOUtils.insertData(pageMap);
                            if (!showedFlag) {
                                showedFlag = true;
                                txtContent.initPage((Integer) txtMap.get("id"), 1);
                            }
                            TxtDAOUtil.updateOverFlag((Integer) txtMap.get("id"));
                            handler.sendEmptyMessage(1);
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                            }
                        };
                        t.start();
                    } else {
                        showedFlag = true;
                        System.out.println("11111+" + txtMap.get("id") + "2222222222++" + txtMap.get("nowPage"));
                        txtContent.initPage((Integer) txtMap.get("id"), (Integer) txtMap.get("nowPage"));
                    }
                    System.out.println("-inputStream!!!!!!!!!!:" + inputStream.toString());
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

    }

    public void BoolPage() {
        String url = "http://" + SERVER_HOST + "/page?method=10";
        System.out.println("url:" + url);
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
                                inputStream = new ByteArrayInputStream(content.getBytes());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    if ((Integer) (txtMap.get("overFlag")) == 0) {
                            t = new Thread() {
                            @Override
                            public void run() {
                        try {
                            PageDAOUtils.deletePageData((Integer) txtMap.get("id"));
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(inputStream));
                            System.out.println("reader!!!!!!!!!!!!!!!!!!:" + reader);
                            String line = null;
                            builder = new StringBuilder();
                            lineCount = 0;
                            pageNum = 1;
                            while ((line = reader.readLine()) != null) {
                                while (line.length() > 20) {
                                    String tempStr = line.substring(0, 20);
                                    System.out.println("content:!!!!!!!!!!!!!!!!!!" + tempStr);
                                    addPageLineData(tempStr);
                                    line = line.substring(20);
                                }
                                addPageLineData(line);
                            }
                            pageMap.put("txtId", txtMap.get("id"));
                            pageMap.put("pageNum", pageNum++);
                            pageMap.put("content", builder.toString());
                            PageDAOUtils.insertData(pageMap);
                            if (!showedFlag) {
                                showedFlag = true;
                                txtContent.initPage((Integer) txtMap.get("id"), 1);
                            }
                            TxtDAOUtil.updateOverFlag((Integer) txtMap.get("id"));
                            handler.sendEmptyMessage(1);
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                            }
                        };
                        t.start();
                    } else {
                        showedFlag = true;
                        System.out.println("11111+" + txtMap.get("id") + "2222222222++" + txtMap.get("nowPage"));
                        txtContent.initPage((Integer) txtMap.get("id"), (Integer) txtMap.get("nowPage"));
                    }
                    System.out.println("-inputStream!!!!!!!!!!:" + inputStream.toString());
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

    }


    @Override
    protected void onDestroy() {
        TxtDAOUtil.updateOverFlag((Integer) txtMap.get("id"));

        System.out.println(nowPage);
        TxtDAOUtil.updateNowPage((Integer) txtMap.get("id"), nowPage);
        System.out.println(txtMap);
        super.onDestroy();
    }

    private void addPageLineData(String lineData) throws InterruptedException {
        lineCount++;
        builder.append(lineData);
        builder.append(Globals.TXT_SEP_FLAG);
        if (lineCount == Globals.LINE_COUNT) {
            pageMap.put("txtId", txtMap.get("id"));
            pageMap.put("pageNum", pageNum++);
            pageMap.put("content", builder.toString());
            PageDAOUtils.insertData(pageMap);
            handler.sendEmptyMessage(0);
            if (!showedFlag) {
                showedFlag = true;
                txtContent.initPage((Integer) txtMap.get("id"), 1);
            }

            Thread.sleep(5);
            builder = new StringBuilder();
            lineCount = 0;
        }
    }
//    private Handler handler;
//    private Map<String, Object> txtMap = null;
//    private Map<String, Object> pageMap = new HashMap<String, Object>();
//    private int lineCount;
//    private int pageNum;
//    private StringBuilder builder;
//    private String novelName="123";
//    private TextView txtPage;
//    private boolean showedFlag = false;
//    private int nowPage = 1;
//
//    public int getNowPage() {
//        return nowPage;
//    }
//
//    public void setNowPage(int nowPage) {
//        this.nowPage = nowPage;
//    }
//
//    private MyView txtContent;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
//        txtPage = (TextView) findViewById(R.id.txt_page);
//        txtContent = (MyView) findViewById(R.id.txt_content);
//        // novelName = getIntent().getStringExtra("novelName");
//
//        System.out.println(txtPage.getText());
//
//        insertData(novelName);
//
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        System.out.println("out:"+txtMap);
//        getTxtDataBynovelName(novelName);
//        System.out.println("out:yes");
//        return super.onCreateView(name, context, attrs);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        TxtDAOUtil.updateNowPage((Integer) txtMap.get("id"), nowPage);
//        super.onDestroy();
//    }
//
//    private void addPageLineData(String lineData) throws InterruptedException {
//        lineCount++;
//        builder.append(lineData);
//        builder.append(Globals.TXT_SEP_FLAG);
//        if (lineCount == Globals.LINE_COUNT) {
//            pageMap.put("txtId", txtMap.get("id"));
//            pageMap.put("pageNum", pageNum++);
//            pageMap.put("content", builder.toString());
//            PageDAOUtils.insertData(pageMap);
//            handler.sendEmptyMessage(0);
//            if (!showedFlag) {
//                showedFlag = true;
//                txtContent.initPage((Integer) txtMap.get("id"), 1);
//            }
//            Thread.sleep(5);
//            builder = new StringBuilder();
//            lineCount = 0;
//        }
//    }
//
//    public void getTxtDataBynovelName(final String novelName) {
//        //String sql = "SELECT id,now_page,over_flag FROM txt WHERE novelName = ?";
//        String url = "http://" + SERVER_HOST + "/page?method=5";
//        final JSONObject reqObject = new JSONObject();
//        JSONObject userObject = new JSONObject();
//        JSONArray userArray = new JSONArray();
//        try {
//            userArray.put(userObject.put("novelName", novelName));
//            reqObject.put("ReqCode", "1").put("info", userArray);
//        } catch (JSONException e) {
//        }
//        System.out.println(reqObject);
//        System.out.println("in:yes");
//        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.POST, url, reqObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("xyf", "correct resp111:" + response.toString());
//                try {
//                    Map<String, Object> hashMap = new HashMap<>();
//                    int reqCode = response.getInt("reqCode");
//                    Log.d("xty", "reqCode: " + reqCode);
//                    if (1 == reqCode) {
//                        JSONArray list = response.getJSONArray("info");
//                        for (int i = 0; i < list.length(); i++) {
//                            JSONObject userObject = (JSONObject) list.opt(i);
//                            hashMap.put("id",
//                                    userObject.getInt("id"));
//                            hashMap.put("page",
//                                    userObject.getInt("page"));
//                            hashMap.put("flag",
//                                    userObject.getInt("flag"));
//                        }
//                        txtMap = hashMap;
//                        System.out.println("in:"+txtMap);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("in2:"+txtMap);
//                BOOL(txtMap);
//                CreatHandle(txtPage,pageNum);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("xyf", "error:" + error.toString());
//            }
//        });
//        mRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, 1.0f));
//        MyApplication.getRequestQueue().add(mRequest);
//    }
//
//    public void CreatHandle(final TextView txtPage, final int pageNum) {
//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == 0) {
//                    txtPage.setText("已经处理好了：" + (pageNum - 1) + "页的数据");
//                } else {
//                    txtPage.setText("分页完成，当前：1/" + (pageNum - 1));
//                }
//            }
//        };
//    }
//
//    public void BOOL(final Map txtMap) {
//        System.out.println("over_flag:"+txtMap.get("flag"));
//        if ((Integer) (txtMap.get("flag")) == 0) {
//            Thread t = new Thread() {
//                @Override
//                public void run() {
//
//                    try {
//                        PageDAOUtils.deletePageData((Integer) txtMap.get("id"));
//                        NovelDaoUtils novelDaoUtils = new NovelDaoUtils();
//                        System.out.println("!!!!!!!!!!!!!!!!!!!:"+novelDaoUtils.getNovelContent(novelName));
//                        BufferedReader reader = new BufferedReader(
//                                new InputStreamReader(novelDaoUtils.getNovelContent(novelName), "GBK"));
//                        String line = null;
//                        builder = new StringBuilder();
//                        lineCount = 0;
//                        pageNum = 1;
//
//                        while ((line = reader.readLine()) != null) {
//                            while (line.length() > 20) {
//                                String tempStr = line.substring(0, 20);
//                                addPageLineData(tempStr);
//                                line = line.substring(20);
//                            }
//                            addPageLineData(line);
//                        }
//                        pageMap.put("txtId", txtMap.get("id"));
//                        pageMap.put("pageNum", pageNum++);
//                        pageMap.put("content", builder.toString());
//
//                        PageDAOUtils.insertData(pageMap);
//                        if (!showedFlag) {
//                            showedFlag = true;
//                            txtContent.initPage((Integer) txtMap.get("id"), 1);
//                        }
//                        TxtDAOUtil.updateOverFlag((Integer) txtMap.get("id"));
//                        handler.sendEmptyMessage(1);
//                        reader.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            t.start();
//        } else {
//            showedFlag = true;
//            txtContent.initPage((Integer) txtMap.get("id"), (Integer) txtMap.get("nowPage"));
//        }
//    }
}
