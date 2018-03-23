package com.tmall.servlet;

import com.tmall.bean.Category;
import com.tmall.bean.Product;
import com.tmall.dao.CategoryDAO;
import com.tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class ProductServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String subTitle = request.getParameter("subTitle");
        Float originalPrice = Float.parseFloat(request.getParameter("originalPrice"));
        Float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        int cid = Integer.parseInt(request.getParameter("cid"));

        Product product = new Product();
        product.setName(name);
        product.setSubTitle(subTitle);
        product.setOriginalPrice(originalPrice);
        product.setPromotePrice(promotePrice);
        product.setStock(stock);
        product.setCategory(categoryDAO.get(cid));
        product.setCreateDate(new Date(System.currentTimeMillis()));
        productDAO.add(product);
        return "@admin_product_list?cid=" + cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        int id = Integer.parseInt(request.getParameter("id"));
        productDAO.delete(id);
        return "@admin_product_list?cid=" + cid;
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
        Category category = categoryDAO.get(cid);
        List<Product> products = productDAO.list(cid, page.getStart(), page.getCount());
        page.setTotal(productDAO.getTotal(cid));
        page.setParam("&cid=" + cid);
        request.setAttribute("cid", cid);
        request.setAttribute("category", category);
        request.setAttribute("products", products);
        request.setAttribute("page", page);
        return "admin/listProduct.jsp";
    }
}
