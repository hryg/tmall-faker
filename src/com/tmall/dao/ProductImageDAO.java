package com.tmall.dao;

import com.tmall.bean.Category;
import com.tmall.bean.Product;
import com.tmall.bean.ProductImage;
import com.tmall.bean.Property;
import com.tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductImageDAO {
    public static final String TYPE_SINGLE = "type_single";
    public static final String TYPE_DETAIL = "type_detail";

    public int getTotal() {
        int total = 0;
        String sql = "select count(*) from productimage";

        try (Connection connection = DBUtil.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                total = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public void add(ProductImage productImage) {
        String sql = "insert into productimage values(null, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productImage.getProduct().getId());
            statement.setString(2, productImage.getType());
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                productImage.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ProductImage productImage) {

    }

    public void delete(int id) {
        String sql = "delete * from productimage where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ProductImage get(int id) {
        ProductImage productImage = new ProductImage();
        String sql = "select * from productimage where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                productImage = new ProductImage();
                productImage.setId(id);
                productImage.setType(resultSet.getString("type"));

                Product product = new ProductDAO().get(resultSet.getInt("pid"));
                productImage.setProduct(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productImage;
    }

    public List<ProductImage> list(Product product, String type) {
        return list(product, type, 0, Short.MAX_VALUE);
    }

    public List<ProductImage> list(Product product, String type, int start, int count) {
        List<ProductImage> productImages = new ArrayList<ProductImage>();
        String sql = "select * from productimage where pid = ? and type = ? order by id desc limit ?, ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, product.getId());
            statement.setString(2, type);
            statement.setInt(3, start);
            statement.setInt(4, count);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ProductImage productImage = new ProductImage();
                productImage.setId(resultSet.getInt("id"));
                productImage.setType(resultSet.getString("type"));
                productImage.setProduct(new ProductDAO().get(resultSet.getInt("pid")));
                productImages.add(productImage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productImages;
    }
}
