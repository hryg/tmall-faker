package com.tmall.dao;

import com.tmall.bean.Order;
import com.tmall.bean.OrderItem;
import com.tmall.bean.Product;
import com.tmall.util.DBUtil;
import com.tmall.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {
    public int getTotal() {
        int total = 0;
        String sql = "select count(*) from orderitem";

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

    public void add(OrderItem orderItem) {
        String sql = "insert into orderitem values(null, ?, ?, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, orderItem.getProduct().getId());
            preparedStatement.setInt(3, orderItem.getUser().getId());
            preparedStatement.setInt(4, orderItem.getNumber());

            if (null == orderItem.getOrder()) {
                preparedStatement.setInt(2, -1);
            } else {
                preparedStatement.setInt(2, orderItem.getOrder().getId());
            }

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                orderItem.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(OrderItem orderItem) {
        String sql = "update orderitem set pid = ?, oid = ?, uid = ?, number = ?  where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, orderItem.getProduct().getId());
            preparedStatement.setInt(2, orderItem.getOrder().getId());
            preparedStatement.setInt(3, orderItem.getUser().getId());
            preparedStatement.setInt(4, orderItem.getNumber());
            preparedStatement.setInt(5, orderItem.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "delete * from orderitem where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public OrderItem get(int id) {
        OrderItem orderItem = null;
        String sql = "select * from orderitem where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orderItem.setId(id);
//                orderItem.setProduct(new ProductDAO().get(resultSet.getInt("pid")));
                orderItem.setOrder(new OrderDAO().get(resultSet.getInt("oid")));
                orderItem.setUser(new UserDAO().get(resultSet.getInt("uid")));
                orderItem.setNumber(resultSet.getInt("number"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItem;
    }

    public List<OrderItem> listByUser(int uid) {
        return listByUser(uid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByUser(int uid, int start, int count) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        String sql = "select * from orderitem where uid = ? and oid = -1 order by id desc limit ?, ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, uid);
            statement.setInt(2, start);
            statement.setInt(3, count);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                OrderItem orderItem = new OrderItem();

                orderItem.setId(resultSet.getInt("id"));
//                orderItem.setProduct(new ProductDAO().get(resultSet.getInt("pid")));
                orderItem.setOrder(new OrderDAO().get(resultSet.getInt("oid")));
                orderItem.setUser(new UserDAO().get(resultSet.getInt("uid")));
                orderItem.setNumber(resultSet.getInt("number"));

                orderItems.add(orderItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }

    public List<OrderItem> listByOrder(int oid) {
        return listByOrder(oid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByOrder(int oid, int start, int count) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        String sql = "select * from orderitem where oid = ? order by id desc limit ?, ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, oid);
            statement.setInt(2, start);
            statement.setInt(3, count);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                OrderItem orderItem = new OrderItem();

                orderItem.setId(resultSet.getInt("id"));
//                orderItem.setProduct(new ProductDAO().get(resultSet.getInt("pid")));
                orderItem.setOrder(new OrderDAO().get(resultSet.getInt("oid")));
                orderItem.setUser(new UserDAO().get(resultSet.getInt("uid")));
                orderItem.setNumber(resultSet.getInt("number"));

                orderItems.add(orderItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }

    public List<OrderItem> listByProduct(int pid) {
        return listByProduct(pid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByProduct(int pid, int start, int count) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        String sql = "select * from orderitem where pid = ? order by id desc limit ?, ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, pid);
            statement.setInt(2, start);
            statement.setInt(3, count);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                OrderItem orderItem = new OrderItem();

                orderItem.setId(resultSet.getInt("id"));
//                orderItem.setProduct(new ProductDAO().get(resultSet.getInt("pid")));
                orderItem.setOrder(new OrderDAO().get(resultSet.getInt("oid")));
                orderItem.setUser(new UserDAO().get(resultSet.getInt("uid")));
                orderItem.setNumber(resultSet.getInt("number"));

                orderItems.add(orderItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }

    public void fill(Order order) {
        List<OrderItem> orderItems = listByOrder(order.getId());
        int totalNumber = 0;
        float total = 0;

        for (OrderItem orderItem :
                orderItems) {
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            totalNumber += orderItem.getNumber();
        }

        order.setTotal(total);
        order.setTotalNumber(totalNumber);
        order.setOrderItems(orderItems);
    }

    public void fill(List<Order> orders) {
        for (Order order : orders) {
            fill(order);
        }
    }

    public int getSaleCount(int pid){
        int total = 0;
        String sql = "select sum(number) from orderitem where pid = ?";

        try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, pid);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
}
