<%-- 
    Document   : chat
    Created on : 2016/1/8, 上午 08:54:31
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<sec:authorize access="isAuthenticated()"  var="isAuthenticated" />
<!DOCTYPE html>
<html>
    <head>
        <meta charset=UTF-8>
        <title>${initParam.pageTitle}</title>
        <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />">
        <link rel="stylesheet" href="<c:url value="/webjars/datatables/1.10.16/css/jquery.dataTables.min.css" />">
        <style>
            .non-padding{
                padding: 0 0;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js" />"></script>
        <script src="<c:url value="/js/reconnecting-websocket.min.js"/>"></script>
        <script src="<c:url value="/js/jquery.tablesorter.min.js"/>"></script> 
        <script src="<c:url value="/js/jquery.cookie.js" /> "></script>
        <script src="<c:url value="/webjars/datatables/1.10.16/js/jquery.dataTables.min.js" /> "></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>
        <script>
            $(function () {
                var holdHours = 0;
                var maxHoldHours = 8;
                initRefreshTimer();

                var sitefloor = ${param.sitefloor};

                var cookieTNum = $.cookie("user_sel");//cookie save the user selected 站別 in babpage.jsp，search when it is exist
                //Init the table when no data send from the server. (Datatype json, default "[]")
                var defaultstring = "<tr style='background-color:black'><td>n/a</td><td>n/a</td><td>n/a</td><td>n/a</td><td>n/a</td><td>n/a</td></tr>";

                var hostname = window.location.host;//Get the host ipaddress to link to the server.
                var table;

//--------------websocket functions
                //websocket will reconnect by reconnecting-websocket.min.js when client or server is disconnect
                var ws = new ReconnectingWebSocket("ws://" + hostname + "${pageContext.request.contextPath}/echo");
                ws.timeoutInterval = 3000;
                ws.onopen = function () {

                };

                //Get the server message and transform into table.
                ws.onmessage = function (message) {
                    if (table != undefined) {
                        table.fnDestroy();
                    }
                    $("#sen_table tbody").children().remove();
                    var json = message.data;
                    var arr = $.parseJSON(json);
                    var string = "";
                    if (arr.length == 0) {
                        string = defaultstring;
                    } else {
                        var string = "";
                        var flag = false;
                        for (var i = 0; i < arr.length; i++) {
                            var arrobj = arr[i];
                            if (sitefloor != arrobj.sitefloor) {
                                /*
                                 * Get user's station number in cookies, and show the Sensor data that matches.
                                 */
                                continue;
                            }
                            var diff = arrobj.diff;
                            string +=
                                    "<tr style='background-color:" + (diff != 0 ? "#008800" : "#CC0000") + "'>" +
                                    "<td>" + arrobj.tagName + "</td>" +
                                    "<td>" + (diff == 0 ? 1 : 0) + "</td>" +
                                    "<td>" + arrobj.barcode + "</td>" +
                                    "<td>" + (diff == 0 ? "計算中..." : diff + "秒") + "</td>" +
                                    "<td>" + arrobj.lastUpdateTime + "</td>" +
                                    "<td>" + arrobj.bab_id + "</td>" +
                                    "</tr>";
                            flag = true;
                        }
                        if (flag == false) {
                            string = defaultstring;
                        }
                    }
                    $("#sen_table tbody").html(string);
                    table = $("#sen_table").dataTable({
                        paging: false,
                        searching: false,
                        info: false,
                        order: [[0, 'asc']],
                        "columnDefs": [{"targets": [0, 1, 2, 3, 4], "orderable": false}],
                        "aoColumnDefs": [
                            {"sClass": "non-padding", "aTargets": [0, 1, 2, 3, 4]}
                        ]
                    });

                };
                ws.onerror = function (event) {
                    console.log("error");
                    console.log(event.data);
                };

                //generate the unnormal close event hint
                ws.onclose = function (event) {
                    var reason;
                    if (event.code == 1000)
                        reason = "Normal closure, meaning that the purpose for which the connection was established has been fulfilled.";
                    else if (event.code == 1001)
                        reason = "An endpoint is \"going away\", such as a server going down or a browser having navigated away from a page.";
                    else if (event.code == 1002)
                        reason = "An endpoint is terminating the connection due to a protocol error";
                    else if (event.code == 1003)
                        reason = "An endpoint is terminating the connection because it has received a type of data it cannot accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).";
                    else if (event.code == 1004)
                        reason = "Reserved. The specific meaning might be defined in the future.";
                    else if (event.code == 1005)
                        reason = "No status code was actually present.";
                    else if (event.code == 1006)
                        reason = "The connection was closed abnormally, e.g., without sending or receiving a Close control frame";
                    else if (event.code == 1007)
                        reason = "An endpoint is terminating the connection because it has received data within a message that was not consistent with the type of the message (e.g., non-UTF-8 [http://tools.ietf.org/html/rfc3629] data within a text message).";
                    else if (event.code == 1008)
                        reason = "An endpoint is terminating the connection because it has received a message that \"violates its policy\". This reason is given either if there is no other sutible reason, or if there is a need to hide specific details about the policy.";
                    else if (event.code == 1009)
                        reason = "An endpoint is terminating the connection because it has received a message that is too big for it to process.";
                    else if (event.code == 1010) // Note that this status code is not used by the server, because it can fail the WebSocket handshake instead.
                        reason = "An endpoint (client) is terminating the connection because it has expected the server to negotiate one or more extension, but the server didn't return them in the response message of the WebSocket handshake. <br /> Specifically, the extensions that are needed are: " + event.reason;
                    else if (event.code == 1011)
                        reason = "A server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.";
                    else if (event.code == 1015)
                        reason = "The connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate can't be verified).";
                    else
                        reason = "Unknown reason";
                    console.log("The connection was closed for reason: " + reason);
                };
                function postToServer() {
                }
                function closeConnect() {
                    ws.close();
                    console.log("websocket connection is now end");
                }
//-----------------------
                $("#redirectBtn").click(function () {
                    console.log("click");
                });
                
                //Redirect page when user stay on page too long.
                function initRefreshTimer() {
                    setInterval(function () {
                        holdHours++;
                        var now = moment().get('hour');
                        if (holdHours >= maxHoldHours || now == 22) {
                            parent.document.location.href = "<c:url value="SysInfo" />";
                        }
                    }, 60 * 60 * 1000);
                }
            });
        </script>
    </head>
    <body>
        <div id="servermsg" style="color:red; border: 2px"></div>
        <div id="sensorMsg">
            <table id="sen_table" class="table table-bordered" style="text-align: center;">
                <thead>
                    <tr>
                        <th>感應器代號</th>
                        <th>擋住(1) / 拿開(0)</th>
                        <th>組別</th>
                        <th>花費時間</th>
                        <th>動作時間</th>
                        <th>工單id</th>
                    </tr>
                </thead>
                <tbody style="color: white;">
                    <tr style='background-color:black'>
                        <td>n/a</td>
                        <td>n/a</td>
                        <td>n/a</td>
                        <td>n/a</td>
                        <td>n/a</td>
                        <td>n/a</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </body>
</html>
