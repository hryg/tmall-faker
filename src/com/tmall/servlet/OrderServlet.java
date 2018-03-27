package com.tmall.servlet;

import com.tmall.bean.Order;
import com.tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class OrderServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Order> orders = orderDAO.list(page.getStart(), page.getCount());
        orderItemDAO.fill(orders);
        int total = orderDAO.getTotal();
        page.setTotal(total);
        request.setAttribute("orders", orders);
        request.setAttribute("page", page);
        return "admin/listOrder.jsp";
    }
}
