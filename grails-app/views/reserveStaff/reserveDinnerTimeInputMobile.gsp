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
    <meta name="layout" content="staff_mobile_template"/>
    <title></title>
    <style type="text/css">
    </style>
    <link type="text/css" href="${resource(dir: "js/bootstrap-3.3.4/css", file: "bootstrap-datetimepicker.min.css")}" rel="stylesheet" />
    <script type="text/javascript" src="${resource(dir: "js/bootstrap-3.3.4/js", file: "bootstrap-datetimepicker.min.js")}" charset="UTF-8"></script>
    <script type="text/javascript" src="${resource(dir: "js/bootstrap-3.3.4/js/locales", file: "bootstrap-datetimepicker.zh-CN.js")}" charset="UTF-8"></script>
    <script type="text/javascript">
        $(function () {
            $('#dinnerTime').datetimepicker({
                format: 'yyyy-mm-dd hh:ii:ss'
            });
        });
    </script>
</head>

<body>

<!--提示消息-->
%{--<g:set var="errors" value="测试一个错误"/>--}%
<g:render template="/layouts/staff_mobile_msgs_and_errors"></g:render>
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

</body>
</html>