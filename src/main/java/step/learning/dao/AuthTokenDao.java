package step.learning.dao;

import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dto.entities.AuthToken;
import step.learning.dto.entities.User;
import step.learning.services.db.DbProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * API servlet for authentication and authorization
 */
@Singleton
public class AuthTokenDao extends DaoBase {
    private final DbProvider dbProvider;
    private final String dbPrefix;
    private final Logger logger;
    private final UserDao userDao;

    @Inject
    public AuthTokenDao(DbProvider dbProvider, @Named("db-prefix") String dbPrefix, Logger logger, UserDao userDao) {
        super(dbProvider, logger);
        this.dbProvider = dbProvider;
        this.dbPrefix = dbPrefix;
        this.logger = logger;
        this.userDao = userDao;
    }

    public void renewal(AuthToken token) {
        String sql = "UPDATE " + dbPrefix + "auth_tokens SET `exp` = DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 12 HOUR) WHERE `jti` = UUID_TO_BIN(?)";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setString(1, token.getJti());
            prep.executeUpdate();
            token.setExp(new Date(token.getExp().getTime() + 1000L * 60 * 60 * 12));
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage() + " -- " + sql);
        }
    }

    public boolean install() {
        String sql = "CREATE TABLE " + dbPrefix + "auth_tokens (" +
                "`jti`          BINARY(16)        PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())), " +
                "`sub`          BIGINT UNSIGNED   NOT NULL, " +
                "`iat`          DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "`exp`          DATETIME          NOT NULL" +
                ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;";

        try (Statement statement = dbProvider.getConnection().createStatement()) {
            statement.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage() + " -- " + sql);
        }
        return false;
    }


    public AuthToken getTokenByCredentials(String login, String password) {
        User user = userDao.getUserByCredentials(login, password);
        if (user == null) {
            return null;
        }
        String sql = "SELECT BIN_TO_UUID(`jti`) AS jti, `sub`, `iat`, `exp` FROM " + dbPrefix + "auth_tokens t " +
                "INNER JOIN " + dbPrefix + "users u ON t.sub = u.id " +
                "WHERE u.login = ?";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)){
            prep.setString(1, login);
            ResultSet resultSet = prep.executeQuery();
            if(resultSet.next()){
                return new AuthToken(resultSet);
            }
        }
        catch (Exception ex){
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
        }
        AuthToken token = new AuthToken();
        token.setJti(UUID.randomUUID().toString());
        token.setSub(user.getId());
        Timestamp now = getDbTimestamp();
        if (now == null) {
            return null;
        }
        token.setIat(new Date(now.getTime()));
        token.setExp(new Date(now.getTime() + 1000L * 60 * 60 * 24));

        sql = "INSERT INTO " + dbPrefix + "auth_tokens (`jti`, `sub`, `iat`, `exp`) VALUES (UUID_TO_BIN(?), ?, ?, ?)";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setString(1, token.getJti());
            prep.setString(2, token.getSub());
            prep.setTimestamp(3, new Timestamp(token.getIat().getTime()));
            prep.setTimestamp(4, new Timestamp(token.getExp().getTime()));
            prep.executeUpdate();
            return token;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage() + " -- " + sql);
        }
        return null;
    }

    public AuthToken getTokenByBearer(String bearerContent) {
        String jti;
        try {
            jti = JsonParser.parseString(new String(Base64.getUrlDecoder().decode(bearerContent))).getAsJsonObject().get("jti").getAsString();
        } catch (Exception e) {
            return null;
        }

        String sql = "SELECT BIN_TO_UUID(a.`jti`) AS jti, a.`sub`, a.`iat`, a.`exp`, u.`login` AS nik FROM " +
                dbPrefix +
                "auth_tokens a JOIN " + dbPrefix + "users u ON a.sub = u.id WHERE a.`jti` = UUID_TO_BIN(?) AND a.`exp` > CURRENT_TIMESTAMP";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setString(1, jti);
            ResultSet resultSet = prep.executeQuery();
            if (resultSet.next()) {
                return new AuthToken(resultSet);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage() + " -- " + sql);
        }
        return null;
    }

    public User getUserByToken(String bearerContent) {
        AuthToken token = getTokenByBearer(bearerContent);
        if (token == null) {
            return null;
        }
        return userDao.getUserById(token.getSub());
    }
}
