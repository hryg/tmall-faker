package com.tmall.dao;

import com.tmall.bean.*;
import com.tmall.util.DBUtil;
import com.tmall.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public int getTotal(int cid) {
        int total = 0;
        String sql = "select count(*) from product where cid = " + cid;

        try (Connection connection = DBUtil.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public void add(Product product) {
        String sql = "insert into product values(null, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getSubTitle());
            preparedStatement.setFloat(3, product.getOriginalPrice());
            preparedStatement.setFloat(4, product.getPromotePrice());
            preparedStatement.setInt(5, product.getStock());
            preparedStatement.setInt(6, product.getCategory().getId());
            preparedStatement.setTimestamp(7, DateUtil.d2t(product.getCreateDate()));
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                product.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Product product) {
        String sql = "update product set name = ?, subTitle = ?, orignalPrice = ?, promotePrice = ?, stock = ?, " +
                "cid = ?, createDate = ? where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getSubTitle());
            preparedStatement.setFloat(3, product.getOriginalPrice());
            preparedStatement.setFloat(4, product.getPromotePrice());
            preparedStatement.setInt(5, product.getStock());
            preparedStatement.setInt(6, product.getCategory().getId());
            preparedStatement.setTimestamp(7, DateUtil.d2t(product.getCreateDate()));
            preparedStatement.setInt(8, product.getId());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                product.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "delete from product where id = " + id;

        try (Connection connection = DBUtil.getConnection(); Statement statement = connection.createStatement();) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product get(int id) {
        Product product = null;
        String sql = "select * from product where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                product = new Product();
                product.setId(id);
                product.setName(resultSet.getString("name"));
                product.setSubTitle(resultSet.getString("subTitle"));
                product.setOriginalPrice(resultSet.getFloat("orignalPrice"));
                product.setPromotePrice(resultSet.getFloat("promotePrice"));
                product.setStock(resultSet.getInt("stock"));
                product.setCategory(new CategoryDAO().get(resultSet.getInt("cid")));
                product.setCreateDate(DateUtil.t2d(resultSet.getTimestamp("createDate")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    public List<Product> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<Product> list(int start, int count) {
        List<Product> products = new ArrayList<Product>();
        String sql = "select * from product order by id desc limit ?, ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, start);
            statement.setInt(2, count);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();

                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setSubTitle(resultSet.getString("subTitle"));
                product.setOriginalPrice(resultSet.getFloat("orignalPrice"));
                product.setPromotePrice(resultSet.getFloat("promotePrice"));
                product.setStock(resultSet.getInt("stock"));
                product.setCategory(new CategoryDAO().get(resultSet.getInt("cid")));
                product.setCreateDate(DateUtil.t2d(resultSet.getTimestamp("createDate")));

                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> list(int cid) {
        return list(cid, 0, Short.MAX_VALUE);
    }

    public List<Product> list(int cid, int start, int count) {
        List<Product> products = new ArrayList<Product>();
        String sql = "select * from product where cid = ? order by id desc limit ?, ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cid);
            statement.setInt(2, start);
            statement.setInt(3, count);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();

                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setSubTitle(resultSet.getString("subTitle"));
                product.setOriginalPrice(resultSet.getFloat("orignalPrice"));
                product.setPromotePrice(resultSet.getFloat("promotePrice"));
                product.setStock(resultSet.getInt("stock"));
                product.setCategory(new CategoryDAO().get(resultSet.getInt("cid")));
                product.setCreateDate(DateUtil.t2d(resultSet.getTimestamp("createDate")));

                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public void fill(Category category) {
        List<Product> products = this.list(category.getId());
        category.setProducts(products);
    }

    public void fill(List<Category> categories) {
        for (Category category : categories) {
            fill(category);
        }
    }

    public void fillByRow(List<Category> categories) {
        int productNumberEachRow = 8;
        for (Category category : categories) {
            List<Product> products = category.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();
            for (int i = 0; i < products.size(); i += productNumberEachRow) {
                int size = i + productNumberEachRow;
                size = size > products.size() ? products.size() : size;
                List<Product> productsOfEachRow = products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productsByRow);
        }
    }

    public void setFirstProductImage(Product p) {
        List<ProductImage> pis= new ProductImageDAO().list(p, ProductImageDAO.TYPE_SINGLE);
        if (!pis.isEmpty()) {
            p.setFirstProductImage(pis.get(0));
        }
    }

    public void setSaleAndReviewNumber(Product p) {
        int saleCount = new OrderItemDAO().getSaleCount(p.getId());
        p.setSaleCount(saleCount);

        int reviewCount = new ReviewDAO().getCount(p.getId());
        p.setReviewCount(reviewCount);

    }

    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product p : products) {
            setSaleAndReviewNumber(p);
        }
    }

    public List<Product> search(String keyword, int start, int count) {
        List<Product> products = new ArrayList<Product>();

        if (null == keyword || 0 == keyword.trim().length()) {
            return products;
        }

        String sql = "select * from product where name like ? limit ?,? ";

        try (Connection connection = DBUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setString(1, "%"+keyword.trim()+"%");
            preparedStatement.setInt(2, start);
            preparedStatement.setInt(3, count);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Product product = new Product();

                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setSubTitle(rs.getString("subTitle"));
                product.setOriginalPrice(rs.getFloat("orignalPrice"));
                product.setPromotePrice(rs.getFloat("promotePrice"));
                product.setStock(rs.getInt("stock"));
                product.setCategory(new CategoryDAO().get(rs.getInt("cid")));
                product.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
                setFirstProductImage(product);

                products.add(product);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return products;
    }

}
