package com.tmall.dao;

import com.tmall.bean.Product;
import com.tmall.bean.Review;
import com.tmall.bean.User;
import com.tmall.util.DBUtil;
import com.tmall.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public int getTotal() {
        int total = 0;
        String sql = "select count(*) form review";

        try (Connection connection = DBUtil.getConnection(); Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public int getTotal(int pid) {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement statement = c.createStatement();) {

            String sql = "select count(*) from Review where pid = " + pid;

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    public void add(Review review) {
        String sql = "insert into review values(null, ?, ?, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, review.getContent());
            preparedStatement.setInt(2, review.getUser().getId());
            preparedStatement.setInt(3, review.getProduct().getId());
            preparedStatement.setTimestamp(4, DateUtil.d2t(review.getCreateDate()));
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                review.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Review review) {
        String sql = "update review set content = ?, uid = ?, pid = ?, createDate = ? where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, review.getContent());
            preparedStatement.setInt(2, review.getUser().getId());
            preparedStatement.setInt(3, review.getProduct().getId());
            preparedStatement.setTimestamp(4, DateUtil.d2t(review.getCreateDate()));
            preparedStatement.setInt(5, review.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "delete from review where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Review get(int id) {
        Review review = null;
        String sql = "select * from review where id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                review = new Review();
                review.setId(id);
                review.setContent(resultSet.getString("content"));
                review.setUser(new UserDAO().get(resultSet.getInt("uid")));
                review.setProduct(new ProductDAO().get(resultSet.getInt("pid")));
                review.setCreateDate(DateUtil.t2d(resultSet.getTimestamp("createDate")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return review;
    }

    public List<Review> list(int pid) {
        return list(pid, 0, Short.MAX_VALUE);
    }

    public int getCount(int pid) {
        String sql = "select count(*) from review where pid = ? ";

        try (Connection connection = DBUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1, pid);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return 0;
    }

    public List<Review> list(int pid, int start, int count) {
        List<Review> reviews = new ArrayList<Review>();
        String sql = "select * from review where pid = ? order by id desc limit ?,? ";

        try (Connection connection = DBUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1, pid);
            preparedStatement.setInt(2, start);
            preparedStatement.setInt(3, count);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Review bean = new Review();
                bean.setId(resultSet.getInt(1));
                bean.setContent(resultSet.getString("content"));
                bean.setUser(new UserDAO().get(resultSet.getInt("uid")));
                bean.setCreateDate(DateUtil.t2d(resultSet.getTimestamp("createDate")));
                bean.setProduct(new ProductDAO().get(pid));
                reviews.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return reviews;
    }

    public boolean isExist(String content, int pid) {

        String sql = "select * from review where content = ? and pid = ?";

        try (Connection connection = DBUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, content);
            preparedStatement.setInt(2, pid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }

}
