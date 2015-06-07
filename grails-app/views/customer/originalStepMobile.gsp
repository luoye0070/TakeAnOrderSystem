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
</head>

<body>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">用餐点菜-第一步</h3>
    </div>

    <div class="panel-body">
        <form class="form-horizontal" method="POST" id="create_form" action="${createLink(controller: "customer",action: "getOrCreateOrder")}">
            <div class="form-group">
            <g:if test="${!isNeedPartakeCode}">
                <label class="col-sm-2 control-label" style="margin-top: 15px;">
                    请输入桌位编码后重试
                </label>
                <div class="col-sm-8" style="margin-top: 15px;">
                    <input type="number" class="form-control" name="code" id="code" placeholder="输入桌位编码"
                           value=""/>
                </div>

                %{--<div class="control-group">--}%
                    %{--<label class="control-label" style="font-size: 16px;font-weight: bolder;">--}%

                    %{--</label>--}%

                    %{--<div class="controls" style="font-size: 16px;font-weight: bolder;">--}%

                    %{--</div>--}%
                %{--</div>--}%

                %{--<div class="control-group">--}%
                    %{--<label class="control-label" for="code"><g:message code="tableInfo.code.label"--}%
                                                                       %{--default="Code"/><span--}%
                            %{--class="required-indicator">*</span></label>--}%

                    %{--<div class="controls">--}%
                        %{--<input type="text" style="width: 280px;" name="code" id="code"--}%
                               %{--value=""/>--}%
                    %{--</div>--}%
                %{--</div>--}%
            </g:if>
            <g:if test="${isNeedPartakeCode}">
                <label class="col-sm-2 control-label" style="margin-top: 15px;" for="partakeCode">
                    输入参与码参与点菜
                </label>
                <div class="col-sm-8" style="margin-top: 15px;">
                    <input type="number" class="form-control" name="partakeCode" id="partakeCode" placeholder="点菜参与码"
                           value=""/>
                </div>
            </g:if>

            <div class="col-sm-2" style="margin-top: 15px;">
                <input type="submit"
                       value="${message(code: 'default.button.next.label', default: 'Next')}"
                       class="form-control btn btn-primary"/>
            </div>

            %{--<div class="control-group">--}%
                %{--<label class="control-label"></label>--}%

                %{--<div class="controls">--}%
                    %{--<input type="submit" value="${message(code: 'default.button.next.label', default: 'Next')}"--}%
                           %{--class="btn send_btn"/>--}%
                %{--</div>--}%
            %{--</div>--}%
        </div>
        </form>
    </div>
</div>


%{--<div class="mc_main">--}%
    %{--<div class="mcm_top">--}%
        %{--<div class="mcm_top_name"><g:message code='restaurantInfo.create.label'/></div>--}%

        %{--<div class="mcm_top_banner"></div>--}%
    %{--</div>--}%

    %{--<div class="span10" style="margin-left: 10px;margin-top: 0px;">--}%
        %{--<g:render template="../layouts/msgs_and_errors"></g:render>--}%
    %{--</div>--}%

    %{--<div class="span11">--}%
        %{--<form class="form-horizontal" method="POST" id="create_form" action="${createLink(controller: "customer",action: "getOrCreateOrder")}">--}%
            %{--<g:if test="${!isNeedPartakeCode}">--}%
            %{--<div class="control-group">--}%
                %{--<label class="control-label" style="font-size: 16px;font-weight: bolder;">--}%

                %{--</label>--}%

                %{--<div class="controls" style="font-size: 16px;font-weight: bolder;">--}%
                    %{--请输入桌位编码后重试--}%
                %{--</div>--}%
            %{--</div>--}%

            %{--<div class="control-group">--}%
                %{--<label class="control-label" for="code"><g:message code="tableInfo.code.label"--}%
                                                                   %{--default="Code"/><span--}%
                        %{--class="required-indicator">*</span></label>--}%

                %{--<div class="controls">--}%
                    %{--<input type="text" style="width: 280px;" name="code" id="code"--}%
                           %{--value=""/>--}%
                %{--</div>--}%
            %{--</div>--}%
            %{--</g:if>--}%
            %{--<g:if test="${isNeedPartakeCode}">--}%
                %{--<div class="control-group">--}%
                    %{--<label class="control-label" for="partakeCode"><g:message code="orderInfo.partakeCode.label"--}%
                                                                       %{--default="Code"/><span--}%
                            %{--class="required-indicator">*</span></label>--}%

                    %{--<div class="controls">--}%
                        %{--<input type="text" style="width: 280px;" name="partakeCode" id="partakeCode"--}%
                               %{--value=""/>--}%
                    %{--</div>--}%
                %{--</div>--}%
            %{--</g:if>--}%

            %{--<div class="control-group">--}%
                %{--<label class="control-label"></label>--}%

                %{--<div class="controls">--}%
                    %{--<input type="submit" value="${message(code: 'default.button.next.label', default: 'Next')}"--}%
                           %{--class="btn send_btn"/>--}%
                %{--</div>--}%
            %{--</div>--}%

        %{--</form>--}%
    %{--</div>--}%
%{--</div>--}%
</body>
</html>