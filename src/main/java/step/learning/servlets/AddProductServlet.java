package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.ProductDao;
import step.learning.dto.entities.Product;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Singleton
public class AddProductServlet extends HttpServlet {
    private final ProductDao productDao;
    @Inject
    public AddProductServlet(ProductDao productDao) {
        this.productDao = productDao;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("page-body", "product.jsp");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String priceStr = req.getParameter("price");
        String image = req.getParameter("image");

        double price = 0.0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        productDao.add(name, description, image, String.valueOf(price));

        resp.sendRedirect(req.getRequestURI());
    }
}
