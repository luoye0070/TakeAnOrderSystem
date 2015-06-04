<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 15-2-10
  Time: 下午11:11
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="lj.enumCustom.ReserveOrderStatus; lj.enumCustom.OrderValid; lj.FormatUtil; lj.enumCustom.DishesValid; lj.enumCustom.DishesStatus" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="staff_mobile_template"/>
    <title></title>
    <style type="text/css">
    </style><link type="text/css" href="${resource(dir: "js/bootstrap-3.3.4/css", file: "bootstrap-datetimepicker.min.css")}" rel="stylesheet" />
    <script type="text/javascript" src="${resource(dir: "js/bootstrap-3.3.4/js", file: "bootstrap-datetimepicker.min.js")}" charset="UTF-8"></script>
    <script type="text/javascript" src="${resource(dir: "js/bootstrap-3.3.4/js/locales", file: "bootstrap-datetimepicker.zh-CN.js")}" charset="UTF-8"></script>
    <script type="text/javascript">
        $(function () {
            $('#beginDate').datetimepicker({
                format: 'yyyy-mm-dd hh:ii:ss'
            });
            $('#endDate').datetimepicker({
                format: 'yyyy-mm-dd hh:ii:ss'
            });
        });
    </script>
</head>
<body>

<!--提示消息-->
%{--<g:set var="errors" value="测试一个错误"/>--}%
<g:render template="/layouts/staff_mobile_msgs_and_errors"></g:render>
<form class="well form-horizontal" action="${createLink(controller: "reserveStaff", action: "reserveOrderList")}">
    <div class="form-group">
    <div class="col-sm-4" style="margin-top: 15px;">
        <input class="form-control" type="number" name="phone" value="${params.phone}" placeholder="请输入手机号"/>
    </div>

    %{--<div class="mcmcs_field_middle" style="width: 180px">--}%
    %{--<label>--}%
    %{--预定类型：--}%
    %{--</label>--}%
    %{--<select name="reserveType" class="mcmcsf_input_middle">--}%
    %{--<option value="-1" ${params.reserveType == "-1" ? "selected='selected'" : ""}>全部</option>--}%
    %{--<option value="0" ${params.reserveType == "0" ? "selected='selected'" : ""}>非预定</option>--}%
    %{--<g:each in="${lj.enumCustom.ReserveType.reserveTypes}">--}%
    %{--<option value="${it.code}" ${(lj.Number.toInteger(params.reserveType) == it.code) ? "selected='selected'" : ""}>${it.label}</option>--}%
    %{--</g:each>--}%
    %{--</select>--}%
    %{--</div>--}%

    <div class="col-sm-4" style="margin-top: 15px;">
        <select name="valid" class="form-control">
            <option value="-1" ${params.status == "-1" ? "selected='selected'" : ""}>全部</option>
            <g:each in="${lj.enumCustom.OrderValid.valids}">
                <option value="${it.code}" ${params.valid == it.code.toString() ? "selected='selected'" : ""}>${it.label}</option>
            </g:each>
        </select>
    </div>

    <div class="col-sm-4" style="margin-top: 15px;">
        <select name="status" class="form-control">
            <option value="-1" ${params.status == "-1" ? "selected='selected'" : ""}>全部</option>
            <g:each in="${lj.enumCustom.ReserveOrderStatus.statuses}">
                <option value="${it.code}" ${params.status == it.code.toString() ? "selected='selected'" : ""}>${it.label}</option>
            </g:each>
        </select>
    </div>


        <div class="col-sm-4" style="margin-top: 15px;">
            <input class="form-control" id="beginDate" name="beginTime" type="text" readonly
                   value="${params.beginDate}" placeholder="请输入查询开始时间"/>
        </div>
        <div class="col-sm-4" style="margin-top: 15px;">
        <input class="form-control" id="endDate" name="endTime" type="text" readonly=""
               value="${params.endDate}" placeholder="请输入查询结束时间"/>
         </div>
    <div class="col-sm-4" style="margin-top: 15px;">
        <input type="submit" value="${message(code: 'default.button.search.label', default: 'search')}"
               class="form-control btn btn-primary"/>


        %{--<g:actionSubmit value="导出Excel" action="exportOrderList" class="btn btn-primary"/>--}%

    </div>
    </div>
</form>

