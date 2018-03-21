package com.tmall.servlet;

import com.tmall.bean.Category;
import com.tmall.bean.Property;
import com.tmall.dao.CategoryDAO;
import com.tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PropertyServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        String name = request.getParameter("name");

        Property property = new Property();
        property.setCategory(category);
        property.setName(name);
        propertyDAO.add(property);
        return "@admin_property_list?cid=" + cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Property property = propertyDAO.get(id);
        propertyDAO.delete(id);
        return "@admin_property_list?cid=" + property.getCategory().getId();
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Property property = propertyDAO.get(id);
        request.setAttribute("property", property);
        return "admin/editProperty.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        int cid = Integer.parseInt(request.getParameter("cid"));

        Property property = propertyDAO.get(id);
        String name = request.getParameter("name");
        Category category = categoryDAO.get(cid);
        property.setName(name);
        property.setCategory(category);
        propertyDAO.update(property);
        return "admin/listProperty.jsp";
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt("cid");
        Category category = categoryDAO.get(cid);
        List<Property> properties = propertyDAO.list(cid, page.getStart(), page.getCount());
        int total = propertyDAO.getTotal(cid);
        page.setTotal(total);
        page.setParam("&cid=" + cid);

        request.setAttribute("properties", properties);
        request.setAttribute("category", category);
        request.setAttribute("page", page);
        return "admin/listProperty.jsp";
    }
}
