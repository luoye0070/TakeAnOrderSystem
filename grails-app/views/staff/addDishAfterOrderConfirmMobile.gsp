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
    <meta name="layout" content="staff_mobile_template"/>
    <title></title>
    <style type="text/css">
    .col-sm-3{
        margin-top: 5px;
        margin-bottom: 5px;
    }

    .col-sm-6{
        float: none;
    }
    </style>
    <script type="text/javascript">
        function doDish(obj,foodId,orderId){
            var doDishUrl="${createLink(controller: "staffAjax",action: "addDishAfterOrderConfirmAjax")}";
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
            var delDishUrl="${createLink(controller: "staffAjax",action: "delDishAfterOrderConfirmAjax")}";
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
</head>
<body>

<!--提示消息-->
%{--<g:set var="errors" value="测试一个错误"/>--}%
<g:render template="/layouts/staff_mobile_msgs_and_errors"></g:render>
<!--点菜列表-->
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">订单${orderInfo?.numInRestaurant}-加菜</h3>
    </div>
    <div class="panel-body">

        <div class="col-xs-8">
            <a href="${params.backUrl ?: createLink(controller: "staff", action: "orderList")}">返回</a>
            <g:if test="${orderInfo?.tableInfo?.name}">
                &nbsp;&nbsp;
                <g:message code="orderInfo.tableInfo.label" default="Table Info"/>
                :
                ${orderInfo?.tableInfo?.name}
            </g:if>
            <g:if test="${orderInfo?.clientInfo?.nickname}">
                &nbsp;&nbsp;
                <g:message code="orderInfo.clientInfo.label" default="Client Info"/>
                :
                ${orderInfo?.clientInfo?.nickname}
            </g:if>
            <g:form class="form-horizontal" method="POST" id="cancel_form" action="orderConfirmAfterOrderConfirm">
                <div class="control-group">
                    <label class="control-label"></label>
                    <input type="hidden" name="orderId" value="${orderInfo?.id}"/>
                    <input type="hidden" name="code" value="${params.code}"/>
                    <div class="controls">
                        <input type="submit" value="${message(code: 'default.button.confirmDishAdd.label', default: '确认加菜')}"
                               class="btn send_btn"/>
                    </div>
                </div>
            </g:form>
        </div>
        <div class="col-xs-4" style="text-align: right">
            <button class="btn" type="button" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                %{--<span class="caret"></span>--}%
                收起/展开
            </button>
        </div>

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

                    <g:sortableColumn property="status"
                                      title="${message(code: 'dishesInfo.status.label', default: 'Status')}"
                                      params="${params}"/>

                    <g:sortableColumn property="valid" title="${message(code: 'dishesInfo.valid.label', default: 'Valid')}"
                                      params="${params}"/>

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

                        %{--<td><g:customerDishesOperation--}%
                        %{--dishesId="${dishesInfoInstance?.id}"></g:customerDishesOperation></td>--}%
                        <td>
                            %{--<g:if test="${isOwner}">--}%
                            <button onclick="delDish(${dishesInfoInstance?.id},${orderInfo?.id})"
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

<div name="info"></div>

<div>
    <g:set var="paramsT" value="${params.clone()}"></g:set>
    <ul class="breadcrumb">
        <li class="active">
            <g:if test="${params.foodClassId==null||params.foodClassId=="0"}">
                全部
            </g:if>
            <g:else>
            <a href="${createLink(controller: "staff",action: "addDishAfterOrderConfirmView",params: paramsT<<[foodClassId:0])}">全部</a>
            </g:else>
        </li>
        <g:each in="${foodClassInfoInstanceList}" status="i" var="foodClassInfoInstance">
            %{--${params.foodClassId}--${foodClassInfoInstance.id.toString()}--}%
            <g:if test="${params.foodClassId==foodClassInfoInstance.id.toString()}">
                <li class="active">${foodClassInfoInstance.name}</li>
            </g:if>
            <g:else>
                <li><a href="${createLink(controller: "staff",action: "addDishAfterOrderConfirmView",params: paramsT<<[foodClassId:foodClassInfoInstance.id])}">${foodClassInfoInstance.name}</a></li>
            </g:else>
        </g:each>
    </ul>
</div>

<g:if test="${foodList}">
    <g:set var="backUrl" value="${createLink(controller: "staff",action: "addDishAfterOrderConfirmView",params: params,absolute: true)}"></g:set>
    <ul class="list-group">
        <g:each in="${foodList}" status="i" var="foodInfoInstance">
            <li class="list-group-item" style="padding: 10px 5px;">
                <div class="col-sm-6">
                    <label>
                        <a target="_parent" title="${foodInfoInstance?.name}"
                           href="${createLink(controller: "infoShow", action: "foodShow", params: [id: foodInfoInstance.id,backUrl:backUrl])}">${foodInfoInstance?.name}</a>
                    </label>
                    <label>￥${fieldValue(bean: foodInfoInstance, field: 'price')}</label>
                    <g:if test="${foodInfoInstance?.originalPrice}">
                        <label>￥${fieldValue(bean: foodInfoInstance, field: 'originalPrice')}</label>
                    </g:if>
                </div>
                <div class="col-sm-6">
                    <label id="counts${foodInfoInstance?.id}">数量:</label>
                    <input id="counts${foodInfoInstance?.id}" name="counts" type="text" style="width: 50px;" value="1"/>

                    <label id="remarks${foodInfoInstance?.id}">备注:</label>
                    <input id="remarks${foodInfoInstance?.id}" name="remarks" type="text" style="width: 80px;" value=""/>
                    <button onclick="doDish(this,${foodInfoInstance?.id},${orderInfo?.id})"
                            class="">点一个</button>
                </div>
            </li>
        </g:each>
    </ul>
    <taos:paginateForBs3 total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}" action="getOrCreateOrder"/>
</g:if>
<g:else>
    <div style="margin: 0px auto;">
        <label style="text-align: center">没有搜索到记录</label>
    </div>
</g:else>

</body>
</html>