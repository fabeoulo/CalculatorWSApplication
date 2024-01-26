<%-- 
    Document   : sensorAdjust
    Created on : 2016/4/14, 下午 02:11:41
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <c:set var="userSitefloor" value="${param.sitefloor}" />
    <c:set var="numberRE" value="\\d+" />
    <c:set var="userLineType" value="${param.lineType}" />
    <c:if test="${(userSitefloor == null) || !(userSitefloor.matches(numberRE)) || (userSitefloor < 1 || userSitefloor > 7)}">
        <c:redirect url="SysInfo" />
    </c:if>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />">
        <style>
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
            #wigetCtrl{
                margin: 0px auto;
                width: 98%
            }
            iframe1{

            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script>
            $(function () {
                $.getJSON("../../json/sitefloor.json", function (data) {
                    var sf = ${userSitefloor};
                    var lt = "${userLineType}";

                    var matchFlag = false;
                    var sitefloors = data.sitefloors_mgr;
                    for (var i = 0, j = sitefloors.length; i < j; i++) {
                        if (sf === sitefloors[i].floor && lt === sitefloors[i].lineType) {
                            matchFlag = true;
                            break;
                        }
                    }
                    if (!matchFlag) {
                        window.location.href = "<c:url value="/SysInfo" />";
                    }
                });

                $("#iframe1").load(function () {
                    console.log("This table is update.");
                });
            });
        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <div id="wigetCtrl">
            <iframe id="iframe1" style='width:100%; height:650px' frameborder="0" scrolling="no" 
                    src="map_totalStatus.jsp?sitefloor=${userSitefloor}&lineType=${userLineType}" 
                    webkitAllowFullScreen mozAllowFullScreen allowFullScreen></iframe>
        </div>
        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
