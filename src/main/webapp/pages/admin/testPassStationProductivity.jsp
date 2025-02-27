<%-- 
    Document   : babDetailInfo
    Created on : 2016/6/14, 下午 03:18:11
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<sec:authorize access="isAuthenticated()"  var="isAuthenticated" />
<sec:authorize access="hasRole('BACKDOOR_4876_')"  var="isBackDoor4876" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />">
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
            .title, .subTitle{
                display: inline !important;
            }
            .subTitle{
                color: red;
            }
            table td{
                text-align: center;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
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
        <script src="<c:url value="/js/param.check.js" />"></script>
        <script src="<c:url value="/js/countermeasure.js" />"></script>
        <script src="<c:url value="/js/pivot.min.js" />"></script>
        <script src="<c:url value="/js/jquery_pivot.js" />"></script>

        <script>
            var table;
            var lockDays = 14;
            $(function () {

                var momentFormatString = 'YYYY-MM-DD';
                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");

                var options = {
                    defaultDate: moment().subtract(1, "days"),
                    useCurrent: true,
                    //locale: "zh-tw",
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };

                var beginTimeObj = $('#fini').datetimepicker(options);
                var endTimeObj = $('#ffin').datetimepicker(options);

                beginTimeObj.on("dp.change", function (e) {
                    endTimeObj.data("DateTimePicker").minDate(e.date);
                    var beginDate = e.date;
                    var endDate = endTimeObj.data("DateTimePicker").date();
                    var dateDiff = endDate.diff(beginDate, 'days');
                    if (dateDiff > lockDays) {
                        endTimeObj.data("DateTimePicker").date(beginDate.add(lockDays, 'days'));
                    }
                });

                endTimeObj.on("dp.change", function (e) {
                    var beginDate = beginTimeObj.data("DateTimePicker").date();
                    var endDate = e.date;
                    var dateDiff = endDate.diff(beginDate, 'days');
                    if (dateDiff > lockDays) {
                        beginTimeObj.data("DateTimePicker").date(endDate.add(-lockDays, 'days'));
                    }
                });

                $(":text").keyup(function () {
                    $(this).val($(this).val().toUpperCase());
                });

                $("#send").click(function () {
                    getDetail();
                });

            });

            function getDetail() {
                $("#send").attr("disabled", true);

                $('#tb1').DataTable({
                    dom: 'Bfrtip',
                    buttons: [
                        'copy',
                        {
                            extend: 'excelHtml5',
                            footer: true
                        }
                    ],
                    fixedHeader: {
                        headerOffset: 50
                    },
                    "ajax": {
                        "url": "<c:url value="/SqlViewController/findTestPassStationProductivity" />",
                        "type": "Get",
                        data: {
                            startDate: $('#fini').val(),
                            endDate: $('#ffin').val()
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(xhr.responseText);
                        }
                    },
                    "columns": [
                        {data: "po", title: "工單"},
                        {data: "modelName", title: "機種"},
                        {data: "jobnumber", title: "工號"},
                        {data: "userName", title: "姓名"},
                        {data: "station", title: "站別"},
                        {data: "cnt", title: "數量"},
                        {data: "minD", title: "過站時間(min)"},
                        {data: "maxD", title: "過站時間(max)"},
                        {data: "standardTime", title: "標工"},
                        {data: "timeCost", title: "花費(秒)"},
                        {data: "productivity", title: "效率"}
                    ],
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": [6, 7],
                            'render': function (data, type, full, meta) {
                                return data == null ? 'n/a' : formatDate(data);
                            }
                        },
                        {
                            "type": "html",
                            "targets": [8, 9],
                            'render': function (data, type, full, meta) {
                                if (data == null) {
                                    return 'n/a';
                                } else {
                                    return data == 0 ? 0 : roundDecimal(data, 2);
                                }
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
                    displayLength: -1,
                    "processing": true,
                    "initComplete": function (settings, json) {
                        $("#send").attr("disabled", false);
                    },
                    filter: false,
                    destroy: true,
                    paginate: false,
                    "order": [[2, "asc"], [6, "asc"]]
                });

            }

            function formatDate(dateString) {
                return moment(dateString).format('YYYY-MM-DD HH:mm:ss');
            }

            function getPercent(val) {
                return roundDecimal((val * 100), 2) + '%';
            }

            function roundDecimal(val, precision) {
                var size = Math.pow(10, precision);
                return Math.round(val * size) / size;
            }

        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <div class="container-fluid">
            <div>
                <h3 class="title">MES測試過站查詢</h3>
                <h5 class="subTitle">※最多顯示14天紀錄</h5>
            </div>
            <div class="row form-inline">
                <div class="col form-group">
                    <label for="beginTime">日期: 從</label>
                    <div class='input-group date' id='beginTime'>
                        <input type="text" id="fini" placeholder="請選擇起始時間"> 
                    </div> 
                </div>
                <div class="col form-group">
                    <label for="endTime"> 到 </label>
                    <div class='input-group date' id='endTime'>
                        <input type="text" id="ffin" placeholder="請選擇結束時間"> 
                    </div>
                </div>
                <div class="col form-group">
                    <input type="button" id="send" value="確定(Ok)">
                </div>
            </div>

            <div class="row">
                <table id="tb1" class="table table-striped">
                </table>
            </div>
        </div>

        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
