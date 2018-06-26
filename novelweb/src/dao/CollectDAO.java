package dao;

import db.SqlManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CollectDAO {
    SqlManager manager;
    String sql = "";
    String sql2="";
    String sql3="";
    ResultSet rs;
    public CollectDAO() throws IOException, ClassNotFoundException, SQLException {
        manager = SqlManager.createInstance();
    }

    /**
     * 获取所有收藏
     */
    public JSONObject getAll() throws SQLException{
        JSONObject jsonObject=new JSONObject();;
        JSONArray jsonArray=new JSONArray();
        sql = "select * from collect";
        manager.connectDB();
        rs = manager.executeQuery(sql, null);
        try{
            int count = 0;
            while (rs.next()){
                JSONObject jsonObject2 = new JSONObject();
                String bookid = rs.getString("bookid");
                String types = rs.getString("type");
                String novelName = rs.getString("novelName");
                String author = rs.getString("author");
                String content=rs.getString("content");
                String introduction=rs.getString("introduction");
                String chapterNum=rs.getString("chapterNum");
                jsonObject2.put("bookid", bookid);
                jsonObject2.put("type", types);
                jsonObject2.put("novelName", novelName);
                jsonObject2.put("author", author);
                jsonObject2.put("content",content);
                jsonObject2.put("introduction",introduction);
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
    /**
     * 加入小说到收藏
     */
    public void insertNovelById(String id) throws SQLException{
        sql = "select * from novel where _id="+id;
        sql3="update novel set isCollect=1 where _id="+id;
        System.out.println(sql);
        manager.connectDB();
        rs = manager.executeQuery(sql, null);
        while (rs.next()){
            int _id=rs.getInt("_id");
            String types = rs.getString("type");
            String novelName = rs.getString("novelName");
            String author = rs.getString("author");
            String content=rs.getString("content");
            String introduction=rs.getString("introduction");
            String chapterNum=rs.getString("chapterNum");
            sql2="insert into collect (id,bookid,type,novelName,author,content,introduction,chapterNum) values(null,'"+_id+"','"+types+"','"+novelName+"','"+author+"','"+content+"','"+introduction+"','"+chapterNum+"')";
        }
        System.out.println(sql2);
        System.out.println(sql3);
        manager.executeUpdate(sql2,null);
        manager.executeUpdate(sql3,null);
        manager.closeDB();
    }

    /**
     * 长按删除收藏
     */
    public void deleteNovelById(String id) throws SQLException{
        sql = "delete from collect where bookid="+id;
        sql2="update novel set isCollect=0 where _id="+id;
        manager.connectDB();
        System.out.println(sql);
        System.out.println(sql2);
        manager.executeUpdate(sql,null);
        manager.executeUpdate(sql2,null);
        manager.closeDB();
    }
}
