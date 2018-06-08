<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<sec:authorize access="isAuthenticated()"  var="isAuthenticated" />
<sec:authorize access="hasRole('ADMIN')"  var="isAdmin" />
<sec:authorize access="hasRole('OPER_MFG_LINEOWNER')"  var="isMfgLineOwner" />
<sec:authorize access="hasRole('OPER_MFG')"  var="isMfgOper" />
<sec:authorize access="hasRole('OPER_FQC')"  var="isFqcOper" />
<link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />">
<link rel="stylesheet" href="<c:url value="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />" >
<style>
    a[disabled] {
        pointer-events: none;
    }
    span{
        display: inline;
    }
    #logoImg{
        width: 40px;
        height: 25px;
    }
    body{
        font-family: 微軟正黑體;
    }
</style>
<script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js" />"></script>

<div style="text-align:center; color: red">
    <noscript>For full functionality of this page it is necessary to enable JavaScript. Here are the <a href="http://www.enable-javascript.com" target="_blank"> instructions how to enable JavaScript in your web browser</a></noscript>
</div>
<div id="jquery-require-message" style="text-align:center; color: red">
</div>
<script>
    if (!window.jQuery) {
        document.getElementById("jquery-require-message").innerHTML =
                "Sorry, this page require jquery plugin\
                , please check your system environment or contact system administrator";
    }
    $(function () {
        $.getJSON("../../json/sitefloor.json", function (data) {
            var sitefloors = data.sitefloors;
            var navbar = $("#bs-example-navbar-collapse-1");
            for (var i = 0, j = sitefloors.length; i < j; i++) {
                var sitefloor = sitefloors[i].floor;
                navbar.find(".totalMapSelect").append("<li><a href='TotalMap?sitefloor=" + sitefloor + "'>狀態平面圖" + sitefloor + "F</a></li>");
                navbar.find(".sensorAdjustSelect").append("<li><a href='SensorAdjust?sitefloor=" + sitefloor + "'>" + sitefloor + "樓感應器狀態(校正用)</a></li>");
            }
        });
    });
</script>
<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="SysInfo">
                <span><img id="logoImg" src="<c:url value="/images/bulb.png" />" alt="sysIcon" /></span>
                    ${initParam.pageTitle}
                <!--<span><img id="logoImg" src="../../images/bulb.png" alt="sysIcon" /></span>-->
            </a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-list-alt" aria-hidden="true" /> 
                        測試
                        <span class="caret" />
                    </a>
                    <ul class="dropdown-menu">
                        <!--<li><a href="testTotal.jsp">測試線別狀態</a></li>-->
                        <li><a href="TestTotalDetail">測試線別狀態</a></li>
                        <li><a href="TestTotal">測試線別紀錄</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-list-alt" aria-hidden="true" /> 
                        組裝
                        <span class="caret" />
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="BabTotal?lineType=ASSY">線平衡資訊查詢</a></li>
                        <li><a href="BabDetailInfo">各站機台時間查詢</a></li>
                        <li><a href="BabDetailInfo2?lineType=ASSY">各站時間查詢(報表格式)</a></li>
                            <c:if test="${isMfgLineOwner || isMfgOper || isAdmin}">
                            <li><a href="ModelSopRemark">Sop維護</a></li>
                            </c:if>
                    </ul>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-list-alt" aria-hidden="true" /> 
                        包裝
                        <span class="caret" />
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="BabTotal?lineType=Packing">線平衡資訊查詢</a></li>
                        <li><a href="BabDetailInfo">各站機台時間查詢</a></li>
                        <li><a href="BabDetailInfo2?lineType=Packing">各站時間查詢(報表格式)</a></li>
                            <c:if test="${isMfgLineOwner || isMfgOper || isAdmin}">
                            <li><a href="modelSopRemark.jsp">Sop維護</a></li>
                            </c:if>
                    </ul>
                </li>
                <li>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-list-alt" aria-hidden="true" /> 
                        FQC
                        <span class="caret" />
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="FqcDashBoard?sitefloor=6">FQC效率</a></li>
                        <li><a href="FqcRecord">FQC效率記錄查詢</a></li>
                            <c:if test="${isFqcOper || isAdmin}">
                            <li><a href="FqcModelStandardTime">FQC標工維護</a></li>
                            </c:if>
                    </ul>
                </li>
                <li>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-list-alt" aria-hidden="true" /> 
                        平面圖
                        <span class="caret" />
                    </a>
                    <ul class="dropdown-menu totalMapSelect"></ul>
                </li>
                <c:if test="${isMfgLineOwner || isMfgOper || isAdmin}">
                    <li>
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                            <span class="glyphicon glyphicon-list-alt" aria-hidden="true" /> 
                            感應器
                            <span class="caret" />
                        </a>
                        <ul class="dropdown-menu sensorAdjustSelect"></ul>
                    </li>
                </c:if>
            </ul>
            <ul class="nav navbar-nav navbar-right pull-right">
                <c:choose>
                    <c:when test="${!isAuthenticated}">
                        <li>
                            <a href="<c:url value="/login" />">
                                <span class="glyphicon glyphicon-log-in" />
                                login
                            </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="dropdown">
                            <a href="properties.php?type=showall" class="dropdown-toggle" data-toggle="dropdown">
                                <c:out value="${user.username}" />
                                <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="<c:url value="/logout" />">
                                        <span class="glyphicon glyphicon-log-out" />
                                        logout
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container -->
</nav>
<!-- /.container -->

<!-- 為了省略include所造成多餘的<html><body>標籤而簡化，encoding會有問題還是要加上開頭 -->