<%-- 
    Document   : babDetailInfo
    Created on : 2016/6/14, 下午 03:18:11
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />">
        <link rel="stylesheet" href="<c:url value="/webjars/jquery-ui-themes/1.12.1/redmond/jquery-ui.min.css" />" >
        <link rel="stylesheet" href="<c:url value="/css/bootstrap-datetimepicker.min.css"/>">
        <link rel="stylesheet" href="<c:url value="/webjars/datatables/1.10.16/css/jquery.dataTables.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/fixedHeader.dataTables.min.css"/>">
        <link rel="stylesheet" href="<c:url value="/css/buttons.dataTables.min.css"/>">
        <style>
            body{
                font-size: 16px;
                padding-top: 70px;
            }
            .wiget-ctrl{
                width: 98%;
                margin: 5px auto;
            }
            table th{
                text-align: center;
            }
            .alarm{
                color:red;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/js/jquery-ui-1.10.0.custom.min.js" />"></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>
        <script src="<c:url value="/js/bootstrap-datetimepicker.min.js" />"></script>
        <script src="<c:url value="/webjars/datatables/1.10.16/js/jquery.dataTables.min.js" /> "></script>
        <script src="<c:url value="/js/dataTables.fixedHeader.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/dataTables.buttons.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.flash.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/jszip.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/pdfmake.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/vfs_fonts.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.html5.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.print.min.js" />"></script>
        <script src="<c:url value="/js/urlParamGetter.js"/>"></script>
        <script src="<c:url value="/js/ajax-option-select-loader/babLineType.loader.js" />"></script>
        <script src="<c:url value="/js/ajax-option-select-loader/floor.loader.js" />"></script>

        <script>
            var round_digit = 2;
            lineTypeLoaderUrl = "<c:url value="/BabLineController/findLineType" />";
            floorLoaderUrl = "<c:url value="/FloorController/findAll" />";

            $(function () {
                setLineObject();
                initOptions($("#lineType"));
                initFloorOptions($("#sitefloor"));

                var urlLineType = getQueryVariable("lineType");
                if (urlLineType == null) {
                    window.location.href = 'SysInfo';
                    return false;
                }

                var urlSitefloor = getQueryVariable("sitefloor") + "F";

                var datePickerLockDays = 7;
                var momentFormatString = 'YYYY-MM-DD';
                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");
                var options = {
                    defaultDate: moment().subtract(1, 'days'),
                    useCurrent: true,
                    //locale: "zh-tw",
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };
                var beginTimeObj = $('#fini').datetimepicker(options);
                var endTimeObj = $('#ffin').datetimepicker(options);

                var table;

                $("#send").click(function () {
                    var lineType_id = $('#lineType').val();
                    var floor_id = $('#sitefloor').val();

                    var startDate = beginTimeObj.data("DateTimePicker").date();
                    var endDate = endTimeObj.data("DateTimePicker").date();

                    $("input").attr("disabled", true);
                    table = $("#table1").DataTable({
                        dom: 'Bfrtip',
                        buttons: [
                            'copy', 'excel', 'print'
                        ],
                        "processing": true,
                        "serverSide": false,
                        fixedHeader: {
                            headerOffset: 50
                        },
                        "ajax": {
                            "url": "<c:url value="/SqlViewController/findBabPreAssyProductivity" />",
                            "type": "Get",
                            data: {
                                lineType_id: lineType_id,
                                floor_id: floor_id,
                                startDate: startDate == null ? null : startDate.format(momentFormatString),
                                endDate: endDate == null ? null : endDate.format(momentFormatString)
                            }
                        },

                        "columns": [
                            {data: "id"},
                            {data: "po"},
                            {data: "modelName"},
                            {data: "line_name"},
                            {data: "people"},
                            {data: "btime"},
                            {data: "lastUpdateTime"},
                            {data: "pcs"},
                            {data: "preAssyTime"},
                            {data: "time_period"},
                            {data: "productivity"}
                        ],
                        "columnDefs": [
                            {
                                "type": "html",
                                "targets": [5, 6],
                                'render': function (data, type, full, meta) {
                                    return data == null ? 'n/a' : formatDate(data);
                                }
                            },
                            {
                                "type": "html",
                                "targets": [7],
                                'render': function (data, type, full, meta) {
                                    return data == -1 ? 'n/a' : data;
                                }
                            },
                            {
                                "type": "html",
                                "targets": [10],
                                'render': function (data, type, full, meta) {
                                    return data == null ? 'n/a' : getPercent(data);
                                }
                            }
                        ],
                        "oLanguage": {
                            "sLengthMenu": "顯示 _MENU_ 筆記錄",
                            "sZeroRecords": "無符合資料",
                            "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                        },
                        bAutoWidth: true,
                        displayLength: -1,
                        lengthChange: false,
                        filter: false,
                        info: false,
                        paginate: false,
                        destroy: true,
                        "initComplete": function (settings, json) {
                            $("input").removeAttr("disabled");
                        },
                        "order": [[0, "asc"]]
                    });
                });

                $("#lineType").val($('#lineType option:contains(' + urlLineType + ')').val());
                $("#sitefloor").val($('#sitefloor option:contains(' + urlSitefloor + ')').val());
            });

            function formatDate(dateString) {
                return moment(dateString).format('YYYY-MM-DD hh:mm:ss');
            }

            function getPercent(val) {
                return roundDecimal((val * 100), round_digit) + '%';
            }

            function roundDecimal(val, precision) {
                var size = Math.pow(10, precision);
                return Math.round(val * size) / size;
            }
        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <div class="container form-inline">
            <div style="width:100%">
                <h3>前置工單明細查詢</h3>
                <table id="leaveRequest" class="table">
                    <tr>
                        <td>
                            <div class="form-group form-inline">
                                <select id="lineType"> 
                                    <option value="-1">請選擇製程</option>
                                </select>
                                <select id="sitefloor">
                                    <option value="-1">請選擇樓層</option>
                                </select>
                                日期:從
                                <div class='input-group date' id='beginTime'>
                                    <input type="text" id="fini" placeholder="請選擇起始時間"> 
                                </div> 
                                到 
                                <div class='input-group date' id='endTime'>
                                    <input type="text" id="ffin" placeholder="請選擇結束時間"> 
                                </div>
                                <input type="button" id="send" value="確定(Ok)">
                            </div>
                        </td>
                    </tr>
                </table>
                <div style="width:100%">
                    <table id="table1" class="display" cellspacing="0" width="100%" style="text-align: center" >
                        <thead>
                            <tr>
                                <th>系統工單id</th>
                                <th>po</th>
                                <th>modelName</th>
                                <th>線別</th>
                                <th>人數</th>
                                <th>起始時間</th>
                                <th>結束時間</th>
                                <th>pcs</th>
                                <th>標工(分)</th>
                                <th>花費(秒)</th>
                                <th>效率</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <hr />

                <div id="serverMsg"></div>
            </div>
        </div>
        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
