<%--
  Created by IntelliJ IDEA.
  User: hryg
  Date: 2018/3/22
  Time: 下午10:10
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="../include/admin/adminHeader.jsp" %>
<%@ include file="../include/admin/adminNavigator.jsp" %>

<script>
    $(function () {
        $("#addForm").submit(function () {
            if (!checkEmpty("name", "产品名称")) {
                return false;
            }
            return true;
        });
    });
</script>

<title>属性管理</title>
<div class="workingArea">
    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a></li>
        <li><a href="admin_product_list?cid=${category.id}">${category.name}</a></li>
        <li class="active">产品管理</li>
    </ol>

    <div class="listDataTableDiv">
        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
            <tr class="success">
                <th>ID</th>
                <th>产品名称</th>
                <th>产品小标题</th>
                <th>原价格</th>
                <th>优惠价格</th>
                <th>库存数量</th>
                <th>编辑</th>
                <th>删除</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${products}" var="product">
                <tr>
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.subTitle}</td>
                    <td>${product.orignalPrice}</td>
                    <td>${product.promotePrice}</td>
                    <td>${product.stock}</td>
                    <td>
                        <a href="admin_product_edit?id=${product.id}"><span
                                class="glyphicon glyphicon-edit"></span></a>
                    </td>
                    <td>
                        <a deleteLink="true" href="admin_product_delete?id=${product.id}"><span
                                class="glyphicon glyphicon-trash"></span></a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="pageDiv">
        <%@ include file="../include/admin/adminPage.jsp" %>
    </div>

    <div class="panel panel-warning addDiv">
        <div class="pannel-heading">新增产品</div>
        <div class="panel-body">
            <form method="post" id="addForm" action="admin_product_add">
                <table class="addTable">
                    <tr>
                        <td>产品名称</td>
                        <td><input id="name" name="name" type="text" class="form-control"></td>
                    </tr>
                    <tr>
                        <td>产品小标题</td>
                        <td><input id="subTitle" name="subTitle" type="text" class="form-control"></td>
                    </tr>
                    <tr>
                        <td>原价格</td>
                        <td><input id="orignalPrice" name="orignalPrice" type="text" class="form-control"></td>
                    </tr>
                    <tr>
                        <td>优惠价格</td>
                        <td><input id="promotePrice" name="promotePrice" type="text" class="form-control"></td>
                    </tr>
                    <tr>
                        <td>库存</td>
                        <td><input id="stock" name="stock" type="text" class="form-control"></td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                            <input id="cid" name="cid" type="hidden" value="${category.id}" />
                            <button type="submit" class="btn btn-success">提 交</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>

<%@ include file="../include/admin/adminFooter.jsp" %>
