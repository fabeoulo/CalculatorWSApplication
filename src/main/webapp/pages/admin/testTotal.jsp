<%-- 
    Document   : test
    Created on : 2015/11/20, 上午 11:36:50
    Author     : Wei.Cheng
https://datatables.net/forums/discussion/20388/trying-to-access-rowdata-in-render-function-with-ajax-datasource-getting-undefined
Auto polling test record page by client.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />"/>
        <link rel="stylesheet" href="<c:url value="/webjars/datatables/1.10.16/css/jquery.dataTables.min.css" />">
        <style>
            .nodata{
                opacity: 0.2
            }
            #final_time {
                opacity: 0.5;
                position: fixed;
                right: 10px;
                bottom: 10px;
                padding: 5px 5px;
                font-size: 14px;
                background: #777;
                color: white;
            }
            #wigetCtrl{
                margin: 0 auto;
                width: 98%;
            }
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/datatables/1.10.16/js/jquery.dataTables.min.js" /> "></script>
        <script src="<c:url value="/js/jquery.cookie.js" /> "></script>
        <script>
            var maxProductivity = 200;
            $(document).ready(function () {

                Array.prototype.max = function () {
                    var max = this[0];
                    var len = this.length;
                    for (var i = 1; i < len; i++)
                        if (this[i] > max)
                            max = this[i];
                    return max;
                };

                var d = new Date();
                $("#final_time").text(d);//Get the final polling database time.
                var interval = null;//Polling database variable.
                var testtables = <spring:message code="test.maxTable" />;//測試table數量(空值要塞入null)

                //DataTable sort init.
                jQuery.fn.dataTableExt.oSort['pct-asc'] = function (x, y) {
                    x = parseFloat(x);
                    y = parseFloat(y);

                    return ((x < y) ? -1 : ((x > y) ? 1 : 0));
                };

                jQuery.fn.dataTableExt.oSort['pct-desc'] = function (x, y) {
                    x = parseFloat(x);
                    y = parseFloat(y);

                    return ((x < y) ? 1 : ((x > y) ? -1 : 0));
                };

                //測試table initialize.
                var table = $("#data").DataTable({
                    "processing": false,
                    "serverSide": false,
                    "ajax": {
                        "url": "<c:url value="/LineTypeFacadeController/findTestProcessResult" />",
                        "type": "GET",
                        "data": {"type": "type1"}
                    },
                    "columns": [
                        {data: "name"},
                        {data: "number"},
                        {data: "table"},
                        {data: "PRODUCTIVITY"},
                        {data: "isalarm"}
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: true,
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": 3,
                            'render': function (data, type, full, meta) {
                                var productivity = getPercent(data);
                                return (productivity > maxProductivity ? maxProductivity : productivity) + "%";
                            }
                        },
                        {
                            "type": "html",
                            "targets": 4,
                            'render': function (data, type, full, meta) {
                                if (data == 2) {
                                    return "異常";
                                }
                                return (data == 1 ? "<img src='../../images/red-light.jpg' width=20>" : "");
                            }
                        }
                    ],
                    "order": [[2, "asc"]],
                    "initComplete": function (settings, json) {
                        insertempty();
                    },
                    displayLength: -1,
                    lengthChange: false,
                    filter: true,
                    stateSave: true,
                    paginate: false
                });

                $.fn.dataTable.ext.errMode = function (settings, helpPage, message) {
                    console.log(message);
                };

                var countdownnumber = 8 * 60 * 60;
                var diff = 10;
                interval = setInterval(function () {
                    if (countdownnumber == 0) {
                        $("#final_time").text("您於此網頁停留時間過久，網頁自動更新功能已經關閉。");
                        clearInterval(interval);
                    } else {
                        table.ajax.reload(function () {
                            insertempty();
                        });
                        d = new Date();
                        $("#final_time").text(d);
                    }
                    countdownnumber -= diff;
                }, diff * 1000);

                $(window).unload(function () {
                    var cookies = $.cookie();
                    for (var cookie in cookies) {
                        $.removeCookie(cookie);
                    }
                });

                //後端丟出的map中把空隙塞入null值
                function insertempty() {
                    var obj = table.column(2).data().toArray();
                    for (var i = 1; i <= testtables; i++) {
                        if (obj.indexOf(i.toString()) === -1) {
                            table.rows.add([
                                {
                                    name: 'null',
                                    number: 'null',
                                    table: i,
                                    PRODUCTIVITY: 'null',
                                    isalarm: 'null'
                                }
                            ]).draw(false).nodes().to$().addClass("nodata");
                        }
                    }
                }

                function getPercent(val) {
                    return roundDecimal((val * 100), 0);
                }

                function roundDecimal(val, precision) {
                    var size = Math.pow(10, precision);
                    return Math.round(val * size) / size;
                }
            });
        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <div id="wigetCtrl">
            <h3>測試各站別狀態</h3><!----------------------------------------------->
            <div style="width: 50%; background-color: #F5F5F5">
                <div style="padding: 10px">
                    <table id="data" class="display" cellspacing="0" width="100%" style="text-align: center">
                        <thead>
                            <tr>
                                <th>姓名</th>
                                <th>工號</th>
                                <th>桌號</th>
                                <th>生產率</th>
                                <th>亮燈</th>
                            </tr>
                        </thead>

                        <tfoot>
                            <tr>
                                <th>姓名</th>
                                <th>工號</th>
                                <th>桌號</th>
                                <th>生產率</th>
                                <th>亮燈</th>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
        <div id="final_time"></div>
        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
