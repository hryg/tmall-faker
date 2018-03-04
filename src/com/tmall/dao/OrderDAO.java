package com.tmall.dao;

import com.tmall.bean.Order;
import com.tmall.util.DBUtil;
import com.tmall.util.DateUtil;

import java.sql.*;

public class OrderDAO {
    public static final String WAIT_PAY = "waitPay";
    public static final String WAIT_DELIVERY = "waitDelivery";
    public static final String WAIT_CONFIRM = "waitConfirm";
    public static final String WAIT_REVIEW = "waitReview";
    public static final String FINISH = "finish";
    public static final String DELETE = "delete";

    public int getTotal(){
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
}
