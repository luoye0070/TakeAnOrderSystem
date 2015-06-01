<%@ page import="lj.enumCustom.DishesValid; lj.enumCustom.DishesStatus; lj.enumCustom.OrderValid; lj.enumCustom.OrderStatus; lj.enumCustom.ReserveType; lj.FormatUtil; lj.data.OrderInfo" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="staff_mobile_template">
    <g:set var="entityName" value="${message(code: 'orderInfo.label', default: 'OrderInfo')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <style>
        .col-sm-3{
        margin-top: 5px;
            margin-bottom: 5px;
    }
    </style>
</head>

<body>
<!--提示消息-->
%{--<g:set var="errors" value="测试一个错误"/>--}%
<g:render template="/layouts/staff_mobile_msgs_and_errors"></g:render>

<div class="panel panel-default">
<div class="panel-heading">
    <h3 class="panel-title">订单${params.orderId}-详情</h3>
</div>

<div class="panel-body">
    %{--<g:if test="${orderInfoInstance?.id}">--}%

        %{--<div class="col-sm-3">--}%
            %{--<g:message code="orderInfo.id.label" default="id"/>:--}%
            %{--<g:fieldValue bean="${orderInfoInstance}" field="id"/>--}%
        %{--</div>--}%
    %{--</g:if>--}%

    <g:if test="${orderInfoInstance?.tableInfo?.name}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.tableInfo.label" default="Table Info"/>
            :
                ${orderInfoInstance?.tableInfo?.name}
        </div>
    </g:if>

    <g:if test="${orderInfoInstance?.clientInfo?.nickname}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.clientInfo.label" default="Client Info"/>
            :
                ${orderInfoInstance?.clientInfo?.nickname}
        </div>
    </g:if>
    %{--<g:elseif test="${orderInfoInstance?.clientInfo?.clientMark}">--}%
        %{--<div class="col-sm-3">--}%

            %{--<g:message code="orderInfo.clientInfo.label" default="Client Info"/>--}%
            %{--:--}%
            %{--${orderInfoInstance?.clientInfo?.clientMark}--}%
        %{--</div>--}%
    %{--</g:elseif>--}%

    <g:if test="${orderInfoInstance?.createTime}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.createTime.label" default="Time"/>
            :
                ${FormatUtil.dateTimeFormat(orderInfoInstance?.createTime)}
        </div>
    </g:if>

    <g:if test="${orderInfoInstance?.status}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.status.label" default="Status"/>
            :
                ${OrderStatus.getLable(orderInfoInstance?.status)}
        </div>
    </g:if>

    <g:if test="${orderInfoInstance?.valid != null}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.valid.label" default="Valid"/>
           :
                ${OrderValid.getLable(orderInfoInstance?.valid)}
        </div>
    </g:if>

    <g:if test="${orderInfoInstance?.cancelReason}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.cancelReason.label" default="Cancel Reason"/>
            :
                <g:fieldValue bean="${orderInfoInstance}" field="cancelReason"/>
        </div>
    </g:if>


    <g:if test="${orderInfoInstance?.waiter}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.waiter.label" default="Waiter Id"/>:
                <g:if test="${orderInfoInstance?.waiter?.name}">
                    ${orderInfoInstance?.waiter?.name}
                </g:if>
                <g:else>
                    ${orderInfoInstance?.waiter?.loginName}
                </g:else>
        </div>
    </g:if>

    <g:if test="${orderInfoInstance?.cashier}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.cashier.label" default="Cashier"/>
                :
                <g:fieldValue bean="${orderInfoInstance}" field="cashier"/>
        </div>
    </g:if>

    <g:if test="${orderInfoInstance?.remark}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.remark.label" default="Remark"/>:
                <g:fieldValue bean="${orderInfoInstance}" field="remark"/>
        </div>
    </g:if>

    <g:if test="${orderInfoInstance?.numInRestaurant}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.numInRestaurant.label" default="Num In Restaurant"/>:
                <g:fieldValue bean="${orderInfoInstance}" field="numInRestaurant"/>
        </div>
    </g:if>

    <g:if test="${orderInfoInstance?.orderNum}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.orderNum.label" default="Order Num"/>:
                <g:fieldValue bean="${orderInfoInstance}" field="orderNum"/>
        </div>
    </g:if>

    <g:if test="${orderInfoInstance?.partakeCode}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.partakeCode.label" default="Partake Code"/>:
                <g:fieldValue bean="${orderInfoInstance}" field="partakeCode"/>
        </div>
    </g:if>

    <g:if test="${orderInfoInstance?.totalAccount}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.totalAccount.label" default="Total Account"/>:
                <g:fieldValue bean="${orderInstance}" field="totalAccount"/>
        </div>
    </g:if>

    <g:if test="${orderInfoInstance?.realAccount}">
        <div class="col-sm-3">
            
                <g:message code="orderInfo.realAccount.label" default="Real Account"/>:
                <g:fieldValue bean="${orderInfoInstance}" field="realAccount"/>
        </div>
    </g:if>

    <div class="col-sm-12" style="margin-left: 0px;margin-right: 0px;">
        <taos:staffOrderOperation orderId="${orderInfoInstance?.id}"
                                  backUrl="${createLink(controller: "staff", action: "orderShow", params: params, absolute: true)}"></taos:staffOrderOperation>
        <a href="${createLink(controller: "staff", action: "orderList")}"
           class="btn btn-link">返回订单列表</a>
    </div>
