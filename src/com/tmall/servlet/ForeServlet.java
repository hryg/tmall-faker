package com.tmall.servlet;

        import com.tmall.bean.Category;
        import com.tmall.bean.User;
        import com.tmall.util.Page;
        import org.springframework.web.util.HtmlUtils;

        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
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
}
