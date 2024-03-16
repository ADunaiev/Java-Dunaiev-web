package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.db.DbService;
import step.learning.services.hash.HashService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class HomeServlet extends HttpServlet {

    private final HashService hashService;
    private final DbService dbService;

    @Inject
    public HomeServlet(HashService hashService, DbService dbService) {
        this.hashService = hashService;
        this.dbService = dbService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("hash", hashService.digest(("123")));
        req.setAttribute("db", dbService.getConnection() != null ? "Success" : "Error");
        // додаємо до атрибутів запиту додатковий - щодо тіла у шаблоні
        req.setAttribute("page-body", "home");
        // імітуємо наче запит є "/WEB_INF/_layout.jsp" і передаємо у нього
        // req із доданим атрібутом
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }
}
/*

Сервлети - це спеціалізовані класи в Java для спеціалізованої роботи.
Можна вважати їх аналогами контролерів.
Для роботи з сервлетами потрібно підключити javax servlet API



 */
