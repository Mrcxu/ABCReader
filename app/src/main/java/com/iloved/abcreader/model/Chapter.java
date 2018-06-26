package com.iloved.abcreader.model;

/**
 * Created by Administrator on 2018/6/4.
 */

public class Chapter {
    private String chapterno;
    private String chaptername;


    public Chapter(String chapterno, String chaptername){
        this.chapterno=chapterno;
        this.chaptername=chaptername;
    }

    public String getChapterno() {
        return chapterno;
    }

    public void setChapterno(String chapterno) {
        this.chapterno = chapterno;
    }

    public String getChaptername() {
        return chaptername;
    }

    public void setChaptername(String chaptername) {
        this.chaptername = chaptername;
    }
}
