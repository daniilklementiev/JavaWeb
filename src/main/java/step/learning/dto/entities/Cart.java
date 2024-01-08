package step.learning.dto.entities;

import com.google.inject.Inject;
import step.learning.dao.CartDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Cart {
    private String id;
    private String userId;
    private String productId;
    private String quantity;
    private String price;
    private String total;

    public Cart() {

    }

    public Cart(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getString("id"));
        this.setUserId(resultSet.getString("user_id"));
        this.setProductId(resultSet.getString("product_id"));
        this.setQuantity(resultSet.getString("quantity"));
        this.setPrice(resultSet.getString("price"));
        this.setTotal(resultSet.getString("total"));

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
