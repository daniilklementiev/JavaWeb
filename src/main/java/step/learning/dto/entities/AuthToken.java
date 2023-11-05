package step.learning.dto.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AuthToken {
    // по принципу JWT
    private String jti; // JWT ID
    private  String sub; // user-id
    private Date iat;   // Issued at - create-moment
    private Date exp;  // Expires at - expire-moment
    private String nik; // getter only ~ navigation - username
    public AuthToken() {

    }

    public AuthToken(ResultSet resultSet) throws SQLException {
        this.setJti(resultSet.getString("jti"));
        this.setSub(resultSet.getString("sub"));
        this.setIat(resultSet.getDate("iat"));
        this.setExp(resultSet.getDate("exp"));
        try { this.nik = resultSet.getString("nik"); }
        catch (Exception ignored) {}
    }

    public String getNik() {
        return nik;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Date getIat() {
        return iat;
    }

    public void setIat(Date iat) {
        this.iat = iat;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }
}
