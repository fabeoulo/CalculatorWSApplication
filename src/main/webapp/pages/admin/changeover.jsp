<%-- 
    Document   : changeover
    Created on : 2017/3/28, 下午 02:38:22
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/tooltipster.bundle.min.css"/>" >
        <style>
            .table td, .table th {
                text-align: center;   
            }
            a.done{
                color: grey;
            }
            a.no-record, #log>.error{
                color: red;
            }
            a.process, #log>.active{
                color: green;
            }
            #log {
                background: white;
                margin: 0;
                padding: 0.5em 0.5em 0.5em 0.5em;
                position: fixed;
                left: 0px;
                bottom: 0px;
                overflow: auto;
                height: 100px;
                width: 600px;
                border-width: 3px;
                border-style: solid;
                border-color: #FFAC55;
            }
            #tableArea{
                overflow: auto;
                height: 4000px;
            }
            #calc-area {
                background: white;
                margin: 0;
                padding: 0.5em 0.5em 0.5em 0.5em;
                position: fixed;
                right: 10px;
                bottom: 0px;
                overflow: auto;
                height: 100px;
                width: 50%;
                border-width: 3px;
                border-style: solid;
                border-color: #FFAC55;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js" />"></script>
        <script src="<c:url value="/js/urlParamGetter.js" />"></script>
        <script src="<c:url value="/js/tooltipster.bundle.min.js"/>"></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>

        <script>
            $(function () {
                var sitefloor = getQueryVariable("sitefloor");
                var monitor = getQueryVariable("monitor");

                var msg = $("#sendMessage");
                var log = $("#log");

                if (sitefloor == null) {
                    alert("Sitefloor not found");
                    return;
                }

                var lineObj = getLine();

                for (var i = 0; i < lineObj.length; i++) {
                    var line = lineObj[i];
                    var lineTypeId = line.lineType.id;
                    var tableString =
                            "<div class='col-md-2'><table id=tb_" +
                            line.id +
                            " class='table table-bordered'><thead></thead><tbody></tbody></table></div>";
                    var areaSelector = "";
                    if (lineTypeId == 1) {
                        areaSelector = "#tableArea>#bab";
                    } else {
                        areaSelector = "#tableArea>#pkg";
                    }
                    $(areaSelector).append(tableString);
                    $(areaSelector + " #tb_" + line.id).find("thead").append("<tr><th>" + line.name + "</th></tr>");
                }

                $("#sendMessage").keydown(function (event) {
                    if (event.which == 13) {
                        event.preventDefault();
                        sendMessage($(this).val());
                        $(this).val(null);
                    }
                });

                $("#sync").click(function () {
                    websocket.send("sync");
                });

                $("#chatClear").click(function () {
                    log.html("");
                });

                $("#calc-submit").click(function () {
                    var po = $("#calc-po").val();
                    var resultArea = $("#calc-result");
                    var lineType = $("#calc-lineType").val();

                    $.ajax({
                        type: "Get",
                        url: "<c:url value="/SqlViewController/calculateChangeover" />",
                        data: {
                            po: po,
                            maxChangeover: 40,
                            lineType: lineType
                        },
                        dataType: "json",
                        success: function (response) {
                            var obj = response;
                            var resultStr = "";
                            resultStr += "<h5>機種: " + obj.modelName + " ";
                            resultStr += (lineType == 1 ? "組裝" : "包裝") + "標工(" + obj.worktime + ") ";
                            resultStr += "人數(" + obj.people + ") ";
                            resultStr += "最大換線(" + obj.maxChangeover + ")</h5>";
                            resultStr += "<h5>換算=(" + obj.worktime + "/" + obj.people + ")+";
                            resultStr += "(" + obj.maxChangeover + "/" + obj.people + ")=";
                            resultStr += obj.result + "</h5>";

                            resultArea.html(resultStr);
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            console.log(xhr.responseText);
                        }
                    });
                });

                init();

                $("#ws-wiget").toggle(monitor == "true");

                function appendLog(message, style) {
                    log.append("<div class='" + (style == null ? '' : style) + "'>" + message + "</div>");
                    log.scrollTop(log.prop("scrollHeight"));
                }

                function getLine() {
                    var obj;
                    $.ajax({
                        type: "Get",
                        url: "<c:url value="/BabLineController/findBySitefloorAndLineType" />",
                        data: {
                            floorName: sitefloor,
                            lineType_id: [1, 3]
                        },
                        dataType: "json",
                        async: false,
                        success: function (response) {
                            obj = response;
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            console.log(xhr.responseText);
                        }
                    });
                    return obj;
                }

                function init() {
                    console.log(1);
                    var hostname = window.location.host;//Get the host ipaddress to link to the server.
                    //var hostname = "172.20.131.52:8080";

                    //websocket will reconnect by reconnecting-websocket.min.js when client or server is disconnect

                    var clientId = $("#clientId").val();

                    websocket = new WebSocket("ws://" + hostname + "${pageContext.request.contextPath}/echo6");

                    websocket.onopen = function () {
                        appendLog("CONNECTED", "active");
                    };

                    websocket.onmessage = function (evt) {
                        var data = evt.data;
                        console.log(data);
                        try {
                            var jsonObject = JSON.parse(data);
                        } catch (e) {
                            appendLog(data);

                            return false;
                        }
                        var action = jsonObject.action;
                        var currentStatus = jsonObject.status;

                        appendLog("Receive data", "active");

                        if (action == 'init') {
                            $(".table > tbody").html("");
                            for (var i = 0; i < lineObj.length; i++) {
                                var line = lineObj[i];
                                if (line.id in currentStatus) {
                                    var tb = $("#tb_" + line.id);
                                    var tbhead = tb.find("thead");
                                    var tbbody = tb.find("tbody");
                                    var jsonArray = currentStatus[line.id];

                                    var prevBab = null;

                                    for (var j = 0; j < jsonArray.length; j++) {
                                        var bab = jsonArray[j];
                                        console.log(bab);
                                        var STATUS_PROCESSING = (bab.babStatus == null);
                                        var STATUS_FINISHED = (bab.babStatus != null);

                                        var diff;
                                        if (prevBab != null) {
                                            if (prevBab.lastUpdateTime != null) {
                                                var prevEndTime = moment(prevBab.lastUpdateTime);
                                                var currentStartTime = moment(bab.beginTime);
                                                if (prevEndTime.isAfter(currentStartTime)) {
                                                    diff = "00:00:00";
                                                } else {
                                                    diff = moment.utc(currentStartTime.diff(prevEndTime)).format("HH:mm:ss");
                                                }
                                            }
                                        }

                                        if (j < jsonArray.length && j != 0) {
                                            tbbody.append(
                                                    "<tr><td><span class='" + (bab.isused == 0 ? "process_arrow" : "") + " glyphicon glyphicon-arrow-down'><strong>" +
                                                    (diff == null ? "" : diff) +
                                                    "</strong></span></td></tr>"
                                                    );
                                            diff = null;
                                        }

                                        tbbody.append("<tr><td><a id='data_" + bab.line.id + j + "' class='" +
                                                (STATUS_PROCESSING ? "process" : "done") + "'>" +
                                                bab.modelName + (bab.ispre == 1 ? "(前置)" : "") + "</a></td></tr>");
                                        var babStatus = loopObjectParams(bab);
                                        if (bab.lastUpdateTime != null) {
                                            var startTime = moment(bab.beginTime);
                                            var endTime = moment(bab.lastUpdateTime);
                                            babStatus += "<p>Time processed: " + moment.utc(endTime.diff(startTime)).format("HH:mm:ss") + "</p>";
                                        }

                                        $("#data_" + bab.line.id + j).tooltipster({trigger: "hover", side: "right", contentAsHTML: true, updateAnimation: null, 'content': babStatus});

                                        prevBab = bab;
                                    }

                                    var notPreBabs = jsonArray.filter(b => b.ispre == 0);
                                    var lastBab = notPreBabs[notPreBabs.length - 1];
                                    if (lastBab != null && lastBab.babStatus != null) {
                                        tbbody.append("<tr class='suspend_col text-danger'><td><a class='text-danger'>閒置: </a><input type='hidden' class='interval_suspend_time' hidden value='" + lastBab.lastUpdateTime + "' /><a class='text-danger'></a></td></tr>");
                                    } else {
                                        tbbody.append("<tr class='col'><td><a>工單進行中...</a></td></tr>");
                                    }
                                }
                            }


                        } else if (action == 'update') {
                            sendMessage("sync");
                        }

                        if ($(".interval_suspend_time").length != 0) {
                            setInterval(function () {
                                $(".interval_suspend_time").each(function () {
                                    var suspendTime = moment($(this).val());

                                    var now = moment();
                                    $(this).next().html(moment.utc(now.diff(suspendTime)).format("HH:mm:ss"));
                                });
                            }, 1000);
                        }

                    };

                    websocket.onerror = function (evt) {
                        appendLog("ERROR", "error");
                    };

                }

                function sendMessage(message) {
                    websocket.send(message);
                }

                function loopObjectParams(target) {
                    var str = "<p>Po: " + target.po + "</p>" +
                            "<p>ModelName: " + target.modelName + "</p>" +
                            "<p>Line: " + target.line.id + "</p>" +
                            "<p>People: " + target.people + "</p>" +
                            "<p>StartTime: " + target.beginTime + "</p>" +
                            "<p>EndTime: " + (target.babStatus == null ? null : target.lastUpdateTime) + "</p>";
                    return str;
                }
            });

            window.onerror = function (msg, url, linenumber) {
                alert('Error message: ' + msg + '\nURL: ' + url + '\nLine Number: ' + linenumber);
                return true;
            };

            //window.addEventListener("load", init, false);
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div id="ws-wiget">
                    <input id="clientId" placeholder="id"/>
                    <input id="connect" type="button" value="connect">
                    <input id ="sendMessage" placeholder="message" />
                    <input type="button" id="sync" value="sync">
                    <input type="button" id="chatClear" value="chatClear">
                </div>
            </div>
            <div class="row">
                <div id="output"></div>
                <div id="log"></div>
                <div id="tableArea">
                    <div id="bab" class="col-md-12"></div>
                    <div class="col-md-12">
                        <hr />
                    </div>
                    <div id="pkg" class="col-md-12"></div>
                </div>
                <div id="calc-area">
                    <input id="calc-po" type="text" placeholder="請輸入工單"/>
                    <select id="calc-lineType" type="text">
                        <option value="1">Assy</option>
                        <option value="2">Packing</option>
                    </select>
                    <input id="calc-submit" type="button" value="確定"/>
                    <div id="calc-result"></div>
                </div>
            </div>
        </div>
    </body>
</html>
