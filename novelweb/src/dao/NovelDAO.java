package dao;

import db.SqlManager;
import model.Novel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class NovelDAO {
    SqlManager manager;
    String sql = "";
    ResultSet rs;
    public NovelDAO() throws IOException, ClassNotFoundException, SQLException {
        manager = SqlManager.createInstance();
    }

    /**
     * 从数据库通过ID获取小说
     * @param id
     * @return
     * @throws SQLException
     */
    public Novel getNovelById(String id) throws SQLException{
        sql = "select * from novel where _id="+id;
        manager.connectDB();
        rs = manager.executeQuery(sql, null);
            Novel novel = new Novel(rs.getString("_id"),
                    rs.getString("type"), rs.getString("novelName"),
                    rs.getString("author"),rs.getString("content"),rs.getString("introduction"),rs.getString("isCollect"),rs.getString("chapterNum"));
        manager.closeDB();
        return novel;
    }

    /**
     * 从数据库通过类型获取小说
     * @param type
     * @return
     * @throws SQLException
     */
    public JSONObject getNovelByType(String type)throws SQLException{
        JSONObject jsonObject=new JSONObject();;
        JSONArray jsonArray=new JSONArray();
        sql = "select * from novel where type=" +type;
        System.out.println(sql);
        manager.connectDB();
        rs = manager.executeQuery(sql, null);
        try{
            int count=0;
            while (rs.next()){
                JSONObject jsonObject2 = new JSONObject();
                String _id = rs.getString("_id");
                String types = rs.getString("type");
                String novelName = rs.getString("novelName");
                String author = rs.getString("author");
                String content=rs.getString("content");
                String introduction=rs.getString("introduction");
                String isCollect=rs.getString("isCollect");
                String chapterNum=rs.getString("chapterNum");
                jsonObject2.put("_id", _id);
                jsonObject2.put("type", types);
                jsonObject2.put("novelName", novelName);
                jsonObject2.put("author", author);
                jsonObject2.put("content", content);
                jsonObject2.put("introduction", introduction);
                jsonObject2.put("isCollect", isCollect);
                jsonObject2.put("chapterNum",chapterNum);
                jsonArray.put(jsonObject2);
                count++;
            }
            jsonObject.put("reqCode","1").put("totalNum", count).put("info",jsonArray);
        }catch (JSONException e) {
            throw new RuntimeException(e);
        }

        manager.closeDB();
        return jsonObject;
    }

    public JSONObject getSearch(String search) throws SQLException{
        JSONObject jsonObject=new JSONObject();;
        JSONArray jsonArray=new JSONArray();
        sql = "SELECT * FROM novel WHERE CONCAT(IFNULL(novelName,''),IFNULL(author,'')) LIKE '%"+search+"%'";
        manager.connectDB();
        rs = manager.executeQuery(sql, null);
        try{
            int count=0;
            while (rs.next()){
                JSONObject jsonObject2 = new JSONObject();
                String _id = rs.getString("_id");
                String types = rs.getString("type");
                String novelName = rs.getString("novelName");
                String author = rs.getString("author");
                String content = rs.getString("content");
                String introduction=rs.getString("introduction");
                String isCollect=rs.getString("isCollect");
                String chapterNum=rs.getString("chapterNum");
                jsonObject2.put("_id", _id);
                jsonObject2.put("type", types);
                jsonObject2.put("novelName", novelName);
                jsonObject2.put("author", author);
                jsonObject2.put("content",content);
                jsonObject2.put("introduction", introduction);
                jsonObject2.put("isCollect",isCollect);
                jsonObject2.put("chapterNum",chapterNum);
                jsonArray.put(jsonObject2);
                count++;
            }
            jsonObject.put("reqCode","1").put("totalNum", count).put("info",jsonArray);
        }catch (JSONException e) {
            throw new RuntimeException(e);
        }

        manager.closeDB();
        return jsonObject;
    }
}
