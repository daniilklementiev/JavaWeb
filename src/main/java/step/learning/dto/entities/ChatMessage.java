package step.learning.dto.entities;

import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatMessage {
    private String id;
    private String senderId;
    private String message;
    private Date moment;
    private String senderNik;

    private static final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat isoDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'UTC'", Locale.US);


    public ChatMessage() {

    }

    public JsonObject toJsonObject() {
        JsonObject result = new JsonObject();
        result.addProperty("id", id);
        result.addProperty("senderId", senderId);
        result.addProperty("message", message);
        result.addProperty("moment", isoDateFormat.format(moment));
        if(senderNik != null)
            result.addProperty("senderNik", senderNik);
        return result;
    }

    public String getSenderNik() {
        return senderNik;
    }

    public ChatMessage(ResultSet resultSet) throws SQLException {
        setId(resultSet.getString("id"));
        setSenderId(resultSet.getString("sender_id"));
        setMessage(resultSet.getString("message"));
        setMoment(new Date( resultSet.getTimestamp("moment").getTime() ));
        try {
            int nikIndex = resultSet.findColumn("sender_nik");
            this.senderNik = resultSet.getString(nikIndex);
        }
        catch (Exception ignored) {}
    }

    public ChatMessage(String senderId, String message) {
        this.id = null;
        setSenderId(senderId);
        setMessage(message);
        this.moment = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }
}
