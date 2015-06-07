<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 15-2-10
  Time: 下午11:11
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="lj.enumCustom.DishesValid; lj.enumCustom.DishesStatus" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="customer_mobile_template"/>
    <title></title>
    <style type="text/css">
    .col-xs-6{
        margin-top: 5px;
        margin-bottom: 5px;
    }
    .col-xs-4{
        margin-top: 10px;
    }
    </style>
    <script type="text/javascript">
        function doDish(obj,foodId,orderId){
            var doDishUrl="${createLink(controller: "reserveCustomer",action: "dishAjax")}";
            var selectVar="#counts"+foodId;
            var countVal=$(obj).parent().parent().find("input[name='counts']").val();
            selectVar="#remarks"+foodId;
            var remarkVal= $(obj).parent().parent().find("input[name='remarks']").val();
            var countName="counts"+foodId;
            var remarkName= "remarks"+foodId;
            $.ajax({
                context:this,
                url:doDishUrl,
                async:false,
                type:'post',
                //data:{'orderId':orderId,countName:counts,remarkName:foodIds,'remarks':remarks},
                data:"reserveOrderId="+orderId+"&"+countName+"="+countVal+"&"+remarkName+"="+remarkVal+"&foodIds="+foodId,
                dataType: 'json',
                success:function(data){
                    $("div[name='info']").html("");
                    if(data.recode.code==0){
                        //$("div[name='info']").html("<label style='height: 50px;line-height:50px;color: green'>点菜"+data.recode.label+"</label>");
                        location.reload();
                    }else{
                        if(data.recode.code==5)
                            $("div[name='info']").html("<label style='height: 50px;line-height:50px;color: red'>点菜不成功："+data.failedList[0].msg+"</label>");
                        else
                            $("div[name='info']").html("<label style='height: 50px;line-height:50px;color: red'>点菜不成功："+data.recode.label+"</label>");
                    }
                },
                error:function(data){
                    $("div[name='info']").html("<label style='height: 50px;line-height:50px;color: red'>"+"未知错误"+"</label>");
                }
            });

        }

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
</head>
<body>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">预定订单${params.reserveOrderId}-点菜</h3>
    </div>
    <div class="panel-body">

        <div class="col-xs-12">

            <g:if test="${reserveOrderInfo}">
                <g:if test="${reserveOrderInfo?.numInRestaurant}">
                    <g:message code="reserveOrderInfo.numInRestaurant.label" default="Number In Restaurant"/> :
                    ${reserveOrderInfo?.numInRestaurant}
                </g:if>

                &nbsp;&nbsp;
                <g:if test="${reserveOrderInfo?.tableInfo}">
                    <g:message code="reserveOrderInfo.tableInfo.label"
                               default="Table Name"/>:
                    ${reserveOrderInfo.tableInfo.name}
                </g:if>
                &nbsp;&nbsp;
                <g:if test="${reserveOrderInfo?.waiter}">
                    <g:message code="reserveOrderInfo.waiter.label"
                               default="Waiter"/>:
                    ${reserveOrderInfo.waiter.name}
                </g:if>
                &nbsp;&nbsp;
            </g:if>

        </div>
        <div class="col-xs-6">
            <a  class="btn btn-default" href="${params.backUrl ?: createLink(controller: "reserveCustomer", action: "reserveOrderList")}">返回</a>
        </div>
        <div class="col-xs-6" style="text-align: right">
            <button class="btn" type="button" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                %{--<span class="caret"></span>--}%
                收起/展开
            </button>
        </div>

    </div>
    <g:if test="${reserveOrderInfo?.dishes}">
        <div class="collapse in"  id="collapseExample">
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>
                    <g:sortableColumn property="foodName"
                                      title="${message(code: 'dishesInfo.foodName.label', default: 'Food Name')}"
                                      params="${params}"/>

                    <g:sortableColumn property="num" title="${message(code: 'dishesInfo.num.label', default: 'num')}"
                                      params="${params}"/>

                    <g:sortableColumn property="remark"
                                      title="${message(code: 'dishesInfo.remark.label', default: 'Remark')}"
                                      params="${params}"/>

                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <g:each in="${reserveOrderInfo?.dishes}" status="i" var="dishesInfoInstance">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                        <td>${fieldValue(bean: dishesInfoInstance, field: "foodName")}</td>

                        <td>${fieldValue(bean: dishesInfoInstance, field: "num")}</td>

                        %{--<td>${fieldValue(bean: dishesInfoInstance, field: "cancelReason")}</td>--}%

                        <td>${fieldValue(bean: dishesInfoInstance, field: "remark")}</td>

                        %{--<td><g:customerDishesOperation--}%
                        %{--dishesId="${dishesInfoInstance?.id}"></g:customerDishesOperation></td>--}%
                        <td>
                            %{--<g:if test="${isOwner}">--}%
                            <button onclick="delDish(${dishesInfoInstance?.id},${reserveOrderInfo?.id})"
                                    class="">删除</button>
                            %{--<taos:staffDishesOperation dishesId="${dishesInfoInstance?.id}"--}%
                            %{--backUrl="${createLink(controller:"staff",action:  "doDish",params: params,absolute: true)}"/>--}%
                            %{--</g:if>--}%
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>

            %{--<taos:paginate action="orderShow" total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}"/>--}%

        </div>
    </g:if>
    <g:else>
        <div class="panel-body">
            还没有点菜，点菜吧
        </div>
    </g:else>
