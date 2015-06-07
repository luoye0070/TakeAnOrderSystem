<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-11-25
  Time: 下午11:59
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="lj.enumCustom.DishesValid; lj.enumCustom.DishesStatus; lj.enumCustom.OrderValid; lj.enumCustom.OrderStatus; lj.enumCustom.ReserveType; lj.FormatUtil" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="staff_mobile_template"/>
    <title>结账</title>
    <style type="text/css">
    .col-sm-3{
        margin-top: 5px;
        margin-bottom: 5px;
    }
    </style>
    <script type="text/javascript">
        $(function(){
            $("#getAccount").change(function(){
                var getAccount=$(this).val();
                var realAccount=$("#realAccount").val();
                var change=getAccount-realAccount;
                $("#change").val(change);
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
        <h3 class="panel-title">订单${orderInfo?.numInRestaurant}-结账</h3>
    </div>

    <div class="panel-body">
    %{--<g:if test="${orderInfoInstance?.id}">--}%

    %{--<div class="col-sm-3">--}%
    %{--<g:message code="orderInfo.id.label" default="id"/>:--}%
    %{--<g:fieldValue bean="${orderInfoInstance}" field="id"/>--}%
    %{--</div>--}%
    %{--</g:if>--}%

        <g:if test="${orderInfo?.tableInfo?.name}">
            <div class="col-sm-3">

                <g:message code="orderInfo.tableInfo.label" default="Table Info"/>
                :
                ${orderInfo?.tableInfo?.name}
            </div>
        </g:if>

        <g:if test="${orderInfo?.clientInfo?.nickname}">
            <div class="col-sm-3">

                <g:message code="orderInfo.clientInfo.label" default="Client Info"/>
                :
                ${orderInfo?.clientInfo?.nickname}
            </div>
        </g:if>
    %{--<g:elseif test="${orderInfo?.clientInfo?.clientMark}">--}%
    %{--<div class="col-sm-3">--}%

    %{--<g:message code="orderInfo.clientInfo.label" default="Client Info"/>--}%
    %{--:--}%
    %{--${orderInfo?.clientInfo?.clientMark}--}%
    %{--</div>--}%
    %{--</g:elseif>--}%

        <g:if test="${orderInfo?.createTime}">
            <div class="col-sm-3">

                <g:message code="orderInfo.createTime.label" default="Time"/>
                :
                ${FormatUtil.dateTimeFormat(orderInfo?.createTime)}
            </div>
        </g:if>

        <g:if test="${orderInfo?.status}">
            <div class="col-sm-3">

                <g:message code="orderInfo.status.label" default="Status"/>
                :
                ${OrderStatus.getLable(orderInfo?.status)}
            </div>
        </g:if>

        <g:if test="${orderInfo?.valid != null}">
            <div class="col-sm-3">

                <g:message code="orderInfo.valid.label" default="Valid"/>
                :
                ${OrderValid.getLable(orderInfo?.valid)}
            </div>
        </g:if>

        <g:if test="${orderInfo?.cancelReason}">
            <div class="col-sm-3">

                <g:message code="orderInfo.cancelReason.label" default="Cancel Reason"/>
                :
                <g:fieldValue bean="${orderInfo}" field="cancelReason"/>
            </div>
        </g:if>


        <g:if test="${orderInfo?.waiter}">
            <div class="col-sm-3">

                <g:message code="orderInfo.waiter.label" default="Waiter Id"/>:
                <g:if test="${orderInfo?.waiter?.name}">
                    ${orderInfo?.waiter?.name}
                </g:if>
                <g:else>
                    ${orderInfo?.waiter?.loginName}
                </g:else>
            </div>
        </g:if>

        <g:if test="${orderInfo?.cashier}">
            <div class="col-sm-3">

                <g:message code="orderInfo.cashier.label" default="Cashier"/>
                :
                <g:fieldValue bean="${orderInfo}" field="cashier"/>
            </div>
        </g:if>

        <g:if test="${orderInfo?.remark}">
            <div class="col-sm-3">

                <g:message code="orderInfo.remark.label" default="Remark"/>:
                <g:fieldValue bean="${orderInfo}" field="remark"/>
            </div>
        </g:if>

        <g:if test="${orderInfo?.numInRestaurant}">
            <div class="col-sm-3">

                <g:message code="orderInfo.numInRestaurant.label" default="Num In Restaurant"/>:
                <g:fieldValue bean="${orderInfo}" field="numInRestaurant"/>
            </div>
        </g:if>

        <g:if test="${orderInfo?.orderNum}">
            <div class="col-sm-3">

                <g:message code="orderInfo.orderNum.label" default="Order Num"/>:
                <g:fieldValue bean="${orderInfo}" field="orderNum"/>
            </div>
        </g:if>

        <g:if test="${orderInfo?.partakeCode}">
            <div class="col-sm-3">

                <g:message code="orderInfo.partakeCode.label" default="Partake Code"/>:
                <g:fieldValue bean="${orderInfo}" field="partakeCode"/>
            </div>
        </g:if>

        <g:if test="${orderInfo?.totalAccount}">
            <div class="col-sm-3">

                <g:message code="orderInfo.totalAccount.label" default="Total Account"/>:
                <g:fieldValue bean="${orderInfo}" field="totalAccount"/>
            </div>
        </g:if>

        <g:if test="${orderInfo?.realAccount}">
            <div class="col-sm-3">

                <g:message code="orderInfo.realAccount.label" default="Real Account"/>:
                <g:fieldValue bean="${orderInfo}" field="realAccount"/>
            </div>
        </g:if>

        <div class="col-sm-12" style="margin-left: 0px;margin-right: 0px;">
            <a href="${params.backUrl ?: createLink(controller: "staff", action: "orderList")}"
               class="btn btn-link">返回</a>
        </div>
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
            <g:sortableColumn property="foodPrice" title="${message(code: 'dishesInfo.foodPrice.label', default: 'Food Price')}"
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

                <td>${fieldValue(bean: dishesInfoInstance, field: "foodPrice")}</td>

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
            没有点菜
        </div>
    </div>
</g:else>

    <div class="panel-body">
        <div class="col-sm-12">
            <!--提交算账的表单-->
            <form method="post" action="${createLink(controller: "staff", action: "settleAccounts",params:[backUrl:params.backUrl])}"
                  class="form-horizontal offset1">
                <input type="hidden" name="orderId" value="${orderInfo?.id}"/>
                %{--<g:if test="${orderInfo?.realAccount!=null}">--}%
                <div class="control-group">
                    <label for="realAccount" class="col-sm-3 control-label">
                        <g:message code="orderInfo.realAccount.label" default="Real Account"/>
                    </label>

                    <div class="col-sm-3">
                        <input type="number" id="realAccount" name="realAccount" value="${orderInfo?.realAccount}" class="form-control"/>
                    </div>

                </div>
                %{--</g:if>--}%

            <div class="control-group">
                <label for="getAccount" class="col-sm-3 control-label">
                    实收
                </label>

                <div class="col-sm-3">
                    <input type="number" id="getAccount" value="" class="form-control"/>
                </div>

            </div>

            <div class="control-group">
                <label for="change" class="col-sm-3 control-label">
                    找零
                </label>

                <div class="col-sm-3">
                    <input type="text" id="change" value="" readonly="" class="form-control"/>
                </div>

            </div>

                <div class="control-group">
                    <label for="realAccount" class="col-sm-3 control-label">

                    </label>

                    <div class="col-sm-3">
                        <input type="submit" class="form-control btn btn-primary" value="${message(code: "default.button.settle.label", default: "submit")}"/>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>