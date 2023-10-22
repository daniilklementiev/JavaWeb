package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dao.CallMeDao;
import step.learning.dto.entities.CallMe;
import step.learning.services.db.DbProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class DbServlet extends HttpServlet {
    private final DbProvider dbProvider;
    private final String dbPrefix;
    private final CallMeDao callMeDao;
    private final Logger logger;


    @Inject
    public DbServlet(DbProvider dbProvider, @Named("db-prefix") String dbPrefix, CallMeDao callMeDao, Logger logger) {
        this.dbProvider = dbProvider;
        this.dbPrefix = dbPrefix;
        this.callMeDao = callMeDao;
        this.logger = logger;
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getMethod().toUpperCase()) {
            case "CALL":
                doCall(req, resp);
                break;
            case "DELETE":
                doDelete(req, resp);
                break;
            case "LINK":
                doLink(req, resp);
                break;
            case "PATCH":
                doPatch(req, resp);
                break;
            case "UNLINK":
                doUnlink(req, resp);
                break;

            default:
                super.service(req, resp);
        }
    }
    protected void doCall(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("application/json");
        String callId = req.getParameter("call-id");
        if(callId == null) {
            resp.setStatus(400);
            resp.getWriter().print("\"Bad Request: 'call-id' parameter is required\"");
            return;
        }
        CallMe callMe = callMeDao.getById(callId);
        if(callMe == null) {
            resp.setStatus(404);
            resp.getWriter().print("Not Found: call with id '" + callId + "' not found");
            return;
        }
        if(callMe.getCall_moment() != null) {
            resp.setStatus(422);
            resp.getWriter().print("Unprocessable content: call with id '" + callId + "' has not been called yet");
            return;
        }
        if(callMeDao.setCallMoment(callMe)) {
            resp.setStatus(200);
            resp.getWriter().print(new Gson().toJson(callMe));
            return;
        }
        else {
            resp.setStatus(500);
            resp.getWriter().print("\"Internal Server Error: details in server logs\"");
            return;
        }
    }
    protected void doLink(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().serializeNulls().create();
        resp.setContentType("application/json");
        resp.getWriter().print(gson.toJson(callMeDao.getAll()));
    }

    protected void doUnlink(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().serializeNulls().create();
        resp.setContentType("application/json");
    resp.getWriter().print(gson.toJson(callMeDao.getAll(true)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String callId = req.getParameter("call-id");
        if(callId == null) {
            resp.setStatus(400);
            resp.getWriter().print("\"Bad Request: 'call-id' parameter is required\"");
            return;
        }
        CallMe callMe = callMeDao.getById(callId);
        if(callMe == null) {
            resp.setStatus(404);
            resp.getWriter().print("Not Found: call with id '" + callId + "' not found");
            return;
        }
        if(callMe.getDelete_moment() != null) {
            resp.setStatus(422);
            resp.getWriter().print("Unprocessable content: call with id '" + callId + "' has not been called yet");
            return;
        }
        if(callMeDao.delete(callMe)) {
            resp.setStatus(204);
        }
        else {
            resp.setStatus(500);
            resp.getWriter().print("\"Internal Server Error: details in server logs\"");
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String contentType = req.getContentType();
        if (contentType == null || !contentType.startsWith("application/json")) {
            resp.setStatus(415);
            resp.getWriter().print("\"Unsupported Media Type: only: 'application/json'\"");
            return;
        }
        JsonObject requestBody;
        String encoding = req.getAttribute("charset").toString();
        Charset charset = encoding == null ? StandardCharsets.UTF_8 : Charset.forName(encoding);
        try ( Reader reader = new InputStreamReader(req.getInputStream(), charset ) ) {
            requestBody = JsonParser.parseReader(reader).getAsJsonObject();
        }
        catch (Exception ex) {
            resp.setStatus(400);
            resp.getWriter().print("\"Invalid JSON object: " + ex.getMessage() + "\"");
            return;
        }
/*        String name, phone;
//        try {
//            name = requestBody.get("name").getAsString();
//            phone = requestBody.get("phone").getAsString();
//        } catch (Exception ex) {
//            resp.setStatus(422);
//            resp.getWriter().print("\"Unprocessable content: " + ex.getMessage() + "\". JSON Object must have 'name' and 'phone' fields");
//            return;
//        }

//        if(!Pattern.matches("^\\+380([\\s-]?\\d){9}$", phone)) {
//            resp.setStatus(422);
//            resp.getWriter().print("\"Unprocessable content: " + phone + "\". Phone number must be in format: '+380 XX XXX XX XX'");
//            return;
//        }
//
//        phone = phone.replaceAll("[\\s-]+", "");
*/
        CallMe item = new CallMe();
        try {
            item = new CallMe(requestBody);
        }
        catch(IllegalArgumentException ex) {
            resp.setStatus(422);
            resp.getWriter().printf("\"Unprocessable content: %s\"", ex.getMessage());
        }
        try{
            callMeDao.add(item);
        } catch (IllegalArgumentException ex) {
            resp.setStatus(500);
            resp.getWriter().print("\"Internal Server Error: details in server logs\"");
            System.err.println(ex.getMessage());
            return;
        }
        resp.setStatus(201);
        resp.getWriter().print(new Gson().toJson(item));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("page-body", "db.jsp");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // этим методом проводится запрос "button click" на создание таблицы БД
//        String sql = "CREATE TABLE " + dbPrefix + "call_me (" +
//                "id          BIGINT UNSIGNED  PRIMARY KEY," +
//                "name        VARCHAR(64)      NULL," +
//                "phone       CHAR(13)         NOT NULL COMMENT '+380 ХХ ХХХ ХХ ХХ'," +
//                "moment      DATETIME         DEFAULT CURRENT_TIMESTAMP," +
//                "call_moment DATETIME         NULL" +
//                ") ENGINE = InnoDB DEFAULT CHARSET = UTF8";

        JsonObject result = new JsonObject();
        try {
            callMeDao.install();
            result.addProperty("db-status", "success");
            result.addProperty("message", "Create OK");
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "SQL error: " + ex.getMessage());
            result.addProperty("status", "error");
            result.addProperty("message",
                    ex.getMessage().toLowerCase().contains("alreadyexists")
                            ? "Table already exists"
                            : ex.getMessage());
        }
        resp.getWriter().print(result);
    }
}
