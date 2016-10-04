<%-- 
    Document   : chart
    Created on : 2016/2/16, 下午 02:26:52
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="cDAO" class="com.advantech.model.CountermeasureDAO" scope="application" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <link rel="stylesheet" href="../../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../../css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <link rel="stylesheet" href="//cdn.datatables.net/buttons/1.2.1/css/buttons.dataTables.min.css">
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
            .expDetail{
                width: 48%;
                float:right;
            }
            .ctrlDetail{
                width: 48%;
                float:left;
            }
            .chartContainer{
                height: 360px; /*限制高度不然會把footer給覆蓋掉*/
                width: 100%;
            }
            .exp{
                background-color: #bce8f1;
            }
            .ctrl{
                background-color: #dca7a7;
            }
            .search-container{
                background-color: wheat;
            }
            .detail{
                height: 650px;
            }
            #balanceCount{
                border:2px black solid;
                width:50%;
            }
            .balance{
                text-align: center;
                border: 1px black solid;
            }
            textArea{
                width: 100%;
                height: 300px;
                resize: none;
            }
            .modal-content table .lab{
                width: 120px;
            }
        </style>
        <script src="//www.gstatic.com/charts/loader.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="../../js/canvasjs.min.js"></script>
        <script src="../../js/jquery.dataTables.min.js"></script>
        <script src="../../js/jquery.blockUI.js"></script>
        <script src="../../js/moment.js"></script>
        <script src="../../js/bootstrap-datetimepicker.min.js"></script>
        <script src="../../js/jquery.cookie.js"></script>
        <script src="//cdn.jsdelivr.net/alasql/0.2/alasql.min.js"></script> 
        <script src="//cdn.datatables.net/buttons/1.2.1/js/dataTables.buttons.min.js"></script>
        <script src="//cdn.datatables.net/buttons/1.2.1/js/buttons.flash.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
        <script src="//cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/pdfmake.min.js"></script>
        <script src="//cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/vfs_fonts.js"></script>
        <script src="//cdn.datatables.net/buttons/1.2.1/js/buttons.html5.min.js"></script>
        <script src="//cdn.datatables.net/buttons/1.2.1/js/buttons.print.min.js"></script>
        <script src="../../js/param.check.js"></script>
        <script>
            var round_digit = 2;
            var historyTable;
            var actionCodes;
            var checkedErrorCodes;
            var checkedActionCodes;
            var checkBoxs;

            function getBAB() {
                var table = $("#data2").DataTable({
                    "processing": false,
                    "serverSide": false,
                    "ajax": {
                        "url": "../../GetTotal",
                        "type": "POST",
                        "data": {"type": "type2"}
                    },
                    "columns": [
                        {},
                        {data: "TagName"},
                        {data: "T_Num"},
                        {data: "groupid"},
                        {data: "diff"},
                        {data: "ismax"}
                    ],
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": 0,
                            "data": "TagName",
                            'render': function (data, type, row) {
                                return ((data == 'L1' || data == 'LA' || data == 'LB' || data == 'L3' || data == 'L4') ? "ASSY" : "PKG");
                            }
                        },
                        {
                            "type": "html",
                            "targets": 4,
                            'render': function (data, type, row) {
                                return data + '秒';
                            }
                        },
                        {
                            "type": "html",
                            "targets": 5,
                            'render': function (data, type, row) {
                                return (data == true ? "<img src='../../images/red-light.jpg' width=20>" : "");
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
                    "initComplete": function (settings, json) {
                        generateOnlineBabDetail(json);
                    },
                    "order": [[1, "asc"], [2, "asc"]]
                });
                return table;
            }

            function generateOnlineBabDetail(json) {
                $("#balanceCount").html("");
                var arr = json.data;
                if (arr == null || arr.length == 0) {
                    return false;
                }

                //balance節省麻煩由alasql做前端計算
                var res = alasql('SELECT PO, TagName, Model_name,\
                                          SUM(diff) / (COUNT(diff) * MAX(diff)) AS balance \
                                          FROM ? \
                                          GROUP BY PO, TagName, Model_name \
                                          ORDER BY TagName', [arr]);
                for (var i = 0; i < res.length; i++) {
                    $("#balanceCount").append(
                            '<p>線別: ' + res[i].TagName +
                            ' 工單號碼: ' + res[i].PO +
                            ' 機種: ' + res[i].Model_name +
                            ' 最後一台平衡率: ' + getPercent(res[i].balance, round_digit) +
                            "</p>");
                }
            }

            function getDetail(tableId, id, isused) {
                $(tableId).DataTable({
                    dom: 'Bfrtip',
                    buttons: [
                        'copy', 'csv', 'excel', 'pdf', 'print'
                    ],
                    "ajax": {
                        "url": "../../LineBalanceDetail",
                        "type": "POST",
                        "data": {
                            id: id,
                            isused: isused
                        }
                    },
                    "columns": [
                        {data: "BABid", visible: false},
                        {data: "groupid"},
                        {data: "balance"},
                        {data: "pass"}
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: true,
                    destroy: true,
                    ordering: true,
                    info: false,
                    bFilter: false,
                    lengthChange: false,
                    "processing": true,
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": 2,
                            'render': function (data, type, full, meta) {
                                return getPercent(data, round_digit);
                            }
                        },
                        {
                            "type": "html",
                            "targets": 3,
                            'render': function (data, type, full, meta) {
                                return data == 1 ? "是" : "否";
                            }
                        },
                        {
                            "sDefaultContent": "",
                            "aTargets": ["_all"]
                        }
                    ],
                    "order": [[1, "asc"]],
                    "footerCallback": function (row, data, start, end, display) {
                        var columnIndex = 3;
                        var api = this.api(), data;

                        // Remove the formatting to get integer data for summation
                        var intVal = function (i) {
                            return typeof i === 'number' ?
                                    (i == 0 ? 1 : i) : 0;
                        };

                        // Total over all pages
                        total = api
                                .column(columnIndex)
                                .data()
                                .reduce(function (a, b) {
                                    return a + intVal(b);
                                }, 0);

                        // Total over this page
                        passTotal = api
                                .column(columnIndex)
                                .data()
                                .reduce(function (a, b) {
                                    return a + (b == 1 ? 1 : 0);
                                }, 0);

                        failTotal = api
                                .column(columnIndex)
                                .data()
                                .reduce(function (a, b) {
                                    return a + (b == 0 ? 1 : 0);
                                }, 0);

                        // Update footer
                        $(api.column(columnIndex).footer()).html(
                                '<h5>達到標準: ' + passTotal + '組 (' + getPercent((passTotal / total), round_digit) + ')</h5>' +
                                '<h5>未達標準<code>(亮燈頻率)</code>: ' + failTotal + '組 (' + getPercent((failTotal / total), round_digit) + ')</h5>' +
                                '<h5>組別共計: ' + total + ' 組</h5>'
                                );
                    }
                });
            }

            function getChartData(id, isused, chartId) {
                $.ajax({
                    url: "../../GetSensorChart",
                    method: 'POST',
                    dataType: 'json',
                    data: {
                        id: id,
                        isused: isused
                    },
                    success: function (d) {
//                        var jsonObj = d;
                        var arr = d.data;

                        if (arr != null) {
                            for (var i = 0, j = arr.length; i < j; i++) {
                                var obj = arr[i];
                                obj.type = "line";
                                obj.lineThickness = 3;
                                obj.axisYType = "secondary";
                                obj.showInLegend = true;
                                obj.name = "站別" + (i + 1);
                            }
                        }
                        generateChart(d, chartId);
                    }
                });
            }

