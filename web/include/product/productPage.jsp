<%--
  Created by IntelliJ IDEA.
  User: hengrui
  Date: 2018/4/9
  Time: 13:56
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>

<title>模仿天猫官网</title>
<div class="categoryPictureInProductPageDiv">
    <img src="img/category/${product.category.id}.jpg" class="categoryPictureInProductPage">
</div>

<div class="productPageDiv">
    <%@ include file="imgAndInfo.jsp"%>
    <%@ include file="productReview.jsp"%>
    <%@ include file="productDetail.jsp"%>
</div>