</div>

<div name='info'></div>

%{--<div>--}%
%{--<ul class="breadcrumb">--}%
%{--<li class="active"><a href="${createLink(controller: "reserveCustomer",action: "dishOfReserveOrder",params: params<<[foodClassId:0])}">全部</a></li>--}%
%{--<g:each in="${foodClassInfoInstanceList}" status="i" var="foodClassInfoInstance">--}%
%{--<li><a href="${createLink(controller: "reserveCustomer",action: "dishOfReserveOrder",params: params<<[foodClassId:foodClassInfoInstance.id])}">${foodClassInfoInstance.name}</a></li>--}%
%{--</g:each>--}%
%{--</ul>--}%
%{--</div>--}%

<div>
    <g:set var="paramsT" value="${params.clone()}"></g:set>
    <ul class="breadcrumb">
        <li class="active">
            <g:if test="${params.foodClassId==null||params.foodClassId=="0"}">
                全部
            </g:if>
            <g:else>
                <a href="${createLink(controller: "reserveCustomer",action: "dishOfReserveOrder",params: paramsT<<[foodClassId:0])}">全部</a>
            </g:else>
        </li>
        <g:each in="${foodClassInfoInstanceList}" status="i" var="foodClassInfoInstance">
        %{--${params.foodClassId}--${foodClassInfoInstance.id.toString()}--}%
            <g:if test="${params.foodClassId==foodClassInfoInstance.id.toString()}">
                <li class="active">${foodClassInfoInstance.name}</li>
            </g:if>
            <g:else>
                <li><a href="${createLink(controller: "reserveCustomer",action: "dishOfReserveOrder",params: paramsT<<[foodClassId:foodClassInfoInstance.id])}">${foodClassInfoInstance.name}</a></li>
            </g:else>
        </g:each>
    </ul>
</div>

<g:if test="${foodList}">
    <g:set var="backUrl" value="${createLink(controller: "reserveCustomer",action: "dishOfReserveOrder",params: params,absolute: true)}"></g:set>
%{--<ul class="thumbnails" style="margin: 0px auto;">--}%
    <g:each in="${foodList}" status="i" var="foodInfoInstance">
        <div class="col-sm-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <a target="_parent" title="${foodInfoInstance?.name}"
                           href="${createLink(controller: "infoShow", action: "foodShow", params: [id: foodInfoInstance.id,backUrl:backUrl])}">${foodInfoInstance?.name}</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <div class="col-sm-12" style="margin-top: 5px;margin-bottom: 5px;">
                        <a target="_parent" title="${foodInfoInstance?.name}"
                           href="${createLink(controller: "infoShow", action: "foodShow", params: [id: foodInfoInstance.id,backUrl:backUrl])}">
                            <img id="imageLabel" class="form-control"  style="height: auto;"
                                 src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfoInstance?.image, width: 140, height: 120])}"/>
                        </a>
                    </div>
                    %{--<div class="col-sm-12">--}%
                    <div class="col-xs-6">
                        <div  class="control-label">
                            <label id="priceLabel">￥${fieldValue(bean: foodInfoInstance, field: 'price')}</label>
                            <g:if test="${foodInfoInstance?.originalPrice}">
                                &nbsp;&nbsp;
                                <label id="originalPriceLabel"
                                       style="font-size:12px;text-decoration:line-through">￥${fieldValue(bean: foodInfoInstance, field: 'originalPrice')}</label>
                            </g:if>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <input id="counts${foodInfoInstance?.id}" name="counts" type="number" class="form-control" value="1"/>
                    </div>
                    %{--</div>--}%
                    %{--<div class="col-sm-12">--}%
                    <div class="col-xs-6">
                        <input id="remarks${foodInfoInstance?.id}" name="remarks" type="text" class="form-control" value="" placeholder="输入备注"/>
                    </div>
                    <div class="col-xs-6">
                        <button onclick="doDish(this,${foodInfoInstance?.id},${reserveOrderInfo?.id})"
                                class="form-control btn btn-primary">点一个</button>
                    </div>
                    %{--</div>--}%
                </div>
            </div>
        </div>
    </g:each>
%{--</ul>--}%
    <div class="col-sm-12">
        <taos:paginateForBs3 total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}" action="dishOfReserveOrder"/>
    </div>
</g:if>
<g:else>
    <div style="margin: 0px auto;">
        <label style="text-align: center">没有搜索到记录</label>
    </div>
</g:else>

</body>
</html>