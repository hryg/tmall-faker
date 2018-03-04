package com.tmall.dao;

import com.tmall.bean.Category;
import com.tmall.bean.Product;
import com.tmall.bean.Property;
import com.tmall.bean.PropertyValue;
import com.tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyValueDao {

    public int getTotal() {
        int total = 0;
        String sql = "select count(*) form propertyvalue";

        try (Connection connection = DBUtil.getConnection();
             Statement statement = connection.createStatement();) {

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public void add(PropertyValue propertyValue) {
        String sql = "insert to propertyvalue values(null, ?, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, propertyValue.getProduct().getId());
            statement.setInt(2, propertyValue.getProperty().getId());
            statement.setString(3, propertyValue.getValue());
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                propertyValue.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(PropertyValue propertyValue) {
        String sql = "update propertyvalue set pid = ?, ptid = ?, value = ? where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, propertyValue.getProduct().getId());
            statement.setInt(2, propertyValue.getProperty().getId());
            statement.setString(3, propertyValue.getValue());
            statement.setInt(4, propertyValue.getId());
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "delete * from propertyvalue where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PropertyValue get(int id) {
        PropertyValue propertyValue = null;
        String sql = "select * from propertyvalue where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                propertyValue = new PropertyValue();
                propertyValue.setId(id);
//                propertyValue.setProduct(new ProductDao().get(resultSet.getInt("pid")));
                propertyValue.setProperty(new PropertyDao().get(resultSet.getInt("ptid")));
                propertyValue.setValue(resultSet.getString("value"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return propertyValue;
    }

    public List<PropertyValue> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<PropertyValue> list(int start, int count) {
        List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
        String sql = "select * from propertyvalue order by id desc limit ?, ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, start);
            statement.setInt(2, count);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PropertyValue propertyValue = new PropertyValue();
                propertyValue.setId(resultSet.getInt("id"));
//                propertyValue.setProduct(new ProductDao().get(resultSet.getInt("pid")));
                propertyValue.setProperty(new PropertyDao().get(resultSet.getInt("ptid")));
                propertyValue.setValue(resultSet.getString("value"));

                propertyValues.add(propertyValue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return propertyValues;
    }

}
