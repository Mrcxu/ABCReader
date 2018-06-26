package servlet;

import dao.CollectDAO;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CollectServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        int method = Integer.parseInt(req.getParameter("method"));
        String mId = null;
        mId = req.getParameter("id");
        switch (method) {
            case 1:
                getCollect(req,resp);
                break;
            case 2:
                insert(mId);
                break;
            case 3:
                delete(mId);
                break;
            default:
                break;
        }


    }

    private void getCollect(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        CollectDAO collectDAO;
        JSONObject jsonObject=new JSONObject();
        try {
            collectDAO=new CollectDAO();
            jsonObject=collectDAO.getAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        PrintWriter out = resp.getWriter();
        out.println(jsonObject);
        out.flush();
        out.close();
    }

    private void insert(String mId) throws IOException{
        CollectDAO collectDAO;
        try {
            collectDAO=new CollectDAO();
            collectDAO.insertNovelById(mId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void delete(String mId) throws IOException{
        CollectDAO collectDAO;
        try {
            collectDAO=new CollectDAO();
            collectDAO.deleteNovelById(mId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
