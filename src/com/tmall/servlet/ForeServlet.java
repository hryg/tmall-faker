package com.tmall.servlet;

import com.tmall.bean.*;
import com.tmall.dao.ProductImageDAO;
import com.tmall.util.Page;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForeServlet extends BaseForeServlet {
    public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> categories = categoryDAO.list();
        productDAO.fill(categories);
        productDAO.fillByRow(categories);
        request.setAttribute("categories", categories);
        return "home.jsp";
    }

    public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        name = HtmlUtils.htmlEscape(name);
        boolean userExisted = userDAO.isExisted(name);
        if (userExisted) {
            request.setAttribute("msg", "用户名已被注册！");
            return "register.jsp";
        }

        User user = new User();
        user.setName(name);
        user.setPassword(password);
        System.out.println("name: " + name);
        System.out.println("password: " + password);
        userDAO.add(user);

        return "@registerSuccess.jsp";
    }

    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        name = HtmlUtils.htmlEscape(name);

        User user = userDAO.get(name, password);
        if (user == null) {
            request.setAttribute("msg", "账号密码错误！");
            return "login.jsp";
        }
        request.getSession().setAttribute("user", user);
        return "@forehome";
    }

    public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
        request.getSession().removeAttribute("user");
        return "@forehome";
    }

    public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product product = productDAO.get(pid);

        List<ProductImage> productSingleImages = productImageDAO.list(product, ProductImageDAO.TYPE_SINGLE);
        List<ProductImage> productDetailImages = productImageDAO.list(product, ProductImageDAO.TYPE_DETAIL);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);

        List<PropertyValue> propertyValues = propertyValueDAO.list(pid);
        List<Review> reviews = reviewDAO.list(pid);
        productDAO.setSaleAndReviewNumber(product);

        request.setAttribute("product", product);
        request.setAttribute("propertyValues", propertyValues);
        request.setAttribute("reviews", reviews);
        return "product.jsp";
    }

    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if (null != user) {
            return "%success";
        } else {
            return "%fail";
        }
    }

    public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        User user = userDAO.get(name, password);

        if (null == user) {
            return "%fail";
        }

        request.getSession().setAttribute("user", user);
        return "%success";
    }

    public String category(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        productDAO.fill(category);
        productDAO.setSaleAndReviewNumber(category.getProducts());
        String sort = request.getParameter("sort");
        if (null != sort) {
            switch (sort) {
                case "review":
                    Collections.sort(category.getProducts(), (p1, p2) -> p2.getReviewCount() - p1.getReviewCount());
                    break;
                case "date":
                    Collections.sort(category.getProducts(), (p1, p2) -> p1.getCreateDate().compareTo(p2.getCreateDate()));
                    break;
                case "saleCount":
                    Collections.sort(category.getProducts(), (p1, p2) -> p2.getSaleCount() - p1.getSaleCount());
                    break;
                case "price":
                    Collections.sort(category.getProducts(), (p1, p2) -> (int) (p1.getPromotePrice() - p2.getPromotePrice()));
                    break;
                case "all":
                    Collections.sort(category.getProducts(), (p1, p2) -> p2.getReviewCount() * p2.getSaleCount() - p1.getReviewCount() * p1.getSaleCount());
                    break;
                default:
                    break;
            }
        }
        request.setAttribute("category", category);
        return "category.jsp";
    }

    public String search(HttpServletRequest request, HttpServletResponse response, Page page) {
        String keyword = request.getParameter("keyword");
        List<Product> products = productDAO.search(keyword, 0, 20);
        productDAO.setSaleAndReviewNumber(products);
        request.setAttribute("products", products);
        return "searchResult.jsp";
    }

    public String buyone(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oiid = 0;
        int pid = Integer.parseInt(request.getParameter("pid"));
        int num = Integer.parseInt(request.getParameter("num"));
        Product product = productDAO.get(pid);

        User user = (User) request.getSession().getAttribute("user");
        boolean bought = false;
        List<OrderItem> orderItems = orderItemDAO.listByUser(user.getId());
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getId() == pid) {
                bought = true;
                orderItem.setNumber(orderItem.getNumber() + num);
                orderItemDAO.update(orderItem);
                oiid = orderItem.getId();
                break;
            }
        }
        if (!bought) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUser(user);
            orderItem.setNumber(num);
            orderItem.setProduct(product);
            orderItemDAO.add(orderItem);
            oiid = orderItem.getId();
        }
        return "@forebuy?oiid=" + oiid;
    }

    public String buy(HttpServletRequest request, HttpServletResponse response, Page page) {
        String[] oiids = request.getParameterValues("oiid");
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;

        for (String id : oiids) {
            int oiid = Integer.parseInt(id);
            OrderItem orderItem = orderItemDAO.get(oiid);
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            orderItems.add(orderItem);
        }

        request.getSession().setAttribute("orderItems", orderItems);
        request.setAttribute("total", total);
        return "buy.jsp";
    }

    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        int num = Integer.parseInt(request.getParameter("num"));
        Product product = productDAO.get(pid);

        User user = (User) request.getSession().getAttribute("user");
        boolean bought = false;

        List<OrderItem> orderItems = orderItemDAO.listByUser(user.getId());
        for (OrderItem orderItem: orderItems) {
            if (orderItem.getProduct().getId() == pid) {
                bought = true;
                orderItem.setNumber(orderItem.getNumber() + num);
                orderItemDAO.update(orderItem);
                break;
            }
        }

        if (!bought) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUser(user);
            orderItem.setNumber(num);
            orderItem.setProduct(product);
            orderItemDAO.add(orderItem);
        }
        return "%success";
    }

    public String cart(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        List<OrderItem> orderItems = orderItemDAO.listByUser(user.getId());
        request.setAttribute("orderItems", orderItems);
        return "cart.jsp";
    }

    public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if (null == user) {
            return "%fail";
        }
        int oiid = Integer.parseInt(request.getParameter("oiid"));
        orderItemDAO.delete(oiid);
        return "%success";
    }
}
