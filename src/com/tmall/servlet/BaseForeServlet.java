package com.tmall.servlet;

import com.tmall.dao.*;
import com.tmall.util.Page;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class BaseForeServlet extends HttpServlet {
    private static final String CLIENT_REDIRECT_PREFIX = "@";
    private static final String RESPONSE_STRING_PREFIX = "%";

    protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            int start = 0;
            int count = 10;

            try {
                start = Integer.parseInt(request.getParameter("page.start"));
                count = Integer.parseInt(request.getParameter("page.count"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            Page page = new Page(start, count);
            String method = (String) request.getAttribute("method");
            Method m = this.getClass().getMethod(method, HttpServletRequest.class, HttpServletResponse.class, Page.class);
            String redirect = m.invoke(this, request, response, page).toString();

            // 根据方法的返回值，进行相应客户端或服务端跳转
            if (redirect.startsWith(CLIENT_REDIRECT_PREFIX)) {
                response.sendRedirect(redirect.substring(1));
            } else if (redirect.startsWith(RESPONSE_STRING_PREFIX)) {
                response.getWriter().print(redirect.substring(1));
            } else {
                request.getRequestDispatcher(redirect).forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
