package com.tmall.filter;

import com.tmall.bean.Category;
import com.tmall.bean.OrderItem;
import com.tmall.bean.User;
import com.tmall.dao.CategoryDAO;
import com.tmall.dao.OrderItemDAO;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ForeServletFilter implements Filter {
    private static String PREFIX = "/fore";
    private static String SERVLET_PATH = "/foreServlet";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String contextPath = request.getServletContext().getContextPath();
        request.getServletContext().setAttribute("contextPath", contextPath);

        User user = (User) request.getSession().getAttribute("user");
        int cartTotalItemNumber = 0;
        if (null != user) {
            List<OrderItem> orderItems = new OrderItemDAO().listByUser(user.getId());
            for (OrderItem orderItem : orderItems) {
                cartTotalItemNumber += orderItem.getNumber();
            }
        }
        request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);

        List<Category> categories = (List<Category>) request.getAttribute("categories");
        if (null == categories) {
            categories = new CategoryDAO().list();
            request.setAttribute("categories", categories);
        }

        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri, contextPath);
        if (uri.startsWith(PREFIX) && !uri.startsWith(SERVLET_PATH)) {
            String method = StringUtils.substringAfterLast(uri, PREFIX);
            request.setAttribute("method", method);
            servletRequest.getRequestDispatcher("/foreServlet").forward(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
