<%--
  Created by IntelliJ IDEA.
  User: hryg
  Date: 2018/3/22
  Time: 上午7:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../include/admin/adminHeader.jsp" %>
<%@ include file="../include/admin/adminNavigator.jsp" %>

<script>
    $(function () {
        $("#addForm").submit(function () {
            if (!checkEmpty("name", "属性名称")) {
                return false;
            }
            return true;
        });
    });
</script>

<title>属性管理</title>
<div class="workingArea">
    <ol class="breadcrumb">
        <Li><a href="admin_category_list">所有分类</a></Li>
        <li><a href="admin_property_list?cid=${category.id}">${category.name}</a></li>
        <li class="active">属性管理</li>
    </ol>

    <div class="listDataTableDiv">
        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
            <tr class="success">
                <th>ID</th>
                <th>属性名称</th>
                <th>编辑</th>
                <th>删除</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${properties}" var="property">
                <tr>
                    <td>${property.id}</td>
                    <td><${property.name}</td>
                    <td>
                        <a href="admin_property_?cid=${c.id}.jpg"><span
                                class="glyphicon glyphicon-shopping-cart"></span></a>
                    </td>
                    <td>
                        <a href="admin_category_edit?id=${c.id}"><span
                                class="glyphicon glyphicon-edit"></span></a>
                    </td>
                    <td>
                        <a deleteLink="true" href="admin_category_delete?id=${c.id}"><span
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
        <div class="pannel-heading">新增分类</div>
        <div class="panel-body">
            <form method="post" id="addForm" action="admin_category_add" enctype="multipart/form-data">
                <table class="addTable">
                    <tr>
                        <td>分类名称</td>
                        <td><input id="name" name="name" type="text" class="form-control"></td>
                    </tr>
                    <tr>
                        <td>分类图片</td>
                        <td><input id="categoryPic" name="filepath" accept="image/*" type="file"></td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                            <button type="submit" class="btn btn-success">提 交</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>

<%@ include file="../include/admin/adminFooter.jsp" %>