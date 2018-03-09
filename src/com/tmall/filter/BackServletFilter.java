package com.tmall.filter;


import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BackServletFilter implements Filter {
    private static String SEPARATOR = "_";
    private static String SERVLET_PREFIX = "/admin";
    private static String SERVLET_SUFFIX = "servlet";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String contextPath = request.getServletContext().getContextPath();
        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri, contextPath);

        if (uri.startsWith(SERVLET_PREFIX)) {
            String servletPath = StringUtils.substringBetween(uri, SEPARATOR, SEPARATOR) + SERVLET_SUFFIX;
            String method = StringUtils.substringAfterLast(uri, SEPARATOR);

            request.setAttribute("method", method);
            servletRequest.getRequestDispatcher("/" + servletPath).forward(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
