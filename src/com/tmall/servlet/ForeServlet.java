package com.tmall.servlet;

        import com.tmall.bean.*;
        import com.tmall.dao.ProductImageDAO;
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
}
