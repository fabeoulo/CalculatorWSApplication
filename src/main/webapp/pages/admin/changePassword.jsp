<%-- 
    Document   : modelSopRemark
    Created on : 2018/5/22, 上午 11:28:40
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
            table td {
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            table {
                table-layout:fixed;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/jquery-blockui/2.70/jquery.blockUI.js" /> "></script>
        <script>
            ﻿$(function () {
                function savePass() {
                    var strongRegex = new RegExp("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.{6,})");
                    var pass = $("#pass").val();
                    var valid = pass == $("#passConfirm").val();
                    if (!valid) {
                        $("#error").html("Confirm password mismatch").show();
                        return;
                    }
                    if (!strongRegex.test(pass)) {
                        $("#error").html("密碼至少一個英文以及一個數字, 長度6").show();
                        return;
                    }
                    $.ajax({
                        method: "POST",
                        url: "<c:url value="/UserController/updatePassword" />",
                        dataType: "html",
                        data: {
                            password: pass,
                            oldpassword: $("#oldpass").val()
                        },
                        success: function (response) {
                            alert("success");
                            $("input").val("");
                            $("#error").hide();
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(xhr.responseText);
                        }
                    });
                }

                $("#submitBtn").click(savePass);
            });
        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <div id="wigetCtrl">
            <div class="container">
                <div class="row">
                    <div class="center-block">
                        <div class="col-md-6 col-md-offset-3">
                            <table class="table">
                                <tr>
                                    <td>
                                        <input id="oldpass" name="oldpassword" type="password" class="form-control" placeholder="請輸入舊密碼" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input id="pass" name="password" type="password" class="form-control" placeholder="請輸入新密碼"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input id="passConfirm" type="password" class="form-control" placeholder="請再次輸入新密碼"/>              
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span id="error" style="display:none">Password mismatch</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <button type="submit" class="form-control" id="submitBtn">Change Password</button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
