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
    <meta name="layout" content="customer_mobile_template"/>
    <title></title>
    <style type="text/css">
    </style>
    <link type="text/css" href="${resource(dir: "js/bootstrap-3.3.4/css", file: "bootstrap-datetimepicker.min.css")}" rel="stylesheet" />
    <script type="text/javascript" src="${resource(dir: "js/bootstrap-3.3.4/js", file: "bootstrap-datetimepicker.min.js")}" charset="UTF-8"></script>
    <script type="text/javascript" src="${resource(dir: "js/bootstrap-3.3.4/js/locales", file: "bootstrap-datetimepicker.zh-CN.js")}" charset="UTF-8"></script>
    <script type="text/javascript">
        $(function () {
            $('#dinnerTime').datetimepicker({
                language:  'zh-CN',
                autoclose: 1,
                todayBtn:  1,
                format: 'yyyy-mm-dd hh:ii:ss'
            });
        });
    </script>
</head>

<body>


<h4 style="margin-top: 0px;">桌位预定</h4>
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">第一步-输入预定时间</h3>
    </div>

    <div class="panel-body">
        <g:form class="form-horizontal" method="POST" id="create_form" action="reserveTables">

            <div class="form-group">
                <label class="col-sm-2 control-label" for="dinnerTime" style="margin-top: 15px;">
                    预定时间<span>*</span></label>

                <div class="col-sm-8" style="margin-top: 15px;">
                    <input type="text" class="form-control" name="dinnerTime" id="dinnerTime" readonly
                           value="${params.dinnerTime}"/>
                </div>
                <div class="col-sm-2" style="margin-top: 15px;">
                    <input type="submit" value="${message(code: 'default.button.next.label', default: 'Next')}"
                           class="form-control btn btn-primary"/>
                </div>
            </div>

        </g:form>
    </div>
</div>


%{--<div class="mc_main">--}%
    %{--<div class="mcm_top">--}%
        %{--<div class="mcm_top_name"><g:message code='restaurantInfo.create.label'/></div>--}%

        %{--<div class="mcm_top_banner"></div>--}%

        %{--<g:render template="../layouts/reserveCustomerMenu"></g:render>--}%
    %{--</div>--}%

    %{--<div class="span10" style="margin-left: 10px;margin-top: 0px;">--}%
        %{--<g:render template="../layouts/msgs_and_errors"></g:render>--}%
    %{--</div>--}%

    %{--<div class="span11">--}%
        %{--<g:form class="form-horizontal" method="POST" id="create_form" action="reserveTables">--}%

                %{--<div class="control-group">--}%
                    %{--<label class="control-label" for="dinnerTime">--}%
                        %{--预定时间<span--}%
                            %{--class="required-indicator">*</span></label>--}%

                    %{--<div class="controls">--}%
                        %{--<input type="text" style="width: 280px;" name="dinnerTime" id="dinnerTime"--}%
                               %{--value="${params.dinnerTime}"/>--}%
                    %{--</div>--}%
                %{--</div>--}%

            %{--<div class="control-group">--}%
                %{--<label class="control-label"></label>--}%

                %{--<div class="controls">--}%
                    %{--<input type="submit" value="${message(code: 'default.button.next.label', default: 'Next')}"--}%
                           %{--class="btn send_btn"/>--}%
                %{--</div>--}%
            %{--</div>--}%

        %{--</g:form>--}%
    %{--</div>--}%
%{--</div>--}%
</body>
</html>