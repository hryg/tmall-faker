<%--
  Created by IntelliJ IDEA.
  User: hengrui
  Date: 2018/4/10
  Time: 14:59
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>

<div class="productDetailDiv">
    <div class="productDetailTopPart">
        <a href="#nowhere" class="productDetailTopPartSelectedLink selected">商品详情</a>
        <a href="#nowhere" class="productDetailTopReviewLink">累计评价
            <span class="productDetailTopReviewLinkNumber">${product.reviewCount}</span>
        </a>
    </div>

    <div class="productParamterPart">
        <div class="productParamter">产品参数：</div>
        <div class="productParamterList">
            <c:forEach items="${propertyValues}" var="propertyValue">
                <span>${propertyValue.property.name}:  ${fn:substring(propertyValue.value, 0, 10)} </span>
            </c:forEach>
        </div>
        <div style="clear:both"></div>
    </div>

    <div class="productDetailImagesPart">
        <c:forEach items="${product.productDetailImages}" var="pi">
            <img src="img/productDetail/${pi.id}.jpg">
        </c:forEach>
    </div>
</div>
