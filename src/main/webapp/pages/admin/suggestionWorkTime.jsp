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
        <link rel="stylesheet" href="<c:url value="/webjars/jquery-ui-themes/1.12.1/redmond/jquery-ui.min.css" />" >
        <link rel="stylesheet" href="<c:url value="/css/ie-worktime.css"/>">

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
        <script src="<c:url value="/js/jquery.fileDownload.js" />"></script>
        <script src="<c:url value="/js/jquery-ui-1.10.0.custom.min.js" />"></script>
        <script src="<c:url value="/js/ie-worktime.js"/>"></script>

        <script>
            var generateExcel = function (e) {
                var startDate = $('#fini').val();
                var endDate = $('#ffin').val();

                $(this).attr("disabled", true);
                $.fileDownload('<c:url value="/ExcelExportController/getSuggestionWorkTimeDetailExcel" />'
                        + '?startDate=' + startDate + '&endDate=' + endDate + '&lineTypeId=' + lineTypeId,
                        {
                            preparingMessageHtml: "We are preparing your report, please wait...",
                            failMessageHtml: "No reports generated. No Survey data is available.",
                            successCallback: function (url) {
                                $(this).attr("disabled", false);
                            }
                            , failCallback: function (html, url) {
                                $(this).attr("disabled", false);
                            }
                        }
                );
            };
        </script>
        <script>
            var table;
            const urlLineType = '${param.lineType}';
            const lineTypeId = (urlLineType === "Packing" ? 3 : urlLineType === "IDS" ? 11 : 1);
            var lockDays = 14;
            $(function () {

                var momentFormatString = 'YYYY-MM-DD';
                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");

                var options = {
                    defaultDate: moment(), //startOf('week') means Sunday is the first date. while startOf('isoWeek') means Monday
                    useCurrent: true,
                    //locale: "zh-tw",
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };

                var beginTimeObj = $('#fini').datetimepicker(options);
                var endTimeObj = $('#ffin').datetimepicker(options);

                let lastDayOfLastWeek = moment().startOf('isoWeek').subtract(1, 'days');
                let firstDayOfLastWeek = lastDayOfLastWeek.clone().startOf('isoWeek');

                beginTimeObj.on("dp.change", function (e) {
//                    endTimeObj.data("DateTimePicker").minDate(e.date);
//                    var beginDate = e.date;
//                    var endDate = endTimeObj.data("DateTimePicker").date();
//                    var dateDiff = endDate.diff(beginDate, 'days');
//                    if (dateDiff > lockDays) {
//                        endTimeObj.data("DateTimePicker").date(beginDate.add(lockDays, 'days'));
//                    }

                    $('#ffin').val(lastDayOfLastWeek.format('YYYY-MM-DD')).prop("hidden", true);
                    $('#fini').val(firstDayOfLastWeek.format('YYYY-MM-DD')).prop("readonly", true);
                });

                endTimeObj.on("dp.change", function (e) {
//                    var beginDate = beginTimeObj.data("DateTimePicker").date();
//                    var endDate = e.date;
//                    var dateDiff = endDate.diff(beginDate, 'days');
//                    if (dateDiff > lockDays) {
//                        beginTimeObj.data("DateTimePicker").date(endDate.add(-lockDays, 'days'));
//                    }

                    $('#ffin').val(lastDayOfLastWeek.format('YYYY-MM-DD')).prop("readonly", true);
                    $('#fini').val(firstDayOfLastWeek.format('YYYY-MM-DD')).prop("readonly", true);
                });

                $('#ffin').trigger("dp.change");

                $(":text").keyup(function () {
                    $(this).val($(this).val().toUpperCase());
                });

                $("#send").data("isProcessing", false);
                $("#send").click(function () {
                    if ($(this).data("isProcessing"))
                        return;
                    $(this).data("isProcessing", true);

                    getDetail();
                });

                setCustomButton();

//                for debug
//                $('#ffin').val(moment("2025-03-06").format('YYYY-MM-DD'));
//                $('#fini').val(moment("2025-03-06").format('YYYY-MM-DD'));
            });

            function getDetail() {
                $("#send").attr("disabled", true);
                setTb1();
                setTb2();
            }

            function setTb1() {
                var tableName = '工時下修';
                $('#tb1').DataTable({
                    dom: 'Bfrtip',
                    buttons: [
                        getDtCopyBtn(), getDtExcelBtn(tableName), btn_search
                    ],
                    fixedHeader: {
                        headerOffset: 50
                    },
                    "ajax": {
                        "url": "<c:url value="/SqlViewController/findSuggestionWorkTime" />",
                        "type": "Post",
                        data: {
                            startDate: $('#fini').val(),
                            endDate: $('#ffin').val(),
                            lineTypeId: lineTypeId
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(xhr.responseText);
                        }
                    },
                    "columns": [
                        {data: "modelName", title: "機種"},
                        {data: "lineBalance_avg", title: "平衡率"},
                        {data: "totalPcsSum", title: "累計數量"},
                        {data: "factor", title: "係數"},
                        {data: "timeCostSum", title: "累計工時", visible: false},
                        {data: "standardTime", title: "標工"},
                        {data: "extraTime", title: "線外工時", visible: false},
                        {data: "opTimeAdjust", title: "建議工時"},
                        {data: "diffTime", title: "差異", visible: false},
                        {data: "diffTimeFactor", title: "差異X係數", visible: false},
                        {data: "diffTimeAdjust", title: "調整量"},
                        {data: "suggestSt", title: "修正工時"},
                        {data: "flag", title: "決策項", visible: false},
                        {data: "flag", title: "決策"}
                    ],
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": [7],
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
                            "targets": [1],
                            'render': function (data, type, full, meta) {
                                return data == null ? 'n/a' : getPercent(data);
                            }
                        },
                        {
                            "className": "export-col",
                            "targets": [-2],
                            'render': function (data, type, full, meta) {
                                return data == 1 ? "下修" :
                                        "";
                            }
                        },
                        {
                            "type": "html",
                            "className": "not-export-col",
                            "targets": [-1],
                            'render': function (data, type, full, meta) {
                                let triangleClass =
                                        data == 1 ? "red-down-triangle-pseudo" :
                                        "";
                                return '<span class="' + triangleClass + '"></span>';
                            }
                        }
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    displayLength: 10,
                    "processing": true,
                    "initComplete": function (settings, json) {
                        $("#send").attr("disabled", false);
                        $("#send").data("isProcessing", false);
                        setTableCaption($('#tb1'), tableName);
                    },
                    filter: true,
                    destroy: true,
//                    paginate: false,
                    "order": []
                });
            }

            function setTb2() {
                var ajax = {
                    "url": "<c:url value="/SqlViewController/findUpwardWorkTime" />",
                    "type": "Post",
                    data: {
                        startDate: $('#fini').val(),
                        endDate: $('#ffin').val(),
                        lineTypeId: lineTypeId
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(xhr.responseText);
                    }
                };
                setTb2DataTable($('#tb2'), ajax);
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
                <h3 class="title">${param.lineType}工時建議統計</h3>
                <h5 class="subTitle">※每週更新日期</h5>
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
                    <input type="button" id="send" value="查詢">
                    <input type="button" id="dlExcel" value="下載明細" onclick="generateExcel()">
                </div>
            </div>

            <div class="container_table">
                <div class="row box_table">
                    <table id="tb1" class="table table-striped">
                    </table>
                </div>

                <div class="row box_table">
                    <table id="tb2" class="table table-striped">
                    </table>
                </div>
            </div>
        </div>

        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