<g:if test="${reserveOrderInfoList}">
    <!--订单列表-->
    <table class="table table-striped table-bordered table-condensed">
        <thead>
        <tr>
            <g:sortableColumn property="id" title="${message(code: 'reserveOrderInfo.id.label', default: 'id')}"  params="${params}"/>
            %{--<g:sortableColumn property="restaurantId"--}%
            %{--title="${message(code: 'reserveOrderInfo.restaurantId.label', default: 'Restaurant Id')}"/>--}%

            %{--<g:sortableColumn property="clientInfo.id" title="${message(code: 'reserveOrderInfo.userName.label', default: 'User Name')}"  params="${params}"/>--}%

            <g:sortableColumn property="tableInfo.id"
                              title="${message(code: 'reserveOrderInfo.tableName.label', default: 'Table Name')}" params="${params}"/>



            <g:sortableColumn property="createTime"
                              title="${message(code: 'reserveOrderInfo.dinnerTime.label', default: 'Time')}" params="${params}"/>

            <g:sortableColumn property="valid" title="${message(code: 'reserveOrderInfo.valid.label', default: 'Valid')}" params="${params}"/>

            <g:sortableColumn property="status" title="${message(code: 'reserveOrderInfo.status.label', default: 'Status')}" params="${params}"/>

            <g:sortableColumn property="status" title="${message(code: 'reserveOrderInfo.phone.label', default: 'Phone')}" params="${params}"/>
            <th>操作</th>
            %{--<th></th>--}%
        </tr>
        </thead>
        <tbody>
        <g:each in="${reserveOrderInfoList}" status="i" var="reserveOrderInfoInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                <td>${reserveOrderInfoInstance.id}</td>
                %{--<td><g:link action="show"--}%
                %{--id="${reserveOrderInfoInstance.id}">${fieldValue(bean: reserveOrderInfoInstance, field: "restaurantId")}</g:link></td>--}%

                %{--<td>--}%
                    %{--<g:if test="${reserveOrderInfoInstance.clientInfo}">--}%
                        %{--<g:if test="${reserveOrderInfoInstance.clientInfo?.nickname}">--}%
                            %{--用户&nbsp;${reserveOrderInfoInstance.clientInfo?.nickname}--}%
                        %{--</g:if>--}%
                        %{--<g:else>--}%
                            %{--用户&nbsp;${reserveOrderInfoInstance.clientInfo?.clientMark}--}%
                        %{--</g:else>--}%
                    %{--</g:if>--}%
                    %{--<g:else>--}%
                        %{--服务员--}%
                    %{--</g:else>--}%
                %{--</td>--}%

                <td style="max-width: 100px">
                    <g:if test="${reserveOrderInfoInstance.tableName}">
                        ${fieldValue(bean: reserveOrderInfoInstance, field: "tableName")}
                    </g:if>
                    <g:else>
                        桌位&nbsp;${reserveOrderInfoInstance.tableInfo.id}
                    </g:else>
                </td>



                <td>${FormatUtil.dateTimeFormat(reserveOrderInfoInstance.dinnerTime)}</td>

                <td>${OrderValid.getLable(reserveOrderInfoInstance.valid)}</td>

                <td>${ReserveOrderStatus.getLable(reserveOrderInfoInstance.status)}</td>

                <td>${reserveOrderInfoInstance.phone}</td>

                %{--<td>${ReserveType.getLabel(reserveOrderInfoInstance.reserveType)}</td>--}%
                <td><a href="${createLink(controller: "reserveStaff", action: "reserveOrderDetail", params: [reserveOrderId: reserveOrderInfoInstance.id])}">订单详情</a>
                </td>
                %{--<td>--}%
                    %{--<taos:staffReserveOrderOperation reserveOrderId="${reserveOrderInfoInstance.id}" backUrl="${createLink(controller: "reserveStaff", action: "reserveOrderList",params: params,absolute: true)}"></taos:staffReserveOrderOperation>--}%
                %{--</td>--}%
            </tr>
        </g:each>
        </tbody>
    </table>
    <!--分页-->
    <taos:paginateForBs3 action="reserveOrderList" total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}"/>
</g:if>
<g:else>
    <div style="margin: 0px auto;text-align: center">
        <label style="text-align: center">没有符合条件的预定订单哦</label>
    </div>
</g:else>

</body>
</html>