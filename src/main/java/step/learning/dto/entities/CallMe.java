package step.learning.dto.entities;

import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Pattern;

public class CallMe {
    String id;
    String name;
    String phone;
    Date moment;
    Date call_moment;
    Date delete_moment;

    public CallMe() {

    }

    public CallMe(JsonObject jsonObject) {
        try {
            setName(jsonObject.get("name").getAsString());
            setPhone(jsonObject.get("phone").getAsString());
        }
        catch (IllegalArgumentException ex) {
            throw ex;
        }
        catch (NullPointerException ex){
            throw new IllegalArgumentException("JSON object must have non-null 'name' and 'phone' fields");
        }
    }

    public CallMe(ResultSet resultSet) throws IllegalArgumentException {
        try {
            setId(resultSet.getString("id"));
            setName(resultSet.getString("name"));
            setPhone(resultSet.getString("phone"));
            setMoment( new Date ( resultSet.getTimestamp ( "moment" ).getTime() ) );
            Timestamp callMomentTimestamp = resultSet.getTimestamp("call_moment");
            if(callMomentTimestamp != null){
                setCall_moment(new Date ( callMomentTimestamp.getTime() ) );
            }
            Timestamp deleteMomentTimestamp = resultSet.getTimestamp("delete_moment");
            if(deleteMomentTimestamp != null){
                setDelete_moment(new Date ( deleteMomentTimestamp.getTime() ) );
            }
        }
        catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public Date getDelete_moment() {
        return delete_moment;
    }

    public void setDelete_moment(Date delete_moment) {
        this.delete_moment = delete_moment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if(Pattern.matches("^\\+380([\\s-]?\\d){9}$", phone)) {
            this.phone = phone.replaceAll("[\\s-]+", "");
        }
        else {
            throw new IllegalArgumentException("Phone number must be in format: '+380 XX XXX XX XX'");
        }

    }

    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    public Date getCall_moment() {
        return call_moment;
    }

    public void setCall_moment(Date call_moment) {
        this.call_moment = call_moment;
    }
}
