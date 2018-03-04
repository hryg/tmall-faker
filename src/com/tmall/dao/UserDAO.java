package com.tmall.dao;

import com.tmall.bean.User;
import com.tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public int getTotal() {
        int total = 0;
        String sql = "select count(*) from user";

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

    public void add(User user) {
        String sql = "insert into user values(null, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(User user) {
        String sql = "update user set name = ?, password = ? where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getId());
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "delete * from user where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User get(int id) {
        User user = null;
        String sql = "select * from user where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setId(id);
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User get(String name) {
        User user = null;
        String sql = "select * from user where name = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User get(String name, String password) {
        User user = null;
        String sql = "select * from user where name = ?, password = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt(1));
                user.setName(name);
                user.setPassword(password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<User> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<User> list(int start, int count) {
        List<User> users = new ArrayList<User>();
        String sql = "select * from user order by id desc limit ?, ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, start);
            statement.setInt(2, count);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

}
