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
<h4 style="margin-top: 0px;">桌位预定</h4>
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">第二步-选择预定桌位和输入预定信息</h3>
    </div>

    <div class="panel-body">
        <div class="col-sm-12">
            <a href="${createLink(controller: "reserveCustomer", action: "reserveDinnerTimeInput", params: [dinnerTime: params.dinnerTime])}">返回时间选择</a>
        </div>

        <g:form class="form-horizontal" method="POST" id="create_form" action="createReserveOrder">
            <g:if test="${tableInfoList}">
                <input type="hidden" name="dinnerTime" id="dinnerTime"
                       value="${params.dinnerTime}"/>

                <div class="form-group">
                    <label class="col-sm-3 control-label" style="margin-top: 15px;">
                        桌位<span>*</span></label>
                    <div class="col-sm-9">
                        <g:each in="${tableInfoList}" var="tableInfo">
                            <g:if test="${tableInfo.canUse}">
                                <div class="col-sm-3" style="margin-top: 15px;">
                                    <div class="form-control">
                                        <input class="" type="radio" name="tableId" id="tableId${tableInfo.tableInfo?.id}"
                                               value="${tableInfo.tableInfo?.id}" ${(lj.Number.toLong(params.tableId) == tableInfo.tableInfo?.id) ? "checked='checked'" : ""}/>
                                        <label for="tableId${tableInfo.tableInfo?.id}">
                                            ${tableInfo.tableInfo?.name}(${tableInfo.tableInfo?.minPeople}-${tableInfo.tableInfo?.maxPeople}人)
                                        </label>
                                    </div>
                                </div>
                            </g:if>
                        </g:each>
                    </div>

                    <label class="col-sm-3 control-label" for="personCount" style="margin-top: 15px;">
                        用餐人数<span>*</span></label>
                    <div class="col-sm-9" style="margin-top: 15px;">
                        <input type="number" name="personCount" id="personCount" class="form-control"
                               value="${params.personCount}"/>
                    </div>

                    <label class="col-sm-3 control-label" for="phone" style="margin-top: 15px;">
                        联系电话<span>*</span></label>
                    <div class="col-sm-9" style="margin-top: 15px;">
                        <input type="number" name="phone" id="phone" class="form-control"
                               value="${params.phone}"/>
                    </div>

                    <label class="col-sm-3 control-label" for="customerName" style="margin-top: 15px;">
                        联系人<span></span></label>
                    <div class="col-sm-9" style="margin-top: 15px;">
                        <input type="text" name="customerName" id="customerName" class="form-control"
                               value="${params.customerName}"/>
                    </div>

                    <label class="col-sm-3 control-label" for="remark" style="margin-top: 15px;">
                        备注<span></span></label>
                    <div class="col-sm-9" style="margin-top: 15px;">
                        <input type="text" name="remark" id="remark" class="form-control"
                               value="${params.remark}"/>
                    </div>

                    <div class="col-sm-12" style="margin-top: 15px;">
                        <input type="submit" value="${message(code: 'default.button.next.label', default: 'Next')}" class="form-control btn btn-primary"/>
                    </div>

                </div>
            </g:if>
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

    %{--<a href="${createLink(controller: "reserveCustomer", action: "reserveDinnerTimeInput", params: [dinnerTime: params.dinnerTime])}">返回时间选择</a>--}%

    %{--<div class="span11">--}%
        %{--<g:form class="form-horizontal" method="POST" id="create_form" action="createReserveOrder">--}%
            %{--<g:if test="${tableInfoList}">--}%
                %{--<input type="hidden" name="dinnerTime" id="dinnerTime"--}%
                       %{--value="${params.dinnerTime}"/>--}%

                %{--<div class="control-group">--}%
                    %{--<label class="control-label">--}%
                        %{--桌位<span--}%
                            %{--class="required-indicator">*</span></label>--}%
                    %{--<g:each in="${tableInfoList}" var="tableInfo">--}%
                        %{--<g:if test="${tableInfo.canUse}">--}%
                            %{--<div class="controls">--}%
                                %{--<input type="radio" name="tableId" id="tableId${tableInfo.tableInfo?.id}"--}%
                                       %{--value="${tableInfo.tableInfo?.id}" ${(lj.Number.toLong(params.tableId) == tableInfo.tableInfo?.id) ? "checked='checked'" : ""}/> ${tableInfo.tableInfo?.name}--}%
                            %{--</div>--}%
                        %{--</g:if>--}%
                    %{--</g:each>--}%
                %{--</div>--}%

                %{--<div class="control-group">--}%
                    %{--<label class="control-label" for="personCount">--}%
                        %{--用餐人数<span--}%
                            %{--class="required-indicator">*</span></label>--}%

                    %{--<div class="controls">--}%
                        %{--<input type="text" style="width: 280px;" name="personCount" id="personCount"--}%
                               %{--value="${params.personCount}"/>--}%
                    %{--</div>--}%
                %{--</div>--}%

                %{--<div class="control-group">--}%
                    %{--<label class="control-label" for="phone">--}%
                        %{--联系电话<span--}%
                            %{--class="required-indicator">*</span></label>--}%

                    %{--<div class="controls">--}%
                        %{--<input type="text" style="width: 280px;" name="phone" id="phone"--}%
                               %{--value="${params.phone}"/>--}%
                    %{--</div>--}%
                %{--</div>--}%

                %{--<div class="control-group">--}%
                    %{--<label class="control-label" for="customerName">--}%
                        %{--联系人<span--}%
                            %{--class="required-indicator"></span></label>--}%

                    %{--<div class="controls">--}%
                        %{--<input type="text" style="width: 280px;" name="customerName" id="customerName"--}%
                               %{--value="${params.customerName}"/>--}%
                    %{--</div>--}%
                %{--</div>--}%

                %{--<div class="control-group">--}%
                    %{--<label class="control-label" for="remark">--}%
                        %{--备注<span--}%
                            %{--class="required-indicator">*</span></label>--}%

                    %{--<div class="controls">--}%
                        %{--<input type="text" style="width: 280px;" name="remark" id="remark"--}%
                               %{--value="${params.remark}"/>--}%
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

        %{--</g:form>--}%
    %{--</div>--}%
%{--</div>--}%
</body>
</html>