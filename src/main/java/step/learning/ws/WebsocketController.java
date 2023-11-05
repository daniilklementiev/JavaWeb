package step.learning.ws;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import step.learning.dao.AuthTokenDao;
import step.learning.dao.ChatMessageDao;
import step.learning.dto.entities.AuthToken;
import step.learning.dto.entities.ChatMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(
        value = "/chat", // адрес: ws://localhost...../chat
        configurator = WebsocketConfigurator.class // конфигуратор
)
public class WebsocketController {
    /*
     * Одна из самых распространенных задач WS - уведомление всех клиентов об определенном собітии. Соответственно, центральной частью сервера является коллекция
     * активных подключений. Так как каждое подключение создает новый объект, коллекция должна быть статической. В данном случае, она хранит ссылки на объекты
     */
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    private final AuthTokenDao authTokenDao;
    private final ChatMessageDao chatMessageDao;

    @Inject
    public WebsocketController(AuthTokenDao authTokenDao, ChatMessageDao chatMessageDao) {
        this.authTokenDao = authTokenDao;
        this.chatMessageDao = chatMessageDao;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig sec) {
//        chatMessageDao.install();
        String culture = (String) sec.getUserProperties().get("culture");
        if(culture == null) {
            try { session.close(); }
            catch(IOException ignored) {}
        }
        else {
            session.getUserProperties().put("culture", culture);
            sessions.add(session);
        }

    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JsonObject request = JsonParser.parseString(message).getAsJsonObject();
        String command = request.get("command").getAsString();
        String data = request.get("data").getAsString();
        switch (command){
            case "auth": {
                AuthToken token = authTokenDao.getTokenByBearer(data);
                if (token == null) {
                    sendToSession(session, 403, "Token rejected");
                    return;
                }
                session.getUserProperties().put("token", token);
                broadcast(token.getNik() + " joined the chat");
//                sendToSession(session, 202, token.getNik())
                break;
            }
            case "chat": {
                AuthToken token = (AuthToken) session.getUserProperties().get("token");
                ChatMessage chatMessage = new ChatMessage(token.getSub(), data);
                chatMessageDao.add(chatMessage);
                broadcast(token.getNik() + ": " + data);
                authTokenDao.renewal(token);
                break;
            }
            default:
                sendToSession(session, 405, "Unknown command");
        }
    }

    @OnError
    public void onError(Throwable ex, Session session) {
        System.err.println("onError: " + ex.getMessage());
    }

    public static void sendToSession(Session session, int status, String message) {
        JsonObject response = new JsonObject();
        // hw
        response.addProperty("status", status);
        response.addProperty("data", message);
        try {
            session.getBasicRemote().sendText(response.toString());
        } catch (Exception ex) {
            System.err.println("sendToSession_error: " + ex.getMessage());
        }
    }

    public static void sendToSession(Session session, JsonObject jsonObject){
        try {
            session.getBasicRemote().sendText(jsonObject.toString());
        } catch (Exception ex) {
            System.err.println("sendToSession: " + ex.getMessage());
        }
    }

    public static void broadcast(String message) {
        JsonObject response = new JsonObject();
        response.addProperty("status", 201);
        response.addProperty("data", message);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date javaDate = new Date();
        String formattedDate = formatter.format(javaDate);
        response.addProperty("date", formattedDate);
        sessions.forEach(session -> {
            sendToSession(session, response);
        });
    }

}
