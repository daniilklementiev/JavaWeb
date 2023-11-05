package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.AuthTokenDao;
import step.learning.dto.entities.AuthToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class TemplatesServlet extends HttpServlet {
    private static final byte[] buffer = new byte[1024];
    private final Logger logger;
    private final AuthTokenDao authTokenDao;

    @Inject
    public TemplatesServlet(Logger logger, AuthTokenDao authTokenDao) {
        this.logger = logger;
        this.authTokenDao = authTokenDao;
    }

    private String checkToken(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if(token == null) {
            return "Authorization header required";
        }
        if(!token.startsWith("Bearer ")) {
            return "Authorization header must start with Bearer";
        }
        token = token.replace("Bearer ", "");
        AuthToken authToken = authTokenDao.getTokenByBearer(token);
        if(authToken == null) {
            return "Token not found";
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* group servlet
         *  req.getServletPath() - постоянная часть (/uk/tpl)
         *  req.getPathInfo() - переменная часть (/uk/tpl/...)
         *  */
//        resp.getWriter().print(req.getServletPath() + " -- " + req.getPathInfo());
        String tokenCheckError = checkToken(req);
        if(tokenCheckError != null) {
            sendResponse(resp, 401, tokenCheckError);
            return;
        }
        String tplName = req.getPathInfo();
        if (tplName == null || tplName.isEmpty()) {
            sendResponse(resp, 400, "Resource name is required");
            return;
        }

        URL tplUrl = this.getClass().getClassLoader().getResource("tpl" + tplName);

        Path tplPath;
        try {
            if (tplUrl == null ||
                    !Files.isRegularFile(tplPath = Paths.get(tplUrl.toURI()))) {
                sendResponse(resp, 404, "Resource not located");
                return;
            }
        } catch (URISyntaxException ignored) {
            sendResponse(resp, 400, "Resource name invalid");
            return;
        }


        try (InputStream tplStream = tplUrl.openStream()) {
            int read;
            OutputStream respStream = resp.getOutputStream();
            resp.setContentType(URLConnection.guessContentTypeFromName(tplName));
            while ((read = tplStream.read(buffer)) > 0) {
                respStream.write(buffer, 0, read);
            }
            respStream.close();

        } catch (IOException e) {
            logger.log(Level.SEVERE, tplName + " --- " + e.getMessage());
            sendResponse(resp, 500, "Look at server logs");
        }
    }

    private void sendResponse(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType("text/plain");
        resp.getWriter().print(message);

    }
}
