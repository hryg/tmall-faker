<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hengrui
  Date: 2018/4/8
  Time: 11:06
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>

<div>
    <a href="/forehome">
        <img id="simpleLogo" class="simpleLogo" src="img/site/simpleLogo.png">
    </a>

    <form action="foresearch" method="post">
        <div class="simpleSearchDiv pull-right">
            <input type="text" placeholder="平衡车 原汁机" value="${param.keyword}" name="keyword">
            <button class="searchButton" type="submit">搜天猫</button>
            <div class="searchBelow">
                <c:forEach items="${categories}" var="category" varStatus="status">
                    <c:if test="${status.count >= 8 and status.count <= 11}">
                        <span>
                            <a href="forecategory?cid=${category.id}">
                                ${category.name}
                            </a>
                            <c:if test="${status.count != 11}">
                                <span>|</span>
                            </c:if>
                        </span>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </form>
    <div style="clear:both"></div>
</div>
