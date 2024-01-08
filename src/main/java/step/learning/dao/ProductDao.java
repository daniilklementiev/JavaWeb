package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dto.entities.Product;
import step.learning.services.db.DbProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

@Singleton
public class ProductDao {
    private final DbProvider dbProvider;
    private final String dbPrefix;
    private final Logger logger;

    @Inject
    public ProductDao(DbProvider dbProvider,
                      @Named("db-prefix")String dbPrefix,
                      Logger logger)
    {
        this.dbProvider = dbProvider;
        this.dbPrefix = dbPrefix;
        this.logger = logger;
        // install();
    }

    public void install() {
        String sql = "CREATE TABLE " + dbPrefix + "products(" +
                "`id`               BIGINT UNSIGNED PRIMARY KEY DEFAULT (UUID_SHORT()), " +
                "`name`             VARCHAR(64)    NOT NULL UNIQUE, " +
                "`description`      VARCHAR(64)    NOT NULL, " +
                "`image`            VARCHAR(256)    NOT NULL, " +
                "`price`            VARCHAR(16)    NOT NULL " +
                ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;";
        try (Statement statement = dbProvider.getConnection().createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage() + " -- " + sql);
        }
    }

    public void add(String name, String description, String image, String price) {
        String sql = "INSERT INTO " + dbPrefix + "products(`name`, `description`, `image`, `price`) VALUES(?, ?, ?, ?)";
        try (PreparedStatement statement = dbProvider.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setString(3, image);
            statement.setString(4, price);
            statement.executeUpdate();
        }
        catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage() + " -- " + sql);
        }
    }

    public List<Product> getProductsList() {
        String sql = "SELECT * FROM " + dbPrefix + "products";
        List<Product> products = new ArrayList<>();
        try (Statement statement = dbProvider.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                products.add(new Product(resultSet));
            }
        }
        catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage() + " -- " + sql);
        }
        return products;
    }

}
