package com.iloved.abcreader.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.iloved.abcreader.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import static com.iloved.abcreader.util.Globals.initWindow;

/**
 * Created by Iloved on 2018/6/4.
 */

public class FeedBack extends Activity{
    SystemBarTintManager tintManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_feedback);
        super.onCreate(savedInstanceState);
        initWindow(this);
        Button btn = findViewById(R.id.feedback_submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FeedBack.this,"提交反馈成功",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

}
