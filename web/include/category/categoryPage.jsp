<%--
  Created by IntelliJ IDEA.
  User: hengrui
  Date: 2018/4/11
  Time: 10:24
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>

<title>模仿天猫官网-${category.name}</title>
<div id="category">
    <div class="categoryPageDiv">
        <img src="img/category/${category.id}.jpg">
        <%@include file="sortBar.jsp"%>
        <%@include file="productsByCategory.jsp"%>
    </div>
</div>