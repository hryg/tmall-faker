package com.tmall.servlet;

import com.tmall.bean.Product;
import com.tmall.bean.ProductImage;
import com.tmall.dao.ProductImageDAO;
import com.tmall.util.ImageUtil;
import com.tmall.util.Page;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductImageServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        InputStream inputStream = null;
        Map<String, String> params = new HashMap<>();
        inputStream = parseUpload(request, params);

        String type = params.get("type");
        int pid = Integer.parseInt(params.get("pid"));
        Product product = productDAO.get(pid);

        ProductImage productImage = new ProductImage();
        productImage.setType(type);
        productImage.setProduct(product);
        productImageDAO.add(productImage);

        // 获取路径
        String fileName = productImage.getId() + ".jpg";
        String imageFolder = null;
        String imageFolderSmall = null;
        String imageFolderMiddle = null;
        if (ProductImageDAO.TYPE_SINGLE.equals(productImage.getType())) {
            imageFolder = request.getServletContext().getRealPath("img/productSingle");
            imageFolderSmall = request.getServletContext().getRealPath("img/productSingle_small");
            imageFolderMiddle = request.getServletContext().getRealPath("img/productSingle_middle");
        } else {
            imageFolder = request.getServletContext().getRealPath("img/productDetail");
        }

        File file = new File(imageFolder, fileName);
        file.getParentFile().mkdirs();
        try {
            if (null != inputStream && 0 != inputStream.available()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] bytes = new byte[1024 * 10240];
                    int length = 0;
                    while (-1 != (length = inputStream.read(bytes))) {
                        fos.write(bytes, 0, length);
                    }
                    fos.flush();
                    // 通过如下代码，把文件保存为jpg格式
                    BufferedImage image = ImageUtil.change2jpg(file);
                    ImageIO.write(image, "jpg", file);
                    if (ProductImageDAO.TYPE_SINGLE.equals(productImage.getType())) {
                        File fileSmall = new File(imageFolderSmall, fileName);
                        File fileMiddle = new File(imageFolderMiddle, fileName);
                        ImageUtil.resizeImage(file, 56, 56, fileSmall);
                        ImageUtil.resizeImage(file, 217, 90, fileMiddle);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "@admin_productImage_list?id=" + product.getId();
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
