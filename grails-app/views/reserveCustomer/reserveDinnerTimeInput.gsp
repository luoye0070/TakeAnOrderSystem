<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 15-2-10
  Time: 下午11:11
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title></title>
    <style type="text/css">
    .mc_main {
        width: 1000px;
        height: auto;
        margin: 0px 50px;
        background-color: #FFFFFF;
        float: left;
    }

    .mcm_top {
        margin-top: 20px;
    }

    .mcm_top_name {
        font-size: 20px;
        font-weight: bold;
        padding: 10px 0px 10px 20px;
    }

    .mcm_top_banner {
        width: 100%;
        height: 4px;
        background: url('${resource(dir:"images",file:"login_banner.gif")}');
        margin: 0px auto;
        margin-bottom: 30px;
    }
    </style>
    <link type="text/css" href="${resource(dir: "js/dateTimePicker/css", file: "jquery-ui-1.8.17.custom.css")}" rel="stylesheet" />

    <link type="text/css" href="${resource(dir: "js/dateTimePicker/css", file: "jquery-ui-timepicker-addon.css")}" rel="stylesheet" />

    <script type="text/javascript" src="${resource(dir: "js/dateTimePicker/js", file: "jquery-1.7.1.min.js")}"></script>

    <script type="text/javascript" src="${resource(dir: "js/dateTimePicker/js", file: "jquery-ui-1.8.17.custom.min.js")}"></script>

    <script type="text/javascript" src="${resource(dir: "js/dateTimePicker/js", file: "jquery-ui-timepicker-addon.js")}"></script>

    <script type="text/javascript" src="${resource(dir: "js/dateTimePicker/js", file: "jquery-ui-timepicker-zh-CN.js")}"></script>

    <script type="text/javascript">
        $(function () {
            //日期选择器
//            $("#beginDate").datepicker({format: "yyyy-mm-dd"});
//            $("#endDate").datepicker({format: "yyyy-mm-dd"});

            $("#dinnerTime").datetimepicker({

                //showOn: "button",

                //buttonImage: "./css/images/icon_calendar.gif",

                //buttonImageOnly: true,

                showSecond: true,

                timeFormat: 'hh:mm:ss',

                stepHour: 1,

                stepMinute: 1,

                stepSecond: 1

            });
        });
    </script>
</head>

<body>
<div class="mc_main">
    <div class="mcm_top">
        %{--<div class="mcm_top_name"><g:message code='restaurantInfo.create.label'/></div>--}%

        %{--<div class="mcm_top_banner"></div>--}%

        <g:render template="../layouts/reserveCustomerMenu"></g:render>
    </div>

    <div class="span10" style="margin-left: 10px;margin-top: 0px;">
        <g:render template="../layouts/msgs_and_errors"></g:render>
    </div>

    <div class="span11">
        <g:form class="form-horizontal" method="POST" id="create_form" action="reserveTables">

                <div class="control-group">
                    <label class="control-label" for="dinnerTime">
                        预定时间<span
                            class="required-indicator">*</span></label>

                    <div class="controls">
                        <input type="text" style="width: 280px;" name="dinnerTime" id="dinnerTime"
                               value="${params.dinnerTime}"/>
                    </div>
                </div>

            <div class="control-group">
                <label class="control-label"></label>

                <div class="controls">
                    <input type="submit" value="${message(code: 'default.button.next.label', default: 'Next')}"
                           class="btn send_btn"/>
                </div>
            </div>

        </g:form>
    </div>
</div>
</body>
</html>