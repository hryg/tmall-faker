<%--
  Created by IntelliJ IDEA.
  User: hryg
  Date: 2018/4/3
  Time: 上午7:00
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>

<c:if test="${empty param.categorycount}">
    <c:set var="categorycount" scope="page" value="100"></c:set>
</c:if>

<c:if test="${!empty param.categorycount}">
    <c:set var="categorycount" scope="page" value="${param.categorycount}"></c:set>
</c:if>

<div class="homepageCategoryProducts">
    <c:forEach items="${categories}" var="category">
        <div class="eachHomepageCategoryProducts">
            <div class="left-mark"></div>
            <span class="categoryTitle">${category.name}</span>
            <br>
            <c:forEach items="${category.products}" var="product" varStatus="statusProduct">
                <c:if test="${statusProduct.count <= 5}">
                    <div class="productItem">
                        <a href="foreproduct?pid=${product.id}">
                            <img src="img/productSingle_middle/${product.firstProductImage.id}.jpg" width="100px">
                        </a>
                        <a class="productItemDescLink" href="foreproduct?pid=${product.id}">
                            <span class="productItemDesc">
                                [热销]${fn:substring(product.name, 0, 20)}
                            </span>
                        </a>
                        <span class="productPrice">
                            <fmt:formatNumber type="number" value="${product.promotePrice}" minFractionDigits="2"/>
                        </span>
                    </div>
                </c:if>
            </c:forEach>
            <div style="clear: both;"></div>
        </div>
    </c:forEach>

    <img id="endpng" class="endpng" src="img/site/end.png">
</div>