//http://canvasjs.com/docs/charts/how-to/hide-unhide-data-series-chart-legend-click/
            function generateChart(d, chartId) {
                var totalAvg = Math.round(d.avg);
//                console.log(totalAvg);
                var chart = new CanvasJS.Chart(chartId,
                        {
                            zoomEnabled: true,
                            animationEnabled: true,
                            exportEnabled: true,
                            exportFileName: "Range Area",
                            zoomType: "xy",
                            title: {
                                text: "各站各機台消耗時間(秒)"
                            },
                            axisY2: {
                                title: "時間",
                                valueFormatString: "0 sec",
                                interlacedColor: "#F5F5F5",
                                gridColor: "#D7D7D7",
                                tickColor: "#D7D7D7",
                                minimum: 0,
                                maximum: 600,
                                stripLines: [
                                    {
                                        value: totalAvg,
                                        label: totalAvg + "sec",
                                        labelPlacement: "outside",
                                        color: "orange",
                                        labelBackgroundColor: "black",
                                        labelFontStyle: "微軟正黑體",
                                        showOnTop: true
                                    }
                                ]
                            },
                            axisX: {
                                title: "台數",
                                intervalType: "number"
                            },
                            theme: "theme2",
                            toolTip: {
                                shared: true
                            },
                            legend: {
                                verticalAlign: "bottom",
                                horizontalAlign: "center",
                                fontSize: 15,
                                fontFamily: "Lucida Sans Unicode",
                                cursor: "pointer",
                                itemclick: function (e) {
                                    if (typeof (e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
                                        e.dataSeries.visible = false;
                                    } else {
                                        e.dataSeries.visible = true;
                                    }
                                    e.chart.render();
                                }
                            },
                            data: (d.data == null ? [] : d.data)
                        });
                isNoDataAvailable(chart);
                chart.render();
            }

            function isNoDataAvailable(chart) {
                var options = chart.options;
                if (!options.axisY)
                    options.axisY = {};

                if (options.data == 0) {
                    options.title.horizontalAlign = "center";
                    options.title.verticalAlign = "center";
                    options.title.text = 'No result';
                    options.axisY.maximum = 0;
                } else {
                    options.axisY.maximum = null;
                }
            }

            function getHistoryBAB() {
                var lineType = $("#lineType2").val();
                var sitefloor = $("#sitefloor").val();
                var startDate = $('#fini').val();
                var endDate = $('#ffin').val();
                var closedOnly = $("#closedOnly").is(":checked");

                var alarmPercentStandard = 0.3;

                var table = $("#babHistory").DataTable({
                    dom: 'lfrtip',
//                    buttons: [
//                        'copy', 'csv', 'excel', 'pdf', 'print'
//                    ],
                    "serverSide": false,
                    "ajax": {
                        "url": "../../AllBAB",
                        "type": "POST",
                        "data": {
                            lineType: lineType,
                            sitefloor: sitefloor,
                            startDate: startDate,
                            endDate: endDate,
                            closedOnly: closedOnly
                        }
                    },
                    "columns": [
                        {data: "id", visible: false},
                        {data: "PO"},
                        {data: "Model_name"},
                        {data: "lineName"},
                        {data: "sitefloor"},
                        {data: "people"},
                        {data: "isused"},
                        {data: "alarmPercent", "sType": "numeric-comma"},
                        {data: "btime"},
                        {}
                    ],
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": 6,
                            "data": "isused",
                            'render': function (data, type, row) {
                                switch (data) {
                                    case 1:
                                        return "已經關閉";
                                        break;
                                    case -1:
                                        return "沒有儲存紀錄";
                                        break;
                                    default:
                                        return "尚未關閉";
                                        break;
                                }
                            }
                        },
                        {
                            "type": "html",
                            "targets": 7,
                            "data": "isused",
                            'render': function (data, type, row) {
                                return roundDecimal(((data == null ? 0.0 : data) * 100), 2);
                            }
                        },
                        {
                            "type": "html",
                            "targets": 8,
                            'render': function (data, type, row) {
                                return formatDate(data);
                            }
                        },
                        {
                            "type": "html",
                            "targets": 9,
                            'render': function (data, type, row) {
                                var isCmReply = row.cm_id != null;
                                var isAboveApStandard = row.alarmPercent > alarmPercentStandard;
                                var isBabAvail = row.isused != -1;

                                if (!isBabAvail) {
                                    return "無紀錄";
                                } else if (!isAboveApStandard && row.alarmPercent != null) {
                                    return "達到標準";
                                } else if ((!isCmReply && isAboveApStandard)) {
                                    return "<input type='button' class='btn btn-danger btn-sm' data-toggle= 'modal' data-target='#myModal' value='檢視詳細' />";
                                } else if (isCmReply && isAboveApStandard) {
                                    return "<input type='button' class='btn btn-info btn-sm' data-toggle= 'modal' data-target='#myModal' value='檢視詳細' />";
                                }
                            }
                        }
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                    destroy: true,
                    stateSave: true,
                    "order": [[8, "desc"]]
                });
                return table;
            }

            function getPercent(val) {
                return roundDecimal((val * 100), round_digit) + '%';
            }

            function roundDecimal(val, precision) {
                var size = Math.pow(10, precision);
                return Math.round(val * size) / size;
            }

            function block() {
                $.blockUI({
                    css: {
                        border: 'none',
                        padding: '15px',
                        backgroundColor: '#000',
                        '-webkit-border-radius': '10px',
                        '-moz-border-radius': '10px',
                        opacity: .5,
                        color: '#fff'
                    },
                    fadeIn: 0
                    , overlayCSS: {
                        backgroundColor: '#FFFFFF',
                        opacity: .3
                    }
                });
            }

            function getBABCompare(BABid, Model_name, lineType, type) {
                $("#lineBalnHistory").show();
                $("#lineBalnHistory").DataTable({
                    "ajax": {
                        "url": "../../GetLineBalancingComparison",
                        "type": "POST",
                        "data": {
                            "BABid": BABid,
                            "Model_name": Model_name.trim(),
                            "lineType": lineType,
                            "type": type
                        }
                    },
                    "columns": [
                        {data: "Model_name", width: "150px"},
//                          已完結工單資訊(對照組)  
                        {data: "ctrl_id", visible: false},
                        {data: "ctrl_PO", width: "140px"},
                        {data: "ctrl_lineName", width: "140px"},
                        {data: "ctrl_alarmPercent", width: "140px"},
                        {data: "ctrl_btime", width: "100px"},
//                          最後一筆生產紀錄(實驗組)  
                        {data: "exp_id", visible: false},
                        {data: "exp_PO", width: "140px"},
                        {data: "exp_lineName", width: "140px"},
                        {data: "exp_alarmPercent", width: "140px"},
                        {data: "exp_btime", width: "100px"},
//                          狀態  
                        {data: 1, width: "40px"}
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": 4,
                            "data": "ctrl_alarmPercent",
                            'render': function (data, type, full, meta) {
                                return ((data == null || data == "") ? 0 : getPercent(data));
                            }
                        },
                        {
                            "type": "html",
                            "targets": 9,
                            "data": "exp_alarmPercent",
                            'render': function (data, type, full, meta) {
                                return ((data == null || data == "") ? 0 : getPercent(data));
                            }
                        },
                        {
                            "type": "html",
                            "targets": 11,
                            'render': function (data, type, full, meta) {

                                var ctrlPercent = full.ctrl_alarmPercent;
                                var expPercent = full.exp_alarmPercent;
                                if (full.exp_id == null || full.ctrl_id == null)
                                    return 'N/A';
                                else if (expPercent > ctrlPercent)
                                    return '變差';
                                else if (expPercent < ctrlPercent)
                                    return '變好';
                                else if (expPercent == ctrlPercent)
                                    return '持平';

                            }
                        },
                        {
                            "type": "html",
                            "targets": '_all',
                            'render': function (data, type, full, meta) {
                                return (data == null || data == "") ? 'N/A' : data;
                            }
                        }
                    ],
                    "createdRow": function (row, data, dataIndex) {
                        var closedAlarmPercent = data.ctrl_alarmPercent;
                        var lastAlarmPercent = data.exp_alarmPercent;
                        var lastBABid = data.ctrl_id;
                        if (closedAlarmPercent < lastAlarmPercent && lastBABid != null) {
                            $(row).addClass('alarm');
                        }

                    },
                    autoWidth: true,
                    destroy: true,
                    paging: false,
                    info: false,
                    bFilter: false,
                    "processing": true,
                    "initComplete": function (settings, json) {
                        var arrObj = json.data;
                        if (arrObj.length == 0) {
                            $("#totalDetail").hide();
                            return;
                        }
                        var jsonObj = arrObj[arrObj.length - 1];

                        var ctrlId = jsonObj.ctrl_id;
                        var ctrl_isused = jsonObj.ctrl_isused;

                        var expId = jsonObj.exp_id;
                        var exp_isused = jsonObj.exp_isused;

                        var ctrlAvgs = json.ctrlAvgs;
                        var expAvgs = json.expAvgs;

                        $("#totalDetail, #totalDetail > div").show();

                        getDetail("#ctrlDetail", ctrlId, ctrl_isused);
                        getChartData(ctrlId, ctrl_isused, "chartContainer1");

                        getDetail("#expDetail", expId, exp_isused);
                        getChartData(expId, exp_isused, "chartContainer2");

                        $("#ctrlBalance").html("線平衡率: " + getPercent(ctrlAvgs, round_digit));
                        $("#expBalance").html("線平衡率: " + getPercent(expAvgs, round_digit));
                    }
                });
            }

            function initDateTimePickerWiget() {
                var momentFormatString = 'YYYY-MM-DD';
                var options = {
                    defaultDate: moment(),
                    useCurrent: true,
                    maxDate: moment(),
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };
                var beginTimeObj = $('#fini').datetimepicker(options);
                var endTimeObj = $('#ffin').datetimepicker(options);

                beginTimeObj.on("dp.change", function (e) {
                    endTimeObj.data("DateTimePicker").minDate(e.date);
                });
            }

            function initCountermeasureDialog() {
                $(".modal-body #errorCon, #responseUser").html("N/A");
                $("input[name='errorCode']").prop("checked", false);
                $('input[name="actionCode"]').prop("checked", false);
                $(" #responseUser").html("");
                $(".modal-body :checkbox").attr("disabled", true);
                checkedErrorCodes = [];
                checkedActionCodes = [];
                setupCheckBox();
                showDialogMsg("");
            }

            function getContermeasure(BABid) {
                initCountermeasureDialog();

                $.ajax({
                    url: "../../CountermeasureServlet",
                    data: {
                        BABid: BABid,
                        action: "selectOne"
                    },
                    type: "POST",
                    dataType: 'json',
                    success: function (msg) {
                        var jsonData = msg;
                        $(".modal-body #errorCon").html(jsonData.solution.replace(/(?:\r\n|\r|\n)/g, '<br />'));

                        var errorCodes = msg.errorCodes;

                        for (var i = 0; i < errorCodes.length; i++) {
                            var obj = errorCodes[i];
                            checkedErrorCodes.push(obj.ec_id);
                            checkedActionCodes.push(obj.ac_id);
                        }

                        setErrorCodeCheckBox(checkedErrorCodes);
                        setupCheckBox();
                        setActionCodeCheckBox(checkedActionCodes);

                        $(":checkbox").attr("disabled", true);

                        var editors = jsonData.editors;
                        for (var i = 0; i < editors.length; i++) {
                            var editor = editors[i].editor;
                            $(".modal-body #responseUser").append("<span class='label label-default'>#" + (editor == null ? 'N/A' : editor) + "</span> ");
                        }
                        $("#editCountermeasure").attr("disabled", jsonData.lock == 1);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showDialogMsg(xhr.responseText);
                    }
                });
            }

            function setupCheckBox() {

                $("#actionCode").html("");
                var data = actionCodes.data;
                var array = $.map($('input[name="errorCode"]:checked'), function (c) {
                    return c.value;
                });

                for (var i = 0, j = array.length; i < j; i++) {
                    var id = array[i];
                    for (var k = 0, l = data.length; k < l; k++) {
                        if (data[k].ec_id == id) {
                            var checkboxObj = checkBoxs.clone();
                            checkboxObj.addClass("ec" + id);
                            checkboxObj.find(":checkbox").attr("value", data[k].id).after(data[k].name);
                            $("#actionCode").append(checkboxObj);
                        }
                    }
                }
            }

            function getActionCode() {
                var result;
                $.ajax({
                    url: "../../CountermeasureServlet",
                    data: {action: "getActionCode"},
                    type: "POST",
                    dataType: "json",
                    async: false,
                    success: function (response) {
                        result = response;
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.responseText);
                    }
                });
                return result;
            }

            function setErrorCodeCheckBox(errorCodes) {
                for (var i = 0; i < errorCodes.length; i++) {
                    $('input[name="errorCode"][value=' + errorCodes[i] + ']').prop("checked", true);
                }

            }

            function setActionCodeCheckBox(actionCodes) {
                for (var i = 0; i < actionCodes.length; i++) {
                    $('input[name="actionCode"][value=' + actionCodes[i] + ']').prop("checked", true);
                }
            }

            function counterMeasureModeUndo() {
                $("#saveCountermeasure, #undoContent").hide();
                $("#editCountermeasure").show();
            }

            function counterMeasureModeEdit() {
                $("#saveCountermeasure, #undoContent").show();
                $("#editCountermeasure").hide();
            }
            function formatDate(dateString) {
                return dateString.substring(0, 16);
            }

            function tableAjaxReload(tableObject) {
                tableObject.ajax.reload();
            }

            function showDialogMsg(msg) {
                $("#dialog-msg").html(msg);
            }

            //看使用者是否存在
            function checkUserExist(jobnumber) {
                var result;
                $.ajax({
                    type: "Post",
                    url: "../../CheckUser",
                    data: {
                        jobnumber: jobnumber
                    },
                    dataType: "json",
                    async: false,
                    success: function (response) {
                        result = response;
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showDialogMsg(xhr.responseText);
                    }
                });
                return result;
            }

            function saveCountermeasure(data) {
                $.ajax({
                    url: "../../CountermeasureServlet",
                    data: data,
                    type: "POST",
                    dataType: 'json',
                    success: function (msg) {
                        if (msg.data == true) {
                            counterMeasureModeUndo();
                            getContermeasure(data.BABid);
                            $("#searchAvailableBAB").trigger("click");
                            showDialogMsg("success");
                        } else {
                            showDialogMsg(msg.data);
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showDialogMsg(xhr.responseText);
                    }
                });
            }

            function unEntity(str) {
                return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
            }

            $(document).ready(function () {
                var interval = null;
                var countdownnumber = 30 * 60;
                var diff = 12;

                var table2 = getBAB();
                checkBoxs = $("#actionCode > div").detach();
                actionCodes = getActionCode();

                var saveModelName = $.cookie('lastPOInsert');
                var saveLineType = $.cookie('lastLineTypeSelect');
                if (saveModelName != null) {
                    $("#Model_name").val(saveModelName);
                    $("#lineType").val(saveLineType);
                }

                $("input, select").not(":checkbox").addClass("form-control");
                $(":button").addClass("btn btn-default");

                initCountermeasureDialog();
                initDateTimePickerWiget()

                //http://stackoverflow.com/questions/14493250/ajax-jquery-autocomplete-with-json-data
                $.ajax({
                    url: "../../GetAvailableModelName",
                    dataType: "json",
                    success: function (data) {
                        $("input#Model_name").autocomplete({
                            width: 300,
                            max: 10,
                            delay: 100,
                            minLength: 3,
                            autoFocus: true,
                            cacheLength: 1,
                            scroll: true,
                            highlight: false,
                            source: function (request, response) {
                                var term = $.trim(request.term);
                                var matcher = new RegExp('^' + $.ui.autocomplete.escapeRegex(term), "i");

                                response($.map(data, function (v, i) {
                                    var text = v;
                                    if (text && (!request.term || matcher.test(text))) {
                                        return {
                                            label: v,
                                            value: v
                                        };
                                    }
                                }));
                            }
                        });
                    }
                });


                $("#searchAvailableBAB").click(function () {
                    historyTable = getHistoryBAB();
                });

                var type;
                $("body").on('dblclick', '#babHistory tbody tr', function () {
                    type = "type2";
                    var selectData = historyTable.row(this).data();
                    var BABid = selectData.id;
                    var ModelName = selectData.Model_name;
                    var isused = selectData.isused;
                    var lineType = $("#lineType2").val();

                    if (isused == -1) {
                        alert("此筆記錄無統計數據。");
                        return;
                    }

                    block();

                    $("#Model_name").val(ModelName);
                    $("#lineType").val(lineType);

                    getBABCompare(BABid, ModelName, lineType, type);

                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        historyTable.$('tr.selected').removeClass('selected');
                        $(this).addClass('selected');
                    }

                });

                $("#send").on("click", function () {
                    type = "type1";
                    var modelName = $("#Model_name").val();
                    var lineType = $("#lineType").val();

                    if (modelName == null || modelName == "" || lineType == -1) {
                        return false;
                    }

                    $.cookie('lastPOInsert', modelName);
                    $.cookie('lastLineTypeSelect', lineType);

                    getBABCompare(null, modelName, lineType, type);
                });

                //edit counterMeasure 
                var editId;

                $("body").on('click', '#babHistory input[type="button"]', function () {

                    var selectData = historyTable.row($(this).parents('tr')).data();
                    editId = selectData.id;
//                    console.log(selectData.cm_id);
                    $("#myModal #titleMessage").html(
                            "號碼: " + editId +
                            " / 工單: " + selectData.PO +
                            " / 機種: " + selectData.Model_name +
                            " / 線別: " + selectData.lineName +
                            " / 時間: " + formatDate(selectData.btime)
                            );

                    if (selectData.cm_id == null) {
                        initCountermeasureDialog();
                    } else {
                        getContermeasure(selectData.id);
                    }
                });

                $('#myModal').on('shown.bs.modal', function () {
                    $(this).find('.modal-dialog').css({width: '90%',
                        height: 'auto',
                        'max-height': '100%'});
                });

                $("#saveCountermeasure, #undoContent").hide();

                var originErrorCon;
                var originResponseUser;

                $("#editCountermeasure").click(function () {
                    counterMeasureModeEdit();

                    $(":checkbox").removeAttr("disabled");

                    originErrorCon = $("#errorCon").html().replace(/<br *\/?>/gi, '\n');
                    originResponseUser = $("#responseUser").html();

                    $("#errorCon").html("<textarea id='errorConText' maxlength='500'>" + (originErrorCon == "N/A" ? "" : originErrorCon) + "</textarea>");
                    $("#responseUser").html("<input type='text' id='responseUserText' maxlength='30'>");

//                    console.log("editing");
                });

                $("#undoContent").click(function () {
                    if (!confirm("確定捨棄修改?")) {
                        return false;
                    }
                    counterMeasureModeUndo();

                    $(":checkbox").attr("disabled", true);

                    $("#errorCon").html(originErrorCon.replace(/(?:\r\n|\r|\n)/g, '<br />'));
                    $("#responseUser").html(originResponseUser);
                });

                $("#saveCountermeasure").click(function () {
                    if (confirm("確定修改內容?")) {
                        var editor = unEntity($("#responseUserText").val()),
                                solution = unEntity($("#errorConText").val());

                        var errorCodes = $.map($('input[name="errorCode"]:checked'), function (c) {
                            return c.value;
                        });

                        var actionCodes = $.map($('input[name="actionCode"]:checked'), function (c) {
                            return c.value;
                        });

                        if (checkVal(editor) == false || checkUserExist(editor) == false) {
                            showDialogMsg("找不到使用者，請重新確認您的工號是否存在");
                            return false;
                        } else if (errorCodes.length == 0) {
                            showDialogMsg("請選擇至少一項ErrorCode");
                            return false;
                        } else if (actionCodes.length == 0) {
                            showDialogMsg("請選擇至少一項ActionCode");
                            return false;
                        } else {
                            showDialogMsg("");
                        }

                        console.log(editor);
                        console.log(solution);
                        saveCountermeasure({
                            BABid: editId,
                            solution: solution,
                            errorCodes: errorCodes,
                            actionCodes: actionCodes,
                            editor: editor,
                            action: "update"
                        });
                    }

                });

                $('#myModal').on('hidden.bs.modal', function () {
                    counterMeasureModeUndo();
                });

                $("#lineType2").val("ASSY");
                $("#searchAvailableBAB").trigger("click");

//                Checkbox change event.
                $("#errorCode :checkbox").change(function () {
                    if (!$(this).is(":checked")) {
                        $("#actionCode").find(".ec" + $(this).val()).remove();
                    } else {
                        checkedActionCodes = $.map($('input[name="actionCode"]:checked'), function (c) {
                            return c.value;
                        });

                        setupCheckBox();
                        setActionCodeCheckBox(checkedActionCodes);
                        $("#actionCode" + " .ec" + $(this).val()).first().find(":checkbox").prop("checked", true);
                    }
                });

                $(document).on("change", "#actionCode :checkbox", function () {
                    if ($(this).is(":checked")) {
                        checkedActionCodes.push($(this).val());
                    } else {
                        var removeVal = $(this).val();
                        checkedActionCodes = jQuery.grep(checkedActionCodes, function (value) {
                            return value != removeVal;
                        });
                    }
//                    console.log(checkedActionCodes);
                });

                $("#generateExcel").click(function () {

                    $(this).attr("disabled", true);

                    var lineType = $('#lineType2').val();
                    var sitefloor = $('#sitefloor').val();
                    var startDate = $('#fini').val();
                    var endDate = $('#ffin').val();

                    var counter = 6;
                    var interval = setInterval(function () {
                        counter--;
                        $("#generateExcel").val("產出excel( " + counter + " 秒)");
                        // Display 'counter' wherever you want to display it.
                        if (counter == 0) {
                            // Display a login box
                            $("#generateExcel").attr("disabled", false).val("產出excel");
                            
                            clearInterval(interval);
                        }
                    }, 1000);

                    window.location.href = '../../BABExcelGenerate?startDate=' + startDate + '&endDate=' + endDate + '&lineType=' + lineType + '&sitefloor=' + sitefloor;
                });

                $("body").on("click", "#searchAvailableBAB, #send", function () {
                    block();
                });

                $(document).on("ajaxStop, ajaxComplete", function () {
                    $.unblockUI();
                });

                $.fn.dataTable.ext.errMode = function (settings, helpPage, message) {
                    console.log(message);
                };
            });

        </script>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <!----->
        <div class="wiget-ctrl form-inline">
            <div id="bab_currentStatus">
                <h3>組裝包裝各站別狀態</h3>
                <div style="width: 80%; background-color: #F5F5F5">
                    <div style="padding: 10px">
                        <table id="data2" class="display" cellspacing="0" width="100%" style="text-align: center">
                            <thead>
                                <tr>
                                    <th>製程</th>
                                    <th>線別</th>
                                    <th>站別</th>
                                    <th>組別</th>
                                    <th>秒數</th>
                                    <th>亮燈</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
            <div id="balanceCount">
            </div>
        </div>

        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 id="titleMessage" class="modal-title"></h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <table id="countermeasureTable" cellspacing="10" class="table table-bordered">
                                <tr>
                                    <td class="lab">Error Code</td>
                                    <td id="errorCode"> 
                                        <div class="checkbox">
                                            <c:forEach var="errorCode" items="${cDAO.getErrorCode()}">
                                                <label class="checkbox-inline">
                                                    <input type="checkbox" name="errorCode" value="${errorCode.id}">${errorCode.name}
                                                </label>
                                            </c:forEach>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lab">Action Code</td>
                                    <td id="actionCode"> 
                                        <div class="checkbox">
                                            <label class="checkbox-inline">
                                                <input type="checkbox" name="actionCode">
                                            </label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lab">說明</td>
                                    <td id="errorCon">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lab">填寫人</td>
                                    <td id="responseUser">
                                    </td>
                                </tr>
                            </table>
                            <div id="dialog-msg" class="alarm"></div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" id="editCountermeasure" class="btn btn-default" >Edit</button>
                        <button type="button" id="saveCountermeasure" class="btn btn-default">Save</button>
                        <button type="button" id="undoContent" class="btn btn-default">Undo</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>

        <hr />
        <!----->
        <div class="wiget-ctrl form-inline">
            <div id="bab_HistoryList">
                <h3>可查詢歷史紀錄</h3>
                <p class="alarm">※雙擊表格內的內容可直接於下方帶出資料。</p>
                <p class="alarm">※亮燈頻率標準為30%。</p>
                <div class="search-container">
                    <div class="ui-widget">
                        <select id="lineType2"> 
                            <option value="ASSY">ASSY</option>
                            <option value="Packing">Packing</option>
                        </select> /
                        <select id="sitefloor">
                            <option value=5>5F</option>
                            <option value=6>6F</option>
                        </select> /

                        日期:從
                        <div class='input-group date' id='beginTime'>
                            <input type="text" id="fini" placeholder="請選擇起始時間"> 
                        </div> 
                        到 
                        <div class='input-group date' id='endTime'>
                            <input type="text" id="ffin" placeholder="請選擇結束時間"> 
                        </div>

                        <input type="button" id="searchAvailableBAB" value="查詢">
                        <input type="button" id="generateExcel" value="產出excel">

                    </div>
                </div>
                <div style="width: 90%; background-color: #F5F5F5">
                    <div style="padding: 10px">
                        <table id="babHistory" class="display" cellspacing="0" width="100%" style="text-align: center">
                            <thead>
                                <tr>
                                    <th>id</th>
                                    <th>工單</th>
                                    <th>機種</th>
                                    <th>線別</th>
                                    <th>樓層</th>
                                    <th>人數</th>
                                    <th>紀錄flag</th>
                                    <th>亮燈頻率(%)</th>
                                    <th>投入時間</th>
                                    <th>異常回覆</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <hr />
        <!----->
        <div class="wiget-ctrl form-inline">
            <h3>機種平衡率紀錄查詢</h3>
            <div class="search-container">
                <div class="ui-widget">
                    <label for="Model_name">請輸入機種號碼: </label>
                    <input type="text" id="Model_name" />
                    <select id="lineType">
                        <option value=-1>請選擇線別</option>
                        <option value="ASSY">ASSY</option>
                        <option value="Packing">Packing</option>
                    </select>
                    <input type="button" id="send" value="查詢">
                </div>
            </div>

            <div id="serverMsg"></div>
            <div>
                <table id="lineBalnHistory" class="table table-bordered" hidden>
                    <thead>
                        <tr>
                            <th>機種</th>
                            <th class="exp">上次生產Id</th>
                            <th class="exp">上次生產<br/>工單</th>
                            <th class="exp">上次生產<br/>線別</th>
                            <th class="exp">上次生產<br/>亮燈頻率</th>
                            <th class="exp">投入時間</th>
                            <th class="ctrl">本次生產Id</th>
                            <th class="ctrl">本次生產<br/>工單</th>
                            <th class="ctrl">本次生產<br/>線別</th>
                            <th class="ctrl">本次生產<br/>亮燈頻率</th>
                            <th class="ctrl">投入時間</th>
                            <th>狀態</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
        <!----->
        <div class="wiget-ctrl">
            <div id="totalDetail" hidden>
                <s><p class="alarm">※下列圖表，組別被跳過的因為是異常資料(重複資料)而會被排除。</p></s>

                <div class="ctrlDetail">
                    <div id="ctrlBalance" class="balance">
                    </div>
                    <h3>上次生產</h3>
                    <div class="detail">
                        <table id="ctrlDetail" class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>BABid</th>
                                    <th>組別</th>
                                    <th>平衡率</th>
                                    <th>是否合格</th>
                                </tr>
                            </thead>
                            <tfoot>
                                <tr>
                                    <th colspan="3" style="text-align:right">Total:</th>
                                    <th></th>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                    <div id="chartContainer1" class="chartContainer">
                    </div>
                </div>

                <div class="expDetail">
                    <div id="expBalance" class="balance">
                    </div>
                    <h3>本次生產</h3>
                    <div class="detail">
                        <table id="expDetail" class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>BABid</th>
                                    <th>組別</th>
                                    <th>平衡率</th>
                                    <th>是否合格</th>
                                </tr>
                            </thead>
                            <tfoot>
                                <tr>
                                    <th colspan="3" style="text-align:right">Total:</th>
                                    <th></th>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                    <div id="chartContainer2" class="chartContainer">
                    </div>
                </div>
            </div>
        </div>
        <div style="clear: both"></div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
