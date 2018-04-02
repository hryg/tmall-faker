<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hryg
  Date: 2018/4/2
  Time: 下午9:38
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>

<c:forEach items="${categories}" var="category">
    <div cid="${category.id}" class="productsAsideCategorys">
        <c:forEach items="${category.productsByRow}" var="products">
            <div class="row show1">
                <c:forEach items="${products}" var="product">
                    <c:if test="${!empty product.subTitle}">
                        <a href="foreproduct?pid=${product.id}">
                            <c:forEach items="${fn:split(product.subTitle, ' ')}" var="title" varStatus="status">
                                <c:if test="${status.index == 0}">
                                    ${title}
                                </c:if>
                            </c:forEach>
                        </a>
                    </c:if>
                </c:forEach>
                <div class="seperator"></div>
            </div>
        </c:forEach>
    </div>
</c:forEach>
