package step.learning.servlets;

import com.google.inject.Singleton;
import step.learning.dal.dao.CartDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class CartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // запит даних
        CartDao cartDao = new CartDao();
        // передача їх до представлення (View)
        req.setAttribute("cart", cartDao.getCart());

        // додаємо до атрибутів запиту додатковий - щодо тіла у шаблоні
        req.setAttribute("page-body", "cart");
        // імітуємо наче запит є "/WEB_INF/_layout.jsp" і передаємо у нього
        // req із доданим атрібутом
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);

    }
}
