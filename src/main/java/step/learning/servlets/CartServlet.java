package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.CartDao;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class CartServlet extends HttpServlet {
    private final CartDao cartDao;

    @Inject
    public CartServlet(CartDao cartDao) {
        this.cartDao = cartDao;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get cookie named "userId"
        Cookie[] cookies = req.getCookies();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        if (userId == null) {
            resp.sendRedirect("/");
        }
        else {
            req.setAttribute("total", cartDao.computeTotalPrice(userId));
            req.setAttribute("page-body", "cart.jsp");
            req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
        }

    }
}
