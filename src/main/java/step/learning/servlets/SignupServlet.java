package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import step.learning.dal.dao.CartDao;
import step.learning.dal.dao.UserDao;
import step.learning.dal.dto.User;
import step.learning.services.form.FormParseResult;
import step.learning.services.form.FormParseService;
import step.learning.services.kdf.KdfService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Singleton
public class SignupServlet extends HttpServlet {

    private final FormParseService formParseService;

    private final UserDao userDao;
    private final KdfService kdfService;

    @Inject
    public SignupServlet(FormParseService formParseService, UserDao userDao, KdfService kdfService) {
        this.formParseService = formParseService;
        this.userDao = userDao;
        this.kdfService = kdfService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //userDao.installTable();
        // додаємо до атрибутів запиту додатковий - щодо тіла у шаблоні
        req.setAttribute("page-body", "signup");
        // імітуємо наче запит є "/WEB_INF/_layout.jsp" і передаємо у нього
        // req із доданим атрібутом
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ;
        FormParseResult parseResult = formParseService.parse(req);

        Map<String, String> fields = parseResult.getFields();
        Map<String, FileItem> files = parseResult.getFiles();

        String userName = fields.get("user-name");
        if(userName == null || userName.isEmpty()) {
            sendRest(resp, "error", "Property 'user-name' required", null);
            return;
        }

        String userEmail = fields.get("user-email");
        if(userEmail == null || userEmail.isEmpty()) {
            sendRest(resp, "error", "Property 'user-email' required", null);
            return;
        }

        String userPassword = fields.get("user-password");
        if(userPassword == null || userPassword.isEmpty()) {
            sendRest(resp, "error", "Property 'user-password' required", null);
            return;
        }
        FileItem avatar = files.get("user-avatar");

        //реєструємо користувача у БД
        User user = new User();

        user.setId(UUID.randomUUID());
        user.setName(userName);
        user.setEmail(userEmail);
        user.setSalt(kdfService.derivedKey( UUID.randomUUID().toString(), ""));
        user.setDerivedKey(kdfService.derivedKey( userPassword, user.getSalt()));

        if(avatar != null) {
            // avatar - не обов'язкове поле, але якщо є то проходдить превірку
            String path = req.getServletContext().getRealPath("/") +
                    "img" + File.separator + "avatar" + File.separator;
            // визначаємо тип файла (розширення)
            int dotPosition = avatar.getName().lastIndexOf(".");
            if(dotPosition < 0) {
                sendRest(resp, "error", "Avatar file must have extension", null);
                return;
            }
            String ext = avatar.getName().substring(dotPosition);

            String savedName;
            File savedFile;
            // формуємо нове ім'я файла, зберігаємо розширення
            do {
                savedName = UUID.randomUUID() + ext;
                savedFile = new File(path, savedName);
            } while(savedFile.exists());

            try {
                avatar.write( savedFile);
                user.setAvatar(savedName);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }


        }

        if( userDao.registerUser(user) ) {
            sendRest(resp, "success", "User is registered", user.getId().toString() );
        }
        else {
            sendRest(resp, "error", "Server internal error, look at server logs", null);
        }


    }

    private  void  sendRest(HttpServletResponse resp, String status, String message, String id) throws IOException {
        JsonObject rest = new JsonObject();
        JsonObject meta = new JsonObject();
            meta.addProperty("service", "signup");
            meta.addProperty("status",  status);
            meta.addProperty("message", message);
            meta.addProperty("time",    Instant.now().getEpochSecond());
        rest.add("meta", meta);
        JsonObject data = null;
        if(id != null) {
            data = new JsonObject();
            data.addProperty("id", id);
        }
        rest.add("data", data);

        Gson gson = new GsonBuilder().serializeNulls().create();
        resp.getWriter().print(gson.toJson(rest));
    }
}

/*
REST {
meta: {
    service: "signup",
    status: "success",
    message: "User is created",
    time: 128711823
},
data: {
    id: "...."
}
}

Щодо файлової системи.
Процеси, що відповідають за веб-застосунок, виконуються веб-сервером.
Тому "домашня директорія" належить йому.
new File(".").getAbsolutePath()  C:\xampp\tomcat\bin\.

Для того, щоб встановити розміщення ресурсів застосунку виконується
звернення до контексту (ServletContext)
req.getServletContext().getRealPath("/")  C:\Users\Lector\source\repos\Java-Web-SPD111\target\Java-Web-SPD111\
Але также звертаємо увагу, що посилання іде на deploy-розміщення
(\target\...)

Розуміємо, що файли які є у проєкті, та файли, які вже напрацьовані проєктом
під час роботи - це різні ресурси з різним розміщенням.

 */
