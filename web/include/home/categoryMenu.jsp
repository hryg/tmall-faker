<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hryg
  Date: 2018/4/2
  Time: 下午9:32
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>

<div class="categoryMenu">
    <c:forEach items="${categories}" var="category">
        <div cid="${category.id}" class="eachCategory">
            <span class="glyphicon glyphicon-link"></span>
            <a href="forecategory?cid=${category.id}">
                ${category.name}
            </a>
        </div>
    </c:forEach>
</div>
