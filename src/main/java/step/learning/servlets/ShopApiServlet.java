package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import step.learning.dal.dao.CartDao;
import step.learning.dal.dao.ProductDao;
import step.learning.dal.dao.UserDao;
import step.learning.dal.dto.Product;
import step.learning.dal.dto.User;
import step.learning.services.form.FormParseResult;
import step.learning.services.form.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Singleton
public class ShopApiServlet extends HttpServlet {

    private final FormParseService formParseService;
    private final ProductDao productDao;
    private final UserDao userDao;

    private final CartDao cartDao;

    @Inject
    public ShopApiServlet(FormParseService formParseService, ProductDao productDao, UserDao userDao, CartDao cartDao) {
        this.formParseService = formParseService;
        this.productDao = productDao;
        this.userDao = userDao;
        this.cartDao = cartDao;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormParseResult parseResult = formParseService.parse(req);

        Map<String, String> fields = parseResult.getFields();
        Map<String, FileItem> files = parseResult.getFiles();

        // Перевірити токен та його валідність
        String token_id = fields.get("token");

        if(token_id == null || token_id.isEmpty()) {
            resp.setStatus(401);
            sendRest(resp,"error", "Token is empty", null);
            return;
        }
        else if (userDao.getUserByToken(token_id) == null) {
            resp.setStatus(403);
            sendRest(resp, "error", "Token is not valid", null);
            return;
        }

        // Перевірити поля з даними
        String name = fields.get("name");
        if(name == null || name.isEmpty()) {
            sendRest(resp, "error", "Property 'name' required", null);
            return;
        }

        String price = fields.get("price");
        if(price == null || price.isEmpty()) {
            sendRest(resp, "error", "Property 'price' required", null);
            return;
        }
        else if (Double.parseDouble(price) <= 0) {
            sendRest(resp, "error", "Property 'price' should be positive", null);
            return;
        }

        String description = fields.get("description");

        if(description == null || description.isEmpty()) {
            sendRest(resp, "error", "Property 'description' required", null);
            return;
        }
        else if (description.length() <= 50) {
            sendRest(resp, "error", "Property 'description' should be more than 50 characters", null);
            return;
        }

        FileItem image = files.get("image");

        //реєструємо користувача у БД
        Product product = new Product();

        product.setId(UUID.randomUUID());
        product.setName(name);
        product.setPrice(Double.parseDouble(price));
        product.setDescription(description);

        if(image != null) {
            // image - не обов'язкове поле, але якщо є то проходдить превірку
            String path = req.getServletContext().getRealPath("/") +
                    "img" + File.separator + "products" + File.separator;
            // визначаємо тип файла (розширення)
            int dotPosition = image.getName().lastIndexOf(".");
            if(dotPosition < 0) {
                sendRest(resp, "error", "Image file must have extension", null);
                return;
            }
            String ext = image.getName().substring(dotPosition);

            // перевірка допустимого типу файла
            String[] allowedExtensions = new String[] {".png", ".jpg", ".jpeg", ".bmp"};
            if (!Arrays.asList(allowedExtensions).contains(ext)) {
                sendRest(resp, "error", "This file extension is not allowed!", null);
                return;
            }

            String savedName;
            File savedFile;
            // формуємо нове ім'я файла, зберігаємо розширення
            do {
                savedName = UUID.randomUUID() + ext;
                savedFile = new File(path, savedName);
            } while(savedFile.exists());

            try {
                image.write( savedFile);
                product.setImage(savedName);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }

        if( productDao.add(product) ) {
            sendRest(resp, "success", "Product added", product.getId().toString() );
        }
        else {
            sendRest( resp, "error", "Internal error, look at server's logs", null ) ;
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("user-id");
        String productId = req.getParameter("product-id");
        cartDao.add(userId, productId, 1);
        sendRest(resp, "success", "Cart item added", null);
    }

    private  void  sendRest(HttpServletResponse resp, String status, String message, Object data) throws IOException {
        JsonObject rest = new JsonObject();
        JsonObject meta = new JsonObject();
        meta.addProperty("service", "shop");
        meta.addProperty("status",  status);
        meta.addProperty("message", message);
        meta.addProperty("time",    Instant.now().getEpochSecond());

        rest.add("meta", meta);

        Gson gson = new GsonBuilder().serializeNulls().create();
        rest.add("data", gson.toJsonTree(data));
        resp.getWriter().print(gson.toJson(rest));
    }
}
