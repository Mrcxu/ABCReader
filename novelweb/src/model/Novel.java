package model;


public class Novel {
    private String _id;
    private String type;
    private String novelName;
    private String author;
    private String content;
    private String introduction;
    private String isCollect;
    private String chapterNum;



    public Novel(String id, String type, String novelName, String author, String content, String introduction, String isCollect,String chapterNum) {
        this.set_id(id);
        this.setType(type);
        this.setNovelName(novelName);
        this.setAuthor(author);
        this.setContent(content);
        this.setIntroduction(introduction);
        this.setIsCollect(isCollect);
        this.setChapterNum(chapterNum);
    }
    public Novel(String id, String type, String novelName, String author) {
        this.set_id(id);
        this.setType(type);
        this.setNovelName(novelName);
        this.setAuthor(author);
    }

    public Novel(String novelName){
        this.setNovelName(novelName);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNovelName() {
        return novelName;
    }

    public void setNovelName(String novelName) {
        this.novelName = novelName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public String getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(String chapterNum) {
        this.chapterNum = chapterNum;
    }
}
