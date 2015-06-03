<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-11-26
  Time: 上午2:30
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="lj.enumCustom.DishesStatus; lj.enumCustom.DishesValid" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="staff_mobile_template"/>
    <title>点菜列表-厨师用</title>
    <style type="text/css">
    </style>
</head>

<body>

<!--提示消息-->
%{--<g:set var="errors" value="测试一个错误"/>--}%
<g:render template="/layouts/staff_mobile_msgs_and_errors"></g:render>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">点菜列表</h3>
    </div>
<g:if test="${dishList}">
    <table class="table table-striped table-bordered table-condensed">
        <thead>
        <tr>

            %{--<g:sortableColumn property="order.id"--}%
                              %{--title="${message(code: 'dishesInfo.order.label', default: 'Order Id')}"--}%
                              %{--params="${params}"/>--}%

            <g:sortableColumn property="foodId"
                              title="${message(code: 'dishesInfo.foodName.label', default: 'Food Name')}"
                              params="${params}"/>

            <g:sortableColumn property="num" title="${message(code: 'dishesInfo.num.label', default: 'num')}"
                              params="${params}"/>

            <g:sortableColumn property="status"
                              title="${message(code: 'dishesInfo.status.label', default: 'Status')}"
                              params="${params}"/>

            <g:sortableColumn property="valid"
                              title="${message(code: 'dishesInfo.valid.label', default: 'Valid')}"
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

                %{--<td>${fieldValue(bean: dishesInfoInstance, field: "order")}</td>--}%

                <td>
                    <g:if test="${dishesInfoInstance?.foodName}">
                        ${dishesInfoInstance?.foodName}
                    </g:if>
                    <g:else>
                        菜谱&nbsp;${dishesInfoInstance?.foodInfo.id}
                    </g:else>
                </td>

                <td>${fieldValue(bean: dishesInfoInstance, field: "num")}</td>

                <td>${DishesStatus.getLable(dishesInfoInstance?.status)}</td>

                <td>${DishesValid.getLable(dishesInfoInstance?.valid)}</td>

                %{--<td>${fieldValue(bean: dishesInfoInstance, field: "cancelReason")}</td>--}%

                <td>${fieldValue(bean: dishesInfoInstance, field: "remark")}</td>

                <td><taos:staffDishesOperation dishesId="${dishesInfoInstance?.id}"
                                               backUrl="${createLink(controller:"staff",action:  "dishList",params: params,absolute: true)}"></taos:staffDishesOperation></td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <!--分页-->
    <div class="panel-footer">
    <taos:paginateForBs3 action="dishList" total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}"/>
    </div>
</g:if>
    <g:else>
        <div class="panel-body">
            <div class="col-sm-12" style="text-align: center;">
                还没有点菜哦
            </div>
        </div>
    </g:else>
</div>

</body>
</html>