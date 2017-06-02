<%-- 
    Document   : abc
    Created on : 2017/5/5, 下午 03:20:39
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Admin page</title>
        <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css" />
        <link href="<c:url value="/css/multi-select.css" />" rel="stylesheet">
        <style>
        </style>
        <script src="//code.jquery.com/jquery-1.12.4.min.js" 
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
        crossorigin="anonymous"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
        <script src="<c:url value="/js/worktime-setting/worktime-columnsetting.js" />"></script>
        <script src="<c:url value="/js/jquery.multi-select.js" />"></script>

        <script>
            $(function () {
                var multiSel = $("#my-select");
//                multiSel.multiSelect({});

                for (var i = 0; i < worktimeCol.length; i++) {
                    var colName = worktimeCol[i].name;
                    multiSel.append("<option value='" + colName + "'>" + colName + "</option>");
                    multiSel.multiSelect('addOption', {value: colName, text: colName, index: i});
                }

                multiSel.multiSelect({});

                $("#testForm").submit(function () {
                    console.log(multiSel.val());
                    return false;
                });
            });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <h5>Dear <strong>${user.username}</strong>, Welcome to Admin Page.</h5>
                <a href="<c:url value="/" />">Home</a>

                <form id="testForm">
                    <select multiple="multiple" id="my-select" name="my-select[]"></select>
                    <input type="submit" value="submit" />
                </form>
            </div>
        </div>
    </body>
</html>