</div>
</div>

%{--<div class="row" style="margin-left: 0px;margin-right: 0px;">--}%
    %{--<taos:staffOrderOperation orderId="${orderInfoInstance?.id}"--}%
                              %{--backUrl="${createLink(controller: "staff", action: "orderShow", params: params, absolute: true)}"></taos:staffOrderOperation>--}%
    %{--<a href="${createLink(controller: "staff", action: "orderList")}"--}%
       %{--class="btn btn-link">返回订单列表</a>--}%
%{--</div>--}%

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">订单${params.orderId}-点菜列表</h3>
    </div>


        <g:if test="${dishList}">
            %{--<div class="panel-body"></div>--}%
        %{--<div>--}%
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>
                    <g:sortableColumn property="foodName"
                                      title="${message(code: 'dishesInfo.foodName.label', default: 'Food Name')}"
                                      params="${params}"/>

                    <g:sortableColumn property="num" title="${message(code: 'dishesInfo.num.label', default: 'num')}"
                                      params="${params}"/>

                    <g:sortableColumn property="status"
                                      title="${message(code: 'dishesInfo.status.label', default: 'Status')}"
                                      params="${params}"/>

                    <g:sortableColumn property="valid" title="${message(code: 'dishesInfo.valid.label', default: 'Valid')}"
                                      params="${params}"/>

                    %{--<g:sortableColumn property="cancelReason"--}%
                    %{--title="${message(code: 'dishesInfo.cancelReason.label', default: 'Cancel Reason')}"--}%
                    %{--params="${params}"/>--}%

                    <g:sortableColumn property="remark"
                                      title="${message(code: 'dishesInfo.remark.label', default: 'Remark')}"
                                      params="${params}"/>

                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <g:each in="${dishList}" status="i" var="dishesInfoInstance">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                        <td>${fieldValue(bean: dishesInfoInstance, field: "foodName")}</td>

                        <td>${fieldValue(bean: dishesInfoInstance, field: "num")}</td>

                        <td>${DishesStatus.getLable(dishesInfoInstance?.status)}</td>

                        <td>${DishesValid.getLable(dishesInfoInstance?.valid)}</td>

                        %{--<td>${fieldValue(bean: dishesInfoInstance, field: "cancelReason")}</td>--}%

                        <td>${fieldValue(bean: dishesInfoInstance, field: "remark")}</td>

                        <td><taos:staffDishesOperation
                                dishesId="${dishesInfoInstance?.id}"></taos:staffDishesOperation></td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <div class="panel-footer">
                <taos:paginateForBs3 action="orderShow" total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}"/>
            </div>
        %{--</div>--}%
        </g:if>
        <g:else>
            <div class="panel-body">
            <div class="col-sm-12" style="text-align: center;">
                还没有点菜，赶快去点菜吧
            </div>
            </div>
        </g:else>
</div>
</body>
</html>
