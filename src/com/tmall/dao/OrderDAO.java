package com.tmall.dao;

import com.tmall.bean.Order;
import com.tmall.util.DBUtil;
import com.tmall.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public static final String WAIT_PAY = "waitPay";
    public static final String WAIT_DELIVERY = "waitDelivery";
    public static final String WAIT_CONFIRM = "waitConfirm";
    public static final String WAIT_REVIEW = "waitReview";
    public static final String FINISH = "finish";
    public static final String DELETE = "delete";

    public int getTotal() {
        int total = 0;
        String sql = "select count(*) from order_";

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

    public void add(Order order) {
        String sql = "insert into order_ values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, order.getOrderCode());
            preparedStatement.setString(2, order.getAddress());
            preparedStatement.setString(3, order.getPost());
            preparedStatement.setString(4, order.getReceiver());
            preparedStatement.setString(5, order.getMobile());
            preparedStatement.setString(6, order.getUserMessage());
            preparedStatement.setTimestamp(7, DateUtil.d2t(order.getCreateDate()));
            preparedStatement.setTimestamp(8, DateUtil.d2t(order.getPayDate()));
            preparedStatement.setTimestamp(9, DateUtil.d2t(order.getDeliveryDate()));
            preparedStatement.setTimestamp(10, DateUtil.d2t(order.getConfirmDate()));
            preparedStatement.setInt(11, order.getUser().getId());
            preparedStatement.setString(12, order.getStatus());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                order.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Order order) {
        String sql = "update order_ set orderCode = ?, address = ?, post = ?, receiver = ?, mobile = ?, " +
                "userMessage = ?, createDate = ?, payDate = ?, deliveryDate = ?, confirmDate = ?, uid = ?, status = ?" +
                " where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, order.getOrderCode());
            preparedStatement.setString(2, order.getAddress());
            preparedStatement.setString(3, order.getPost());
            preparedStatement.setString(4, order.getReceiver());
            preparedStatement.setString(5, order.getMobile());
            preparedStatement.setString(6, order.getUserMessage());
            preparedStatement.setTimestamp(7, DateUtil.d2t(order.getCreateDate()));
            preparedStatement.setTimestamp(8, DateUtil.d2t(order.getPayDate()));
            preparedStatement.setTimestamp(9, DateUtil.d2t(order.getDeliveryDate()));
            preparedStatement.setTimestamp(10, DateUtil.d2t(order.getConfirmDate()));
            preparedStatement.setInt(11, order.getUser().getId());
            preparedStatement.setString(12, order.getStatus());
            preparedStatement.setInt(13, order.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "delete * from order_ where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order get(int id) {
        Order order = null;
        String sql = "select * from order_ where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                order = new Order();
                order.setOrderCode(resultSet.getString("orderCode"));
                order.setAddress(resultSet.getString("address"));
                order.setPost(resultSet.getString("post"));
                order.setReceiver(resultSet.getString("receiver"));
                order.setMobile(resultSet.getString("mobile"));
                order.setUserMessage(resultSet.getString("userMessage"));
                order.setCreateDate(DateUtil.t2d(resultSet.getTimestamp("createDate")));
                order.setPayDate(DateUtil.t2d(resultSet.getTimestamp("payDate")));
                order.setDeliveryDate(DateUtil.t2d(resultSet.getTimestamp("deliveryDate")));
                order.setConfirmDate(DateUtil.t2d(resultSet.getTimestamp("confirmDate")));
                order.setUser(new UserDAO().get(resultSet.getInt("uid")));
                order.setStatus(resultSet.getString("status"));
                order.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

    public List<Order> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<Order> list(int start, int count) {
        List<Order> orders = new ArrayList<Order>();
        String sql = "select * from order_ order by id desc limit ?, ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, start);
            statement.setInt(2, count);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();

                order.setId(resultSet.getInt("id"));
                order.setOrderCode(resultSet.getString("orderCode"));
                order.setAddress(resultSet.getString("address"));
                order.setPost(resultSet.getString("post"));
                order.setReceiver(resultSet.getString("receiver"));
                order.setMobile(resultSet.getString("mobile"));
                order.setUserMessage(resultSet.getString("userMessage"));
                order.setCreateDate(DateUtil.t2d(resultSet.getTimestamp("createDate")));
                order.setPayDate(DateUtil.t2d(resultSet.getTimestamp("payDate")));
                order.setDeliveryDate(DateUtil.t2d(resultSet.getTimestamp("deliveryDate")));
                order.setConfirmDate(DateUtil.t2d(resultSet.getTimestamp("confirmDate")));
                order.setUser(new UserDAO().get(resultSet.getInt("uid")));
                order.setStatus(resultSet.getString("status"));

                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public List<Order> list(int uid, String excludedStatus) {
        return list(uid, excludedStatus, 0, Short.MAX_VALUE);
    }

    public List<Order> list(int uid, String excludedStatus, int start, int count) {
        List<Order> orders = new ArrayList<Order>();

        String sql = "select * from order_ where uid = ? and status != ? order by id desc limit ?,? ";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1, uid);
            preparedStatement.setString(2, excludedStatus);
            preparedStatement.setInt(3, start);
            preparedStatement.setInt(4, count);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();

                order.setId(resultSet.getInt("id"));
                order.setOrderCode(resultSet.getString("orderCode"));
                order.setAddress(resultSet.getString("address"));
                order.setPost(resultSet.getString("post"));
                order.setReceiver(resultSet.getString("receiver"));
                order.setMobile(resultSet.getString("mobile"));
                order.setUserMessage(resultSet.getString("userMessage"));
                order.setCreateDate(DateUtil.t2d(resultSet.getTimestamp("createDate")));
                order.setPayDate(DateUtil.t2d(resultSet.getTimestamp("payDate")));
                order.setDeliveryDate(DateUtil.t2d(resultSet.getTimestamp("deliveryDate")));
                order.setConfirmDate(DateUtil.t2d(resultSet.getTimestamp("confirmDate")));
                order.setUser(new UserDAO().get(resultSet.getInt("uid")));
                order.setStatus(resultSet.getString("status"));

                orders.add(order);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return orders;
    }
}
