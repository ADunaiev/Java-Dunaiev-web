package step.learning.servlets;

import com.google.inject.Singleton;
import step.learning.dal.dao.ActionDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class ActionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ActionDao actionDao = new ActionDao();

        req.setAttribute("action", actionDao.getAction());
        req.setAttribute("page-body", "action");

        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);

    }
}
