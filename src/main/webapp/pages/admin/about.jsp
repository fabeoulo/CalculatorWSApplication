<%-- 
    Document   : sensorAdjust
    Created on : 2016/4/14, 下午 02:11:41
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <style>
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
        </style>
        <script src="../../js/jquery-1.11.3.min.js"></script>
        <script>
            $(function () {
            });
        </script>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div class="container">
            <h3>System discription1</h3>
            <h3>System discription2</h3>
            <h3>System discription3</h3>
            <h3>Tomcat Version : <%= application.getServerInfo()%></h3>
            <h5>Servlet Specification Version : <%= application.getMajorVersion()%>.<%= application.getMinorVersion()%> </h5>
            <h5>JSP version :<%=JspFactory.getDefaultFactory().getEngineInfo().getSpecificationVersion()%></h5>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>