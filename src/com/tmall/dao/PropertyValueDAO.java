package com.tmall.dao;

import com.tmall.bean.Product;
import com.tmall.bean.Property;
import com.tmall.bean.PropertyValue;
import com.tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyValueDAO {

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
        String sql = "delete from propertyvalue where id = ?";

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
                propertyValue.setProduct(new ProductDAO().get(resultSet.getInt("pid")));
                propertyValue.setProperty(new PropertyDAO().get(resultSet.getInt("ptid")));
                propertyValue.setValue(resultSet.getString("value"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return propertyValue;
    }

    public PropertyValue get(int ptid, int pid ) {
        PropertyValue bean = null;

        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

            String sql = "select * from propertyvalue where ptid = " + ptid + " and pid = " + pid;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                bean= new PropertyValue();
                int id = rs.getInt("id");

                String value = rs.getString("value");

                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
                bean.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
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
                propertyValue.setProduct(new ProductDAO().get(resultSet.getInt("pid")));
                propertyValue.setProperty(new PropertyDAO().get(resultSet.getInt("ptid")));
                propertyValue.setValue(resultSet.getString("value"));

                propertyValues.add(propertyValue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return propertyValues;
    }

    public void init(Product product) {
        List<Property> properties = new PropertyDAO().list(product.getCategory().getId());
        for (Property property : properties) {
            PropertyValue propertyValue = get(property.getId(), product.getId());
            if (null == propertyValue) {
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                this.add(propertyValue);
            }
        }
    }

    public List<PropertyValue> list(int pid) {
        List<PropertyValue> beans = new ArrayList<PropertyValue>();

        String sql = "select * from propertyvalue where pid = ? order by ptid desc";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, pid);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PropertyValue bean = new PropertyValue();
                int id = rs.getInt(1);

                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

}
