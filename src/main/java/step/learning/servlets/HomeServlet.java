package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.CartDao;
import step.learning.dao.ProductDao;
import step.learning.dto.entities.Product;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Singleton
public class HomeServlet extends HttpServlet {
    private final ProductDao productDao;
    private final CartDao cartDao;

    @Inject
    public HomeServlet(ProductDao productDao, CartDao cartDao) {
        this.productDao = productDao;
        this.cartDao = cartDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> products = productDao.getProductsList();
        req.setAttribute("products", products);

        req.setAttribute("page-body", "index.jsp");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String json = sb.toString();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        String productId = jsonObject.get("productId").getAsString();
        String productName = jsonObject.get("productName").getAsString();
        String productPrice = jsonObject.get("productPrice").getAsString();
        String userId = jsonObject.get("userToken").getAsString();

        cartDao.add(userId, productId, "1", productPrice);

        resp.sendRedirect(req.getRequestURI());
    }
}
