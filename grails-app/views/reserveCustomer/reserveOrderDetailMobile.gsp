<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 15-2-10
  Time: 下午11:11
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="lj.enumCustom.ReserveOrderStatus; lj.enumCustom.OrderValid; lj.enumCustom.DishesValid; lj.enumCustom.DishesStatus" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="customer_mobile_template"/>
    <title></title>
    <style type="text/css">
    </style>
    <script type="text/javascript">
        function delDish(dishId,orderId){
            if(!confirm("确定要删除吗？")){
                return;
            }
            var delDishUrl="${createLink(controller: "reserveCustomer",action: "delDishAjax")}";
            $.ajax({
                context:this,
                url:delDishUrl,
                async:false,
                type:'post',
                //data:{'orderId':orderId,countName:counts,remarkName:foodIds,'remarks':remarks},
                data:"dishIds="+dishId+"&reserveOrderId="+orderId,
                dataType: 'json',
                success:function(data){
                    $("div[name='info']").html("");
                    if(data.recode.code==0){
                        //$("div[name='info']").html("<label style='height: 50px;line-height:50px;color: green'>点菜"+data.recode.label+"</label>");
                        location.reload();
                    }else{
                        if(data.recode.code==5)
                            $("div[name='info']").html("<label style='height: 50px;line-height:50px;color: red'>不成功："+data.failedList[0].msg+"</label>");
                        else
                            $("div[name='info']").html("<label style='height: 50px;line-height:50px;color: red'>不成功："+data.recode.label+"</label>");
                    }
                },
                error:function(data){
                    $("div[name='info']").html("<label style='height: 50px;line-height:50px;color: red'>"+"未知错误"+"</label>");
                }
            });

        }
    </script>
    <script type="text/javascript" src="${resource(dir: "js",file: "confirm_for_href.js")}"></script>
</head>
<body>

<div name='info'>
</div>
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">预定详情</h3>
    </div>

    <div class="panel-body">
        <g:if test="${reserveOrderInfo}">
            <g:if test="${reserveOrderInfo?.numInRestaurant}">
                <div class="col-sm-3">
                    <g:message code="reserveOrderInfo.numInRestaurant.label" default="Number In Restaurant"/> :
                    ${reserveOrderInfo?.numInRestaurant}
                </div>
            </g:if>

            <g:if test="${reserveOrderInfo?.tableInfo}">
                <div class="col-sm-3">
                    <g:message code="reserveOrderInfo.tableInfo.label"
                               default="Table Name"/>:
                    ${reserveOrderInfo.tableInfo.name}
                </div>
            </g:if>

            <g:if test="${reserveOrderInfo?.valid}">
                <div class="col-sm-3">
                    <g:message code="reserveOrderInfo.valid.label"
                               default="valid"/>:
                    ${OrderValid.getLable(reserveOrderInfo.valid)}
                </div>
            </g:if>

            <g:if test="${reserveOrderInfo?.status}">
                <div class="col-sm-3">
                    <g:message code="reserveOrderInfo.status.label"
                               default="status"/>:
                    ${ReserveOrderStatus.getLable(reserveOrderInfo.status)}
                </div>
            </g:if>

            <g:if test="${reserveOrderInfo?.personCount}">
                <div class="col-sm-3">
                    <g:message code="reserveOrderInfo.personCount.label"
                               default="personCount"/>:
                    ${reserveOrderInfo.personCount}
                </div>
            </g:if>
            <g:if test="${reserveOrderInfo?.createTime}">
                <div class="col-sm-3">
                    <g:message code="reserveOrderInfo.createTime.label"
                               default="createTime"/>:
                    ${reserveOrderInfo.createTime}
                </div>
            </g:if>
            <g:if test="${reserveOrderInfo?.dinnerTime}">
                <div class="col-sm-3">
                    <g:message code="reserveOrderInfo.dinnerTime.label"
                               default="dinnerTime"/>:
                    ${reserveOrderInfo.dinnerTime}
                </div>
            </g:if>
            <g:if test="${reserveOrderInfo?.phone}">
                <div class="col-sm-3">
                    <g:message code="reserveOrderInfo.phone.label"
                               default="phone"/>:
                    ${reserveOrderInfo.phone}
                </div>
            </g:if>
            <g:if test="${reserveOrderInfo?.customerName}">
                <div class="col-sm-3">
                    <g:message code="reserveOrderInfo.customerName.label"
                               default="customerName"/>:
                    ${reserveOrderInfo.customerName}
                </div>
            </g:if>

            <g:if test="${reserveOrderInfo?.waiter}">
                <div class="col-sm-3">
                    <g:message code="reserveOrderInfo.waiter.label"
                               default="Waiter"/>:
                    <g:if test="${reserveOrderInfo.waiter.name}">
                        ${reserveOrderInfo.waiter.name}
                    </g:if>
                    <g:else>
                        ${reserveOrderInfo.waiter.loginName}
                    </g:else>

                </div>
            </g:if>

        </g:if>

        <div class="col-sm-12" style="margin-left: 0px;margin-right: 0px;">
            <taos:customerReserveOrderOperation reserveOrderId="${reserveOrderInfo.id}"
                                                backUrl="${createLink(controller: "reserveCustomer", action: "reserveOrderDetail",params: params,absolute: true)}"/>

            <a href="${params.backUrl ?: createLink(controller: "reserveCustomer", action: "reserveOrderList")}"
               class="btn btn-link">返回</a>
        </div>
    </div>

    <g:if test="${reserveOrderInfo?.dishes}">
    %{--<div>--}%
        <table class="table table-striped table-bordered table-condensed">
            <thead>
            <tr>

                %{--<g:sortableColumn property="foodId"--}%
                %{--title="${message(code: 'dishesInfo.foodId.label', default: 'Food Id')}"--}%
                %{--params="${params}"/>--}%

                <g:sortableColumn property="foodName"
                                  title="${message(code: 'dishesInfo.foodName.label', default: 'Food Name')}"
                                  params="${params}"/>

                <g:sortableColumn property="num" title="${message(code: 'dishesInfo.num.label', default: 'num')}"
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
            <g:each in="${reserveOrderInfo?.dishes}" status="i" var="reserveDishesInfo">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                    %{--<td>${fieldValue(bean: reserveDishesInfo, field: "foodId")}</td>--}%

                    <td>${fieldValue(bean: reserveDishesInfo, field: "foodName")}</td>

                    <td>${fieldValue(bean: reserveDishesInfo, field: "num")}</td>

                    %{--<td>${fieldValue(bean: reserveDishesInfo, field: "cancelReason")}</td>--}%

                    <td>${fieldValue(bean: reserveDishesInfo, field: "remark")}</td>

                    %{--<td><g:customerDishesOperation--}%
                    %{--dishesId="${reserveDishesInfo?.id}"></g:customerDishesOperation></td>--}%
                    <td>
                        <button onclick="delDish(${reserveDishesInfo?.id},${reserveOrderInfo?.id})"
                                class="">删除</button>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>

    %{--<taos:paginate action="orderShow" total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}"/>--}%

    %{--</div>--}%
    </g:if>
    <g:else>
        <div class="panel-body" style="text-align: center">
            还没有点菜，点菜吧
        </div>
    </g:else>

</div>

</body>
</html>