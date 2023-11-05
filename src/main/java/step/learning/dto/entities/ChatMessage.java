package step.learning.dto.entities;

import java.sql.ResultSet;
import java.util.Date;

public class ChatMessage {
    private String id;
    private String senderId;
    private String message;
    private Date moment;

    public ChatMessage() {

    }

    public ChatMessage(ResultSet resultSet) throws Exception{
        setId(resultSet.getString("id"));
        setSenderId(resultSet.getString("sender_id"));
        setMessage(resultSet.getString("message"));
        setMoment(new Date( resultSet.getTimestamp("sent_at").getTime() ));
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
