<%-- 
    Document   : chart
    Created on : 2016/2/16, 下午 02:26:52
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />"/>
        <link rel="stylesheet" href="<c:url value="/webjars/datatables/1.10.16/css/jquery.dataTables.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/bootstrap-datetimepicker.min.css" />">
        <link rel="stylesheet" href="<c:url value="/webjars/jquery-ui-themes/1.12.1/redmond/jquery-ui.min.css" />" >
        <link rel="stylesheet" href="<c:url value="/css/buttons.dataTables.min.css" />">
        <style>
            body{
                font-size: 16px;
                padding-top: 70px;
            }
            table{
                width:100%;
            }
            .alarm{
                color:red;
            }
            .wiget-ctrl{
                width: 98%;
                margin: 5px auto;

            }
            #balanceCount{
                border:2px black solid;
                width:50%;
            }
        </style>
        <script src="<c:url value="/js/charts.loader.js" />"></script>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/js/jquery-ui-1.10.0.custom.min.js" />"></script>
        <script src="<c:url value="/js/canvasjs.min.js" />"></script>
        <script src="<c:url value="/webjars/datatables/1.10.16/js/jquery.dataTables.min.js" /> "></script>
        <script src="<c:url value="/js/dataTables.fnMultiFilter.js" />"></script>
        <script src="<c:url value="/webjars/jquery-blockui/2.70/jquery.blockUI.js" /> "></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>
        <script src="<c:url value="/js/bootstrap-datetimepicker.min.js" />"></script>
        <script src="<c:url value="/js/jquery.cookie.js" />"></script>
        <script src="<c:url value="/js/alasql.min.js" />"></script> 
        <script src="<c:url value="/js/jquery-datatable-button/dataTables.buttons.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.flash.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/jszip.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/pdfmake.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/vfs_fonts.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.html5.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.print.min.js" />"></script>
        <script src="<c:url value="/js/param.check.js"/>"></script>
        <script src="<c:url value="/js/urlParamGetter.js"/>"></script>
        <script src="<c:url value="/js/jquery.fileDownload.js"/>"></script>
        <script src="<c:url value="/js/ajax-option-select-loader/babLine.loader.js"/>"></script>
        <script src="<c:url value="/js/ajax-option-select-loader/floor.loader.js"/>"></script>
        <script>
            $(function () {
                var lineType = getQueryVariable("lineType");
                var iframeUrl = "babMainSearch_1.jsp?t=1";
                if (lineType !== null) {
                    iframeUrl += "&lineType=" + lineType;
                }
                $("#frame1").attr("src", iframeUrl);
                var lastHeight = 400, $frame = $('iframe:eq(0)');
                setInterval(function () {
                    var curHeight = $frame.contents().find('body').height();
                    var newHeight = curHeight + 50;
                    lastHeight = lastHeight < newHeight ? newHeight : lastHeight;
                    $frame.css('height', lastHeight + 'px');
                }, 500);
            });

        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <!----->
        <div>
            <div>
                <iframe id="frame1" width="100%" frameborder="0" scrolling="no"></iframe> 
            </div>
        </div>

        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
