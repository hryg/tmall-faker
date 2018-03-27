<%--
  Created by IntelliJ IDEA.
  User: hengrui
  Date: 2018/3/27
  Time: 14:37
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="../include/admin/adminHeader.jsp"%>
<%@ include file="../include/admin/adminNavigator.jsp"%>

<script>
    $(function () {
        $("input.pvValue").keyup(function () {
            var value = $(this).val();
            var page = "admin_product_updatePropertyValue";
            var pvid = $(this).attr("pvid");
            var parentSpan = $(this).parent("span");
            parentSpan.css("border", "1px solid yellow");
            $.post(
                page,
                {"value": value, "pvid": pvid},
                function (result) {
                    if("success") {
                        parentSpan.css("border", "1px solid green");
                    } else {
                        parentSpan.css("border", "1px solid red");
                    }
                }
            );
        });
    });
</script>

<title>编辑产品属性</title>
<div class="workingArea">
    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a></li>
        <li><a href="admin_product_list?cid=${product.category.id}">${product.category.name}</a></li>
        <li class="active">${product.name}</li>
        <li class="active">编辑产品属性</li>
    </ol>

    <div class="editPVDiv">
        <c:forEach items="${propertyValues}" var="propertyValue">
            <div class="eachPV">
                <span class="pvName">${propertyValue.property.name}</span>
                <span class="pvValue">
                    <input class="pvValue" pvid="${propertyValue.id}" type="text" value="${propertyValue.value}">
                </span>
            </div>
        </c:forEach>
    </div>
</div>

