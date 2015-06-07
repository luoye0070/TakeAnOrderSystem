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
            var doDishUrl="${createLink(controller: "customer",action: "addDishAfterOrderConfirmAjax")}";
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
                data:"orderId="+orderId+"&"+countName+"="+countVal+"&"+remarkName+"="+remarkVal+"&foodIds="+foodId,
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
            var delDishUrl="${createLink(controller: "customer",action: "delDishAfterOrderConfirmAjax")}";
            $.ajax({
                context:this,
                url:delDishUrl,
                async:false,
                type:'post',
                //data:{'orderId':orderId,countName:counts,remarkName:foodIds,'remarks':remarks},
                data:"dishIds="+dishId+"&orderId="+orderId,
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
    <script type="text/javascript" src="${resource(dir: "js",file: "confirm_for_form.js")}"></script>
</head>
<body>


<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">订单${orderInfo?.numInRestaurant}-加菜</h3>
    </div>

    <div class="panel-body">
        <g:if test="${orderInfo}">
            <div class="col-sm-12">
                <g:if test="${orderInfo?.tableInfo}">
                    <g:message code="orderInfo.tableInfo.label"
                               default="Table Name"/>:
                    ${orderInfo.tableInfo.name}
                </g:if>
                &nbsp;&nbsp;
                <g:if test="${orderInfo?.waiter}">
                    <g:message code="orderInfo.waiter.label"
                               default="Waiter"/>:
                    <g:if test="${orderInfo.waiter.name}">
                        ${orderInfo.waiter.name}
                    </g:if>
                    <g:else>
                        ${orderInfo.waiter.loginName}
                    </g:else>
                </g:if>
                &nbsp;&nbsp;
                <g:if test="${orderInfo?.partakeCode}">
                    <g:message code="orderInfo.partakeCode.label"
                               default="Partake Code"/>：<g:fieldValue
                        bean="${orderInfo}" field="partakeCode"/>
                </g:if>
            </div>
        </g:if>
    %{--<div class="col-sm-12" style="margin-top: 10px;">--}%
        %{--<g:if test="${isOwner}">--}%
            <div class="col-xs-4">
                <a class="btn btn-default" href="${createLink(controller: "customer",action: "getOrCreateOrder",params: [orderId:orderInfo.id])}">返回</a>
            </div>
            <div class="col-xs-4">
                <form class="form-horizontal" method="POST" id="confirm_add_dish_form" action="${createLink(controller: "customer",action: "orderConfirmAfterOrderConfirm")}" confirm="确定加菜完成了吗？">
                        <input type="hidden" name="orderId" value="${orderInfo?.id}"/>
                        <input type="hidden" name="code" value="${params.code}"/>
                            <input type="submit" value="${message(code: 'default.button.confirmDishAdd.label', default: '确认加菜')}"
                                   class="btn btn-default"/>
                </form>
            </div>
        %{--</g:if>--}%
        %{--<g:else>--}%
            %{--<div class="col-xs-4"></div>--}%
            %{--<div class="col-xs-4"></div>--}%
        %{--</g:else>--}%
        <div class="col-xs-4">
            <button class="btn btn-default" type="button" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                %{--<span class="caret"></span>--}%
                收起/展开
            </button>
        </div>
        %{--</div>--}%
    </div>

    <g:if test="${dishList}">
        <div class="collapse in"  id="collapseExample">
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>

                    <g:sortableColumn property="foodName"
                                      title="${message(code: 'dishesInfo.foodName.label', default: 'Food Name')}"
                                      params="${params}"/>

                    <g:sortableColumn property="num" title="${message(code: 'dishesInfo.num.label', default: 'num')}"
                                      params="${params}"/>

                    %{--<g:sortableColumn property="status"--}%
                    %{--title="${message(code: 'dishesInfo.status.label', default: 'Status')}"--}%
                    %{--params="${params}"/>--}%

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

                        %{--<td>${DishesStatus.getLable(dishesInfoInstance?.status)}</td>--}%

                        <td>${DishesValid.getLable(dishesInfoInstance?.valid)}</td>

                        %{--<td>${fieldValue(bean: dishesInfoInstance, field: "cancelReason")}</td>--}%

                        <td>${fieldValue(bean: dishesInfoInstance, field: "remark")}</td>

                        %{--<td><g:customerDishesOperation--}%
                        %{--dishesId="${dishesInfoInstance?.id}"></g:customerDishesOperation></td>--}%
                        <td>
                            %{--<g:if test="${isOwner}">--}%
                                <button onclick="delDish(${dishesInfoInstance?.id},${orderInfo?.id})"
                                        class="">删除</button>
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
        <div class="panel-body" style="text-align: center">
            还没有点菜，点菜吧
        </div>
    </g:else>

</div>

<div name='info'></div>

<div>
    <g:set var="paramsT" value="${params.clone()}"></g:set>
    <ul class="breadcrumb">
        <g:if test="${params.foodClassId==null||params.foodClassId=="0"}">
            <li class="active">全部</li>
        </g:if>
        <g:else>
            <li class="active"><a href="${createLink(controller: "customer",action: "addDishAfterOrderConfirmView",params: paramsT<<[foodClassId:0])}">全部</a></li>
        </g:else>
        <g:each in="${foodClassInfoInstanceList}" status="i" var="foodClassInfoInstance">
        %{--${params.foodClassId}--${foodClassInfoInstance.id.toString()}--}%
            <g:if test="${params.foodClassId==foodClassInfoInstance.id.toString()}">
                <li class="active">${foodClassInfoInstance.name}</li>
            </g:if>
            <g:else>
                <li><a href="${createLink(controller: "customer",action: "addDishAfterOrderConfirmView",params: paramsT<<[foodClassId:foodClassInfoInstance.id])}">${foodClassInfoInstance.name}</a></li>
            </g:else>
        </g:each>
    </ul>
</div>

<g:if test="${foodList}">
    <g:set var="backUrl" value="${createLink(controller: "customer",action: "addDishAfterOrderConfirmView",params: params,absolute: true)}"></g:set>
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
                        <button onclick="doDish(this,${foodInfoInstance?.id},${orderInfo?.id})"
                                class="form-control btn btn-primary">点一个</button>
                    </div>
                    %{--</div>--}%
                </div>
            </div>
        </div>
    </g:each>
%{--</ul>--}%
    <div class="col-sm-12">
        <taos:paginateForBs3 total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}" action="addDishAfterOrderConfirmView"/>
    </div>
</g:if>
<g:else>
    <div style="margin: 0px auto;">
        <label style="text-align: center">没有搜索到记录</label>
    </div>
</g:else>

</body>
</html>