package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dto.entities.Cart;
import step.learning.dto.entities.Product;
import step.learning.services.db.DbProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class CartDao {
    private final DbProvider dbProvider;
    private final String dbPrefix;
    private final Logger logger;

    @Inject
    public CartDao(DbProvider dbProvider,
                   @Named("db-prefix")String dbPrefix,
                   Logger logger)
    {
        this.dbProvider = dbProvider;
        this.dbPrefix = dbPrefix;
        this.logger = logger;
    }

    public void install() {
        String sql = "CREATE TABLE " + dbPrefix + "carts(" +
                "`id`               BIGINT UNSIGNED PRIMARY KEY DEFAULT (UUID_SHORT()), " +
                "`user_id`          BIGINT UNSIGNED NOT NULL, " +
                "`product_id`       BIGINT UNSIGNED NOT NULL, " +
                "`quantity`         BIGINT UNSIGNED NOT NULL, " +
                "`price`            VARCHAR(16)    NOT NULL, " +
                "`total`            VARCHAR(32)    NOT NULL " +
                ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;";
        try (Statement statement = dbProvider.getConnection().createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage() + " -- " + sql);
        }
    }

    public List<Cart> get(String userId) {
        String sql = "SELECT * FROM " + dbPrefix + "carts WHERE `user_id` = ?";
        List<Cart> carts = new ArrayList<>();
        try (PreparedStatement preparedStatement = dbProvider.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Cart cart = new Cart();
                    cart.setId(resultSet.getString("id"));
                    cart.setUserId(resultSet.getString("user_id"));
                    cart.setProductId(resultSet.getString("product_id"));
                    cart.setQuantity(resultSet.getString("quantity"));
                    cart.setPrice(resultSet.getString("price"));
                    carts.add(cart);
                }
            }
        }
        catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage() + " -- " + sql);
        }
        return carts;
    }

    public void add(String userId, String productId, String quantity, String price) {
        List<Cart> userCart = get(userId);

        boolean productFound = false;

        if (!userCart.isEmpty()) {
            for (Cart cart : userCart) {
                if (cart.getProductId().equals(productId)) {
                    String sql = "UPDATE " + dbPrefix + "carts SET `quantity` = `quantity` + 1 WHERE `user_id` = ? AND `product_id` = ?";
                    try (PreparedStatement preparedStatement = dbProvider.getConnection().prepareStatement(sql)) {
                        preparedStatement.setString(1, userId);
                        preparedStatement.setString(2, productId);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        logger.log(Level.WARNING, e.getMessage() + " -- " + sql);
                    }
                    productFound = true;
                    break;
                }
            }
        }

        if (!productFound) {
            addNewProductInUserCart(userId, productId, quantity, price);
        }
    }

    private void addNewProductInUserCart(String userId, String productId, String quantity, String price) {
        String sql = "INSERT INTO " + dbPrefix + "carts (`user_id`, `product_id`, `quantity`, `price`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dbProvider.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, productId);
            preparedStatement.setString(3, quantity);
            preparedStatement.setString(4, price);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage() + " -- " + sql);
        }
    }


    public double computeTotalPrice(String userId) {
        String sql = "SELECT SUM(quantity * price) AS total FROM " + dbPrefix + "carts WHERE user_id = ?";

        try (PreparedStatement preparedStatement = dbProvider.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double total = resultSet.getDouble("total");
                    // Используйте значение 'total' в вашем коде
                    return total;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Обработайте исключение в соответствии с вашими потребностями
        }
        return 0;
    }
}
