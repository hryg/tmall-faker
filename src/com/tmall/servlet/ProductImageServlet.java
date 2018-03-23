package com.tmall.servlet;

import com.tmall.bean.Product;
import com.tmall.bean.ProductImage;
import com.tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ProductImageServlet extends BaseBackServlet {
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
        int pid = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.get(pid);
        List<ProductImage> pisSingle = productImageDAO.list(product, "type_single", page.getStart(), page.getCount());
        List<ProductImage> pisDetail = productImageDAO.list(product, "type_detail", page.getStart(), page.getCount());
        request.setAttribute("product", product);
        request.setAttribute("pisSingle", pisSingle);
        request.setAttribute("pisDetail", pisDetail);
        return "admin/listProductImage.jsp";
    }
}
