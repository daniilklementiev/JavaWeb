package step.learning.dto.entities;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.ProductDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {
    public Product() {

    }
    public Product(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getString("id"));
        this.setName(resultSet.getString("name"));
        this.setDescription(resultSet.getString("description"));
        this.setImage(resultSet.getString("image"));
        this.setPrice(resultSet.getString("price"));
    }
    private String id;
    private String name;
    private String description;
    private String image;
    private String price;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
