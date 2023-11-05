package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.db.DbProvider;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class DaoBase {
    private final DbProvider dbProvider;
    private final Logger logger;

    @Inject
    public DaoBase(DbProvider dbProvider, Logger logger) {
        this.dbProvider = dbProvider;
        this.logger = logger;
    }

    protected Timestamp getDbTimestamp() {
        try(Statement statement = dbProvider.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT CURRENT_TIMESTAMP");
            resultSet.next();
            return resultSet.getTimestamp(1);

        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return null;
    }
}
