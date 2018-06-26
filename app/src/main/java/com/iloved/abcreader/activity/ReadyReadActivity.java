package com.iloved.abcreader.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iloved.abcreader.R;
import com.iloved.abcreader.adapter.ChapterListAdapter;
import com.iloved.abcreader.interfaces.OnChapterClickListener;
import com.iloved.abcreader.model.Chapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.iloved.abcreader.util.Globals.initWindow;

/**
 * Created by admin on 2018/6/6.
 */

public class ReadyReadActivity extends AppCompatActivity implements View.OnClickListener{
    private OnChapterClickListener listener;
    private List<Chapter> mList=new ArrayList<Chapter>();
    RecyclerView recyclerView;
    ImageView imageView;
    TextView textView_title,textView_author,textView_style,textView_introduce,textView_more;
    Button btn1,btn2;
    private String bookid;
    private String chapterNum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ready_read);
        initWindow(this);
        Bundle bundle = this.getIntent().getExtras();

        initData();
        setClick();
        int picture = bundle.getInt("picture");
        String title = bundle.getString("title");
        String author = bundle.getString("author");
        String style = bundle.getString("style");
        String introduction = bundle.getString("introduction");
        String content=bundle.getString("content");
        String isCollect=bundle.getString("isCollect");
        chapterNum=bundle.getString("chapterNum");
        bookid = bundle.getString("bookid");
        textView_title.setText(title);
        imageView.setImageResource(picture);
        textView_author.setText(author);
        textView_style.setText(style);
        textView_introduce.setText(introduction);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        if(isCollect.equals("0")){
            btn1.setText("订阅");
        }else if(isCollect.equals("1")) {
            btn1.setText("取消订阅");
        }
        textView_more.setOnClickListener(new View.OnClickListener() {
            Boolean flag = true;
            @Override
            public void onClick(View view) {
                if (flag) {
                    flag = false;
                    textView_introduce.setEllipsize(null); // 展开
                    textView_introduce.setMaxLines(5);
                } else {
                    flag = true;
                    textView_introduce.setEllipsize(TextUtils.TruncateAt.END);
                    textView_introduce.setMaxLines(1);// 收缩
                }
            }
        });
        init();
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        ChapterListAdapter chapterListAdapter=new ChapterListAdapter(mList);
        chapterListAdapter.setOnItemClickListener(new OnChapterClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent intent = new Intent(ReadyReadActivity.this, DetailActivity.class);
                intent.putExtra("novelName", textView_title.getText());
                intent.putExtra("chapterID",(id+1)+"");
                intent.putExtra("ifChapter",1);
                startActivity(intent);
                Toast.makeText(ReadyReadActivity.this,"onClick事件 点击了第："+(id+1)+"章",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(int id) {
                Toast.makeText(ReadyReadActivity.this,"onLongClick事件 点击了第："+id+"章",Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setAdapter(chapterListAdapter);
    }
    private void setClick(){

    }

    private void initData(){
        recyclerView= findViewById(R.id.list_chapter);
        imageView = findViewById(R.id.detile_picture);
        textView_title = findViewById(R.id.book_title);
        textView_author = findViewById(R.id.bookauthor);
        textView_introduce = findViewById(R.id.bookintroduce);
        textView_style = findViewById(R.id.bookstyle);
        btn1 = findViewById(R.id.dingyue);
        btn2 = findViewById(R.id.begin);
        textView_more = findViewById(R.id.bookmore);
    }
    private void init(){
        int chapterNum2=Integer.parseInt(chapterNum);
        Log.d("chapterNum",chapterNum2+"");
        for (int i=1;i<chapterNum2+1;i++){
            Chapter chapter=new Chapter("第"+i+"章","");
            mList.add(chapter);
        }

    }

    @Override
    public void onClick(View view) {
        Boolean flag = true;
        switch (view.getId()){
            case R.id.dingyue:
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("wythread", "sssss");
                        try
                        {
                            if(btn1.getText().equals("取消订阅")){
                                btn1.setText("订阅");
                                String url="http://106.15.196.105:8080/novelweb_war/collect?method=3&id="+bookid;
//                                String url="http://192.168.43.172:8080/collect?method=3&id="+bookid;
                                Log.d("url=",url);
                                HttpClient httpClient=new DefaultHttpClient();
                                HttpGet httpGet=new HttpGet(url);
                                HttpResponse response = httpClient.execute(httpGet);
                                Log.d("wyhttp","sssssssssssssssssssssssss");
                            }else {
                                btn1.setText("取消订阅");
                                String url="http://106.15.196.105:8080/novelweb_war/collect?method=2&id="+bookid;
//                                String url="http://192.168.43.172:8080/collect?method=2&id="+bookid;
                                Log.d("url=",url);
                                HttpClient httpClient=new DefaultHttpClient();
                                HttpGet httpGet=new HttpGet(url);
                                HttpResponse response = httpClient.execute(httpGet);
                                Log.d("wyhttp","sssssssssssssssssssssssss");
                            }

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                break;
            case R.id.begin:
                Intent intent = new Intent(this,DetailActivity.class);
                intent.putExtra("novelName",textView_title.getText());
                startActivity(intent);
                break;
        }
    }
}
