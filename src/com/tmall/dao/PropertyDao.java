package com.tmall.dao;

import com.tmall.bean.Category;
import com.tmall.bean.Property;
import com.tmall.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PropertyDao {

    public int getTotal(int cid) {
        int total = 0;
        String sql = "select count(*) from property where cid = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cid);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public void add(Property property) {
        String sql = "insert to property values(null, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, property.getCategory().getId());
            statement.setString(2, property.getName());
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                property.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Property property) {
        String sql = "update property set cid = ?, name = ? where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, property.getCategory().getId());
            statement.setString(2, property.getName());
            statement.setInt(3, property.getId());
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "delete * from property where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Property get(int id) {
        Property property = null;
        String sql = "select * from property where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                property = new Property();
                property.setId(id);
                property.setName(resultSet.getString("name"));

                int cid = resultSet.getInt("cid");
                Category category = new CategoryDao().get(cid);
                property.setCategory(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return property;
    }

    public List<Property> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<Property> list(int start, int count) {
        List<Property> properties = new ArrayList<Property>();
        String sql = "select * from property order by id desc limit ?, ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, start);
            statement.setInt(2, count);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Property property = new Property();
                property.setId(resultSet.getInt("id"));
                property.setName(resultSet.getString("name"));

                int cid = resultSet.getInt("cid");
                Category category = new CategoryDao().get(cid);
                property.setCategory(category);

                properties.add(property);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return properties;
    }

}
