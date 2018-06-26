package com.iloved.abcreader.view;

/**
 * Created by Iloved on 2018/6/7.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.iloved.abcreader.R;
import com.iloved.abcreader.activity.DetailActivity;
import com.iloved.abcreader.util.Globals;
import com.iloved.abcreader.util.PageDAOUtils;

public class MyView extends View {
    private int pageNum;
    private int txtId;

    private String preContent;
    private String content;
    private String nextContent;
    private float startX = 0;
    private float nowX = 0;
    private TextView txtPage;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startX = event.getX();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    nowX = event.getX();
                    postInvalidate();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    nowX = event.getX();
                    if (Math.abs(nowX - startX) < 50) {
                    } else if (nowX > startX) {
                        if (pageNum > 1) {
                            pageNum--;
                            changePage();
                        }
                    } else {
                        if (pageNum < PageDAOUtils.getAllCount(txtId)) {
                            pageNum++;
                            changePage();
                        }
                    }
                    nowX = 0;
                    startX = 0;
                }
                return false;
            }

        });
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initPage(int txtId, int pageNum) {
        this.txtId = txtId;
        this.pageNum = pageNum;
        changePage();
    }

    private void changePage() {
        content = PageDAOUtils.getPageContent(txtId, pageNum);
        if (pageNum > 1) {
            preContent = PageDAOUtils.getPageContent(txtId, pageNum - 1);
        } else {
            preContent = null;
        }
        if (pageNum < PageDAOUtils.getAllCount(txtId)) {
// 有下一页
            nextContent = PageDAOUtils.getPageContent(txtId, pageNum + 1);
        } else {
            nextContent = null;
        }

        DetailActivity a = (DetailActivity) getContext();

        if (txtPage == null) {
            txtPage = a.findViewById(R.id.txt_page);
        }
        txtPage.setText("当前：" + pageNum + "/"
                + PageDAOUtils.getAllCount(txtId));
        a.setNowPage(pageNum);
        super.postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (content != null) {
            String[] allStr = content.split(Globals.TXT_SEP_FLAG);
            Paint paint = new Paint();
            paint.setTextSize(Globals.CHAR_SIZE);
            paint.setColor(Color.BLACK);
            for (int i = 0; i < allStr.length; i++) {
                for (int j = 0; j < allStr[i].length(); j++) {
                    canvas.drawText(String.valueOf(allStr[i].charAt(j)),
                            Globals.PAGE_SEP + j
                                    * (Globals.CHAR_SIZE + Globals.CHAR_SEP)
                                    + (nowX - startX), (i + 1)
                                    * (Globals.CHAR_SIZE + Globals.LINE_SEP),
                            paint);
                }
            }

            if (nowX < startX && nextContent != null) {
                allStr = nextContent.split(Globals.TXT_SEP_FLAG);
                for (int i = 0; i < allStr.length; i++) {
                    for (int j = 0; j < allStr[i].length(); j++) {
                        canvas.drawText(
                                String.valueOf(allStr[i].charAt(j)),
                                Globals.PAGE_SEP
                                        + j
                                        * (Globals.CHAR_SIZE + Globals.CHAR_SEP)
                                        + (nowX - startX)
                                        + Globals.SCREEN_WIDTH,
                                (i + 1)
                                        * (Globals.CHAR_SIZE + Globals.LINE_SEP),
                                paint);
                    }
                }
            }
            if (nowX > startX && preContent != null) {
                allStr = preContent.split(Globals.TXT_SEP_FLAG);
                for (int i = 0; i < allStr.length; i++) {
                    for (int j = 0; j < allStr[i].length(); j++) {
                        canvas.drawText(
                                String.valueOf(allStr[i].charAt(j)),
                                Globals.PAGE_SEP
                                        + j
                                        * (Globals.CHAR_SIZE + Globals.CHAR_SEP)
                                        + (nowX - startX)
                                        - Globals.SCREEN_WIDTH,
                                (i + 1)
                                        * (Globals.CHAR_SIZE + Globals.LINE_SEP),
                                paint);
                    }
                }
            }
        }
    }
}