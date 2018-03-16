package com.tmall.servlet;

import com.tmall.bean.Category;
import com.tmall.util.ImageUtil;
import com.tmall.util.Page;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryServlet extends BaseBackServlet {

    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        Map<String, String> params = new HashMap<String, String>();
        InputStream is = super.parseUpload(request, params);

        Category category = new Category();
        category.setName(params.get("name"));
        categoryDAO.add(category);
        saveAsJpg(request, is, category.getId());

        return "@admin_category_list";
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        categoryDAO.delete(id);
        return "@admin_category_list";
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Category category = categoryDAO.get(id);
        request.setAttribute("category", category);
        return "admin/editCategory.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        Map<String, String> params = new HashMap<String, String>();
        InputStream is = super.parseUpload(request, params);

        Category category = new Category();
        category.setId(Integer.parseInt(params.get("id")));
        category.setName(params.get("name"));
        categoryDAO.update(category);
        saveAsJpg(request, is, category.getId());

        return "@admin_category_list";
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> cs = categoryDAO.list(page.getStart(), page.getCount());
        page.setTotal(categoryDAO.getTotal());
        request.setAttribute("thecs", cs);
        request.setAttribute("page", page);
        return "admin/listCategory.jsp";
    }

    /**
     * 将上传的图片保存为jpg格式
     * @param request 请求对象
     * @param inputStream 文件流
     * @param categoryId 操作对应的分类ID
     */
    private void saveAsJpg(HttpServletRequest request, InputStream inputStream, int categoryId) {
        File imageFolder = new File(request.getSession().getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, categoryId + ".jpg");
        try {
            if (null != inputStream && 0 != inputStream.available()) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    byte[] bytes = new byte[1024 * 1024];
                    int length = 0;
                    while (-1 != (length = inputStream.read())) {
                        fileOutputStream.write(bytes, 0, length);
                    }
                    fileOutputStream.flush();
                    // 通过如下代码，将文件保存为jpg格式
                    BufferedImage img = ImageUtil.change2jpg(file);
                    ImageIO.write(img, "jpg", file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
