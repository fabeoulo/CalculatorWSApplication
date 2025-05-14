<%-- 
    Document   : modelSopRemark
    Created on : 2018/5/22, 上午 11:28:40
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<sec:authorize access="hasRole('ADMIN')"  var="isAdmin" />
<sec:authorize access="hasRole('OPER_MFG')"  var="isMfgOper" />
<sec:authorize access="hasRole('BACKDOOR_4876_')"  var="isBackDoor4876" />
<sec:authorize access="hasRole('OPER_IE')"  var="isIeOper" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />" />
        <link rel="stylesheet" href="<c:url value="/webjars/jquery-ui-themes/1.12.1/redmond/jquery-ui.min.css" />" >
        <link rel="stylesheet" href="<c:url value="/webjars/datatables/1.10.16/css/jquery.dataTables.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/buttons.dataTables.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/bootstrap-datetimepicker.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/select2.min.css" />">
        <style>
            #wigetCtrl{
                margin: 0 auto;
                width: 98%;
            }
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
            td.details-control {
                background: url('<c:url value="/images/details_open.png" />') no-repeat center center;
                cursor: pointer;
            }
            tr.shown td.details-control {
                background: url('<c:url value="/images/details_close.png" />') no-repeat center center;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/js/jquery-ui-1.10.0.custom.min.js" />"></script>
        <script src="<c:url value="/webjars/datatables/1.10.16/js/jquery.dataTables.min.js" /> "></script>
        <script src="<c:url value="/js/jquery-datatable-button/dataTables.buttons.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/jszip.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/pdfmake.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/vfs_fonts.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.html5.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.print.min.js" />"></script>
        <script src="<c:url value="/webjars/jquery-blockui/2.70/jquery.blockUI.js" /> "></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>
        <script src="<c:url value="/js/param.check.js"/>"></script>
        <script src="<c:url value="/js/dataTables.cellEdit.js"/>"></script>
        <script src="<c:url value="/js/select2.min.js"/>"></script>
        <script src="<c:url value="/js/jquery.fileDownload.js" />"></script>

        <script>
            ﻿$(function () {
                var table, typeTable;
                var tableRow, typeTableRow;
                var typeOptions = [];
                var lineTypeOptions = [];
                var standardTimeEditable = '${isAdmin || isMfgOper || isBackDoor4876 || isIeOper}';
                var typeMap = new Map();
                setTypeOptions();
                setLineTypeOptions();
                //This function will load the datatable
                loadTable();
                $(":text").on("keyup change", function () {
                    $(this).val($(this).val().toUpperCase());
                });
                //Hook up the click event for the save button on the add/edit popup window
                $("#AddRemarkButton").click(function () {
                    //validate that name and last name were entered

                    var modelName = $("#modelName").val();
                    var standardTime = $("#standardTime").val();
                    var moduleId = $("#preAssyModuleType\\.id").val();
                    var errorMsg = "";
                    if (!modelName) {
                        errorMsg += "\n* enter the modelName";
                    }

                    if (!moduleId) {
                        errorMsg += "\n* select the ModuleType";
                    }

                    if (!standardTime) {
//                        errorMsg += "\n* enter the standardTime";
                        standardTime = 0;
                    }

                    var stModifyDate;
                    if (tableRow && tableRow.standardTimeModifyDate) {
                        stModifyDate = moment(tableRow.standardTimeModifyDate).toISOString();
                    } else {
                        stModifyDate = moment().toISOString();
                    }

                    if (errorMsg != "") {
                        errorMsg = "The following errors were found: \n" + errorMsg + "\n\n Please enter the required information and try again.";
                        alert(errorMsg);
                    } else {
                        //JQuery ajax call
                        $.ajax({
                            method: "POST",
                            url: "<c:url value="/PreAssyModuleStandardTimeController/saveOrUpdate" />",
                            dataType: "html",
                            data: {
                                id: $("#currentID").val(),
                                modelName: modelName,
                                standardTime: standardTime,
                                "preAssyModuleType.id": moduleId,
                                sopName: $("#sopName").val(),
                                sopPage: $("#sopPage").val(),
                                "floor.id": ${user.floor.id},
                                standardTimeModifyDate: stModifyDate
                            },
                            success: function (response) {
                                if (response == "success") {
                                    table.ajax.reload(); //refresh the datatable to reflect the changes    
                                    $('#addEditRemark').modal('hide'); //hide the popup window
                                    alert(response);
                                }
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(xhr.responseText);
                            }
                        });
                    }
                });
                //Hook up the click event for the save button on the add/edit popup window
                $("#AddRemarkButton2").click(function () {
                    //validate that name and last name were entered

                    var baseModelName = $("#baseModelName").val();
                    var targetModelName = $("#targetModelName").val();
                    var errorMsg = "";
                    if (!baseModelName || !targetModelName) {
                        errorMsg += "\n* enter the modelName";
                    }

                    if (errorMsg != "") {
                        errorMsg = "The following errors were found: \n" + errorMsg + "\n\n Please enter the required information and try again.";
                        alert(errorMsg);
                    } else {
                        //JQuery ajax call
                        $.ajax({
                            method: "POST",
                            url: "<c:url value="/PreAssyModuleStandardTimeController/saveBySeries" />",
                            dataType: "html",
                            data: {
                                baseModelName: $("#baseModelName").val(),
                                targetModelName: $("#targetModelName").val()
                            },
                            success: function (response) {
                                if (response == "success") {
                                    table.ajax.reload(); //refresh the datatable to reflect the changes    
                                    $('#addEditRemark2').modal('hide'); //hide the popup window
                                    alert(response);
                                }
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(xhr.responseText);
                            }
                        });
                    }
                });
                //Hook up the click event for the save button on the add/edit popup window
                $("#AddRemarkButton3").click(function () {
                    //validate that name and last name were entered

                    var name = $("#preAssyModuleType\\.name").val();
                    var errorMsg = "";
                    if (!name) {
                        errorMsg += "\n* enter the modelName";
                    }

                    if (errorMsg != "") {
                        errorMsg = "The following errors were found: \n" + errorMsg + "\n\n Please enter the required information and try again.";
                        alert(errorMsg);
                    } else {
                        //JQuery ajax call
                        $.ajax({
                            method: "POST",
                            url: "<c:url value="/PreAssyModuleTypeController/saveOrUpdate" />",
                            dataType: "html",
                            data: {
                                id: $("#currentID2").val(),
                                name: name,
                                "lineType.id": $("#lineType\\.id").val()
                            },
                            success: function (response) {
                                if (response == "success") {
                                    typeTable.ajax.reload(); //refresh the datatable to reflect the changes    
                                    $('#addEditRemark3').modal('hide'); //hide the popup window
                                    alert(response);
                                }
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(xhr.responseText);
                            }
                        });
                    }
                });
                //hook up event for edit record buttons
                $(document).on('click', '#remark-info .EditButton', function (event) { //any element with the class EditButton will be handled here
                    var data = table.row($(this).parents('tr')).data();
                    clearForm($("#addEditRemark"));
                    $("#modelName").val(data.modelName);
                    $("#standardTime").val(data.standardTime);
                    $("#currentID").val(data.id);
                    $("#preAssyModuleType\\.id").val(data.preAssyModuleType.id);
                    $("#sopName").val(data.sopName);
                    $("#sopPage").val(data.sopPage);
                    $('#addEditRemark').modal('show');

                    tableRow = data;
                });
                //hook up event for edit record buttons
                $(document).on('click', '#preAssyModuleType-info .EditButton', function (event) { //any element with the class EditButton will be handled here
                    var data = typeTable.row($(this).parents('tr')).data();
                    clearForm($("#addEditRemark3"));
                    $("#currentID2").val(data.id);
                    $("#preAssyModuleType\\.name").val(data.name);
                    $("#lineType\\.id").val(data.lineType.id);
                    $('#addEditRemark3').modal('show');

                    typeTableRow = data;
                });
                if (standardTimeEditable == 'false') {
                    $("#standardTimeField").hide().attr("disabled", true);
                }

                function clearForm(dialog) {//blank the add/edit popup form
                    dialog.find(":text, textarea, input[type='number']").val("");
                    dialog.find("#currentID, #currentID2").val(0);
                    $(".worktimeLineType").val(-1).trigger("change");
                }

                function loadTable() {
                    console.log(1);
                    table = $('#remark-info').DataTable({
                        dom: 'Bfrtip',
                        "buttons": [
                            {
                                "text": 'Add',
                                "attr": {
                                    "data-toggle": "modal",
                                    "data-target": "#addEditRemark"
                                },
                                "action": function (e, dt, node, config) {
                                    clearForm($("#addEditRemark"));
                                    $('#addEditRemark').modal('toggle');
                                }
                            },
                            {
                                "text": 'AddSeries',
                                "attr": {
                                    "data-toggle": "modal",
                                    "data-target": "#addEditRemark2"
                                },
                                "action": function (e, dt, node, config) {
                                    clearForm($("#addEditRemark2"));
                                    $('#addEditRemark2').modal('toggle');
                                }
                            },
                            {
                                "text": 'Download data',
                                "attr": {
                                },
                                "action": function (e, dt, node, config) {
                                    var btn = $(".dt-button");
                                    $.fileDownload('<c:url value="/ExcelExportController/getPreAssyModuleStandardTimeSetting" />', {
                                        preparingMessageHtml: "We are preparing your report, please wait...",
                                        failMessageHtml: "No reports generated. No Survey data is available.",
                                        successCallback: function (url) {
                                            btn.attr("disabled", false);
                                        }
                                        , failCallback: function (html, url) {
                                            btn.attr("disabled", false);
                                        }
                                    });
                                }
                            }
                        ],
                        "processing": true,
                        "serverSide": false,
                        "ajax": {
                            "url": "<c:url value="/PreAssyModuleStandardTimeController/findAll" />",
                            "type": "GET"
                        },
                        "columnDefs": [
                            {
                                //this definition is set so the column with the action buttons is not sortable
                                "targets": -1, //this references the last column of the data
                                "orderable": false
                            },
                            {
                                "targets": [2],
                                'render': function (data, type, full, meta) {
                                    return typeOptions[data];
                                }
                            },
                            {
                                "type": "html",
                                "targets": 6,
                                "width": "10%",
                                'render': function (data, type, row) {
                                    if (data != null)
                                        return moment(data).format('YYYY-MM-DD HH:mm'); //YYYY-MM-DDTHH:mm:ss.SSS
                                    else
                                        return "";
                                }
                            }
                        ],
                        "columns": [
                            {data: "id", title: "id", visible: false},
                            {data: "modelName", title: "機種"},
                            {data: "preAssyModuleType.id", title: "模組種類"},
                            {data: "standardTime", title: "標工"},
                            {data: "sopName", title: "文件編號"},
                            {data: "sopPage", title: "頁數"},
                            {data: "standardTimeModifyDate", title: "工時更新"},
                            {
                                "data": "id",
                                "width": "20%",
                                "title": "action",
                                "render": function (data, type, full, meta) { //this column is redefinied to show the action buttons
                                    return '<div class="btn-toolbar"><button class="btn btn-sm btn-primary EditButton">Edit</button><button class="btn btn-sm btn-danger DeleteButton">Delete</button></div>';
                                }
                            }
                        ],
                        "pageLength": 20,
                        "order": [[6, 'desc'], [1, 'asc'], [2, 'asc']]
                    });
                    typeTable = $('#preAssyModuleType-info').DataTable({
                        dom: 'Bfrtip',
                        "buttons": [
                            {
                                "text": 'Add',
                                "attr": {
                                    "data-toggle": "modal",
                                    "data-target": "#addEditRemark3"
                                },
                                "action": function (e, dt, node, config) {
                                    clearForm($("#addEditRemark3"));
                                    $('#addEditRemark3').modal('toggle');
                                }
                            }
                        ],
                        "processing": true,
                        "serverSide": false,
                        "ajax": {
                            "url": "<c:url value="/PreAssyModuleTypeController/findAll" />",
                            "type": "GET"
                        },
                        "columnDefs": [
                            {//this definition is set so the column with the action buttons is not sortable
                                "targets": -1, //this references the last column of the data
                                "orderable": false
                            },
                            {
                                "targets": [2],
                                'render': function (data, type, full, meta) {
                                    return lineTypeOptions[data];
                                }
                            }
                        ],
                        "columns": [
                            {data: "id", title: "id", visible: false},
                            {data: "name", title: "模組"},
                            {data: "lineType.id", title: "製程"},
                            {
                                "data": "id",
                                "width": "20%",
                                "title": "action",
                                "render": function (data, type, full, meta) { //this column is redefinied to show the action buttons
                                    return '<div class="btn-toolbar"><button class="btn btn-sm btn-primary EditButton">Edit</button><button class="btn btn-sm btn-danger DeleteButton">Delete</button></div>';
                                }
                            }
                        ],
                        "pageLength": 20,
                        "order": [[1, 'asc']]
                    });
                }

                function setTypeOptions() {
                    var sel = $("#preAssyModuleType\\.id");
                    $.ajax({
                        url: "<c:url value="/PreAssyModuleTypeController/findAll" />",
                        type: "GET",
                        dataType: "json",
                        async: false,
                        success: function (response) {
                            var arr = response.data;
                            for (var i = 0, j = arr.length; i < j; i++) {
                                var obj = arr[i];
                                sel.append("<option value=" + obj.id + ">" + obj.name + "</option>");
                                typeOptions[obj.id] = obj.name;

                                var key = obj.lineType.id.toString();
                                var value = obj.name;
                                if (typeMap.has(key)) {
                                    typeMap.get(key).push(value);
                                } else {
                                    typeMap.set(key, [value]);
                                }
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            console.log(xhr.responseText);
                        }
                    });
                }

                function setLineTypeOptions() {
                    var sel = $("#lineType\\.id");
                    $.ajax({
                        url: "<c:url value="/PreAssyModuleTypeController/findLineTypeAll" />",
                        type: "GET",
                        dataType: "json",
                        async: false,
                        success: function (response) {
                            var arr = response.data;
                            for (var i = 0, j = arr.length; i < j; i++) {
                                var obj = arr[i];
                                sel.append("<option value=" + obj.id + ">" + obj.name + "</option>");
                                lineTypeOptions[obj.id] = obj.name;
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            console.log(xhr.responseText);
                        }
                    });
                }

                $(".worktimeLineType").on('change', function () {
                    var key = $(this).val();
                    var sel = $("#preAssyModuleType\\.id");
                    var selOpts = sel.find("option");

                    sel.val("");
                    var firstShownOption = null;

                    if (key === "-1") {
                        selOpts.each(function () {
                            $(this).show();
                            if (!firstShownOption) {
                                firstShownOption = $(this);
                            }
                        });
                    } else if (typeMap.has(key)) {
                        var moduleTypeTexts = typeMap.get(key);
                        selOpts.each(function () {
                            if (moduleTypeTexts.includes($(this).html())) {
                                $(this).show();
                                if (!firstShownOption) {
                                    firstShownOption = $(this);
                                }
                            } else {
                                $(this).hide();
                            }
                        });
                    } else {
                        selOpts.hide();
                    }

                    if (firstShownOption) {
                        sel.val(firstShownOption.val());
                    }
                });

                $(document).on('click', '#remark-info .DeleteButton', function (event) {
                    var data = table.row($(this).parents('tr')).data();
                    const modelName = data.modelName;
                    const moduleName = typeOptions[data.preAssyModuleType.id];

                    if (confirm('Delete ' + modelName + '/' + moduleName + '. Plese click OK to confirm.')) {
                        //load from database
                        $.ajax({
                            method: "POST",
                            url: "<c:url value="/PreAssyModuleStandardTimeController/delete" />",
                            dataType: "html",
                            data: {
                                id: data.id
                            },
                            success: function (response) {
                                if (response == "success") {
                                    table.ajax.reload();
                                    alert("Record deleted successfully.");
                                }
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(xhr.responseText);
                            }
                        });
                    }
                });
                $(document).on('click', '#preAssyModuleType-info .DeleteButton', function (event) {
                    var data = typeTable.row($(this).parents('tr')).data();
                    const moduleName = data.name;
                    const lineType = lineTypeOptions[data.lineType.id];

                    if (confirm('Delete ' + lineType + '/' + moduleName + '. Plese click OK to confirm.')) {
                        //load from database
                        $.ajax({
                            method: "POST",
                            url: "<c:url value="/PreAssyModuleTypeController/delete" />",
                            dataType: "html",
                            data: {
                                id: data.id
                            },
                            success: function (response) {
                                if (response == "success") {
                                    typeTable.ajax.reload();
                                    alert("Record deleted successfully.");
                                }
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(xhr.responseText);
                            }
                        });
                    }
                });
            });
        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <div id="wigetCtrl" class="container">

            <div class="row">
                <div class="alert">
                    <h3>Model前置模組工時維護</h3>
                </div>
                <table id="remark-info" class="table table-bordered display cell-border">
                </table>
                <hr />
            </div>

            <div class="row">
                <div class="control-label col-sm-12">
                    <div class="alert">
                        <h3>模組種類維護</h3>
                    </div>
                    <table id="preAssyModuleType-info" class="table table-bordered display cell-border">
                    </table>
                </div>
            </div>

            <!-- add/edit popup window -->
            <div class="modal fade" id="addEditRemark" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">                
                            <h4 class="modal-title" id="myModalLabel">Add/Edit Module Worktime</h4>
                        </div>
                        <div class="modal-body">
                            <fieldset>
                                <!-- Form Name -->
                                <!-- Text input-->
                                <table class="table table-sm table-striped">
                                    <tr>
                                        <td>機種</td>
                                        <td>
                                            <input id="modelName" name="modelName" type="text" class="form-control">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>模組種類</td>
                                        <td>
                                            <select id="preAssyModuleType.id" name="preAssyModuleType.id" class="form-control"></select>
                                        </td>
                                    </tr>
                                    <tr id="standardTimeField">
                                        <td>標工</td>
                                        <td>
                                            <input type="number" id="standardTime" name="standardTime" class="form-control">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>文件編號</td>
                                        <td>
                                            <input type="text" id="sopName" name="sopName" class="form-control">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>頁數</td>
                                        <td>
                                            <input type="text" id="sopPage" name="sopPage" class="form-control">
                                        </td>
                                    </tr>
                                </table>
                                <input type="hidden" id="currentID" value="" />                        
                            </fieldset>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" id="AddRemarkButton">Save changes</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>    
            <!-- end popup window -->

            <!-- add/edit popup window -->
            <div class="modal fade" id="addEditRemark2" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">                
                            <h4 class="modal-title" id="myModalLabel2">Add Modules</h4>
                        </div>
                        <div class="modal-body">
                            <fieldset>
                                <!-- Form Name -->
                                <!-- Text input-->
                                <table class="table table-sm table-striped">
                                    <tr>
                                        <td>base機種</td>
                                        <td>
                                            <input id="baseModelName" name="baseModelName" type="text" class="form-control">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>目標機種</td>
                                        <td>
                                            <input id="targetModelName" name="targetModelName" type="text" class="form-control">
                                        </td>
                                    </tr>
                                </table>                     
                            </fieldset>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" id="AddRemarkButton2">Save changes</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>    
            <!-- end popup window -->

            <!-- add/edit popup window -->
            <div class="modal fade" id="addEditRemark3" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">                
                            <h4 class="modal-title" id="myModalLabel3">Add/Edit Module</h4>
                        </div>
                        <div class="modal-body">
                            <fieldset>
                                <!-- Form Name -->
                                <!-- Text input-->
                                <table class="table table-sm table-striped">
                                    <tr>
                                        <td>名稱</td>
                                        <td>
                                            <input id="preAssyModuleType.name" name="preAssyModuleType.name" type="text" class="form-control">
                                        </td>
                                    </tr> 
                                    <tr>
                                        <td>製程</td>
                                        <td>
                                            <select id="lineType.id" name="lineType.id" class="form-control"></select>
                                        </td>
                                    </tr>
                                </table>       
                                <input type="hidden" id="currentID2" value="" />          
                            </fieldset>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" id="AddRemarkButton3">Save changes</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>    
            <!-- end popup window -->

        </div>
    </body>
</html>
