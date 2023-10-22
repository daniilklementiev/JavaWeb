package step.learning.dao;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import jdk.nashorn.internal.codegen.CompilerConstants;
import step.learning.dto.entities.CallMe;
import step.learning.services.db.DbProvider;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class CallMeDao {
    private final DbProvider dbProvider;
    private final String dbPrefix;
    private final Logger logger;

    @Inject
    public CallMeDao(DbProvider dbProvider, @Named("db-prefix")String dbPrefix, Logger logger) {
        this.dbProvider = dbProvider;
        this.dbPrefix = dbPrefix;
        this.logger = logger;
    }

    public void install() throws SQLException {
        String sql = "CREATE TABLE " + dbPrefix + "call_me (" +
                "id          BIGINT UNSIGNED  PRIMARY KEY," +
                "name        VARCHAR(64)      NULL," +
                "phone       CHAR(13)         NOT NULL COMMENT '+380 ХХ ХХХ ХХ ХХ'," +
                "moment      DATETIME         DEFAULT CURRENT_TIMESTAMP," +
                "call_moment DATETIME         NULL" +
                ") ENGINE = InnoDB DEFAULT CHARSET = UTF8";

        JsonObject result = new JsonObject();
        try(Statement statement = dbProvider.getConnection().createStatement()){
            statement.executeUpdate(sql);
        }
        catch(SQLException ex){
            throw new SQLException(ex);
        }
    }

    public void add(CallMe item) throws IllegalArgumentException {
        if (item.getId() != null) {
            throw new IllegalArgumentException("Item already has an ID");
        }

        if (item.getName() == null || item.getName().isEmpty() || item.getPhone() == null || item.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Item name or phone is null or empty");
        }
        String sql = "INSERT INTO " + dbPrefix + "call_me (`name`, `phone`) VALUES (?, ?)";

        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setString(1, item.getName());
            prep.setString(2, item.getPhone());

            prep.execute();

            long id = prep.getGeneratedKeys().getLong(1);
            item.setId(String.valueOf(id));
            item.setMoment(new Date());
        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
            throw new IllegalArgumentException(ex);
        }
    }

    public boolean delete(CallMe item){
        return delete(item,false);
    }
    public boolean delete(CallMe item, boolean hardDelete) {
        if(item == null || item.getId() == null) {
            return false;
        }
        String sql;
        StringBuilder sb = new StringBuilder();
        if(hardDelete){
            sql = "DELETE FROM " + dbPrefix + "call_me WHERE id = ?";
        }
        else {
            sql = "UPDATE " + dbPrefix + "call_me SET delete_moment = CURRENT_TIMESTAMP WHERE id = ?";
        }
        try(PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)){
            prep.setString(1, item.getId());
            prep.executeUpdate();
            return true;
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql );
        }
        return false;
    }
    public List<CallMe> getAll() {
        return getAll(false);
    }

    public List<CallMe> getAll(boolean includeDeleted) {
        List<CallMe> res = new ArrayList<>();
        String sql = "SELECT * FROM " + dbPrefix + "call_me";
        if(!includeDeleted) {
            sql += " WHERE delete_moment IS NULL";
        }
        try(Statement statement = dbProvider.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                res.add(new CallMe(resultSet));
            }
        }
        catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql );
        }

        return res;
    }

    public CallMe getById(String id) {
        return getById(id, false);
    }

    public CallMe getById(String id, boolean includeDeleted) {
        String sql = "SELECT * FROM " + dbPrefix + "call_me WHERE id = ?";
        if(!includeDeleted) {
            sql += " AND delete_moment IS NULL";
        }
        try(PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)){
            prep.setString(1, id);
            ResultSet resultSet = prep.executeQuery();
            if(resultSet.next()) {
                return new CallMe(resultSet);
            }
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql );
        }
        return null;
    }

    public boolean setCallMoment(CallMe item) {
        String sql = "SELECT CURRENT_TIMESTAMP";
        Timestamp timestamp;
        try (Statement statement = dbProvider.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            timestamp = resultSet.getTimestamp(1);
            item.setCall_moment(new Date(timestamp.getTime()));
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
            return false;
        }

        sql = "UPDATE " + dbPrefix + "call_me SET call_moment = ? WHERE id = ?";
        try(PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)){
            prep.setTimestamp(1, timestamp);
            prep.setString(2, item.getId());
            prep.execute();
            return true;
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
            return false;
        }
    }
}

// DAO - Data Access Object
