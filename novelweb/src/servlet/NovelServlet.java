package servlet;


import dao.NovelDAO;
import model.Novel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class NovelServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        try {
            getNovel(req,resp);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将获取到的数据存入Json
     * @param request
     * @param response
     * @throws IOException
     */
    private void getNovel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        int method = Integer.parseInt(request.getParameter("method"));
        String mId = request.getParameter("id");
        String type=request.getParameter("type");
        switch (method) {
            case 1:
                byid(request,response);
                break;
            case 2:
                bytype(request,response);
                break;
            case 3:
                search(request,response);
                break;
            default:
                break;
        }
    }

    public void byid(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String mId = request.getParameter("id");
        JSONObject jObject = new JSONObject();
        NovelDAO novelDAO;
        try {
            novelDAO = new NovelDAO();
            Novel novel = novelDAO.getNovelById(mId);
            JSONObject retObject = new JSONObject();
            retObject.put("totalNum", 1);
            JSONArray userlist = new JSONArray();
            JSONObject novelObject = new JSONObject();
            novelObject.put("_id", novel.get_id());
            novelObject.put("type", novel.getType());
            novelObject.put("novelName", novel.getNovelName());
            novelObject.put("author", novel.getAuthor());
            novelObject.put("content", novel.getContent());
            userlist.put(novelObject);
            retObject.put("info", userlist);
            jObject.put("retCode", 0);
            jObject.put("msg", "ok");
            jObject.put("data", retObject);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                jObject.put("ret", 1);
                jObject.put("msg", e.getMessage());
                jObject.put("data", "");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        PrintWriter out = response.getWriter();
        out.println(jObject);
        out.flush();
        out.close();
    }

    public void bytype(HttpServletRequest request, HttpServletResponse respons) throws IOException{
        String type = request.getParameter("type");
        JSONObject jsonObject=new JSONObject();
        NovelDAO novelDAO;
        try{
            novelDAO=new NovelDAO();
            jsonObject=novelDAO.getNovelByType(type);
        }catch (Exception e){
            e.printStackTrace();
        }
        PrintWriter out = respons.getWriter();
        out.println(jsonObject);
        out.flush();
        out.close();
    }

    public void search(HttpServletRequest request, HttpServletResponse respons) throws IOException{
        String search = request.getParameter("search");
        JSONObject jsonObject=new JSONObject();
        NovelDAO novelDAO;
        try{
            novelDAO=new NovelDAO();
            jsonObject=novelDAO.getSearch(search);
        }catch (Exception e){
            e.printStackTrace();
        }
        PrintWriter out = respons.getWriter();
        out.println(jsonObject);
        out.flush();
        out.close();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
