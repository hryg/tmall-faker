<%--
  Created by IntelliJ IDEA.
  User: hengrui
  Date: 2018/3/15
  Time: 15:19
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <script src="js/jquery/2.0.0/jquery.min.js"></script>
    <script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <link href="css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet"/>
    <link href="css/back/style.css" rel="stylesheet" />

    <script type="text/javascript">
        function checkEmpty(id, name) {
            var value = $("#" + id).val();
            if (value.length == 0) {
                alert(name + "不能为空");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }

        function checkNumber(id, name) {
            if (!checkEmpty(id, name)) {
                return false;
            }

            var value = $("#" + id).val();
            if (isNaN(value)) {
                alert(name + "必须是数字");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }

        function checkInt(id, name) {
            if (!checkEmpty(id, name)) {
                return false;
            }

            var value = $("#" + id).val();
            if(parseInt(value) != value) {
                alert(name + "必须是整数");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }

        $(function(){
            $("a").click(function() {
                var deleteLink = $(this).attr("deleteLink");
                console.log(deleteLink);
                if("true" == deleteLink) {
                    var confirmDelete = confirm("确认要删除？");
                    if (confirmDelete) {
                        return true;
                    }
                    return false;
                }
            });
        })
    </script>
</head>
<body>
