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
    <meta name="layout" content="customer_mobile_template"/>
    <title></title>
    <style type="text/css">
    </style>
</head>
<body>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">预定列表</h3>
    </div>

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



                %{--<g:sortableColumn property="createTime"--}%
                                  %{--title="${message(code: 'reserveOrderInfo.createTime.label', default: 'Time')}" params="${params}"/>--}%

                <g:sortableColumn property="dinnerTime"
                                  title="${message(code: 'reserveOrderInfo.dinnerTime.label', default: 'dinnerTime')}" params="${params}"/>

                <g:sortableColumn property="valid" title="${message(code: 'reserveOrderInfo.valid.label', default: 'Valid')}" params="${params}"/>

                %{--<g:sortableColumn property="status" title="${message(code: 'reserveOrderInfo.status.label', default: 'Status')}" params="${params}"/>--}%
                <th>操作</th>
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



                    %{--<td>${FormatUtil.dateTimeFormat(reserveOrderInfoInstance.createTime)}</td>--}%

                    <td>${FormatUtil.dateTimeFormat(reserveOrderInfoInstance.dinnerTime)}</td>

                    <td>${OrderValid.getLable(reserveOrderInfoInstance.valid)}</td>

                    %{--<td>${ReserveOrderStatus.getLable(reserveOrderInfoInstance.status)}</td>--}%

                    %{--<td>${ReserveType.getLabel(reserveOrderInfoInstance.reserveType)}</td>--}%
                    <td><a href="${createLink(controller: "reserveCustomer", action: "reserveOrderDetail", params: [reserveOrderId: reserveOrderInfoInstance.id])}">订单详情</a>
                    </td>
                    %{--<td>--}%
                        %{--<a href="${createLink(controller: "reserveCustomer", action: "reserveOrderCancel", params: [reserveOrderId: reserveOrderInfoInstance.id])}">取消</a>--}%
                        %{--<taos:customerReserveOrderOperation reserveOrderId="${reserveOrderInfoInstance.id}"--}%
                                                            %{--backUrl="${createLink(controller: "reserveCustomer", action: "reserveOrderList",params: params,absolute: true)}"/>--}%
                    %{--</td>--}%
                </tr>
            </g:each>
            </tbody>
        </table>
        <!--分页-->
        <div class="panel-body" style="text-align: center;">
        <taos:paginateForBs3 action="reserveOrderList" total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}"/>
        </div>
    </g:if>
    <g:else>
        <div class="panel-body" style="text-align: center;">
            <label style="text-align: center">还没有预定订单哦</label>
        </div>
    </g:else>
</div>

</body>
</html>