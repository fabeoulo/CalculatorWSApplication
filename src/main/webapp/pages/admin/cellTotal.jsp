
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<sec:authorize access="isAuthenticated()"  var="isAuthenticated" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />"/>
        <link rel="stylesheet" href="<c:url value="/webjars/jquery-ui-themes/1.12.1/redmond/jquery-ui.min.css" />" >
        <link rel="stylesheet" href="<c:url value="/css/bootstrap-datetimepicker.min.css" />">
        <link rel="stylesheet" href="<c:url value="/webjars/datatables/1.10.16/css/jquery.dataTables.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/fixedHeader.dataTables.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/buttons.dataTables.min.css" />">
        <style>
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
            table th{
                text-align: center;
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
        <script src="<c:url value="/js/urlParamGetter.js" />"></script>
        <script src="<c:url value="/js/param.check.js" />"></script>
        <script>
            var maxProductivity = 200;
            var table;
            $(function () {

                var momentFormatString = 'YYYY-MM-DD';
                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");
                var options = {
                    useCurrent: true,
                    //locale: "zh-tw",
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };

                //以免把時間區間定在六日沒有資料，此狀況會在moment是禮拜一時發生
                var prevDay = moment().add(-2, "days");
                if (prevDay.day() == 6 || prevDay.day() == 7) {
                    prevDay.add(-2, "days");
                }

                options.defaultDate = prevDay;
                $('#fini').datetimepicker(options);
                options.defaultDate = moment();
                $('#ffin').datetimepicker(options);

                $("#send").click(function () {

                    var startDate = $('#fini').val();
                    var endDate = $('#ffin').val();

                    getDetail(startDate, endDate);
                });

                $("body").on('click', '#cellDetail tbody tr', function () {
                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        $('#cellDetail tr.selected').removeClass('selected');
                        $(this).addClass('selected');
                    }
                });

                var jobnumber = getQueryVariable("jobnumber");
                if (jobnumber != null) {
                    $("#send").trigger("click");
                    table.search(decodeURIComponent(jobnumber)).draw();
                }

                /*
                 Remark dialog area
                 */

                var originRemark;
                var originResponseUser;
                var editId;

                $("body").on("click", ".testRecord-remark", function () {
                    var d = table.row($(this).parents('tr')).data();
                    editId = d.id;
                    var modal = $($(this).attr("data-target"));
                    modal.find(".modal-title").html(
                            "號碼: " + editId +
                            " / 工號: " + d.userId +
                            " / 人名: " + d.userName +
                            " / 效率: " + d.productivity +
                            " / 桌次: " + (d == null ? "n/a" : d.testTable.name) +
                            " / 時間: " + d.lastUpdateTime
                            );

                    if (d.replyStatus == "UNREPLIED") {
                        initRemarkDialog();
                    } else {
                        getRemark(editId);
                    }
                });

                $("#editRemark").click(function () {
                    remarkModeEdit();

                    originRemark = $("#remark").html().replace(/<br *\/?>/gi, '\n');
                    originResponseUser = $("#responseUser").html();

                    $("#remark").html("<textarea id='remarkText' maxlength='500'>" + (originRemark == "N/A" ? "" : originRemark) + "</textarea>");

                    $("#responseUser").html("<input type='text' id='responseUserText' maxlength='30' value='" + '${isAuthenticated ? user.jobnumber : null}' + "' readonly disabled>");
                });

                $("#undoContent").click(function () {
                    if (!confirm("確定捨棄修改?")) {
                        return false;
                    }
                    remarkModeUndo();

                    $(".modal-body").attr("disabled", true);
                    $("#remark").html(originRemark.replace(/(?:\r\n|\r|\n)/g, '<br />'));
                    $("#responseUser").html(originResponseUser);
                });

                $("#saveRemark").click(function () {
                    if (confirm("確定修改內容?")) {

                        var editor = unEntity($("#responseUserText").val()),
                                remark = unEntity($("#remarkText").val());

                        if (checkVal(editor) == false) {
                            showDialogMsg("找不到使用者，請重新確認您的工號是否存在");
                            return false;
                        } else {
                            showDialogMsg("");
                        }
                        saveRemark({
                            recordId: editId,
                            remark: remark
                        });
                    }

                });

                if (${!isAuthenticated}) {
                    $("#editRemark").off("click").attr("disabled", true);
                    $("#remarkEditHint").show();
                }

                $('#myModal').on('hidden.bs.modal', function () {
                    remarkModeUndo();
                });
            });

            function getDetail(startDate, endDate) {

                table = $("#cellDetail").DataTable({
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
                        "url": "<c:url value="/CellStationRecordController/findByDate" />",
                        "type": "GET",
                        data: {
                            startDate: startDate,
                            endDate: endDate,
                            unReplyOnly: $("#unReplyOnly").is(":checked")
                        }
                    },
                    "columns": [
                        {data: "id", visible: true},
                        {data: "userId"},
                        {data: "userName", visible: true},
                        {data: "productivity", visible: false},
                        {
                            data: "cellStation",
                            render: function (data, type, row) {
                                return data == null ? "n/a" : data.name;
                            }
                        },
                        {data: "lastUpdateTime"},
                        {
                            visible: false,
                            data: "replyStatus",
                            render: function (data, type, row) {
                                if (data == 'UNREPLIED') {
                                    return "<input type='button' class='testRecord-remark btn btn-danger btn-sm' data-toggle= 'modal' " +
                                            "data-target='#myModal' value='檢視詳細' />";
                                } else if (data == "REPLIED") {
                                    return "<input type='button' class='testRecord-remark btn btn-info btn-sm' "+
                                            " data-toggle= 'modal' data-target='#myModal' value='檢視詳細' />";
                                } else {
                                    return data;
                                }
                            }
                        }
                    ],
                    "columnDefs": [
                        {
                            "targets": 3,
                            'render': function (data, type, full, meta) {
                                var productivity = getPercent(data);
                                return (productivity > maxProductivity ? maxProductivity : productivity) + "%";
                            }
                        },
                        {
                            "targets": 5,
                            'render': function (data, type, full, meta) {
                                return formatDate(data);
                            }
                        }
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: false,
                    displayLength: -1,
                    lengthChange: false,
                    filter: true,
                    info: true,
                    paginate: false,
                    destroy: true,
                    "initComplete": function (settings, json) {
                        $("#cellDetail").show();
                    },
                    "order": [[5, "asc"], [1, "asc"]]
                });
            }

            function formatDate(dateString) {
                return dateString.substring(0, 16);
            }

            function getPercent(val) {
                return roundDecimal((val * 100), 0);
            }

            function roundDecimal(val, precision) {
                var size = Math.pow(10, precision);
                return Math.round(val * size) / size;
            }

            function initRemarkDialog() {
                $(".modal-body #remark, #responseUser").html("N/A");
                $("#responseUser").html("");
                showDialogMsg("");
                remarkModeUndo();
            }

            function getRemark(row_id) {
                initRemarkDialog();

                $.ajax({
                    url: "<c:url value="/CellStationRecordController/findRemark" />",
                    data: {
                        recordId: row_id
                    },
                    type: "GET",
                    dataType: 'json',
                    success: function (msg) {
                        var jsonData = msg;
                        $(".modal-body #remark").html(jsonData.remark.replace(/(?:\r\n|\r|\n)/g, '<br />'));

                        var lastEditor = jsonData.user;
                        $(".modal-body #responseUser").append("<span class='label label-default'>#" +
                                (lastEditor == null ? 'N/A' : lastEditor.usernameCh) + "</span> ");
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showDialogMsg(xhr.responseText);
                    }
                });
            }

            function saveRemark(data) {
                $.ajax({
                    url: "<c:url value="/CellStationRecordController/updateRemark" />",
                    data: data,
                    type: "POST",
                    dataType: 'json',
                    success: function (msg) {
                        remarkModeUndo();
                        getRemark(data.recordId);
                        $("#send").trigger("click");
                        showDialogMsg("success");
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showDialogMsg(xhr.responseText);
                    }
                });
            }

            function showDialogMsg(msg) {
                $("#dialog-msg").html(msg);
            }

            function remarkModeUndo() {
                $("#saveRemark, #undoContent").hide();
                $("#editRemark").show();
            }

            function remarkModeEdit() {
                $("#saveRemark, #undoContent").show();
                $("#editRemark").hide();
            }

            //隔離特殊字元
            function unEntity(str) {
                return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
            }

        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />

        <div class="container">
            <h3>Cell站別登入紀錄</h3>

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
                                <table id="remarkTable" cellspacing="10" class="table table-bordered">
                                    <tr>
                                        <td class="lab">效率異常備註</td>
                                        <td id="remark" >
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="lab">最後修改人員</td>
                                        <td id="responseUser" >
                                        </td>
                                    </tr>
                                </table>
                                <div id="dialog-msg" class="alarm"></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <label id="remarkEditHint" for="editRemark" hidden="">請<a href="<c:url value="/login" /> " target='_parent'>登入</a>做異常回覆</label>
                            <button type="button" id="editRemark" class="btn btn-default" >Edit</button>
                            <button type="button" id="saveRemark" class="btn btn-default">Save</button>
                            <button type="button" id="undoContent" class="btn btn-default">Undo</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>

                </div>
            </div>

            <div>
                <table class="table">
                    <tr>
                        <td>
                            <div class="form-group form-inline">
                                日期:從
                                <div class='input-group date' id='beginTime'>
                                    <input type="text" id="fini" placeholder="請選擇起始時間"> 
                                </div> 
                                到 
                                <div class='input-group date' id='endTime'>
                                    <input type="text" id="ffin" placeholder="請選擇結束時間"> 
                                </div>
                                <div class="checkbox" style="display: none;">
                                    <input type="checkbox" id="unReplyOnly" /><label for="unReplyOnly">只顯示異常未維護</label>
                                </div>
                                <input type="button" id="send" value="確定(Ok)">
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <table id="cellDetail" class="display" cellspacing="0" width="100%" style="text-align: center" hidden>
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>人員工號</th>
                            <th>人員姓名</th>
                            <th>效率</th>
                            <th>桌次</th>
                            <th>儲存時間</th>
                            <th>ReplyStatus</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>

        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
