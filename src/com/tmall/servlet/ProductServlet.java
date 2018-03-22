package com.tmall.servlet;

import com.tmall.bean.Product;
import com.tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ProductServlet extends BaseBackServlet {
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
        int cid = Integer.parseInt(request.getParameter("cid"));
        List<Product> products = productDAO.list(cid, page.getStart(), page.getCount());
        page.setTotal(productDAO.getTotal(cid));
        page.setParam("&cid=" + cid);
        request.setAttribute("cid", cid);
        request.setAttribute("products", products);
        request.setAttribute("page", page);
        return "admin/listProduct.jsp";
    }
}
