<%-- 
    Document   : test
    Created on : 2017/3/13, 上午 10:46:44
    Author     : Wei.Cheng
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="https://code.jquery.com/jquery-1.12.4.min.js" 
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
                crossorigin="anonymous"
        ></script>
        <script src="js/moment.js"></script>
        <script>
            $(function () {
                var i = 0;

                var obj = {field1: i++, field2: i++, field3: i++};

                $("#test").html(JSON.stringify(obj));
            });
        </script>
    </head>
    <body>
        <h1>Hello World!</h1>
        <div id="test"></div>
    </body>
</html>
